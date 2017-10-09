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
import com.sereneast.orchestramdm.keysight.mdmcustom.rest.client.JitterbitRestClient;
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
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class PublishService implements UserService<TableViewEntitySelection>,ApplicationContextAware
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PublishService.class);

    private static final String DATA_SPACE = "BReference";

    private static final String DATA_SET = "Account";

    private static final String PATH_ACCOUNT = "root/Account";

    private static final String PATH_ADDRESS = "root/Address";

    private static final String ERROR_MESSAGE_REFERENCE_FAIL = "Error promoting records to Reference dataspace";

    private static final String ERROR_MESSAGE_JITTERBIT_FAIL = "Error publishing records to Jitterbit";

    private static final String ERROR_MESSAGE_ACCOUNT_NOT_PUBLISHED = "Can not publish address record. Related account has not been published.";

    private static final String ERROR_UPDATING_FLAG = "Error updating published flag";

    private static final int MAX_RETRY_COUNT = 5;

    private static final int RETRY_WAIT_MILLIS = 1000;

    private ApplicationContext applicationContext;
    private String objectName;
    private ApplicationCacheUtil applicationCacheUtil;

    private List<ObjectKey> objectKeys = new ArrayList<>();

    public PublishService()
    {
    }

    @Override
    public void setupObjectContext(
            UserServiceSetupObjectContext<TableViewEntitySelection> aContext,
            UserServiceObjectContextBuilder aBuilder)
    {
        LOGGER.debug("In Setup Object Context");
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
        }
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

    /**
     * Displays the form.
     */
    private void writeForm(UserServicePaneContext aContext, UserServicePaneWriter aWriter)
    {
        String errorMessage = "";
        try {
            LOGGER.debug("In writeform");
            List<OrchestraObject> orchestraObjects = new ArrayList<OrchestraObject>();
            List<Adaptation> adaptations = new ArrayList<>();
            for(ObjectKey objectKey: objectKeys){
                LOGGER.debug("Getting adaptation for objectKey="+objectKey.getName());
                Adaptation adaptationFromObjectKey = (Adaptation)aContext.getValueContext(objectKey).getValue();
                /*LOGGER.debug(adaptationFromObjectKey.getLabelOrName(Locale.ENGLISH));
                LOGGER.debug(adaptationFromObjectKey.getAdaptationName().getStringName());
                LOGGER.debug(adaptationFromObjectKey.getSchemaNode().toString());*/
                adaptations.add(adaptationFromObjectKey);
            }
            LOGGER.info("Selected records size : " + adaptations.size());
            Map<String, Path> pathFieldsMap = null;
            if(applicationCacheUtil==null){
//                LOGGER.debug("Initializing application cache util");
                applicationCacheUtil = new ApplicationCacheUtil();
            }
            if ("ACCOUNT".equals(objectName)) {
                pathFieldsMap = applicationCacheUtil.getObjectDirectFields(Paths._Account.class.getName());
            } else if ("ADDRESS".equals(objectName)) {
                pathFieldsMap = applicationCacheUtil.getObjectDirectFields(Paths._Address.class.getName());
            }
            Map<Integer,OrchestraObject> parentDsMap = new HashMap<>();
            for (Adaptation adaptation : adaptations) {
                LOGGER.debug("adaptation name="+adaptation.getAdaptationName());
                if ("ADDRESS".equals(objectName)) {
                    LOGGER.debug("Getting account.......");
                    final String condition = Paths._Account._Alias.format()+" = 'SUCCESS' and ("+Paths._Account._MDMAccountId.format()+" = "+Integer.valueOf(adaptation.get(Paths._Address._MDMAccountId).toString())+")";
                    LOGGER.debug("condition="+condition);
                    Adaptation container = adaptation.getContainer();
                    AdaptationTable accountTable = container.getTable(Paths._Account.getPathInSchema());
                    final RequestResult collectedAccounts = accountTable.createRequestResult(condition);
                    if(collectedAccounts!=null && !collectedAccounts.isEmpty()){
                        LOGGER.debug("account found: "+collectedAccounts.nextAdaptation().getString(Paths._Account._AccountName));
                    }else{
                        errorMessage = ERROR_MESSAGE_ACCOUNT_NOT_PUBLISHED;
                        throw new ApplicationRuntimeException(ERROR_MESSAGE_ACCOUNT_NOT_PUBLISHED);
                    }
                }
                OrchestraObject orchestraObject = new OrchestraObject();
                Map<String, OrchestraContent> jsonFieldsMap = new HashMap<>();
                for (String fieldName : pathFieldsMap.keySet()) {
                    LOGGER.debug(fieldName);
                    //   LOGGER.debug(String.valueOf(pathFieldsMap.get(fieldName)));
                    jsonFieldsMap.put(fieldName, new OrchestraContent(adaptation.get(pathFieldsMap.get(fieldName))));
                }
                orchestraObject.setContent(jsonFieldsMap);
                if("ACCOUNT".equals(objectName)) {
                    parentDsMap.put((Integer)orchestraObject.getContent().get("MDMAccountId").getContent(), orchestraObject);
                }else{
                    parentDsMap.put((Integer)orchestraObject.getContent().get("MDMAddressId").getContent(), orchestraObject);
                }
                orchestraObjects.add(orchestraObject);
            }
            OrchestraObjectList rows = new OrchestraObjectList();
            rows.setRows(orchestraObjects);
            ObjectMapper mapper = new ObjectMapper();
            LOGGER.info("Final JSON : \n" + mapper.writeValueAsString(rows));
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("updateOrInsert", "true");

            OrchestraRestClient orchestraRestClient = new OrchestraRestClient();
            orchestraRestClient.setBaseUrl("http://localhost:8080/ebx-dataservices/rest/data/v1");
            orchestraRestClient.setFeature(HttpAuthenticationFeature.basic("admin", "admin"));
            Response response = null;
            try {
                int retryCount = 0;
                do {
                    if(retryCount>0){
                        Thread.sleep(RETRY_WAIT_MILLIS);
                    }
                    if ("ACCOUNT".equals(objectName)) {
                        response = orchestraRestClient.insert(DATA_SPACE, DATA_SET, PATH_ACCOUNT, rows, parameters);
                    } else if ("ADDRESS".equals(objectName)) {
                        response = orchestraRestClient.insert(DATA_SPACE, DATA_SET, PATH_ADDRESS, rows, parameters);
                    }
                    retryCount++;
                }while(retryCount<MAX_RETRY_COUNT && (response==null || response.getStatus()>=300));
            }catch(Exception e) {
                errorMessage = ERROR_MESSAGE_REFERENCE_FAIL;
                throw new ApplicationRuntimeException(ERROR_MESSAGE_REFERENCE_FAIL,e);
            }
            if(response!=null && (response.getStatus()==200 || response.getStatus()==201)){
                LOGGER.debug("rest successful");
                //Begin publish to Jitterbit
                OrchestraObjectList jitterbitRows = rows;
                for(OrchestraObject orchestraObject:jitterbitRows.getRows()){
                    if ("ACCOUNT".equals(objectName)) {
                        orchestraObject.getContent().put("CalcAccountName",orchestraObject.getContent().get("AccountName"));
                    } else if ("ADDRESS".equals(objectName)) {
                        orchestraObject.getContent().put("State",orchestraObject.getContent().get("AddressState"));
                        orchestraObject.getContent().remove("AddressState");
                    }
                }
                Files.write(java.nio.file.Paths.get("publishedrecords.txt"), mapper.writeValueAsString(jitterbitRows).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

                JitterbitRestClient jitterbitRestClient = new JitterbitRestClient();
                if ("ACCOUNT".equals(objectName)) {
                    jitterbitRestClient.setBaseUrl("https://keysight.jitterbit.net/Development/1.0/MDM_Customer_to_Oracle");
                }else{
                    jitterbitRestClient.setBaseUrl("https://keysight.jitterbit.net/Development/1.0/MDM_Customer_Address");
                }
                jitterbitRestClient.setFeature(HttpAuthenticationFeature.basic("MDM_USER", "Keysight@123"));
                response = null;
                try {
                    int retryCount = 0;
                    do {
                        if(retryCount>0){
                            Thread.sleep(RETRY_WAIT_MILLIS);
                        }
                        response = jitterbitRestClient.insert(mapper.writeValueAsString(jitterbitRows), null);
                        retryCount++;
                    }while(retryCount<MAX_RETRY_COUNT && (response==null || response.getStatus()>=300));
                }catch(Exception e) {
                    errorMessage = ERROR_MESSAGE_JITTERBIT_FAIL;
                    throw new ApplicationRuntimeException(ERROR_MESSAGE_JITTERBIT_FAIL,e);
                }
                if(response!=null && (response.getStatus()==200 || response.getStatus()==201)) {
                    for (Adaptation record : adaptations) {
                        Procedure procedure = aContext1 -> {
                            ValueContextForUpdate valueContextForUpdate = aContext1.getContext(record.getAdaptationName());
                            if ("ACCOUNT".equals(objectName)) {
                                valueContextForUpdate.setValue("SUCCESS", Paths._Account._Alias);//TODO change
                            } else {
                                valueContextForUpdate.setValue("SUCCESS", Paths._Address._AddressLine3);//TODO change
                            }
                            aContext1.doModifyContent(record, valueContextForUpdate);
                        };
                        ProgrammaticService svc = ProgrammaticService.createForSession(aContext.getSession(), record.getHome());
                        ProcedureResult result = null;
                        try {
                            result = svc.execute(procedure);
                      /*      List<OrchestraObject> objList = new ArrayList<>();
                            OrchestraObjectList orows = new OrchestraObjectList();
                            if ("ACCOUNT".equals(objectName)) {
                                OrchestraObject obj = parentDsMap.get(record.get_int(Paths._Account._MDMAccountId));
                                obj.getContent().put("Alias",new OrchestraContent("SUCCESS"));
                                objList.add(obj);
                                orows.setRows(objList);
                                LOGGER.debug("orows:"+mapper.writeValueAsString(orows));
                                response = orchestraRestClient.insert(DATA_SPACE, DATA_SET, PATH_ACCOUNT, orows, parameters);
                            } else if ("ADDRESS".equals(objectName)) {
                                OrchestraObject obj = parentDsMap.get(record.get_int(Paths._Address._MDMAddressId));
                                obj.getContent().put("AddressLine3",new OrchestraContent("SUCCESS"));
                                objList.add(obj);
                                orows.setRows(objList);
                                LOGGER.debug("orows:"+mapper.writeValueAsString(orows));
                                response = orchestraRestClient.insert(DATA_SPACE, DATA_SET, PATH_ADDRESS, orows, parameters);
                            } */
                        }catch(Exception e){
                            errorMessage = ERROR_UPDATING_FLAG;
                            throw new ApplicationRuntimeException(ERROR_UPDATING_FLAG,e);
                        }
                        if (result == null || result.hasFailed()) {
                            LOGGER.info("proc failed " + result.getExceptionFullMessage(Locale.ENGLISH));
                            throw new ApplicationRuntimeException(ERROR_UPDATING_FLAG);
                        } else {
                            LOGGER.info("proc success ");
                        }
                    }
                }else{
                    errorMessage = ERROR_MESSAGE_JITTERBIT_FAIL;
                    throw new ApplicationRuntimeException(ERROR_MESSAGE_JITTERBIT_FAIL);
                }
            }else{
                errorMessage = ERROR_MESSAGE_REFERENCE_FAIL;
                throw new ApplicationRuntimeException(ERROR_MESSAGE_REFERENCE_FAIL);
            }
            aWriter.add("Publish successful");
        }catch (ClassNotFoundException | IllegalAccessException | IOException | ApplicationRuntimeException e){
            aWriter.add("ERROR: "+errorMessage);
            LOGGER.error("Error publishing records: \n",e);
        }
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName=objectName;
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
}
