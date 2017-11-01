package com.sereneast.orchestramdm.keysight.mdmcustom.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.ApplicationProperties;
import com.sereneast.orchestramdm.keysight.mdmcustom.exception.ApplicationRuntimeException;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.*;
import com.sereneast.orchestramdm.keysight.mdmcustom.rest.client.OrchestraRestClient;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.AppUtil;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ResultSetToHashMapRowMapper;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private NamedParameterJdbcTemplate oracleDbNamedParameterJdbcTemplate;

    @RequestMapping(path = "/accountsses", method = RequestMethod.GET)
    public String getAllEmployees() {
        String query = "select * from ( select * from EBX_MDM_ACCOUNT order by ACCOUNTNAME) where ROWNUM <= 1000";
        List<Map<String, Object>> resultList = null;
        String result = "[]";
        try {
            resultList = oracleDbNamedParameterJdbcTemplate.query(query, new ResultSetToHashMapRowMapper());
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("data", resultList);
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            result = mapper.writeValueAsString(resultList);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("", e);
        }
        return result;
    }

    @RequestMapping(value = "/account/updateAssignment/{dataSpace}/{mdmAccountId}/{newAssignedTo}", method = RequestMethod.POST)
    public void updateAssignmentAccount(@PathVariable("mdmAccountId") String mdmAccountId,@PathVariable("dataSpace") String dataSpace,@PathVariable("newAssignedTo") String newAssignedTo){
        String mdmRestBaseUrl = ((Map)((Map) AppUtil.getAllPropertiesMap().get("keysight")).get("orchestraRest")).get("baseUrl").toString();//"http://localhost:8080/ebx-dataservices/rest/data/v1";
        String mdmRestUsername = ((Map)((Map) AppUtil.getAllPropertiesMap().get("keysight")).get("orchestraRest")).get("username").toString();//"admin"; // TODO: read from properties file
        String mdmrp = ((Map)((Map) AppUtil.getAllPropertiesMap().get("keysight")).get("orchestraRest")).get("password").toString();//"admin";
        OrchestraRestClient orchestraRestClient = new OrchestraRestClient();
        orchestraRestClient.setBaseUrl(mdmRestBaseUrl);
        orchestraRestClient.setFeature(HttpAuthenticationFeature.basic(mdmRestUsername, mdmrp));
        try {
            orchestraRestClient.updateField(dataSpace,"Account","root/Account/"+mdmAccountId+"/AssignedTo",new OrchestraContent(newAssignedTo),null);
        } catch (IOException e) {
            throw new ApplicationRuntimeException("Error udpating Assinged To field for account");
        }
    }

    @RequestMapping(value = "/address/updateAssignment/{dataSpace}/{mdmAddressId}/{newAssignedTo}", method = RequestMethod.POST)
    public void updateAssignmentAddress(@PathVariable("mdmAddressId") String mdmAddressId,@PathVariable("dataSpace") String dataSpace,@PathVariable("newAssignedTo") String newAssignedTo){
        String mdmRestBaseUrl = ((Map)((Map) AppUtil.getAllPropertiesMap().get("keysight")).get("orchestraRest")).get("baseUrl").toString();//"http://localhost:8080/ebx-dataservices/rest/data/v1";
        String mdmRestUsername = ((Map)((Map) AppUtil.getAllPropertiesMap().get("keysight")).get("orchestraRest")).get("username").toString();//"admin"; // TODO: read from properties file
        String mdmrp = ((Map)((Map) AppUtil.getAllPropertiesMap().get("keysight")).get("orchestraRest")).get("password").toString();//"admin";
        OrchestraRestClient orchestraRestClient = new OrchestraRestClient();
        orchestraRestClient.setBaseUrl(mdmRestBaseUrl);
        orchestraRestClient.setFeature(HttpAuthenticationFeature.basic(mdmRestUsername, mdmrp));
        try {
            orchestraRestClient.updateField(dataSpace,"Account","root/Address/"+mdmAddressId+"/AssignedTo",new OrchestraContent(newAssignedTo),null);
        } catch (IOException e) {
            throw new ApplicationRuntimeException("Error udpating Assinged To field for address");
        }
    }

    @RequestMapping(value = "/crossReferences/{dataSpace}/{dataSet}/{table}/{mdmId}", method = RequestMethod.GET)
    public String getCrossReferences(@PathVariable("dataSpace") String dataSpace, @PathVariable("dataSet") String dataSet, @PathVariable("table") String table, @PathVariable("mdmId") String mdmId) throws JsonProcessingException {
        LOGGER.debug("In getCrossReferences.......");
        //try{
        String tablePath = "root/" + table;
        String mdmRestBaseUrl = ((Map)((Map) AppUtil.getAllPropertiesMap().get("keysight")).get("orchestraRest")).get("baseUrl").toString();//"http://localhost:8080/ebx-dataservices/rest/data/v1";
        String mdmRestUsername = ((Map)((Map) AppUtil.getAllPropertiesMap().get("keysight")).get("orchestraRest")).get("username").toString();//"admin"; // TODO: read from properties file
        String mdmrp = ((Map)((Map) AppUtil.getAllPropertiesMap().get("keysight")).get("orchestraRest")).get("password").toString();//"admin";
        String mdmIdFieldName = table != null && table.contains("Account") ? "MDMAccountId" : "MDMAddressId";
        OrchestraRestClient orchestraRestClient = new OrchestraRestClient();
        orchestraRestClient.setBaseUrl(mdmRestBaseUrl);
        orchestraRestClient.setFeature(HttpAuthenticationFeature.basic(mdmRestUsername, mdmrp));
        OrchestraObject orchestraObject = searchByMdmId(orchestraRestClient, dataSpace, dataSet, tablePath, mdmId, mdmIdFieldName);
        Map content = (Map) orchestraObject.getContent().get("DaqaMetaData").getContent();
        Object targetValue = content.get("TargetRecord") != null ? ((Map)content.get("TargetRecord")).get("content") : null;
        String targetValueString = targetValue!=null?String.valueOf(targetValue):null;
        List<OrchestraObject> resultList = searchByTargetRecursively(orchestraRestClient, dataSpace, dataSet, tablePath, mdmId, mdmIdFieldName, targetValueString);
        OrchestraObjectList objectList = new OrchestraObjectList();
        objectList.setRows(resultList);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(objectList);
    }

    @RequestMapping(value = "calculatedFields/country/{dataSpace}/{dataSet}/{countryCode}", method = RequestMethod.GET)
    public String getCalculatedFields(@PathVariable("dataSpace") String dataSpace, @PathVariable("dataSet") String dataSet,@PathVariable("countryCode") String countryCode) throws IOException {
        String operatingUnitTablePath = "root/OperatingUnit";
        String mdmRestBaseUrl = ((Map)((Map) AppUtil.getAllPropertiesMap().get("keysight")).get("orchestraRest")).get("baseUrl").toString();//"http://localhost:8080/ebx-dataservices/rest/data/v1";
        String mdmRestUsername = ((Map)((Map) AppUtil.getAllPropertiesMap().get("keysight")).get("orchestraRest")).get("username").toString();//"admin"; // TODO: read from properties file
        String mdmrp = ((Map)((Map) AppUtil.getAllPropertiesMap().get("keysight")).get("orchestraRest")).get("password").toString();//"admin";
        OrchestraRestClient orchestraRestClient = new OrchestraRestClient();
        orchestraRestClient.setBaseUrl(mdmRestBaseUrl);
        orchestraRestClient.setFeature(HttpAuthenticationFeature.basic(mdmRestUsername, mdmrp));
        Map<String, String> parameters = new HashMap<>();
        parameters.put("filter","CountryCode='" +countryCode+"'");

        OrchestraObjectListResponse orchestraObjectListResponse = orchestraRestClient.get(dataSpace,dataSet,operatingUnitTablePath,parameters);
        Map<String,String> resultObject = new HashMap<>();
        if (orchestraObjectListResponse.getRows() != null && !orchestraObjectListResponse.getRows().isEmpty()) {
            OrchestraObjectResponse objectResponse = orchestraObjectListResponse.getRows().get(0);
            Map<String,OrchestraContent> content = objectResponse.getContent();
            String operatingUnit = content.get("OperatingUnit").getContent()!=null?content.get("OperatingUnit").getContent().toString():null;
            LOGGER.debug("operatingUnit="+operatingUnit);
            if(operatingUnit!=null){
                resultObject.put("OperatingUnit",operatingUnit);
            }
        }

        orchestraObjectListResponse = orchestraRestClient.get(dataSpace,dataSet,"root/RegimeCode",parameters);
        if (orchestraObjectListResponse.getRows() != null && !orchestraObjectListResponse.getRows().isEmpty()) {
            OrchestraObjectResponse objectResponse = orchestraObjectListResponse.getRows().get(0);
            Map<String,OrchestraContent> content = objectResponse.getContent();
            String value = content.get("TaxRegimeCode").getContent()!=null?content.get("TaxRegimeCode").getContent().toString():null;
            LOGGER.debug("TaxRegimeCode="+value);
            if(value!=null){
                resultObject.put("TaxRegimeCode",value);
            }
        }
        orchestraObjectListResponse = orchestraRestClient.get(dataSpace,dataSet,"root/State",parameters);
        if (orchestraObjectListResponse.getRows() != null && !orchestraObjectListResponse.getRows().isEmpty()) {
            OrchestraObjectResponse objectResponse = orchestraObjectListResponse.getRows().get(0);
            Map<String,OrchestraContent> content = objectResponse.getContent();
            String value = content.get("State").getContent()!=null?content.get("State").getContent().toString():null;
            LOGGER.debug("State="+value);
            if(value!=null){
                resultObject.put("AddressState",value);
            }
        }
        orchestraObjectListResponse = orchestraRestClient.get(dataSpace,dataSet,"root/Province",parameters);
        if (orchestraObjectListResponse.getRows() != null && !orchestraObjectListResponse.getRows().isEmpty()) {
            OrchestraObjectResponse objectResponse = orchestraObjectListResponse.getRows().get(0);
            Map<String,OrchestraContent> content = objectResponse.getContent();
            String value = content.get("Province").getContent()!=null?content.get("Province").getContent().toString():null;
            LOGGER.debug("Province="+value);
            if(value!=null){
                resultObject.put("Province",value);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(resultObject);
    }

    private String getTargetPredicate(String mdmId, String mdmIdFieldName, String targetId) {
        String condition = "DaqaMetaData/TargetRecord='" + mdmId + "'";
        if (StringUtils.isNotBlank(targetId) && StringUtils.isNotBlank(mdmId)) {
            condition = "DaqaMetaData/TargetRecord='" + mdmId + "'" + " or " + mdmIdFieldName + "='" + targetId + "'";
        }else if(StringUtils.isNotBlank(targetId) && StringUtils.isBlank(mdmId)){
            condition = mdmIdFieldName + "='" + targetId + "'";
        }
        return condition;
    }

    private OrchestraObject searchByMdmId(OrchestraRestClient restClient, String dataSpace, String dataSet, String tablePath, String mdmId, String mdmIdFieldName) {
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("filter",mdmIdFieldName + "=" + Integer.valueOf(mdmId));
            OrchestraObjectListResponse orchestraObjectListResponse = restClient.get(dataSpace, dataSet, tablePath, parameters);
            if (orchestraObjectListResponse.getRows() != null && !orchestraObjectListResponse.getRows().isEmpty()) {
                OrchestraObjectResponse objectResponse = orchestraObjectListResponse.getRows().get(0);
                OrchestraObject resultObject = new OrchestraObject();
                resultObject.setContent(objectResponse.getContent());
                return resultObject;
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error finding record by mdmid", e);
        }
    }


    private List<OrchestraObject> searchByTargetRecursively(OrchestraRestClient restClient, String dataSpace, String dataSet, String tablePath, String mdmId, String mdmIdFieldName, String targetId) {
        List<OrchestraObject> resultList = new ArrayList<>();
        try {
            Map parameters = new HashMap<String, String>();
            parameters.put("filter", getTargetPredicate(mdmId, mdmIdFieldName, targetId));
            OrchestraObjectListResponse orchestraObjectListResponse = restClient.get(dataSpace, dataSet, tablePath, parameters);
            List<OrchestraObjectResponse> objectResponseList = orchestraObjectListResponse.getRows();
            if (objectResponseList != null && !objectResponseList.isEmpty()) {
                for (OrchestraObjectResponse objectResponse : objectResponseList) {
                    Map<String, OrchestraContent> responseObjectContent = objectResponse.getContent();
                    OrchestraObject resultObject = new OrchestraObject();
                    Map<String, OrchestraContent> resultContent = new HashMap<>();
                    Object currentMdmId = responseObjectContent.get(mdmIdFieldName).getContent();
                    resultContent.put(mdmIdFieldName, new OrchestraContent(currentMdmId));
                    Object currentStateObjectValue = ((Map)((Map) responseObjectContent.get("DaqaMetaData").getContent()).get("State")).get("content");
                    String currentStateString = currentStateObjectValue!=null?currentStateObjectValue.toString():null;
                    resultContent.put("State", new OrchestraContent(currentStateString));
                    resultContent.put("SystemId", responseObjectContent.get("SystemId"));
                    resultContent.put("SystemName", responseObjectContent.get("SystemName"));
                    Object currentTargetValueObject = ((Map)((Map) responseObjectContent.get("DaqaMetaData").getContent()).get("TargetRecord")).get("content");
                    String currentRecordTargetValue = currentTargetValueObject!=null?String.valueOf(currentTargetValueObject):null;
                    resultObject.setContent(resultContent);
                    resultList.add(resultObject);
                    if(!String.valueOf(currentMdmId).equals(targetId)) {
                        resultList.addAll(searchByTargetRecursively(restClient, dataSpace, dataSet, tablePath, currentMdmId.toString(), mdmIdFieldName, null));
                    }
                   /* if(!"Golden".equalsIgnoreCase(currentStateString) && StringUtils.isNotBlank(currentRecordTargetValue)){
                        resultList.addAll(searchByTargetRecursively(restClient, dataSpace, dataSet, tablePath, null, mdmIdFieldName, currentRecordTargetValue));
                    }*/
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @RequestMapping(value = "/crossReferences/{mdmId}", method = RequestMethod.GET)
    public String testRest(@PathVariable("mdmId") String mdmId) throws JsonProcessingException {
        LOGGER.debug("mdmid=" + mdmId);
        return "";
    }
}