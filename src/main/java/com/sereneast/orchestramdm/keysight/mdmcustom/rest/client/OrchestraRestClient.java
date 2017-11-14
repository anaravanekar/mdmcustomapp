package com.sereneast.orchestramdm.keysight.mdmcustom.rest.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.RestProperties;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraContent;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObjectList;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObjectListResponse;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.RestResponse;
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
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrchestraRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrchestraRestClient.class);

    private RestProperties restProperties;

    private String baseUrl;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setFeature(HttpAuthenticationFeature feature) {
        this.feature = feature;
    }

    private HttpAuthenticationFeature feature;

    public OrchestraRestClient(){
    }

    @Autowired
    public OrchestraRestClient(RestProperties restProperties) {
        this.restProperties = restProperties;
        StringBuilder base = new StringBuilder();
        if("true".equalsIgnoreCase(restProperties.getOrchestra().getSsl())){
            base.append("https://");
        }else{
            base.append("http://");
        }
        base.append(restProperties.getOrchestra().getHost());
        base.append(":"+restProperties.getOrchestra().getPort());
        base.append(restProperties.getOrchestra().getBaseURI());
        base.append(restProperties.getOrchestra().getVersion());
        this.baseUrl = base.toString();
        this.feature = HttpAuthenticationFeature.basic(restProperties.getOrchestra().getUsername(), restProperties.getOrchestra().getPassword());
    }


    public OrchestraObjectListResponse get(final String dataSpace, final String dataSet, final String path, final Map<String,String> parameters) throws IOException {
        Client client = ClientBuilder.newClient();
        try {
            client.register(feature);
            WebTarget target = client.target(baseUrl).path(dataSpace).path(dataSet).path(path);
            if (parameters != null)
                for (Map.Entry<String, String> entry : parameters.entrySet())
                    target = target.queryParam(entry.getKey(), entry.getValue());
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();

            LOGGER.trace(String.valueOf(response.getStatus()));
            LOGGER.trace(response.getStatusInfo().toString());

            if (response.getStatus() == 200) {
                response.bufferEntity();
                ObjectMapper mapper = new ObjectMapper();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                mapper.setDateFormat(df);
                OrchestraObjectListResponse responseJson = mapper.readValue(response.readEntity(String.class), OrchestraObjectListResponse.class);
                LOGGER.trace(mapper.writeValueAsString(responseJson));
                return responseJson;
            }
        }finally{
            client.close();
        }
        return null;
    }

    public Response insert(final String dataSpace, final String dataSet, final String path, OrchestraObjectList requestObject, final Map<String,String> parameters) throws IOException {
        Client client = ClientBuilder.newClient();
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(df);
        try {
            client.register(feature);
            WebTarget target = client.target(baseUrl).path(dataSpace).path(dataSet).path(path);
            if (parameters != null)
                for (Map.Entry<String, String> entry : parameters.entrySet())
                    target = target.queryParam(entry.getKey(), entry.getValue());
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            LOGGER.debug("rest url for reference:"+target.toString());
            LOGGER.info("TIME: {} Updating {} {} records", LocalTime.now(),path,requestObject.getRows().size());
            Response response = invocationBuilder.post(Entity.json(mapper.writeValueAsString(requestObject)));
            LOGGER.info("TIME: {} Updated {} {} records",LocalTime.now(),path,requestObject.getRows().size());

            LOGGER.info(String.valueOf(response.getStatus()));
            LOGGER.info(response.getStatusInfo().toString());

            return response;
        }finally{
            client.close();
        }
    }

    public Response updateField(final String dataSpace, final String dataSet, final String path, OrchestraContent content, final Map<String,String> parameters) throws IOException {
        Client client = ClientBuilder.newClient();
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(df);
        try {
            client.register(feature);
            WebTarget target = client.target(baseUrl).path(dataSpace).path(dataSet).path(path);
            if (parameters != null)
                for (Map.Entry<String, String> entry : parameters.entrySet())
                    target = target.queryParam(entry.getKey(), entry.getValue());
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            LOGGER.debug("rest url for reference:"+target.toString());
            LOGGER.info("TIME: {} Updating {} field", LocalTime.now(),path);
            Response response = invocationBuilder.put(Entity.json(mapper.writeValueAsString(content)));
            LOGGER.info("TIME: {} Updated {} field",LocalTime.now(),path);

            LOGGER.info(String.valueOf(response.getStatus()));
            LOGGER.info(response.getStatusInfo().toString());

            return response;
        }finally{
            client.close();
        }
    }

    public RestResponse promote(final String dataSpace, final String dataSet, final String path, OrchestraObjectList requestObject, final Map<String,String> parameters) throws IOException {
        Client client = ClientBuilder.newClient();
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(df);
        try {
            client.register(feature);
            WebTarget target = client.target(baseUrl).path(dataSpace).path(dataSet).path(path);
            if (parameters != null)
                for (Map.Entry<String, String> entry : parameters.entrySet())
                    target = target.queryParam(entry.getKey(), entry.getValue());
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            LOGGER.debug("TIME: {} Orchestra promote begin", LocalTime.now());
            LOGGER.debug("Orchestra promote request: "+mapper.writeValueAsString(requestObject));
            Response response = invocationBuilder.post(Entity.json(mapper.writeValueAsString(requestObject)));
            response.bufferEntity();
            RestResponse restResponse = new RestResponse();
            restResponse.setStatus(response.getStatus());
            try {
                restResponse.setResponseBody(response.readEntity(new GenericType<HashMap<String, Object>>(){}));
            }catch(Exception e){
                restResponse.setResponseBody(mapper.readValue(response.readEntity(String.class), new TypeReference<Map<String, String>>(){}));
            }
            LOGGER.debug("Orchestra promote response: "+response.readEntity(String.class));
            LOGGER.debug("TIME: {} Orchestra promote end",LocalTime.now());

            return restResponse;
        }finally{
            client.close();
        }
    }
    public RestResponse updateFlag(final String dataSpace, final String dataSet, final String path, OrchestraContent content, final Map<String,String> parameters) throws IOException {
        Client client = ClientBuilder.newClient();
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(df);
        try {
            client.register(feature);
            WebTarget target = client.target(baseUrl).path(dataSpace).path(dataSet).path(path);
            if (parameters != null)
                for (Map.Entry<String, String> entry : parameters.entrySet())
                    target = target.queryParam(entry.getKey(), entry.getValue());
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            LOGGER.debug("rest url for reference:"+target.toString());
            LOGGER.debug("Orchestra update flag field begin", LocalTime.now());
            Response response = invocationBuilder.put(Entity.json(mapper.writeValueAsString(content)));
            response.bufferEntity();
            RestResponse restResponse = new RestResponse();
            restResponse.setStatus(response.getStatus());
            try {
                restResponse.setResponseBody(response.readEntity(new GenericType<HashMap<String, Object>>(){}));
            }catch(Exception e){
                restResponse.setResponseBody(mapper.readValue(response.readEntity(String.class), new TypeReference<Map<String, String>>(){}));
            }
            LOGGER.debug("Orchestra promote response: "+response.readEntity(String.class));
            LOGGER.debug("TIME: {} Orchestra update flag field end",LocalTime.now());

            return restResponse;

        }finally{
            client.close();
        }
    }
}

