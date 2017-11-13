package com.sereneast.orchestramdm.keysight.mdmcustom.rest.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.RestProperties;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalTime;
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


    public Response insert(String jsonRequest, final Map<String,String> parameters, String objectName) throws IOException {
        Client client = ClientBuilder.newClient();
        ObjectMapper mapper = new ObjectMapper();
        try {
            client.register(feature);
            String targetUrl = baseUrl+"/"+restProperties.getJitterbit().getPaths().get(StringUtils.lowerCase(objectName));
            WebTarget target = client.target(targetUrl);
            if (parameters != null)
                for (Map.Entry<String, String> entry : parameters.entrySet())
                    target = target.queryParam(entry.getKey(), entry.getValue());
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);

            LOGGER.info("TIME: {} Updating records", LocalTime.now());
            Response response = invocationBuilder.post(Entity.json(jsonRequest));
            LOGGER.info("TIME: {} Updated records",LocalTime.now());

            LOGGER.info(String.valueOf(response.getStatus()));

            LOGGER.debug("jb request: "+jsonRequest);
            LOGGER.info("jb response:"+response.toString());

            return response;

      /*      if (response.getStatus() == 200 || response.getStatus() == 201) {
                return response;
            }else{
                LOGGER.info("response: "+mapper.writeValueAsString(response.getEntity()));
                throw new RuntimeException("Error publishing record to Jitterbit.");
            }*/
        }finally{
            client.close();
        }
    }
}

