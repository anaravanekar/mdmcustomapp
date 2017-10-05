package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationName;
import com.onwbp.adaptation.RequestResult;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.service.*;
import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.*;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraContent;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObject;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObjectList;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraResponseDetails;
import com.sereneast.orchestramdm.keysight.mdmcustom.rest.client.OrchestraRestClient;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ApplicationCacheUtil;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

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
            for (Adaptation adaptation : adaptations) {
                LOGGER.debug("adaptation name="+adaptation.getAdaptationName());
                OrchestraObject orchestraObject = new OrchestraObject();
                Map<String, OrchestraContent> jsonFieldsMap = new HashMap<>();
                for (String fieldName : pathFieldsMap.keySet()) {
                    LOGGER.debug(fieldName);
                 //   LOGGER.debug(String.valueOf(pathFieldsMap.get(fieldName)));
                    jsonFieldsMap.put(fieldName, new OrchestraContent(adaptation.get(pathFieldsMap.get(fieldName))));
                }
                orchestraObject.setContent(jsonFieldsMap);
                orchestraObjects.add(orchestraObject);
            }
            OrchestraObjectList rows = new OrchestraObjectList();
            rows.setRows(orchestraObjects);
            ObjectMapper mapper = new ObjectMapper();
            LOGGER.info("Final JSON : \n" + mapper.writeValueAsString(rows));
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("updateOrInsert", "true");
            try {
                OrchestraRestClient orchestraRestClient = new OrchestraRestClient();
                orchestraRestClient.setBaseUrl("http://localhost:8080/ebx-dataservices/rest/data/v1");
                orchestraRestClient.setFeature(HttpAuthenticationFeature.basic("admin", "admin"));
				OrchestraResponseDetails responseDetails = null;
				if("ACCOUNT".equals(objectName)){
                    responseDetails = orchestraRestClient.insert(DATA_SPACE, DATA_SET, PATH_ACCOUNT, rows, parameters);
                }else if("ADDRESS".equals(objectName)){
                    responseDetails = orchestraRestClient.insert(DATA_SPACE, DATA_SET, PATH_ADDRESS, rows, parameters);
                }
			    if(responseDetails!=null){
                    for(Adaptation record : adaptations) {
                        Procedure procedure = aContext1 -> {
                            ValueContextForUpdate valueContextForUpdate = aContext1.getContext(record.getAdaptationName());
                            if("ACCOUNT".equals(objectName)) {
                                valueContextForUpdate.setValue("SUCCESS", Paths._Account._Alias);//TODO change
                            }else{
                                valueContextForUpdate.setValue("SUCCESS", Paths._Address._AddressLine3);//TODO change
                            }
                            aContext1.doModifyContent(record, valueContextForUpdate);
                        };
                        ProgrammaticService svc = ProgrammaticService.createForSession(aContext.getSession(), record.getHome());
                        ProcedureResult result = svc.execute(procedure);
                        if (result.hasFailed()) {
                            LOGGER.info("proc failed " + result.getExceptionFullMessage(Locale.ENGLISH));
                        } else {
                            LOGGER.info("proc success ");
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error making rest call ", e);
            }
            Files.write(java.nio.file.Paths.get("publishedrecords.txt"), mapper.writeValueAsString(rows).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            aWriter.add("Publish successful");
        }catch (ClassNotFoundException | IllegalAccessException | IOException e){
            LOGGER.error("Error publishing ",e);
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
