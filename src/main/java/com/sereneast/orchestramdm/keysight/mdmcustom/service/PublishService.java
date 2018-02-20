package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.RequestResult;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.service.Procedure;
import com.orchestranetworks.service.ProcedureResult;
import com.orchestranetworks.service.ProgrammaticService;
import com.orchestranetworks.service.ValueContextForUpdate;
import com.orchestranetworks.ui.UICSSClasses;
import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.*;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.SpringContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.EbxProperties;
import com.sereneast.orchestramdm.keysight.mdmcustom.exception.ApplicationRuntimeException;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraContent;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObject;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObjectList;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.RestResponse;
import com.sereneast.orchestramdm.keysight.mdmcustom.rest.client.JitterbitRestClient;
import com.sereneast.orchestramdm.keysight.mdmcustom.rest.client.OrchestraRestClient;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ApplicationCacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class PublishService implements UserService<TableViewEntitySelection> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublishService.class);

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

    private static final String ERROR_MESSAGE_REFERENCE_FAIL = "An error occurred while promoting records to the Reference dataspace in MDM.";

    private static final String ERROR_MESSAGE_JITTERBIT_FAIL = "Publish request to Jitterbit failed with the following error.";

    private static final String ERROR_MESSAGE_PARENT_NOT_PUBLISHED = "Can not publish address. Account has not been published yet.";

    private static final String ERROR_MESSAGE_PARENT_NOT_FOUND = "Can not publish address. Account does not exist.";

    private static final String ERROR_UPDATING_FLAG = "An error occurred while updating Published field in MDM.";

    private static final String ERROR_MDM_DATA = "There's a problem with the data in MDM.";

    private static final int MAX_RETRY_COUNT = 5;

    private static final int RETRY_WAIT_MILLIS = 500;

    private int retryWaitJb = 500;

    private int retryWaitMdm = 500;

    private int maxRetryMdm  = 3;

    private int maxRetryJb  = 3;

    private static final String CROSS_REFERENCES_LABEL = "CrossReferences";

    private boolean checkParentIsPublished;

    private Path parentIdPath;

    private Path parentForeignKeyPath;

    private Path parentPathInSchema;

    private Path parentIdPathInChild;

    private Path childPathInSchema;

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
        String urlForClose = aWriter.getURLForEndingService();
        LOGGER.debug("urlForClose="+urlForClose);

        String divId = "publishMessageDiv";
        String loadingDivId = "divLoading";
        aWriter.add("<div ");
        aWriter.addSafeAttribute("id", divId);
        aWriter.addSafeAttribute("style", "height:100%;");
        aWriter.add("></div>");
        aWriter.add("<div ");
        aWriter.addSafeAttribute("id", loadingDivId);
        aWriter.add("></div>");

        aWriter.addJS_cr();
        aWriter.addJS_cr("function callAjax(url, targetDivId) {");
        aWriter.addJS_cr("  var ajaxHandler = new EBX_AJAXResponseHandler();");

        aWriter.addJS_cr("  ajaxHandler.handleAjaxResponseSuccess = function(responseContent) {");
        aWriter.addJS_cr("    var element = document.getElementById(targetDivId);");
        aWriter.addJS_cr("    element.innerHTML = responseContent;");
        aWriter.addJS_cr("    document.getElementById(\"divLoading\").classList.remove(\"show\");");
        aWriter.addJS_cr("  };");

        aWriter.addJS_cr("  ajaxHandler.handleAjaxResponseFailed = function(responseContent) {");
        aWriter.addJS_cr("    var element = document.getElementById(targetDivId);");
        aWriter.addJS_cr("    element.innerHTML = \"<span class='" + UICSSClasses.TEXT.ERROR
                + "'>An error occurred in processing the request.</span>\";");
        aWriter.addJS_cr("    document.getElementById(\"divLoading\").classList.remove(\"show\");");
        aWriter.addJS_cr("  }");

        aWriter.addJS_cr("  ajaxHandler.sendRequest(url);");
        aWriter.addJS_cr("document.getElementById(\"divLoading\").classList.add(\"show\");");
        aWriter.addJS_cr("}");

        // Generate the URL of the Ajax callback.
        String url = aWriter.getURLForAjaxRequest((userServiceAjaxContext, userServiceAjaxResponse) -> {
            PublishService.this.ajaxCallback(userServiceAjaxContext, userServiceAjaxResponse,aContext);
        });
        aWriter.addJS("ebx_confirm({question: \"Do you want to publish selected records?\", jsCommandYes: \"callAjax('"+url+"','"+divId+"');\", labelYes: \"Yes\", labelNo: \"No\", jsCommandNo: \"window.location.href='"+urlForClose+"';\" });");
    }

    private void ajaxCallback(UserServiceAjaxContext ajaxContext,UserServiceAjaxResponse anAjaxResponse,UserServicePaneContext aContext) {
        LOGGER.debug("In ajaxCallback");
        String finalMessage = null;
        List<Adaptation> selectedRecords = new ArrayList<>();
        try {
            EbxProperties ebxProperties = (EbxProperties) SpringContext.getApplicationContext().getBean("ebxProperties");
            if(ebxProperties.getRetryWaitJb()!=null){
                setRetryWaitJb(ebxProperties.getRetryWaitJb());
            }
            if(ebxProperties.getRetryWaitMdm()!=null){
                setRetryWaitMdm(ebxProperties.getRetryWaitMdm());
            }
            if(ebxProperties.getMaxRetryMdm()!=null){
                setMaxRetryMdm(ebxProperties.getMaxRetryMdm());
            }
            if(ebxProperties.getMaxRetryJb()!=null){
                setMaxRetryJb(ebxProperties.getMaxRetryJb());
            }
            for (ObjectKey objectKey : objectKeys) {
                LOGGER.debug("Getting adaptation for objectKey=" + objectKey.getName());
                Adaptation adaptation = (Adaptation) aContext.getValueContext(objectKey).getValue();
                if("Golden".equalsIgnoreCase(adaptation.getString(daqaStateFieldPath))){
                    selectedRecords.add(adaptation);
                }else{
                    throw new ApplicationRuntimeException("Only Golden records can be published. Please select Golden records and try again.");
                }
                if(!validateRecord(objectName,adaptation.getContainer(),adaptation)){
                    throw new ApplicationRuntimeException("At least one golden state address and at least one golden state business purpose is mandatory for publish.");
                }
            }
            finalMessage = promoteAndPublish(selectedRecords, aContext);
        }catch(ApplicationRuntimeException e){
            finalMessage = e.getMessage();
        }
        if(!"Selected records were promoted and published successfully.".equalsIgnoreCase(finalMessage)) {
            anAjaxResponse.getWriter().add("<div class=\"custom-error-image\"></div>");
            anAjaxResponse.getWriter().add("<div class=\"custom-error-header\">Oh No! Something went wrong.</div>");
            anAjaxResponse.getWriter().add("<div class=\"custom-error-message\">" + finalMessage);
            anAjaxResponse.getWriter().add("</div>");
        }else{
            anAjaxResponse.getWriter().add("<div class=\"custom-success-image\"></div>");
            anAjaxResponse.getWriter().add("<div class=\"custom-success-header\">Success!</div>");
            anAjaxResponse.getWriter().add("<div class=\"custom-success-message\">" + finalMessage);
            anAjaxResponse.getWriter().add("</div>");
        }
    }

    private boolean validateRecord(String objectName, Adaptation container,Adaptation adaptation) {
        String condition = null;
        AdaptationTable childTable = null;
        boolean valid = false;
        if ("ACCOUNT".equalsIgnoreCase(objectName)) {
            condition = Paths._Address._MDMAccountId.format()+" = '"+String.valueOf(adaptation.get(Paths._Account._MDMAccountId))+"'";
            childTable = container.getTable(Paths._Address.getPathInSchema());
        } else if ("ADDRESS".equalsIgnoreCase(objectName)) {
            condition = Paths._BusinessPurpose._MDMAddressId.format()+" = '"+String.valueOf(adaptation.get(Paths._Address._MDMAddressId))+"'";
            childTable = container.getTable(Paths._BusinessPurpose.getPathInSchema());
        }
        final RequestResult childTableRequestResult = childTable.createRequestResult(condition);
        if (childTableRequestResult != null && !childTableRequestResult.isEmpty()) {
            for (Adaptation child; (child = childTableRequestResult.nextAdaptation()) != null; ) {
                if("GOLDEN".equalsIgnoreCase(String.valueOf(child.get(daqaStateFieldPath)))){
                    if("ACCOUNT".equalsIgnoreCase(objectName)){
                        if(validateRecord("ADDRESS",container,child)){
                            valid = true;
                            break;
                        }
                    }else{
                        valid = true;
                        break;
                    }
                }
            }
        }
        return valid;
    }

    public String promoteAndPublish(List<Adaptation> selectedRecords,UserServicePaneContext aContext){
        String message = null;
        String errorMessage = null;
        List<OrchestraObject> recordsToUpdateInReference = new ArrayList<>();
        List<OrchestraObject>  recordsToUpdateInJitterbit = new ArrayList<>();
        List<OrchestraObject>  childrenToUpdateInJitterbit = new ArrayList<>();
        List<Adaptation> children = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        try {
            // Set up requests
            for (Adaptation adaptation : selectedRecords) {
                childrenToUpdateInJitterbit = new ArrayList<>();
                if (checkParentIsPublished) {
                    if (adaptation.get(parentForeignKeyPath) == null) {
                        throw new ApplicationRuntimeException(ERROR_MESSAGE_PARENT_NOT_FOUND);
                    }
                    LOGGER.debug("Getting account.......");
                    final String condition = flagFieldPath.format() + " = 'Y' and (" + parentIdPath.format() + " = " + Integer.valueOf(adaptation.get(parentForeignKeyPath).toString()) + ")";
                    LOGGER.debug("condition=" + condition);
                    Adaptation container = adaptation.getContainer();
                    AdaptationTable parentTable = container.getTable(parentPathInSchema);
                    final RequestResult parentTableRequestResult = parentTable.createRequestResult(condition);
                    if (parentTableRequestResult != null && !parentTableRequestResult.isEmpty()) {
                        LOGGER.debug("Parent found");
                    } else {
                        errorMessage = ERROR_MESSAGE_PARENT_NOT_PUBLISHED;
                        throw new ApplicationRuntimeException(ERROR_MESSAGE_PARENT_NOT_PUBLISHED);
                    }
                }

                if (parentIdPathInChild != null) {
                    Adaptation container = adaptation.getContainer();
                    AdaptationTable childTable = container.getTable(childPathInSchema);
                    final RequestResult childTableRequestResult = childTable.createRequestResult(parentIdPathInChild.format() + "='" + adaptation.get(objectPrimaryKeyPath) + "'");
                    if (childTableRequestResult != null && !childTableRequestResult.isEmpty()) {
                        Map<String, Path> fieldPathMap = null;
                        ApplicationCacheUtil applicationCacheUtil = (ApplicationCacheUtil) SpringContext.getApplicationContext().getBean("applicationCacheUtil");
                        try {
                            fieldPathMap = applicationCacheUtil.getObjectDirectFields(Paths._BusinessPurpose.class.getName());
                        } catch (IllegalAccessException | ClassNotFoundException e) {
                            throw new ApplicationRuntimeException("Error populating children", e);
                        }
                        for (Adaptation child; (child = childTableRequestResult.nextAdaptation()) != null; ) {
                            if (!"ADDRESS".equalsIgnoreCase(objectName)) {
                                if ("Golden".equalsIgnoreCase(child.getString(Paths._Address._DaqaMetaData_State))) {
                                    children.add(child);
                                }
                            } else {
                                if ("Golden".equalsIgnoreCase(child.getString(Paths._BusinessPurpose._DaqaMetaData_State))) {
                                    OrchestraObject orchestraChildToUpdateInJitterbit = new OrchestraObject();
                                    Map<String, OrchestraContent> jsonFieldsMapForJitterbit = new HashMap<>();
                                    for (String fieldName : fieldPathMap.keySet()) {
                                        Object fieldValue = child.get(fieldPathMap.get(fieldName));
                                        if (fieldValue instanceof List) {
                                            List objArray = (List) fieldValue;
                                            List<OrchestraContent> contentList = new ArrayList<>();
                                            for (Object obj : objArray) {
                                                contentList.add(new OrchestraContent(obj));
                                            }
                                            fieldValue = contentList;
                                        }
                                        jsonFieldsMapForJitterbit.put(fieldName, new OrchestraContent(fieldValue));
                                    }
                                    Path tempAddressKeyPath = objectPrimaryKeyPath;
                                    Path tempTablePath = tablePathInSchema;
                                    objectPrimaryKeyPath = Paths._BusinessPurpose._MDMPurposeId;
                                    tablePathInSchema = Paths._BusinessPurpose.getPathInSchema();
                                    List<OrchestraObject> suspects = new ArrayList<>();
                                    List<OrchestraObject> suspectsFound = getSuspects(child);
                                    OrchestraObject orchestraObject = new OrchestraObject();
                                    Map<String, OrchestraContent> jsonFieldsMap = new HashMap<>();
                                    jsonFieldsMap.put(objectPrimaryKeyPath.format().replaceAll("\\.\\/", ""), new OrchestraContent(child.get(objectPrimaryKeyPath)));
                                    jsonFieldsMap.put(systemIdPath.format().replaceAll("\\.\\/", ""), new OrchestraContent(child.get(systemIdPath)));
                                    jsonFieldsMap.put(systemNamePath.format().replaceAll("\\.\\/", ""), new OrchestraContent(child.get(systemNamePath)));
                                    jsonFieldsMap.put("DQState", new OrchestraContent(child.get(Path.parse("./DaqaMetaData/State"))));
                                    orchestraObject.setContent(jsonFieldsMap);
                                    suspects.add(orchestraObject);
                                    if (suspectsFound != null && !suspectsFound.isEmpty()) {
                                        suspects.addAll(suspectsFound);
                                    }
                                    objectPrimaryKeyPath = tempAddressKeyPath;
                                    tablePathInSchema = tempTablePath;
                                    if (suspects != null && !suspects.isEmpty()) {
                                        jsonFieldsMapForJitterbit.put(CROSS_REFERENCES_LABEL, new OrchestraContent(suspects));
                                    } else {
                                        jsonFieldsMapForJitterbit.put(CROSS_REFERENCES_LABEL, new OrchestraContent(null));
                                    }
                                    orchestraChildToUpdateInJitterbit.setContent(jsonFieldsMapForJitterbit);
                                    childrenToUpdateInJitterbit.add(orchestraChildToUpdateInJitterbit);
                                    children.add(child);
                                }
                            }
                        }
                    }
                }

                //Set up for update in Reference
                OrchestraObject orchestraObjectToUpdateInReference = new OrchestraObject();
                Map<String, OrchestraContent> jsonFieldsMapForReference = new HashMap<>();
                for (String fieldName : fieldPathMap.keySet()) {
                    Object fieldValue = adaptation.get(fieldPathMap.get(fieldName));
                    if (fieldValue instanceof List) {
                        List objArray = (List) fieldValue;
                        List<OrchestraContent> contentList = new ArrayList<>();
                        for (Object obj : objArray) {
                            contentList.add(new OrchestraContent(obj));
                        }
                        fieldValue = contentList;
                    }
                    /*if("LastPublished".equals(fieldName)){
                        if(fieldValue!=null)
                            fieldValue = sdf.format(adaptation.getDate(fieldPathMap.get(fieldName)));
                    }*/
                    jsonFieldsMapForReference.put(fieldName, new OrchestraContent(fieldValue));
                }
                orchestraObjectToUpdateInReference.setContent(jsonFieldsMapForReference);
                recordsToUpdateInReference.add(orchestraObjectToUpdateInReference);

                //Set up for update in Jitterbit
                OrchestraObject orchestraObjectToUpdateInJitterbit = new OrchestraObject();
                Map<String, OrchestraContent> jsonFieldsMapForJitterbit = new HashMap<>();
                if (!"BUSINESSPURPOSE".equalsIgnoreCase(objectName)) {
                    for (String fieldName : fieldPathMap.keySet()) {
                        Object fieldValue = adaptation.get(fieldPathMap.get(fieldName));
                        /*if("ACCOUNT".equals(objectName)) {
                            String published = adaptation.getString(Paths._Account._Published);
                            if(published!=null && StringUtils.isNotBlank(published) && "ProfileClass".equals(fieldName)){
                                continue;
                            }
                        }*/
                        if (fieldValue instanceof List) {
                            List objArray = (List) fieldValue;
                            List<OrchestraContent> contentList = new ArrayList<>();
                            for (Object obj : objArray) {
                                contentList.add(new OrchestraContent(obj));
                            }
                            fieldValue = contentList;
                        }
                        /*if("LastPublished".equals(fieldName)){
                            if(fieldValue!=null)
                                fieldValue = sdf.format(adaptation.getDate(fieldPathMap.get(fieldName)));
                        }*/
                        jsonFieldsMapForJitterbit.put(fieldName, new OrchestraContent(fieldValue));
                    }
                    //Find cross references for the object
                    List<OrchestraObject> suspects = new ArrayList<>();
                    if (!"BUSINESSPURPOSE".equalsIgnoreCase(objectName)) {
                        List<OrchestraObject> suspectsFound = getSuspects(adaptation);
                        OrchestraObject orchestraObject = new OrchestraObject();
                        Map<String, OrchestraContent> jsonFieldsMap = new HashMap<>();
                        jsonFieldsMap.put(objectPrimaryKeyPath.format().replaceAll("\\.\\/", ""), new OrchestraContent(adaptation.get(objectPrimaryKeyPath)));
                        jsonFieldsMap.put(systemIdPath.format().replaceAll("\\.\\/", ""), new OrchestraContent(adaptation.get(systemIdPath)));
                        jsonFieldsMap.put(systemNamePath.format().replaceAll("\\.\\/", ""), new OrchestraContent(adaptation.get(systemNamePath)));
                        jsonFieldsMap.put("DQState", new OrchestraContent(adaptation.get(Path.parse("./DaqaMetaData/State"))));
                        orchestraObject.setContent(jsonFieldsMap);
                        suspects.add(orchestraObject);
                        if (suspectsFound != null && !suspectsFound.isEmpty()) {
                            suspects.addAll(suspectsFound);
                        }
                    }
                    if (suspects != null && !suspects.isEmpty()) {
                        jsonFieldsMapForJitterbit.put(CROSS_REFERENCES_LABEL, new OrchestraContent(suspects));
                    } else {
                        jsonFieldsMapForJitterbit.put(CROSS_REFERENCES_LABEL, new OrchestraContent(null));
                    }
                    if ("ADDRESS".equalsIgnoreCase(objectName)) {
                        List<String>  operatingUnits = adaptation.getList(Paths._Address._OperatingUnit);
                        if(operatingUnits==null || operatingUnits.isEmpty()){
                            throw new ApplicationRuntimeException("Operating Unit is required for address.");
                        }
                        List<String> removedOperatingUnits = adaptation.getList(Paths._Address._RemovedOperatingUnits)!=null?adaptation.getList(Paths._Address._RemovedOperatingUnits):new ArrayList<>();
                        operatingUnits.addAll(removedOperatingUnits);
                        for(String operatingUnit:operatingUnits){
                            List<OrchestraObject> businessPurposesFinal = new ArrayList<>();
                            int activeBpOus = 0;
                            if (childrenToUpdateInJitterbit != null && !childrenToUpdateInJitterbit.isEmpty()) {
                                for(OrchestraObject businessPurposeObject:childrenToUpdateInJitterbit) {
                                    if(businessPurposeObject.getContent().get("OperatingUnit").getContent()==null || ((List)businessPurposeObject.getContent().get("OperatingUnit").getContent()).isEmpty()){
                                        //throw new ApplicationRuntimeException("OperatingUnit is required for Business Purpose");
                                        LOGGER.trace("skipping bp as ou is not present");
                                        continue;
                                    }
                                    List<OrchestraContent> bpOus = (List<OrchestraContent>)businessPurposeObject.getContent().get("OperatingUnit").getContent();
                                    List<OrchestraContent> removedOus = (List<OrchestraContent>)businessPurposeObject.getContent().get("RemovedOperatingUnits").getContent();
                                    removedOus = removedOus!=null?removedOus:new ArrayList<>();
                                    List<String> removedBpOus = new ArrayList<>();
                                    for(OrchestraContent removedOuContent: removedOus){
                                        removedBpOus.add(removedOuContent.getContent().toString());
                                    }
                                    bpOus.addAll(removedOus);
                                    String mdmPurposeId = String.valueOf(businessPurposeObject.getContent().get("MDMPurposeId").getContent());
                                    String mdmAddressId = String.valueOf(businessPurposeObject.getContent().get("MDMAddressId").getContent());
                                    String bpStatus = String.valueOf(businessPurposeObject.getContent().get("Status").getContent());
                                    for(OrchestraContent bpOuContent:bpOus) {
                                        if(!operatingUnits.contains(String.valueOf(bpOuContent.getContent())) && !removedBpOus.contains(String.valueOf(bpOuContent.getContent()))){
                                            throw new ApplicationRuntimeException(ERROR_MDM_DATA+" Operating unit "+String.valueOf(bpOuContent.getContent())+" found in Business Purpose "+mdmPurposeId+" does not exist for Address "+mdmAddressId+".");
                                        }
                                        if (String.valueOf(bpOuContent.getContent()).equals(operatingUnit)) {
                                            OrchestraObject businessPurposeToJb = new OrchestraObject();
                                            Map<String, OrchestraContent> bpToJbContent = new HashMap<>();
                                            bpToJbContent.putAll(businessPurposeObject.getContent());
                                            List<OrchestraContent> primaryInOus = (List<OrchestraContent>)bpToJbContent.get("Primary").getContent();
                                            boolean primary = false;
                                            for(OrchestraContent content:primaryInOus){
                                                if(content.getContent().toString().equals(operatingUnit)){
                                                    primary=true;
                                                    break;
                                                }
                                            }
                                            if(primary){
                                                bpToJbContent.put("Primary",new OrchestraContent("Y"));
                                            }else{
                                                bpToJbContent.put("Primary",new OrchestraContent("N"));
                                            }
                                            bpToJbContent.remove("OperatingUnit");
                                            if(removedBpOus.contains(operatingUnit) || "I".equals(bpStatus)){
                                                bpToJbContent.put("Status",new OrchestraContent("I"));
                                            }else{
                                                bpToJbContent.put("Status",new OrchestraContent("A"));
                                                activeBpOus++;
                                            }
                                            bpToJbContent.remove("RemovedOperatingUnits");
                                            businessPurposeToJb.setContent(bpToJbContent);
                                            businessPurposesFinal.add(businessPurposeToJb);
                                        }
                                    }
                                }
                                if(!businessPurposesFinal.isEmpty()) {
                                    jsonFieldsMapForJitterbit.put("BusinessPurpose", new OrchestraContent(businessPurposesFinal));
                                }else if(!removedOperatingUnits.contains(operatingUnit)){
                                    throw new ApplicationRuntimeException(ERROR_MDM_DATA+" Business Purpose does not exist for Operating Unit "+operatingUnit+".");
                                    //jsonFieldsMapForJitterbit.put("BusinessPurpose", new OrchestraContent(null));
                                }
                                if(activeBpOus==0 && !removedOperatingUnits.contains(operatingUnit)){
                                    throw new ApplicationRuntimeException(ERROR_MDM_DATA+" Business Purpose does not exist for Operating Unit "+operatingUnit+".");
                                }
                            } else {
                                throw new ApplicationRuntimeException(ERROR_MDM_DATA+" Business Purpose does not exist for Operating Unit "+operatingUnit+".");
                                //jsonFieldsMapForJitterbit.put("BusinessPurpose", new OrchestraContent(null));
                            }
                            Map<String, OrchestraContent> addressContent = new HashMap<>();
                            addressContent.putAll(jsonFieldsMapForJitterbit);
                            addressContent.put("OperatingUnit",new OrchestraContent(operatingUnit));
                            if(removedOperatingUnits!=null && removedOperatingUnits.contains(operatingUnit)){
                                addressContent.put("OperatingUnitStatus",new OrchestraContent("I"));
                            }else{
                                addressContent.put("OperatingUnitStatus",new OrchestraContent("A"));
                            }
                            addressContent.remove("RemovedOperatingUnits");
                            OrchestraObject addressObjectForJb = new OrchestraObject();
                            addressObjectForJb.setContent(addressContent);
                            recordsToUpdateInJitterbit.add(addressObjectForJb);
                        }
                    }else {
                        orchestraObjectToUpdateInJitterbit.setContent(jsonFieldsMapForJitterbit);
                        recordsToUpdateInJitterbit.add(orchestraObjectToUpdateInJitterbit);
                    }
                }
            }//for selected records

            //Execute updates
            promoteToReference(recordsToUpdateInReference);
            LOGGER.info("Promoted {} to reference",objectName);
            if(recordsToUpdateInJitterbit!=null && !recordsToUpdateInJitterbit.isEmpty()) {
                publishToJitterbit(recordsToUpdateInJitterbit);
                LOGGER.info("Published {} to Jitterbit",objectName);
            }
            if(!"BUSINESSPURPOSE".equalsIgnoreCase(objectName)) {
                updateFlagToSuccess(aContext, selectedRecords, recordsToUpdateInReference);
                LOGGER.info("Updated {} flag",objectName);
            }
            if(!children.isEmpty()){
                try {
                    PublishService childPublishService = null;
                    if("ACCOUNT".equalsIgnoreCase(objectName)){
                        childPublishService = new AddressPublishService();
                        LOGGER.debug("publishing Account children");
                    }else if("ADDRESS".equalsIgnoreCase(objectName)){
                        childPublishService = new BusinessPurposePublishService();
                        LOGGER.debug("publishing Address children");
                    }
                    childPublishService.setCheckParentIsPublished(false);
                    message = childPublishService.promoteAndPublish(children,aContext);
                } catch (ClassNotFoundException | IllegalAccessException e) {
                    throw new ApplicationRuntimeException("Error publishing children",e);
                }
            }else{
                message = "Selected records were promoted and published successfully.";
            }
        }catch(ApplicationRuntimeException e){
            String rootCauseMessage = e.getRootCause()!=null?" Root Cause: "+e.getRootCause().getMessage():"";
            message = e.getMessage()+rootCauseMessage;
            if("ADDRESS".equalsIgnoreCase(objectName)){
                message=message+" None of the addresses were published.";
            }
            LOGGER.error("Error publishing records: \n",e);
        }
        return message;
    }

    public void promoteToReference(List<OrchestraObject> recordsToUpdateInReference){
        try{
            ObjectMapper mapper = new ObjectMapper();
            OrchestraObjectList orchestraObjectList = new OrchestraObjectList();
            orchestraObjectList.setRows(recordsToUpdateInReference);
            OrchestraRestClient orchestraRestClient = (OrchestraRestClient) SpringContext.getApplicationContext().getBean("orchestraRestClient");
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("updateOrInsert", "true");
            RestResponse response = null;
            int retryCount = 0;

            do {
                if(retryCount>0){
                    Thread.sleep(retryWaitMdm);
                }
                LOGGER.debug("Promoting to Reference: \n"+mapper.writeValueAsString(orchestraObjectList));
                response = orchestraRestClient.promote(referenceDataSpaceUrl, referenceDataSetUrl, tablePathUrl, orchestraObjectList, parameters);
                LOGGER.info("MDM {} Retry attempt:{} Status:{}",objectName,retryCount,response.getStatus());
                retryCount++;
            }while(retryCount<maxRetryMdm && (response==null || response.getStatus()>=300));
            if(response.getStatus()!=200 && response.getStatus()!=201){
                throw new ApplicationRuntimeException("Error promoting to reference: "+String.valueOf(mapper.writeValueAsString(response.getResponseBody())));
            }
        }catch(InterruptedException | IOException e){
            throw new ApplicationRuntimeException(ERROR_MESSAGE_REFERENCE_FAIL,e);
        }
    }

    private void publishToJitterbit(List<OrchestraObject> recordsToUpdateInJitterbit){
        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.setDateFormat(df);
            for(OrchestraObject orchestraObject:recordsToUpdateInJitterbit){
                if(orchestraObject.getContent().get("Published").getContent()==null || StringUtils.isBlank(orchestraObject.getContent().get("Published").getContent().toString())){
                    orchestraObject.getContent().put("Published",new OrchestraContent("I"));
                }else{
                    orchestraObject.getContent().put("Published",new OrchestraContent("U"));
                }
                if(orchestraObject.getContent().get("AddressState")!=null && orchestraObject.getContent().get("AddressState").getContent()!=null
                        && orchestraObject.getContent().get("AddressState").getContent().toString().contains("|")){
                    orchestraObject.getContent().put("AddressState",new OrchestraContent(orchestraObject.getContent().get("AddressState").getContent().toString().split("\\|")[1]));
                }
            }
            OrchestraObjectList orchestraObjectList = new OrchestraObjectList();
            orchestraObjectList.setRows(recordsToUpdateInJitterbit);

            RestResponse response = null;
            JitterbitRestClient jitterbitRestClient = (JitterbitRestClient)SpringContext.getApplicationContext().getBean("jitterbitRestClient");

            int retryCount = 0;
            do {
                if (retryCount > 0) {
                    Thread.sleep(retryWaitJb);
                }
                response = jitterbitRestClient.insert(mapper.writeValueAsString(orchestraObjectList), null,objectName.toLowerCase());
                LOGGER.info("JB {} Retry attempt:{} Status:{}",objectName,retryCount,response.getStatus());
                retryCount++;
            } while (retryCount < maxRetryJb && (response == null || response.getStatus() >= 300));
            if(response.getStatus()!=200 && response.getStatus()!=201){
                throw new ApplicationRuntimeException("Error publishing to Jitterbit: "+String.valueOf(response.getResponseBody().get("errorMsg")));
            }
        }catch(InterruptedException | IOException e){
            throw new ApplicationRuntimeException(ERROR_MESSAGE_JITTERBIT_FAIL,e);
        }
    }

    private void updateFlagToSuccess(UserServicePaneContext aContext,List<Adaptation> selectedRecords,List<OrchestraObject> recordsToUpdateInReference){
        Date currentTime = Date.from(Instant.now());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        for (int i=0;i<selectedRecords.size();i++) {
            Adaptation record = selectedRecords.get(i);
            if(!"Y".equals(record.get(Paths._Account._Published))) {
                Procedure procedure = procedureContext -> {
                    ValueContextForUpdate valueContextForUpdate = procedureContext.getContext(record.getAdaptationName());
                    valueContextForUpdate.setValue("Y", flagFieldPath);//TODO change
                    valueContextForUpdate.setValue(currentTime, Paths._Address._LastPublished);
                    procedureContext.doModifyContent(record, valueContextForUpdate);
                };
                ProgrammaticService svc = ProgrammaticService.createForSession(aContext.getSession(), record.getHome());
                ProcedureResult result = null;
                result = svc.execute(procedure);
                if (result == null || result.hasFailed()) {
                    LOGGER.debug("proc failed " + result.getExceptionFullMessage(Locale.ENGLISH));
                    throw new ApplicationRuntimeException(ERROR_UPDATING_FLAG);
                } else {
                    LOGGER.debug("proc success ");
                }
            }
            OrchestraObject obj = recordsToUpdateInReference.get(i);
            obj.getContent().put(flagFieldPath.format().replaceAll("\\.\\/", ""),new OrchestraContent("Y"));
            obj.getContent().put(Path.parse("./LastPublished").format().replaceAll("\\.\\/", ""),new OrchestraContent(sdf.format(currentTime)));
        }
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(df);
        OrchestraRestClient orchestraRestClient = (OrchestraRestClient) SpringContext.getApplicationContext().getBean("orchestraRestClient");        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("updateOrInsert", "true");
        OrchestraObjectList objList = new OrchestraObjectList();
        objList.setRows(recordsToUpdateInReference);
        try {
//            LOGGER.debug("Request to update flag:"+mapper.writeValueAsString(objList));
            RestResponse response = orchestraRestClient.promote(referenceDataSpaceUrl, referenceDataSetUrl, tablePathUrl, objList, parameters);
            if(response.getStatus()!=200 && response.getStatus()!=201){
                throw new ApplicationRuntimeException(ERROR_UPDATING_FLAG+" "+String.valueOf(mapper.writeValueAsString(response.getResponseBody())));
            }
        } catch (IOException e) {
            throw new ApplicationRuntimeException(ERROR_UPDATING_FLAG,e);
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
                jsonFieldsMap.put("DQState",new OrchestraContent(tableRequestResultRecord.get(Path.parse("./DaqaMetaData/State"))));
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

    public Path getDaqaStateFieldPath() {
        return daqaStateFieldPath;
    }

    public void setDaqaStateFieldPath(Path daqaStateFieldPath) {
        this.daqaStateFieldPath = daqaStateFieldPath;
    }

    public boolean isCheckParentIsPublished() {
        return checkParentIsPublished;
    }

    public void setCheckParentIsPublished(boolean checkParentIsPublished) {
        this.checkParentIsPublished = checkParentIsPublished;
    }

    public Path getParentIdPath() {
        return parentIdPath;
    }

    public void setParentIdPath(Path parentIdPath) {
        this.parentIdPath = parentIdPath;
    }

    public Path getParentForeignKeyPath() {
        return parentForeignKeyPath;
    }

    public void setParentForeignKeyPath(Path parentForeignKeyPath) {
        this.parentForeignKeyPath = parentForeignKeyPath;
    }

    public Path getParentPathInSchema() {
        return parentPathInSchema;
    }

    public void setParentPathInSchema(Path parentPathInSchema) {
        this.parentPathInSchema = parentPathInSchema;
    }

    public Path getParentIdPathInChild() {
        return parentIdPathInChild;
    }

    public void setParentIdPathInChild(Path parentIdPathInChild) {
        this.parentIdPathInChild = parentIdPathInChild;
    }

    public Path getChildPathInSchema() {
        return childPathInSchema;
    }

    public void setChildPathInSchema(Path childPathInSchema) {
        this.childPathInSchema = childPathInSchema;
    }

    public int getRetryWaitJb() {
        return retryWaitJb;
    }

    public void setRetryWaitJb(int retryWaitJb) {
        this.retryWaitJb = retryWaitJb;
    }

    public int getRetryWaitMdm() {
        return retryWaitMdm;
    }

    public void setRetryWaitMdm(int retryWaitMdm) {
        this.retryWaitMdm = retryWaitMdm;
    }

    public int getMaxRetryMdm() {
        return maxRetryMdm;
    }

    public void setMaxRetryMdm(int maxRetryMdm) {
        this.maxRetryMdm = maxRetryMdm;
    }

    public int getMaxRetryJb() {
        return maxRetryJb;
    }

    public void setMaxRetryJb(int maxRetryJb) {
        this.maxRetryJb = maxRetryJb;
    }
}
