package com.sereneast.orchestramdm.keysight.mdmcustom.rest.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.RestProperties;
import com.sereneast.orchestramdm.keysight.mdmcustom.exception.ApplicationRuntimeException;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

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
        Client client = ClientBuilder.newBuilder()
                .property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY)
                .property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "FINE").build();
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

    public RestResponse insertBulk(String fileName, final Map<String,String> parameters, String objectName) throws IOException {
        Client client = ClientBuilder.newBuilder()
                .property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY)
                .property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "FINE").build();
        ObjectMapper mapper = new ObjectMapper();
        try {
            //this.baseUrl = "https://Keysight.jitterbit.net/Development/1.0";
            //this.feature = HttpAuthenticationFeature.basic("keysight", "keysight123");
            client.register(HttpAuthenticationFeature.basic("keysight", "keysight123"));
            String targetUrl = "https://Keysight.jitterbit.net/Development/1.0"+"/"+("account".equalsIgnoreCase(objectName)?"MDMAccounts":"MDMAddress");
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
            LOGGER.debug("jb request: "+new String(Files.readAllBytes(Paths.get(System.getProperty("ebx.home"),fileName))));
            Response response = request.post(Entity.json(new String(Files.readAllBytes(Paths.get(System.getProperty("ebx.home"),fileName)))));
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

    public void publishBulk(String dirName, final Map<String,String> parameters, String objectName) throws IOException {
        Client client = ClientBuilder.newBuilder()
                .property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY)
                .property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "FINE").build();
        ObjectMapper mapper = new ObjectMapper();
        try {
            client.register(HttpAuthenticationFeature.basic("keysight", "keysight123"));
            String targetUrl = "https://Keysight.jitterbit.net/Development/1.0"+"/"+("account".equalsIgnoreCase(objectName)?"MDMAccounts":"MDMAddress");
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
            try (Stream<Path> paths = Files.walk(Paths.get(System.getProperty("ebx.home"),dirName))) {
                paths.forEach(path1 -> {
                    try {
//                        LOGGER.debug("jb request: "+new String(Files.readAllBytes(path1)));
                        Response response = request.post(Entity.json(new String(Files.readAllBytes(path1))));
                        if(!(response.getStatus()>=200 && response.getStatus()<300)){
                            throw new ApplicationRuntimeException("Error bulk publishing to jitterbit. Status: "+response.getStatus()+" Response: "+
                                    response.readEntity(new GenericType<HashMap<String, Object>>() {}));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }catch (IOException e) {
                e.printStackTrace();
            }
            LOGGER.debug("TIME: {} Jitterbit publish bulk end",LocalTime.now());
        }finally{
            client.close();
        }
    }
}

