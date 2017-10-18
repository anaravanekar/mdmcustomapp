package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.RequestResult;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.service.Procedure;
import com.orchestranetworks.service.ProcedureResult;
import com.orchestranetworks.service.ProgrammaticService;
import com.orchestranetworks.service.ValueContextForUpdate;
import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.*;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.exception.ApplicationRuntimeException;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraContent;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObject;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObjectList;
import com.sereneast.orchestramdm.keysight.mdmcustom.rest.client.OrchestraRestClient;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ApplicationCacheUtil;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

public class PublishService implements UserService<TableViewEntitySelection>,ApplicationContextAware
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PublishService.class);

    private ApplicationContext applicationContext;

    private String objectName;

    private Map<String, Path> fieldPathMap;

    private String referenceDataSpaceUrl;

    private String referenceDataSetUrl;

    private String tablePathUrl;

    private Path tablePathInSchema;

    private Path objectPrimaryKeyPath;

    private Class objectPrimaryKeyType;

    private Path daqaTargetFieldPath;

    private Path daqaStateFieldPath;

    private Path systemIdPath;

    private Path systemNamePath;

    private Path flagFieldPath;

    private ApplicationCacheUtil applicationCacheUtil;

    private List<ObjectKey> objectKeys = new ArrayList<>();

    private static final String ERROR_MESSAGE_REFERENCE_FAIL = "Error promoting records to Reference dataspace";

    private static final String ERROR_MESSAGE_JITTERBIT_FAIL = "Error publishing records to Jitterbit";

    private static final String ERROR_MESSAGE_ACCOUNT_NOT_PUBLISHED = "Can not publish address record. Related account has not been published.";

    private static final String ERROR_MESSAGE_ACCOUNT_NOT_FOUND = "Can not publish address record. Related account does not exist.";

    private static final String ERROR_UPDATING_FLAG = "Error updating published flag";

    private static final int MAX_RETRY_COUNT = 5;

    private static final int RETRY_WAIT_MILLIS = 1000;

    private static final String CROSS_REFERENCES_LABEL = "CrossReferences";

    private String mdmRestBaseUrl;

    private String mdmRestUsername;

    private String mdmrp;

    private String jitterbitBaseUrl;

    private String jitterbitUsername;

    private String jitterbitrp;

    public PublishService() {
    }

    @Override
    public void setupObjectContext(
            UserServiceSetupObjectContext<TableViewEntitySelection> aContext,
            UserServiceObjectContextBuilder aBuilder)
    {
/*        LOGGER.debug("In Setup Object Context");
        LOGGER.debug("Dataspace name/label="+aContext.getEntitySelection().getDataspace().getLabelOrName(Locale.ENGLISH));
        LOGGER.debug("Dataspace label="+aContext.getEntitySelection().getDataspace().getLabel());
        RequestResult result = aContext.getEntitySelection().getSelectedRecords().execute();
        int i= 0;
        for (Adaptation record; (record = result.nextAdaptation()) != null; ) {
            ObjectKey oKey = null;//
            if ("ACCOUNT".equals(objectName)) {
                oKey = ObjectKey.forName("ACCOUNT"+i);
            } else if ("ADDRESS".equals(objectName)) {
                oKey = ObjectKey.forName("ADDRESS"+i);
            }
            aBuilder.registerRecordOrDataSet(oKey,record);
            objectKeys.add(oKey);
            i++;
        }*/
    }

    /**
     * Sets up the display.
     */
    @Override
    public void setupDisplay(
            UserServiceSetupDisplayContext<TableViewEntitySelection> aContext,
            UserServiceDisplayConfigurator aConfigurator)

    {
        LOGGER.debug("In Setup Display");
        // Sets content.
        aConfigurator.setContent(new UserServicePane()
        {
            @Override
            public void writePane(UserServicePaneContext aContext, UserServicePaneWriter aWriter)
            {
                PublishService.this.writeForm(aContext, aWriter);
            }
        });
        aConfigurator.setLeftButtons(aConfigurator.newCloseButton());
    }

    @Override
    public void validate(UserServiceValidateContext<TableViewEntitySelection> aContext)
    {
        // Not used.

    }

    @Override
    public UserServiceEventOutcome processEventOutcome(
            UserServiceProcessEventOutcomeContext<TableViewEntitySelection> aContext,
            UserServiceEventOutcome anEventOutcome)
    {
        return anEventOutcome;
    }

    private void writeForm(UserServicePaneContext aContext, UserServicePaneWriter aWriter){
        LOGGER.debug("In writeform");
        String finalMessage = null;
        List<Adaptation> selectedRecords = new ArrayList<>();
        try {
            for (ObjectKey objectKey : objectKeys) {
                LOGGER.debug("Getting adaptation for objectKey=" + objectKey.getName());
                Adaptation adaptation = (Adaptation) aContext.getValueContext(objectKey).getValue();
                if("Golden".equalsIgnoreCase(adaptation.getString(daqaStateFieldPath))){
                    selectedRecords.add(adaptation);
                }else{
                    throw new ApplicationRuntimeException("Only Golden records can be published. Please select Golden records and try again.");
                }
            }
            finalMessage = promoteAndPublish(selectedRecords, aContext);
        }catch(ApplicationRuntimeException e){
            finalMessage = e.getMessage();
        }
        aWriter.add(finalMessage);
    }

    public String promoteAndPublish(List<Adaptation> selectedRecords,UserServicePaneContext aContext){
        String message = null;
        String errorMessage = null;
        List<OrchestraObject> recordsToUpdateInReference = new ArrayList<>();
        List<OrchestraObject>  recordsToUpdateInJitterbit = new ArrayList<>();
        List<Adaptation> relatedAddresses = new ArrayList<>();
        try {
            // Set up requests
            for (Adaptation adaptation : selectedRecords) {
                if ("ADDRESS".equals(objectName)) {
                    if(adaptation.get(Paths._Address._MDMAccountId)==null){
                        throw new ApplicationRuntimeException(ERROR_MESSAGE_ACCOUNT_NOT_FOUND);
                    }
                    LOGGER.debug("Getting account.......");
                    final String condition = flagFieldPath.format() + " = 'Y' and (" + Paths._Account._MDMAccountId.format() + " = " + Integer.valueOf(adaptation.get(Paths._Address._MDMAccountId).toString()) + ")";
                    LOGGER.debug("condition=" + condition);
                    Adaptation container = adaptation.getContainer();
                    AdaptationTable accountTable = container.getTable(Paths._Account.getPathInSchema());
                    final RequestResult collectedAccounts = accountTable.createRequestResult(condition);
                    if (collectedAccounts != null && !collectedAccounts.isEmpty()) {
                        LOGGER.debug("account found: " + collectedAccounts.nextAdaptation().getString(Paths._Account._AccountName));
                    } else {
                        errorMessage = ERROR_MESSAGE_ACCOUNT_NOT_PUBLISHED;
                        throw new ApplicationRuntimeException(ERROR_MESSAGE_ACCOUNT_NOT_PUBLISHED);
                    }
                }else{
                    Adaptation container = adaptation.getContainer();
                    AdaptationTable addressTable = container.getTable(Paths._Address.getPathInSchema());
                    final RequestResult addressTableRequestResult = addressTable.createRequestResult(Paths._Address._MDMAccountId.format()+"='"+adaptation.get(Paths._Account._MDMAccountId)+"'");
                    if (addressTableRequestResult != null && !addressTableRequestResult.isEmpty()) {
                        for(Adaptation address;(address=addressTableRequestResult.nextAdaptation()) != null;){
                            if("Golden".equalsIgnoreCase(address.getString(Paths._Address._DaqaMetaData_State))) {
                                relatedAddresses.add(address);
                            }
                        }
                    }
                }

                //Set up for update in Reference
                OrchestraObject orchestraObjectToUpdateInReference = new OrchestraObject();
                Map<String, OrchestraContent> jsonFieldsMapForReference = new HashMap<>();
                for (String fieldName : fieldPathMap.keySet()) {
                    Object fieldValue = adaptation.get(fieldPathMap.get(fieldName));
                    if(fieldValue instanceof List){
                        List objArray = (List)fieldValue;
                        List<OrchestraContent> contentList = new ArrayList<>();
                        for(Object obj:objArray){
                            contentList.add(new OrchestraContent(obj));
                        }
                        fieldValue = contentList;
                    }
                    jsonFieldsMapForReference.put(fieldName, new OrchestraContent(fieldValue));
                }
                orchestraObjectToUpdateInReference.setContent(jsonFieldsMapForReference);
                recordsToUpdateInReference.add(orchestraObjectToUpdateInReference);

                //Set up for update in Jitterbit
                OrchestraObject orchestraObjectToUpdateInJitterbit = new OrchestraObject();
                Map<String, OrchestraContent> jsonFieldsMapForJitterbit = new HashMap<>();
                for (String fieldName : fieldPathMap.keySet()) {
                    Object fieldValue = adaptation.get(fieldPathMap.get(fieldName));
                    if(fieldValue instanceof List){
                        List objArray = (List)fieldValue;
                        List<OrchestraContent> contentList = new ArrayList<>();
                        for(Object obj:objArray){
                            contentList.add(new OrchestraContent(obj));
                        }
                        fieldValue = contentList;
                    }
                    jsonFieldsMapForJitterbit.put(fieldName, new OrchestraContent(fieldValue));
                }
                //Find cross references for the object
                List<OrchestraObject> suspects = getSuspects(adaptation);
                if (suspects != null && !suspects.isEmpty()) {
                    jsonFieldsMapForJitterbit.put(CROSS_REFERENCES_LABEL, new OrchestraContent(suspects));
                }else{
                    jsonFieldsMapForJitterbit.put(CROSS_REFERENCES_LABEL, new OrchestraContent(null));
                }
                orchestraObjectToUpdateInJitterbit.setContent(jsonFieldsMapForJitterbit);
                recordsToUpdateInJitterbit.add(orchestraObjectToUpdateInJitterbit);
            }

            //Execute updates
            promoteToReference(recordsToUpdateInReference);
            publishToJitterbit(recordsToUpdateInJitterbit);
            updateFlagToSuccess(aContext,selectedRecords,recordsToUpdateInReference);
            if(!relatedAddresses.isEmpty()){
                try {
                    AddressPublishService addressPublishService = new AddressPublishService();
                    message = addressPublishService.promoteAndPublish(relatedAddresses,aContext);
                } catch (ClassNotFoundException | IllegalAccessException e) {
                    throw new ApplicationRuntimeException("Error publishing related addresses",e);
                }
            }else{
                message = "Publish successful";
            }
        }catch(ApplicationRuntimeException e){
            String rootCauseMessage = e.getRootCause()!=null?"\nRoot Cause: "+e.getRootCause().getMessage():"";
            message = "ERROR: "+e.getMessage()+rootCauseMessage;
            LOGGER.error("Error publishing records: \n",e);
        }
        return message;
    }

    public void promoteToReference(List<OrchestraObject> recordsToUpdateInReference){
        try{
            ObjectMapper mapper = new ObjectMapper();
            OrchestraObjectList orchestraObjectList = new OrchestraObjectList();
            orchestraObjectList.setRows(recordsToUpdateInReference);
            OrchestraRestClient orchestraRestClient = new OrchestraRestClient();
            orchestraRestClient.setBaseUrl(mdmRestBaseUrl);
            orchestraRestClient.setFeature(HttpAuthenticationFeature.basic(mdmRestUsername, mdmrp));
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("updateOrInsert", "true");
            Response response = null;
            int retryCount = 0;
            do {
                if(retryCount>0){
                    Thread.sleep(RETRY_WAIT_MILLIS);
                }
                LOGGER.debug("Promoting to Reference: \n"+mapper.writeValueAsString(orchestraObjectList));
                response = orchestraRestClient.insert(referenceDataSpaceUrl, referenceDataSetUrl, tablePathUrl, orchestraObjectList, parameters);
                retryCount++;
            }while(retryCount<MAX_RETRY_COUNT && (response==null || response.getStatus()>=300));
            if(response.getStatus()!=200 && response.getStatus()!=201){
                response.bufferEntity();
                throw new ApplicationRuntimeException(response.readEntity(String.class));
            }
        }catch(InterruptedException | IOException e){
            throw new ApplicationRuntimeException(ERROR_MESSAGE_REFERENCE_FAIL,e);
        }
    }

    private void publishToJitterbit(List<OrchestraObject> recordsToUpdateInJitterbit){
        try {
            ObjectMapper mapper = new ObjectMapper();
            OrchestraObjectList orchestraObjectList = new OrchestraObjectList();
            orchestraObjectList.setRows(recordsToUpdateInJitterbit);
           /* JitterbitRestClient jitterbitRestClient = new JitterbitRestClient();
            jitterbitRestClient.setBaseUrl(jitterbitBaseUrl);
            jitterbitRestClient.setFeature(HttpAuthenticationFeature.basic(jitterbitUsername, jitterbitrp));
            Response response = null;
            int retryCount = 0;
            do {
                if (retryCount > 0) {
                    Thread.sleep(RETRY_WAIT_MILLIS);
                }
                response = jitterbitRestClient.insert(mapper.writeValueAsString(recordsToUpdateInJitterbit), null);
                retryCount++;
            } while (retryCount < MAX_RETRY_COUNT && (response == null || response.getStatus() >= 300));
            if(response.getStatus()!=200 && response.getStatus()!=201){
                response.bufferEntity();
                throw new ApplicationRuntimeException(response.readEntity(String.class));
            }*/
            LOGGER.debug("Published to Jitterbit: \n"+mapper.writeValueAsString(orchestraObjectList));
        }catch(IOException e){
//        }catch(InterruptedException | IOException e){
            throw new ApplicationRuntimeException(ERROR_MESSAGE_JITTERBIT_FAIL,e);
        }
    }

    private void updateFlagToSuccess(UserServicePaneContext aContext,List<Adaptation> selectedRecords,List<OrchestraObject> recordsToUpdateInReference){
        for (int i=0;i<selectedRecords.size();i++) {
            Adaptation record = selectedRecords.get(i);
            Procedure procedure = procedureContext -> {
                ValueContextForUpdate valueContextForUpdate = procedureContext.getContext(record.getAdaptationName());
                valueContextForUpdate.setValue("Y", flagFieldPath);//TODO change
                procedureContext.doModifyContent(record, valueContextForUpdate);
            };
            ProgrammaticService svc = ProgrammaticService.createForSession(aContext.getSession(), record.getHome());
            ProcedureResult result = null;
            result = svc.execute(procedure);
            if (result == null || result.hasFailed()) {
                LOGGER.info("proc failed " + result.getExceptionFullMessage(Locale.ENGLISH));
                throw new ApplicationRuntimeException(ERROR_UPDATING_FLAG);
            } else {
                LOGGER.info("proc success ");
            }
            OrchestraObject obj = recordsToUpdateInReference.get(i);
            obj.getContent().put(flagFieldPath.format().replaceAll("\\.\\/", ""),new OrchestraContent("Y"));
        }
        ObjectMapper mapper = new ObjectMapper();
        OrchestraRestClient orchestraRestClient = new OrchestraRestClient();
        orchestraRestClient.setBaseUrl(mdmRestBaseUrl);
        orchestraRestClient.setFeature(HttpAuthenticationFeature.basic(mdmRestUsername, mdmrp));
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("updateOrInsert", "true");
        OrchestraObjectList objList = new OrchestraObjectList();
        objList.setRows(recordsToUpdateInReference);
        try {
            LOGGER.debug("Request to update flag:"+mapper.writeValueAsString(objList));
            Response response = orchestraRestClient.insert(referenceDataSpaceUrl, referenceDataSetUrl, tablePathUrl, objList, parameters);
            if(response.getStatus()!=200 && response.getStatus()!=201){
                response.bufferEntity();
                throw new ApplicationRuntimeException(response.readEntity(String.class));
            }
        } catch (IOException e) {
            throw new ApplicationRuntimeException("Error updating success flag in reference dataspace",e);
        }
    }

    private List<OrchestraObject> getSuspects(Adaptation adaptation) {
        LOGGER.debug("Getting suspects.......");
        final String condition = daqaTargetFieldPath.format()+" = '"+String.valueOf(adaptation.get(objectPrimaryKeyPath))+"'";
        LOGGER.debug("condition for getting suspects ="+condition);
        List<OrchestraObject> suspectList = new ArrayList<>();
        AdaptationTable table =  adaptation.getContainer().getTable(tablePathInSchema);
        final RequestResult tableRequestResult = table.createRequestResult(condition);
        if(tableRequestResult!=null && !tableRequestResult.isEmpty()){
            for (Adaptation tableRequestResultRecord; (tableRequestResultRecord=tableRequestResult.nextAdaptation()) != null;) {
                LOGGER.debug("Suspect Record found: " + String.valueOf(tableRequestResultRecord.get(objectPrimaryKeyPath)));
                OrchestraObject orchestraObject = new OrchestraObject();
                Map<String, OrchestraContent> jsonFieldsMap = new HashMap<>();
                jsonFieldsMap.put(objectPrimaryKeyPath.format().replaceAll("\\.\\/", ""),new OrchestraContent(tableRequestResultRecord.get(objectPrimaryKeyPath)));
                jsonFieldsMap.put(systemIdPath.format().replaceAll("\\.\\/", ""),new OrchestraContent(tableRequestResultRecord.get(systemIdPath)));
                jsonFieldsMap.put(systemNamePath.format().replaceAll("\\.\\/", ""),new OrchestraContent(tableRequestResultRecord.get(systemNamePath)));
                List<OrchestraObject> childSuspectList = getSuspects(tableRequestResultRecord);
                if(childSuspectList!=null && !childSuspectList.isEmpty()){
                    //jsonFieldsMap.put(CROSS_REFERENCES_LABEL,new OrchestraContent(childSuspectList));
                    suspectList.addAll(childSuspectList);
                }
                orchestraObject.setContent(jsonFieldsMap);
                suspectList.add(orchestraObject);
            }
        }
        return suspectList;
    }

    private OrchestraObject getCopy(OrchestraObject orchestraObject) {
        OrchestraObject resultObject = new OrchestraObject();
        Map<String,OrchestraContent> contents = new HashMap<>();
        for(String key:orchestraObject.getContent().keySet()){
            contents.put(key,orchestraObject.getContent().get(key));
        }
        resultObject.setContent(contents);
        return resultObject;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        LOGGER.debug("Setting application context");
        this.applicationContext = applicationContext;
        if(applicationContext!=null) {
            LOGGER.debug("Got application context initializing cacheutil");
            applicationCacheUtil = (ApplicationCacheUtil) applicationContext.getBean("applicationCacheUtil");
        }

    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Map<String, Path> getFieldPathMap() {
        return fieldPathMap;
    }

    public void setFieldPathMap(Map<String, Path> fieldPathMap) {
        this.fieldPathMap = fieldPathMap;
    }

    public String getReferenceDataSpaceUrl() {
        return referenceDataSpaceUrl;
    }

    public void setReferenceDataSpaceUrl(String referenceDataSpaceUrl) {
        this.referenceDataSpaceUrl = referenceDataSpaceUrl;
    }

    public String getReferenceDataSetUrl() {
        return referenceDataSetUrl;
    }

    public void setReferenceDataSetUrl(String referenceDataSetUrl) {
        this.referenceDataSetUrl = referenceDataSetUrl;
    }

    public String getTablePathUrl() {
        return tablePathUrl;
    }

    public void setTablePathUrl(String tablePathUrl) {
        this.tablePathUrl = tablePathUrl;
    }

    public Path getTablePathInSchema() {
        return tablePathInSchema;
    }

    public void setTablePathInSchema(Path tablePathInSchema) {
        this.tablePathInSchema = tablePathInSchema;
    }

    public Path getObjectPrimaryKeyPath() {
        return objectPrimaryKeyPath;
    }

    public void setObjectPrimaryKeyPath(Path objectPrimaryKeyPath) {
        this.objectPrimaryKeyPath = objectPrimaryKeyPath;
    }

    public Class getObjectPrimaryKeyType() {
        return objectPrimaryKeyType;
    }

    public void setObjectPrimaryKeyType(Class objectPrimaryKeyType) {
        this.objectPrimaryKeyType = objectPrimaryKeyType;
    }

    public Path getDaqaTargetFieldPath() {
        return daqaTargetFieldPath;
    }

    public void setDaqaTargetFieldPath(Path daqaTargetFieldPath) {
        this.daqaTargetFieldPath = daqaTargetFieldPath;
    }

    public Path getSystemIdPath() {
        return systemIdPath;
    }

    public void setSystemIdPath(Path systemIdPath) {
        this.systemIdPath = systemIdPath;
    }

    public Path getSystemNamePath() {
        return systemNamePath;
    }

    public void setSystemNamePath(Path systemNamePath) {
        this.systemNamePath = systemNamePath;
    }

    public Path getFlagFieldPath() {
        return flagFieldPath;
    }

    public void setFlagFieldPath(Path flagFieldPath) {
        this.flagFieldPath = flagFieldPath;
    }

    public ApplicationCacheUtil getApplicationCacheUtil() {
        return applicationCacheUtil;
    }

    public void setApplicationCacheUtil(ApplicationCacheUtil applicationCacheUtil) {
        this.applicationCacheUtil = applicationCacheUtil;
    }

    public List<ObjectKey> getObjectKeys() {
        return objectKeys;
    }

    public void setObjectKeys(List<ObjectKey> objectKeys) {
        this.objectKeys = objectKeys;
    }

    public String getMdmRestBaseUrl() {
        return mdmRestBaseUrl;
    }

    public void setMdmRestBaseUrl(String mdmRestBaseUrl) {
        this.mdmRestBaseUrl = mdmRestBaseUrl;
    }

    public String getMdmRestUsername() {
        return mdmRestUsername;
    }

    public void setMdmRestUsername(String mdmRestUsername) {
        this.mdmRestUsername = mdmRestUsername;
    }

    public String getMdmrp() {
        return mdmrp;
    }

    public void setMdmrp(String mdmrp) {
        this.mdmrp = mdmrp;
    }

    public String getJitterbitBaseUrl() {
        return jitterbitBaseUrl;
    }

    public void setJitterbitBaseUrl(String jitterbitBaseUrl) {
        this.jitterbitBaseUrl = jitterbitBaseUrl;
    }

    public String getJitterbitUsername() {
        return jitterbitUsername;
    }

    public void setJitterbitUsername(String jitterbitUsername) {
        this.jitterbitUsername = jitterbitUsername;
    }

    public String getJitterbitrp() {
        return jitterbitrp;
    }

    public void setJitterbitrp(String jitterbitrp) {
        this.jitterbitrp = jitterbitrp;
    }

    public Path getDaqaStateFieldPath() {
        return daqaStateFieldPath;
    }

    public void setDaqaStateFieldPath(Path daqaStateFieldPath) {
        this.daqaStateFieldPath = daqaStateFieldPath;
    }
}
