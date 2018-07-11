package com.sereneast.orchestramdm.keysight.mdmcustom.rest.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.RestProperties;
import com.sereneast.orchestramdm.keysight.mdmcustom.exception.ApplicationRuntimeException;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.*;
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
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

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
            Invocation.Builder request = target.request(MediaType.APPLICATION_JSON);
            request.property(ClientProperties.CONNECT_TIMEOUT, restProperties.getOrchestra().getConnectTimeout()!=null?
                    restProperties.getOrchestra().getConnectTimeout():5000);
            request.property(ClientProperties.READ_TIMEOUT, restProperties.getOrchestra().getReadTimeout()!=null?
                    restProperties.getOrchestra().getReadTimeout():70000);
            Response response = request.get();

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

    public OrchestraObject getById(final String dataSpace, final String dataSet, final String path, final String encodedRecordId, final Map<String,String> parameters) throws IOException {
        Client client = ClientBuilder.newClient();
        try {
            client.register(feature);
            WebTarget target = client.target(baseUrl).path(dataSpace).path(dataSet).path(path).path(encodedRecordId);
            if (parameters != null)
                for (Map.Entry<String, String> entry : parameters.entrySet())
                    target = target.queryParam(entry.getKey(), entry.getValue());
            Invocation.Builder request = target.request(MediaType.APPLICATION_JSON);
            request.property(ClientProperties.CONNECT_TIMEOUT, restProperties.getOrchestra().getConnectTimeout() != null ?
                    restProperties.getOrchestra().getConnectTimeout() : 5000);
            request.property(ClientProperties.READ_TIMEOUT, restProperties.getOrchestra().getReadTimeout() != null ?
                    restProperties.getOrchestra().getReadTimeout() : 70000);
            Response response = request.get();

            LOGGER.trace(String.valueOf(response.getStatus()));
            LOGGER.trace(response.getStatusInfo().toString());

            if (response.getStatus() == 200) {
                response.bufferEntity();
                ObjectMapper mapper = new ObjectMapper();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                mapper.setDateFormat(df);
                OrchestraObject responseJson = mapper.readValue(response.readEntity(String.class), OrchestraObject.class);
                LOGGER.trace("getById response : " + mapper.writeValueAsString(responseJson));
                return responseJson;
            }else if(response.getStatus() == 404){
                return new OrchestraObject();
            }else{
                throw new ApplicationRuntimeException("Received "+response.getStatus()+" response while fetching "+path+"/"+encodedRecordId+". JSON : "+response.readEntity(String.class));
            }
        }finally{
            client.close();
        }
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
            Invocation.Builder request = target.request(MediaType.APPLICATION_JSON);
            request.property(ClientProperties.CONNECT_TIMEOUT, restProperties.getOrchestra().getConnectTimeout()!=null?
                    restProperties.getOrchestra().getConnectTimeout():5000);
            request.property(ClientProperties.READ_TIMEOUT, restProperties.getOrchestra().getReadTimeout()!=null?
                    restProperties.getOrchestra().getReadTimeout():70000);
            LOGGER.debug("rest url for reference:"+target.toString());
            LOGGER.info("TIME: {} Updating {} {} records", LocalTime.now(),path,requestObject.getRows().size());
            Response response = request.post(Entity.json(mapper.writeValueAsString(requestObject)));
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
            Invocation.Builder request = target.request(MediaType.APPLICATION_JSON);
            request.property(ClientProperties.CONNECT_TIMEOUT, restProperties.getOrchestra().getConnectTimeout()!=null?
                    restProperties.getOrchestra().getConnectTimeout():5000);
            request.property(ClientProperties.READ_TIMEOUT, restProperties.getOrchestra().getReadTimeout()!=null?
                    restProperties.getOrchestra().getReadTimeout():70000);
            LOGGER.debug("rest url for reference:"+target.toString());
            LOGGER.info("TIME: {} Updating {} field", LocalTime.now(),path);
            Response response = request.put(Entity.json(mapper.writeValueAsString(content)));
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
            Invocation.Builder request = target.request(MediaType.APPLICATION_JSON);
            request.property(ClientProperties.CONNECT_TIMEOUT, restProperties.getOrchestra().getConnectTimeout()!=null?
                    restProperties.getOrchestra().getConnectTimeout():5000);
            request.property(ClientProperties.READ_TIMEOUT, restProperties.getOrchestra().getReadTimeout()!=null?
                    restProperties.getOrchestra().getReadTimeout():70000);
            LOGGER.debug("TIME: {} Orchestra promote begin", LocalTime.now());
            LOGGER.debug("Orchestra promote request: "+mapper.writeValueAsString(requestObject));
            Response response = request.post(Entity.json(mapper.writeValueAsString(requestObject)));
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
    public void promoteBulk(final String dataSpace, final String dataSet, final String path, String dirName, final Map<String,String> parameters) throws IOException {
        Client client = ClientBuilder.newClient();
        ObjectMapper mapper = new ObjectMapper();
        try {
            client.register(feature);
            WebTarget target = client.target(baseUrl).path(dataSpace).path(dataSet).path(path);
            if (parameters != null)
                for (Map.Entry<String, String> entry : parameters.entrySet())
                    target = target.queryParam(entry.getKey(), entry.getValue());
            Invocation.Builder request = target.request(MediaType.APPLICATION_JSON);
            request.property(ClientProperties.CONNECT_TIMEOUT, restProperties.getOrchestra().getConnectTimeout()!=null?
                    restProperties.getOrchestra().getConnectTimeout():5000);
            request.property(ClientProperties.READ_TIMEOUT, restProperties.getOrchestra().getReadTimeout()!=null?
                    restProperties.getOrchestra().getReadTimeout():70000);
            LOGGER.debug("TIME: {} Orchestra promote begin", LocalTime.now());
            DirectoryStream<Path> directoryStream = null;
            RestResponse restResponse = new RestResponse();
            try (Stream<Path> paths = Files.walk(Paths.get(System.getProperty("ebx.home"),dirName))) {
                paths.forEach(path1 -> {
                    try {
//                        LOGGER.debug("mdm request: "+new String(Files.readAllBytes(path1)));
                        if(Files.isRegularFile(path1)) {
                            LOGGER.debug("Regular file... "+path1.toString());
                            Response response = request.post(Entity.json(new String(Files.readAllBytes(path1))));
                            if (!(response.getStatus() >= 200 && response.getStatus() < 300)) {
                                throw new ApplicationRuntimeException("Error promoting records. Status: " + response.getStatus() + " Response: " +
                                        response.readEntity(new GenericType<HashMap<String, Object>>() {
                                        }));
                            }
                        }else{
                            LOGGER.debug("skipping... not a regular file "+path1.toString());
                        }
                    } catch (IOException e) {
                        throw new ApplicationRuntimeException("Error publish records..."+e.getMessage());
                    }
                });
            }catch (IOException e) {
                e.printStackTrace();
            }
            LOGGER.debug("TIME: {} Orchestra promote end",LocalTime.now());
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
            Invocation.Builder request = target.request(MediaType.APPLICATION_JSON);
            request.property(ClientProperties.CONNECT_TIMEOUT, restProperties.getOrchestra().getConnectTimeout()!=null?
                    restProperties.getOrchestra().getConnectTimeout():5000);
            request.property(ClientProperties.READ_TIMEOUT, restProperties.getOrchestra().getReadTimeout()!=null?
                    restProperties.getOrchestra().getReadTimeout():70000);
            LOGGER.debug("rest url for reference:"+target.toString());
            LOGGER.debug("Orchestra update flag field begin", LocalTime.now());
            Response response = request.put(Entity.json(mapper.writeValueAsString(content)));
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

    public RestResponse post(final String dataSpace, final String dataSet, final String path, OrchestraObjectList requestObject,
                                final Map<String,String> parameters, Integer readTimeout, Integer connectTimeout) throws IOException {
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
            Invocation.Builder request = target.request(MediaType.APPLICATION_JSON);
            if(readTimeout==null){
                readTimeout = restProperties.getOrchestra().getReadTimeout()!=null?
                        restProperties.getOrchestra().getReadTimeout():70000;
            }
            if(connectTimeout==null){
                connectTimeout = restProperties.getOrchestra().getConnectTimeout()!=null?
                        restProperties.getOrchestra().getConnectTimeout():5000;
            }
            request.property(ClientProperties.CONNECT_TIMEOUT, connectTimeout);
            request.property(ClientProperties.READ_TIMEOUT, readTimeout);
            LOGGER.debug("TIME: {} Orchestra POST begin", LocalTime.now());
            LOGGER.debug("Orchestra POST request: "+mapper.writeValueAsString(requestObject));
            Response response = request.post(Entity.json(mapper.writeValueAsString(requestObject)));
            response.bufferEntity();
            RestResponse restResponse = new RestResponse();
            restResponse.setStatus(response.getStatus());
            try {
                restResponse.setResponseBody(response.readEntity(new GenericType<HashMap<String, Object>>(){}));
            }catch(Exception e){
                restResponse.setResponseBody(mapper.readValue(response.readEntity(String.class), new TypeReference<Map<String, String>>(){}));
            }
            LOGGER.debug("Orchestra POST response: "+response.readEntity(String.class));
            LOGGER.debug("TIME: {} Orchestra POST end",LocalTime.now());

            return restResponse;
        }finally{
            client.close();
        }
    }

    public RestResponse delete(final String dataSpace, final String dataSet, final String path, final Map<String,String> parameters) throws IOException {
        Client client = ClientBuilder.newClient();
        ObjectMapper mapper = new ObjectMapper();
        try {
            client.register(feature);
            WebTarget target = client.target(baseUrl).path(dataSpace).path(dataSet).path(path);
            if (parameters != null)
                for (Map.Entry<String, String> entry : parameters.entrySet())
                    target = target.queryParam(entry.getKey(), entry.getValue());
            Invocation.Builder request = target.request(MediaType.APPLICATION_JSON);
            request.property(ClientProperties.CONNECT_TIMEOUT, restProperties.getOrchestra().getConnectTimeout()!=null?
                    restProperties.getOrchestra().getConnectTimeout():5000);
            request.property(ClientProperties.READ_TIMEOUT, restProperties.getOrchestra().getReadTimeout()!=null?
                    restProperties.getOrchestra().getReadTimeout():70000);
            LOGGER.debug("TIME: {} REST Delete operation begin", LocalTime.now());
            Response response = request.delete();
            response.bufferEntity();
            RestResponse restResponse = new RestResponse();
            restResponse.setStatus(response.getStatus());
            try {
                restResponse.setResponseBody(response.readEntity(new GenericType<HashMap<String, Object>>(){}));
            }catch(Exception e){
                restResponse.setResponseBody(mapper.readValue(response.readEntity(String.class), new TypeReference<Map<String, String>>(){}));
            }
            LOGGER.debug("REST Delete response: "+response.readEntity(String.class));
            LOGGER.debug("TIME: {} REST Delete operation end",LocalTime.now());
            return restResponse;
        }finally{
            client.close();
        }
    }

    public static void main(String[] args){
        try (Stream<Path> paths = Files.walk(Paths.get("E:\\tomcat1\\jscripts"))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

