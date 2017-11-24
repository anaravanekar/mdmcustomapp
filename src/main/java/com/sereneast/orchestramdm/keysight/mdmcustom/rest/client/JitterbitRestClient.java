package com.sereneast.orchestramdm.keysight.mdmcustom.rest.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.RestProperties;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class JitterbitRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(JitterbitRestClient.class);

    private RestProperties restProperties;

    private String baseUrl;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setFeature(HttpAuthenticationFeature feature) {
        this.feature = feature;
    }

    private HttpAuthenticationFeature feature;

    public JitterbitRestClient(){
    }

    @Autowired
    public JitterbitRestClient(RestProperties restProperties) {
        this.restProperties=restProperties;
        StringBuilder base = new StringBuilder();
        if("true".equalsIgnoreCase(restProperties.getJitterbit().getSsl())){
            base.append("https://");
        }else{
            base.append("http://");
        }
        base.append(restProperties.getJitterbit().getHost());
        if(StringUtils.isNotBlank(restProperties.getJitterbit().getPort())){
            base.append(":" + restProperties.getJitterbit().getPort());
        }
        base.append(restProperties.getJitterbit().getBaseURI());
        base.append(restProperties.getJitterbit().getVersion());
        this.baseUrl = base.toString();
        this.feature = HttpAuthenticationFeature.basic(restProperties.getJitterbit().getUsername(), restProperties.getJitterbit().getPassword());
    }


    public RestResponse insert(String jsonRequest, final Map<String,String> parameters, String objectName) throws IOException {
        Client client = ClientBuilder.newClient();
        ObjectMapper mapper = new ObjectMapper();
        try {
            client.register(feature);
            String targetUrl = baseUrl+"/"+restProperties.getJitterbit().getPaths().get(StringUtils.lowerCase(objectName));
            WebTarget target = client.target(targetUrl);
            if (parameters != null)
                for (Map.Entry<String, String> entry : parameters.entrySet())
                    target = target.queryParam(entry.getKey(), entry.getValue());
            Invocation.Builder request = target.request(MediaType.APPLICATION_JSON);
            request.property(ClientProperties.CONNECT_TIMEOUT, restProperties.getJitterbit().getConnectTimeout()!=null?
                    restProperties.getJitterbit().getConnectTimeout():5000);
            request.property(ClientProperties.READ_TIMEOUT, restProperties.getJitterbit().getReadTimeout()!=null?
                    restProperties.getJitterbit().getReadTimeout():70000);

            LOGGER.debug("TIME: {} Jitterbit REST begin", LocalTime.now());
            LOGGER.debug("jb request: "+jsonRequest);
            Response response = request.post(Entity.json(jsonRequest));
            response.bufferEntity();
            RestResponse restResponse = new RestResponse();
            restResponse.setStatus(response.getStatus());
            try {
                restResponse.setResponseBody(response.readEntity(new GenericType<HashMap<String, Object>>(){}));
            }catch(Exception e){
                restResponse.setResponseBody(mapper.readValue(response.readEntity(String.class), new TypeReference<Map<String, String>>(){}));
            }
            LOGGER.info("jb response: "+response.readEntity(String.class));
            LOGGER.debug("TIME: {} Jitterbit REST end",LocalTime.now());

            return restResponse;

        }finally{
            client.close();
        }
    }
}

