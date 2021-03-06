package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.RequestResult;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.schema.trigger.*;
import com.orchestranetworks.service.OperationException;
import com.orchestranetworks.service.ProcedureContext;
import com.orchestranetworks.service.ValueContextForUpdate;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.SpringContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.EbxProperties;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraContent;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObject;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObjectList;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.RestResponse;
import com.sereneast.orchestramdm.keysight.mdmcustom.rest.client.OrchestraRestClient;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ApplicationCacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

public class GenericTrigger extends TableTrigger {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericTrigger.class);

    private Path daqaTargetFieldPath = Paths._Account._DaqaMetaData_TargetRecord;

    private Path objectPrimaryKeyPath = Paths._Account._MDMAccountId;

    private Path stateFieldPath = Paths._Account._DaqaMetaData_State;

    private Path mergeOriginPath = Paths._Account._DaqaMetaData_MergeOrigin;

    private Path timestampPath = Paths._Account._DaqaMetaData_Timestamp;

    private Path countryPath = Paths._Account._Country;

    private Path publishedFieldPath = Paths._Account._Published;

    private String objectName;

    private List<String> lovsToMerge;

    private boolean initialized;

    private List<Path> languageDetectionSourceFields;

    protected static LanguageDetector languageDetector;

    private Path localeFieldPath;

    @Override
    public void setup(TriggerSetupContext triggerSetupContext) {

    }

    public void initialize(){

    }


    public void handleAfterCreate(AfterCreateOccurrenceContext aContext) throws OperationException{
        if("CMDReference".equalsIgnoreCase(aContext.getAdaptationHome().getKey().getName())
                && "Account".equalsIgnoreCase(aContext.getOccurrenceContext().getAdaptationInstance().getAdaptationName().getStringName())) {
            initialize();
            ApplicationCacheUtil applicationCacheUtil = (ApplicationCacheUtil) SpringContext.getApplicationContext().getBean("applicationCacheUtil");
            ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
            if(aContext.getOccurrenceContext().getValue(Paths._Account._Published)!=null) {
                ValueContextForUpdate valueContextForUpdate = aContext.getProcedureContext().getContext(aContext.getAdaptationOccurrence().getAdaptationName());
                valueContextForUpdate.setValue(null, Paths._Account._Published);
                aContext.getProcedureContext().doModifyContent(aContext.getAdaptationOccurrence(), valueContextForUpdate);
            }
            if(aContext.getOccurrenceContext().getValue(Paths._Address._AssignedTo)==null){
                String userId = aContext.getSession().getUserReference().getUserId();
                EbxProperties ebxProperties = (EbxProperties)SpringContext.getApplicationContext().getBean("ebxProperties");
                List<String> doNotAssignToUser = ebxProperties.getDoNotAssignToUsers();
                if(doNotAssignToUser==null || doNotAssignToUser.isEmpty() || !doNotAssignToUser.contains(userId)) {
                    ValueContextForUpdate valueContextForUpdate = aContext.getProcedureContext().getContext(aContext.getAdaptationOccurrence().getAdaptationName());
                    valueContextForUpdate.setValue(userId, Paths._Address._AssignedTo);
                    aContext.getProcedureContext().doModifyContent(aContext.getAdaptationOccurrence(), valueContextForUpdate);
                }
            }
            if ("ACCOUNT".equalsIgnoreCase(objectName)) {
                boolean update = false;
                ValueContextForUpdate valueContextForUpdate = aContext.getProcedureContext().getContext(aContext.getAdaptationOccurrence().getAdaptationName());
                List countryList = (List) aContext.getOccurrenceContext().getValue(Paths._Account._Country);
                if (countryList != null && !countryList.isEmpty()) {
                    String countryCode = (String) countryList.get(0);
                    /*String countryReferenceTablePath = "root/CountryReferenceFields";
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("filter", "CountryCode='" + countryCode + "'");
                    OrchestraRestClient orchestraRestClient = (OrchestraRestClient) SpringContext.getApplicationContext().getBean("orchestraRestClient");
                    OrchestraObjectListResponse orchestraObjectListResponse = null;
                    try {
                        orchestraObjectListResponse = orchestraRestClient.get("BReference", "Account", countryReferenceTablePath, parameters);
                    } catch (IOException e) {
                        throw new ApplicationOperationException("Error getting Profile Class and Region for country");
                    }
                    Map<String, String> resultObject = new HashMap<>();*/
                    if("BR".equals(countryCode)){
                        valueContextForUpdate.setValue("Brazil Bank Collection", Paths._Account._PaymentReceiptMethod);
                        update=true;
                    }else if("US".equals(countryCode) || "CA".equals(countryCode) || "GU".equals(countryCode) || "SG".equals(countryCode) ||
                            "JP".equals(countryCode) || "TH".equals(countryCode)){
                        valueContextForUpdate.setValue("Lockbox", Paths._Account._PaymentReceiptMethod);
                        update=true;
                    }else{
                        valueContextForUpdate.setValue("Manual Payment", Paths._Account._PaymentReceiptMethod);
                        update=true;
                    }

                    Map<String,Map<String,String>> countryReferenceFieldsMap = applicationCacheUtil.CountryReferenceFieldsMap("BReference");
                    Map<String,String> resultItem = countryReferenceFieldsMap!=null?countryReferenceFieldsMap.get(countryCode):null;
                    if(resultItem!=null){
                        valueContextForUpdate.setValue(resultItem.get("ProfileClass"), Paths._Account._ProfileClass);
                        valueContextForUpdate.setValue(resultItem.get("Region"), Paths._Account._Region);
                        update=true;
                    }

                   /* AdaptationTable countryReferenceFieldsTable = aContext.getTable().getContainerAdaptation().getTable(Paths._CountryReferenceFields.getPathInSchema());
                    RequestResult tableRequestResult = countryReferenceFieldsTable.createRequestResult(Paths._CountryReferenceFields._CountryCode.format()+" = '"+countryCode+"'");
                    if (tableRequestResult != null && !tableRequestResult.isEmpty()) {
                        Adaptation resultRecord = tableRequestResult.nextAdaptation();
                        // if (orchestraObjectListResponse != null && orchestraObjectListResponse.getRows() != null && !orchestraObjectListResponse.getRows().isEmpty()) {
                        //OrchestraObjectResponse objectResponse = orchestraObjectListResponse.getRows().get(0);
                        //Map<String, OrchestraContent> content = objectResponse.getContent();
                        String profileClass = resultRecord.getString(Paths._CountryReferenceFields._ProfileClass);//content.get("ProfileClass").getContent() != null ? content.get("ProfileClass").getContent().toString() : null;
                        String region = resultRecord.getString(Paths._CountryReferenceFields._Region);;//content.get("Region").getContent() != null ? content.get("Region").getContent().toString() : null;
                        valueContextForUpdate.setValue(profileClass, Paths._Account._ProfileClass);
                        valueContextForUpdate.setValue(region, Paths._Account._Region);
                    }*/
                }
                if(aContext.getOccurrenceContext().getValue(Paths._Account._PaymentStartDate)==null){
                    valueContextForUpdate.setValue(Date.from(utc.toInstant()),Paths._Account._PaymentStartDate);
                    update=true;
                }
                if(update){
                    aContext.getProcedureContext().doModifyContent(aContext.getAdaptationOccurrence(), valueContextForUpdate);
                }
            }
            //get application context
            if("ADDRESS".equalsIgnoreCase(objectName)) {
                /*for(int i=1;i<=4;i++) {
                    validateByteLength("Address Line "+i, String.valueOf(aContext.getOccurrenceContext().getValue(Path.parse("./AddressLine"+i))), 150);
                }*/
                boolean update = false;
                ValueContextForUpdate valueContextForUpdate = aContext.getProcedureContext().getContext(aContext.getAdaptationOccurrence().getAdaptationName());
                if("JP".equals(aContext.getOccurrenceContext().getValue(Paths._Address._Country))){
                    if(aContext.getOccurrenceContext().getValue(Paths._Address._SendAcknowledgement)==null) {
                        update=true;
                        valueContextForUpdate.setValue("N", Paths._Address._SendAcknowledgement);
                    }
                    if(aContext.getOccurrenceContext().getValue(Paths._Address._InvoiceCopies)==null) {
                        update=true;
                        valueContextForUpdate.setValue("0.2", Paths._Address._InvoiceCopies);
                    }
                }else if("PL".equals(aContext.getOccurrenceContext().getValue(Paths._Address._Country))){
                    if(aContext.getOccurrenceContext().getValue(Paths._Address._SendAcknowledgement)==null) {
                        update=true;
                        valueContextForUpdate.setValue("N", Paths._Address._SendAcknowledgement);
                    }
                    if(aContext.getOccurrenceContext().getValue(Paths._Address._InvoiceCopies)==null) {
                        update=true;
                        valueContextForUpdate.setValue("1", Paths._Address._InvoiceCopies);
                    }
                }else if("PH".equals(aContext.getOccurrenceContext().getValue(Paths._Address._Country))){
                    if(aContext.getOccurrenceContext().getValue(Paths._Address._SendAcknowledgement)==null) {
                        update=true;
                        valueContextForUpdate.setValue("Y", Paths._Address._SendAcknowledgement);
                    }
                    if(aContext.getOccurrenceContext().getValue(Paths._Address._InvoiceCopies)==null) {
                        update=true;
                        valueContextForUpdate.setValue("3", Paths._Address._InvoiceCopies);
                    }
                }else if("GU".equals(aContext.getOccurrenceContext().getValue(Paths._Address._Country)) ||
                        "PR".equals(aContext.getOccurrenceContext().getValue(Paths._Address._Country)) ||
                        "VE".equals(aContext.getOccurrenceContext().getValue(Paths._Address._Country)) ||
                        "US".equals(aContext.getOccurrenceContext().getValue(Paths._Address._Country))){
                    if(aContext.getOccurrenceContext().getValue(Paths._Address._SendAcknowledgement)==null) {
                        update=true;
                        valueContextForUpdate.setValue("Y", Paths._Address._SendAcknowledgement);
                    }
                    if(aContext.getOccurrenceContext().getValue(Paths._Address._InvoiceCopies)==null) {
                        update=true;
                        valueContextForUpdate.setValue("2", Paths._Address._InvoiceCopies);
                    }
                }else if("IN".equals(aContext.getOccurrenceContext().getValue(Paths._Address._Country))){
                    if(aContext.getOccurrenceContext().getValue(Paths._Address._SendAcknowledgement)==null) {
                        update=true;
                        valueContextForUpdate.setValue("Y", Paths._Address._SendAcknowledgement);
                    }
                    if(aContext.getOccurrenceContext().getValue(Paths._Address._InvoiceCopies)==null) {
                        update=true;
                        valueContextForUpdate.setValue("0.3", Paths._Address._InvoiceCopies);
                    }
                }else if("KR".equals(aContext.getOccurrenceContext().getValue(Paths._Address._Country))){
                    if(aContext.getOccurrenceContext().getValue(Paths._Address._SendAcknowledgement)==null) {
                        update=true;
                        valueContextForUpdate.setValue("Y", Paths._Address._SendAcknowledgement);
                    }
                    if(aContext.getOccurrenceContext().getValue(Paths._Address._InvoiceCopies)==null) {
                        update=true;
                        valueContextForUpdate.setValue("3", Paths._Address._InvoiceCopies);
                    }
                }else if("MX".equals(aContext.getOccurrenceContext().getValue(Paths._Address._Country))){
                    if(aContext.getOccurrenceContext().getValue(Paths._Address._SendAcknowledgement)==null) {
                        update=true;
                        valueContextForUpdate.setValue("Y", Paths._Address._SendAcknowledgement);
                    }
                    if(aContext.getOccurrenceContext().getValue(Paths._Address._InvoiceCopies)==null) {
                        update=true;
                        valueContextForUpdate.setValue("4", Paths._Address._InvoiceCopies);
                    }
                }else if("AR".equals(aContext.getOccurrenceContext().getValue(Paths._Address._Country))){
                    if(aContext.getOccurrenceContext().getValue(Paths._Address._SendAcknowledgement)==null) {
                        update=true;
                        valueContextForUpdate.setValue("Y", Paths._Address._SendAcknowledgement);
                    }
                    if(aContext.getOccurrenceContext().getValue(Paths._Address._InvoiceCopies)==null) {
                        update=true;
                        valueContextForUpdate.setValue("5", Paths._Address._InvoiceCopies);
                    }
                }else{
                    if(aContext.getOccurrenceContext().getValue(Paths._Address._SendAcknowledgement)==null) {
                        update=true;
                        valueContextForUpdate.setValue("Y", Paths._Address._SendAcknowledgement);
                    }
                    if(aContext.getOccurrenceContext().getValue(Paths._Address._InvoiceCopies)==null) {
                        update=true;
                        valueContextForUpdate.setValue("1", Paths._Address._InvoiceCopies);
                    }
                }
                if(aContext.getOccurrenceContext().getValue(Paths._Address._OperatingUnit)!=null && !((List)aContext.getOccurrenceContext().getValue(Paths._Address._OperatingUnit)).isEmpty()){
                    valueContextForUpdate.setValue(((List)aContext.getOccurrenceContext().getValue(Paths._Address._OperatingUnit)).get(0), Paths._Address._FirstOperatingUnit);
                    update = true;
                }
                String countryCode = aContext.getAdaptationOccurrence().getString(Paths._Address._Country);
                /*if(countryCode!=null){
                    validateStateAndProvince(countryCode,aContext.getAdaptationOccurrence().getString(Paths._Address._AddressState),
                            aContext.getAdaptationOccurrence().getString(Paths._Address._Province),false);
                    validateStateAndProvince(countryCode,aContext.getAdaptationOccurrence().getString(Paths._Address._StateLocalLanguage),
                            aContext.getAdaptationOccurrence().getString(Paths._Address._ProvinceLocalLanguage),true);
                }*/
                if("null".equals(aContext.getAdaptationOccurrence().getString(Paths._Address._AddressState))){
                    update = true;
                    valueContextForUpdate.setValue(null,Paths._Address._AddressState);
                }
                if("null".equals(aContext.getAdaptationOccurrence().getString(Paths._Address._Province))){
                    update = true;
                    valueContextForUpdate.setValue(null,Paths._Address._Province);
                }
                if("null".equals(aContext.getAdaptationOccurrence().getString(Paths._Address._StateLocalLanguage))){
                    update = true;
                    valueContextForUpdate.setValue(null,Paths._Address._StateLocalLanguage);
                }
                if("null".equals(aContext.getAdaptationOccurrence().getString(Paths._Address._ProvinceLocalLanguage))){
                    update = true;
                    valueContextForUpdate.setValue(null,Paths._Address._ProvinceLocalLanguage);
                }
                if(aContext.getOccurrenceContext().getValue(Paths._Address._TaxRegimeCode)==null){
                    Map<String,Map<String,String>> countryReferenceFieldsMap = applicationCacheUtil.CountryReferenceFieldsMap("BReference");
                    Map<String,String> resultItem = countryReferenceFieldsMap!=null?countryReferenceFieldsMap.get(countryCode):null;
                    if(resultItem!=null){
                        update = true;
                        valueContextForUpdate.setValue(resultItem.get("RegimeCode"), Paths._Address._TaxRegimeCode);
                    }
                }
                if(aContext.getOccurrenceContext().getValue(Paths._Address._TaxEffectiveFrom)==null){
                    update = true;
                    valueContextForUpdate.setValue(Date.from(utc.toInstant()), Paths._Address._TaxEffectiveFrom);
                }
                if(aContext.getOccurrenceContext().getValue(Paths._Address._MDMAccountId)!=null) {
                    update = true;
                    Object internalAccountId = null;
                    Integer addressMdmAccountId = Integer.valueOf(aContext.getOccurrenceContext().getValue(Paths._Address._MDMAccountId).toString());
                    String condition = Paths._Account._MDMAccountId.format() + " = " + addressMdmAccountId;
                    Adaptation container = aContext.getTable().getContainerAdaptation();
                    AdaptationTable parentTable = container.getTable(Paths._Account.getPathInSchema());
                    final RequestResult parentTableRequestResult = parentTable.createRequestResult(condition);
                    if (parentTableRequestResult != null && !parentTableRequestResult.isEmpty()) {
                        LOGGER.debug("Parent found");
                        Adaptation adaptation = parentTableRequestResult.nextAdaptation();
                        internalAccountId = adaptation.get(Paths._Account._InternalAccountId);
//                        valueContextForUpdate.setValue(internalAccountId, Paths._Address._InternalAccountId);
                        valueContextForUpdate.setValue(adaptation.get(Paths._Account._RMTId), Paths._Address._RMTId);
//                        valueContextForUpdate.setValue(adaptation.getString(Paths._Account._AccountName), Path.parse("./AccountName"));
//                        valueContextForUpdate.setValue(adaptation.getString(Paths._Account._NameLocalLanguage), Path.parse("./AccountNameLocalLanguage"));
                    } else {
                        LOGGER.error("Parent account not found");
                    }
                    AdaptationTable table = aContext.getTable();//getAdaptationOccurrence().getContainer().getTable(Paths._Address.getPathInSchema());
                    RequestResult tableRequestResult = table.createRequestResult(Paths._Address._MDMAccountId.format() + " = '" + String.valueOf(aContext.getOccurrenceContext().getValue(Paths._Address._MDMAccountId))+"' and ("+Paths._Address._IdentifyingAddress.format()+" = 'Y')");
                    String currentIdentifyingAddress = aContext.getAdaptationOccurrence().getString(Paths._Address._IdentifyingAddress);
                    Object currentMdmAddressId = aContext.getAdaptationOccurrence().get(Paths._Address._MDMAddressId);
                    boolean otherRecordIsIdentifying = false;
                    Integer otherRecordId = null;
                    if(tableRequestResult!=null && tableRequestResult.getSize()>0){
                        for(Adaptation tableRequestResultRecord; (tableRequestResultRecord = tableRequestResult.nextAdaptation()) != null; ){
                            if(!String.valueOf(tableRequestResultRecord.get(Paths._Address._MDMAddressId)).equals(String.valueOf(currentMdmAddressId))){
                                otherRecordId=tableRequestResultRecord.get_int(Paths._Address._MDMAddressId);
                                otherRecordIsIdentifying=true;
                                LOGGER.debug("otherRecordIsIdentifying="+otherRecordIsIdentifying);
                                break;
                            }
                        }
                    }
                    /*if(otherRecordIsIdentifying && "Y".equals(currentIdentifyingAddress)){
                        throw OperationException.createError("Cannot mark address as identifying address. Address with MDMAddressId "+otherRecordId+" is already marked as identifying address.");
                    }*/
                    if(!otherRecordIsIdentifying){
                        valueContextForUpdate.setValue("Y", Paths._Address._IdentifyingAddress);
                        LOGGER.debug("setting identifying flag to Y");
                    }else{
                        valueContextForUpdate.setValue("N", Paths._Address._IdentifyingAddress);
                    }
                }
                if(update){
                    aContext.getProcedureContext().doModifyContent(aContext.getAdaptationOccurrence(), valueContextForUpdate);
                }
            }
            String username = aContext.getSession().getUserReference().getUserId();
/*        if("reduser".equalsIgnoreCase(username)) {
            ApplicationContext context = SpringContext.getApplicationContext();
            EmailHtmlSender emailHtmlSender = (EmailHtmlSender) context.getBean("emailHtmlSender");
            String primaryKey = aContext.getAdaptationOccurrence().get(objectPrimaryKeyPath).toString();
            Context thymeleafContext = new Context();
            thymeleafContext.setVariable("timezone", TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));
            thymeleafContext.setVariable("objectName", objectName);
            thymeleafContext.setVariable("primaryKey", primaryKey);
            String subject = "New record created in MDM by reduser";
            String htmlBody = "New " + objectName + " with primary key " + primaryKey + " was created in MDM by reduser";
            String emailTo = String.valueOf(AppUtil.getMailProperty("email.to"));
            EmailStatus emailStatus = emailHtmlSender.sendEmail(emailTo,subject , htmlBody);
            if (emailStatus.isError()) {
                LOGGER.error("Error while sending email - " + emailStatus.getErrorMessage());
            }
        }*/
            List<String> sourceValues = new ArrayList<>();
            String detectionSource = "";
            LOGGER.debug("hbc languageDetectionSourceFields=" + languageDetectionSourceFields);
            if (languageDetectionSourceFields != null) {
                for (Path path : languageDetectionSourceFields) {
                    LOGGER.debug("bc path " + path);
                    String value = aContext.getOccurrenceContext().getValue(path) != null ? String.valueOf(aContext.getOccurrenceContext().getValue(path)) : null;
                    LOGGER.debug("value=" + value);
                    if (value != null) {
                        sourceValues.add(value);
                    }
                }
                if (!sourceValues.isEmpty()) {
                    detectionSource = StringUtils.join(sourceValues, " ");
                    String locale = getLocale(languageDetector, detectionSource.trim());
                    ValueContextForUpdate valueContextForUpdate = aContext.getProcedureContext().getContext(aContext.getAdaptationOccurrence().getAdaptationName());
                    valueContextForUpdate.setValue(locale, localeFieldPath);
                    aContext.getProcedureContext().doModifyContent(aContext.getAdaptationOccurrence(), valueContextForUpdate);
                }
            }
        }
    }

    public void handleAfterModify(AfterModifyOccurrenceContext aContext) throws OperationException {
        LOGGER.debug(objectName+" GenericTrigger handleAfterModify called...");
        if("CMDReference".equalsIgnoreCase(aContext.getAdaptationHome().getKey().getName())
                && "Account".equalsIgnoreCase(aContext.getOccurrenceContext().getAdaptationInstance().getAdaptationName().getStringName())) {
            initialize();
            ApplicationCacheUtil applicationCacheUtil = (ApplicationCacheUtil) SpringContext.getApplicationContext().getBean("applicationCacheUtil");
            ValueChanges changes = aContext.getChanges();
            int numberOfChanges = changes.getNumberOfChanges();
            LOGGER.debug(objectPrimaryKeyPath+" = "+aContext.getAdaptationOccurrence().get(objectPrimaryKeyPath)+" Number of changes="+numberOfChanges+"\nModified Fields:\n");
            Iterator changeIterator = changes.getChangesIterator();
            while(changeIterator.hasNext()){
                ValueChange change = (ValueChange) changeIterator.next();
                LOGGER.debug(change.getModifiedNode().getPathInSchema().format()+" before:"+change.getValueBefore()+" | after:"+change.getValueAfter());
            }
            if(aContext.getOccurrenceContext().getValue(Paths._Address._AssignedTo)==null){
                String userId = aContext.getSession().getUserReference().getUserId();
                EbxProperties ebxProperties = (EbxProperties)SpringContext.getApplicationContext().getBean("ebxProperties");
                List<String> doNotAssignToUser = ebxProperties.getDoNotAssignToUsers();
                if(doNotAssignToUser==null || doNotAssignToUser.isEmpty() || !doNotAssignToUser.contains(userId)) {
                    ValueContextForUpdate valueContextForUpdate = aContext.getProcedureContext().getContext(aContext.getAdaptationOccurrence().getAdaptationName());
                    valueContextForUpdate.setValue(userId, Paths._Address._AssignedTo);
                    aContext.getProcedureContext().doModifyContent(aContext.getAdaptationOccurrence(), valueContextForUpdate);
                }
            }
/*            if("ADDRESS".equalsIgnoreCase(objectName) && aContext.getChanges().getChange(Paths._Address._Country)!=null && "JP".equals(aContext.getOccurrenceContext().getValue(Paths._Address._Country))){
                ValueContextForUpdate valueContextForUpdate = aContext.getProcedureContext().getContext(aContext.getAdaptationOccurrence().getAdaptationName());
                valueContextForUpdate.setValue("N", Paths._Address._SendAcknowledgement);
                valueContextForUpdate.setValue("Suppress because of special format requirements", Paths._Address._InvoiceCopies);
                aContext.getProcedureContext().doModifyContent(aContext.getAdaptationOccurrence(),valueContextForUpdate);
            }*/
            if("ADDRESS".equalsIgnoreCase(objectName) &&
                    (aContext.getChanges().getChange(Paths._Address._Country)!=null || aContext.getChanges().getChange(Paths._Address._AddressState)!=null
                            || aContext.getChanges().getChange(Paths._Address._Province)!=null)){
                String countryCode = aContext.getAdaptationOccurrence().getString(Paths._Address._Country);
                /*if(countryCode!=null){
                    validateStateAndProvince(countryCode,aContext.getAdaptationOccurrence().getString(Paths._Address._AddressState),
                            aContext.getAdaptationOccurrence().getString(Paths._Address._Province),false);
                    validateStateAndProvince(countryCode,aContext.getAdaptationOccurrence().getString(Paths._Address._StateLocalLanguage),
                            aContext.getAdaptationOccurrence().getString(Paths._Address._ProvinceLocalLanguage),true);
                }*/
            }
            if( "ADDRESS".equalsIgnoreCase(objectName)){
/*                if(changes.getChange(Paths._Address._MDMAccountId)!=null && aContext.getAdaptationOccurrence().getString(Paths._Address._Published)!=null){
                    throw OperationException.createError("Address association to account can't be modified for already published address");
                }*/
                /*for(int i=1;i<=4;i++) {
                    validateByteLength("Address Line "+i, String.valueOf(aContext.getOccurrenceContext().getValue(Path.parse("./AddressLine"+i))), 150);
                }*/
                boolean update = false;
                ValueContextForUpdate valueContextForUpdate = aContext.getProcedureContext().getContext(aContext.getAdaptationOccurrence().getAdaptationName());
                if("null".equals(aContext.getAdaptationOccurrence().getString(Paths._Address._AddressState))){
                    update = true;
                    valueContextForUpdate.setValue(null,Paths._Address._AddressState);
                }
                if("null".equals(aContext.getAdaptationOccurrence().getString(Paths._Address._Province))){
                    update = true;
                    valueContextForUpdate.setValue(null,Paths._Address._Province);
                }
                if("null".equals(aContext.getAdaptationOccurrence().getString(Paths._Address._StateLocalLanguage))){
                    update = true;
                    valueContextForUpdate.setValue(null,Paths._Address._StateLocalLanguage);
                }
                if("null".equals(aContext.getAdaptationOccurrence().getString(Paths._Address._ProvinceLocalLanguage))){
                    update = true;
                    valueContextForUpdate.setValue(null,Paths._Address._ProvinceLocalLanguage);
                }
                if(aContext.getChanges().getChange(Paths._Address._MDMAccountId)!=null &&
                        aContext.getOccurrenceContext().getValue(Paths._Address._MDMAccountId)!=null) {
                    Object internalAccountId = null;
                    Integer addressMdmAccountId = Integer.valueOf(aContext.getOccurrenceContext().getValue(Paths._Address._MDMAccountId).toString());
                    String condition = Paths._Account._MDMAccountId.format() + " = " + addressMdmAccountId;
                    Adaptation container = aContext.getTable().getContainerAdaptation();
                    AdaptationTable parentTable = container.getTable(Paths._Account.getPathInSchema());
                    final RequestResult parentTableRequestResult = parentTable.createRequestResult(condition);
                    if (parentTableRequestResult != null && !parentTableRequestResult.isEmpty()) {
                        LOGGER.debug("Parent found");
                        Adaptation adaptation = parentTableRequestResult.nextAdaptation();
                        internalAccountId = adaptation.get(Paths._Account._InternalAccountId);
//                        valueContextForUpdate.setValue(internalAccountId, Paths._Address._InternalAccountId);
                        valueContextForUpdate.setValue(adaptation.get(Paths._Account._RMTId), Paths._Address._RMTId);
//                        valueContextForUpdate.setValue(adaptation.getString(Paths._Account._AccountName), Path.parse("./AccountName"));
//                        valueContextForUpdate.setValue(adaptation.getString(Paths._Account._NameLocalLanguage), Path.parse("./AccountNameLocalLanguage"));
                        update = true;
                    } else {
                        LOGGER.error("Parent account not found");
                    }
                }
                if(aContext.getChanges().getChange(Paths._Address._Status)!=null){
                    String status = aContext.getAdaptationOccurrence().getString(Paths._Address._Status);
                    AdaptationTable table = aContext.getOccurrenceContext().getAdaptationInstance().getTable(Paths._BusinessPurpose.getPathInSchema());
                    RequestResult bpResult = table.createRequestResult(Paths._BusinessPurpose._MDMAddressId.format()+" = '"+aContext.getAdaptationOccurrence().get(Paths._Address._MDMAddressId)+"'");
                    if(bpResult!=null && !bpResult.isEmpty()) {
                        List<OrchestraObject> bpRowsToUpdateStatus = new ArrayList<>();
                        for (Adaptation bpAdapatation; (bpAdapatation = bpResult.nextAdaptation()) != null; ) {
                            OrchestraObject orchestraObject = new OrchestraObject();
                            Map<String, OrchestraContent> content = new HashMap<>();
                            content.put("Status",new OrchestraContent(status));
                            content.put("MDMPurposeId",new OrchestraContent(bpAdapatation.get(Paths._BusinessPurpose._MDMPurposeId)));
                            content.put("Location",new OrchestraContent(bpAdapatation.get(Paths._BusinessPurpose._Location)));
                            orchestraObject.setContent(content);
                            bpRowsToUpdateStatus.add(orchestraObject);
                        }
                        Runnable updateStatusInBp = () -> {
                            String dataSpace = aContext.getAdaptationHome().getKey().format();
                            OrchestraObjectList orchestraObjectList = new OrchestraObjectList();
                            orchestraObjectList.setRows(bpRowsToUpdateStatus);
                            OrchestraRestClient orchestraRestClient = (OrchestraRestClient) SpringContext.getApplicationContext().getBean("orchestraRestClient");
                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("updateOrInsert", "true");
                            try {
                                RestResponse response = orchestraRestClient.promote(dataSpace, "Account", "root/BusinessPurpose", orchestraObjectList, parameters);
                                if(response.getStatus()>=300){
                                    LOGGER.error("Error updating status on BusinessPurpose. response: "+response.getResponseBody());
                                }
                            } catch (IOException e) {
                                LOGGER.error("Error updating status on BusinessPurpose. ",e);
                            }
                        };
                        new Thread(updateStatusInBp).start();
                    }
                }
                if(aContext.getChanges().getChange(Paths._Address._OperatingUnit)!=null){
                    ValueChange change = aContext.getChanges().getChange(Paths._Address._OperatingUnit);
                    List<String> before = change.getValueBefore()!=null?(List<String>)change.getValueBefore():new ArrayList<>();
                    List<String> now = change.getValueAfter()!=null?(List<String>)change.getValueAfter():new ArrayList<>();
                    String firstOu = !now.isEmpty()?now.get(0):null;
                    List<String> removed = new ArrayList<>(before);
                    List<String> added = new ArrayList<>(now);
                    List<String> ousWithNoBp = new ArrayList<>();
                    HashSet<String> bpOus = new HashSet<>();
                    removed = removed.removeAll(now)?removed:new ArrayList<>();
                    added = added.removeAll(before)?added:null;
                    AdaptationTable table = aContext.getOccurrenceContext().getAdaptationInstance().getTable(Paths._BusinessPurpose.getPathInSchema());
                    RequestResult bpResult = table.createRequestResult(Paths._BusinessPurpose._MDMAddressId.format()+" = '"+aContext.getAdaptationOccurrence().get(Paths._Address._MDMAddressId)+"'");
                    List<OrchestraObject> bpRowsToRemoveOu = new ArrayList<>();
                    List<OrchestraObject> bpRowsToRollback = new ArrayList<>();
                    if(bpResult!=null && !bpResult.isEmpty()) {
                        for (Adaptation bpAdapatation; (bpAdapatation = bpResult.nextAdaptation()) != null; ) {
                            if (bpAdapatation.getList(Paths._BusinessPurpose._OperatingUnit) != null) {
                                List<String> ous = bpAdapatation.getList(Paths._BusinessPurpose._OperatingUnit);
                                List<String> ousRemovedRb = bpAdapatation.getList(Paths._BusinessPurpose._RemovedOperatingUnits);
                                if (ous != null) {
                                    bpOus.addAll(ous);
                                    List<String> bpOusRemoved = new ArrayList<>();
                                    for(String ou: ous){
                                        if(removed.contains(ou)){
                                            bpOusRemoved.add(ou);
                                        }
                                    }
                                    if(!bpOusRemoved.isEmpty()){
                                        List<String> ousNew = new ArrayList<>(ous);
                                        ousNew.removeAll(bpOusRemoved);
                                        OrchestraObject orchestraObject = new OrchestraObject();
                                        OrchestraObject rbOrchestraObject = new OrchestraObject();
                                        Map<String, OrchestraContent> content = new HashMap<>();
                                        Map<String, OrchestraContent> rollBackContent = new HashMap<>();
                                        List<OrchestraContent> ousNewContent = new ArrayList<>();
                                        List<OrchestraContent> ousRbContent = new ArrayList<>();
                                        List<OrchestraContent> ousRemovedContent = new ArrayList<>();
                                        List<OrchestraContent> ousRemovedRbContent = new ArrayList<>();
                                        for(String ounew:ousNew){
                                            ousNewContent.add(new OrchestraContent(ounew));
                                        }
                                        for(String ourb:ous){
                                            ousRbContent.add(new OrchestraContent(ourb));
                                        }
                                        List<String> existingRemovedOus = bpAdapatation.getList(Paths._BusinessPurpose._RemovedOperatingUnits);
                                        existingRemovedOus=existingRemovedOus!=null?existingRemovedOus:new ArrayList<>();
                                        HashSet<String> removedBpOusSet = new HashSet<>(bpOusRemoved);
                                        removedBpOusSet.addAll(existingRemovedOus);
                                        bpOusRemoved = new ArrayList<>(removedBpOusSet);
                                        for(String our:bpOusRemoved){
                                            ousRemovedContent.add(new OrchestraContent(our));
                                        }
                                        for(String ouRemovedRb:existingRemovedOus){
                                            ousRemovedRbContent.add(new OrchestraContent(ouRemovedRb));
                                        }
                                        content.put("MDMPurposeId",new OrchestraContent(bpAdapatation.get(Paths._BusinessPurpose._MDMPurposeId)));
                                        content.put("OperatingUnit",new OrchestraContent(ousNewContent));
                                        content.put("RemovedOperatingUnits",new OrchestraContent(ousRemovedContent));
                                        content.put("Location",new OrchestraContent(bpAdapatation.getString(Paths._BusinessPurpose._Location)));
                                        rollBackContent.put("MDMPurposeId",new OrchestraContent(bpAdapatation.get(Paths._BusinessPurpose._MDMPurposeId)));
                                        rollBackContent.put("OperatingUnit",new OrchestraContent(ousRbContent));
                                        rollBackContent.put("RemovedOperatingUnits",new OrchestraContent(ousRemovedRbContent));
                                        rollBackContent.put("Location",new OrchestraContent(bpAdapatation.getString(Paths._BusinessPurpose._Location)));
                                        rbOrchestraObject.setContent(rollBackContent);
                                        orchestraObject.setContent(content);
                                        bpRowsToRemoveOu.add(orchestraObject);
                                        bpRowsToRollback.add(rbOrchestraObject);
                                    }
                                }
                            }
                        }
                    }
                    if(added!=null && !added.isEmpty()){
                        if(aContext.getAdaptationOccurrence().getList(Paths._Address._RemovedOperatingUnits)!=null){
                            List<String> existingRemovedOus = aContext.getAdaptationOccurrence().getList(Paths._Address._RemovedOperatingUnits);
                            existingRemovedOus.removeAll(added);
                            if(!existingRemovedOus.isEmpty()) {
                                valueContextForUpdate.setValue(existingRemovedOus, Paths._Address._RemovedOperatingUnits);
                            }else{
                                valueContextForUpdate.setValue(null, Paths._Address._RemovedOperatingUnits);
                            }
                            update = true;
                        }
                        if(aContext.getAdaptationOccurrence().getString(Paths._Address._Published)!=null){
                            for(String addedOu:added){
                                if(!bpOus.contains(addedOu)){
                                    ousWithNoBp.add(addedOu);
                                }
                            }
                            /*if(!ousWithNoBp.isEmpty()){
                                throw OperationException.createError("No Business Purpose exists for Operating Units "+StringUtils.join(ousWithNoBp, ',')+". Please add Business Purpose(s) first.");
                            }*/
                        }
                    }
                    if(firstOu!=null){
                        valueContextForUpdate.setValue(firstOu, Paths._Address._FirstOperatingUnit);
                        update = true;
                    }
                    if(removed!=null && !removed.isEmpty()){
                        if(aContext.getAdaptationOccurrence().get(Paths._Address._Published)!=null) {
                            if(aContext.getAdaptationOccurrence().getList(Paths._Address._RemovedOperatingUnits)!=null){
                                List<String> existingRemovedOus = aContext.getAdaptationOccurrence().getList(Paths._Address._RemovedOperatingUnits);
                                HashSet<String> removedOusSet = new HashSet<>(removed);
                                removedOusSet.addAll(existingRemovedOus);
                                if(added!=null && !added.isEmpty()){
                                    removedOusSet.removeAll(added);
                                }
                                removed = new ArrayList<>(removedOusSet);
                            }
                            valueContextForUpdate.setValue(removed, Paths._Address._RemovedOperatingUnits);
                            update = true;
                        }
                        if(!bpRowsToRemoveOu.isEmpty()){
                            Runnable deleteOusFromBpRunnable = () -> {
                                String dataSpace = aContext.getAdaptationHome().getKey().format();
                                OrchestraObjectList orchestraObjectList = new OrchestraObjectList();
                                orchestraObjectList.setRows(bpRowsToRemoveOu);
                                OrchestraRestClient orchestraRestClient = (OrchestraRestClient) SpringContext.getApplicationContext().getBean("orchestraRestClient");
                                Map<String, String> parameters = new HashMap<String, String>();
                                parameters.put("updateOrInsert", "true");
                                try {
                                    RestResponse response = orchestraRestClient.promote(dataSpace, "Account", "root/BusinessPurpose", orchestraObjectList, parameters);
                                    if(response.getStatus()>=300){
                                        LOGGER.error("Error removing Operating Units from BusinessPurpose. response: "+response.getResponseBody());
                                    }
                                } catch (IOException e) {
                                    LOGGER.error("Error removing Operating Units from BusinessPurpose. ",e);
                                }
                            };
                            new Thread(deleteOusFromBpRunnable).start();
                        }
                    }
                }
/*                if(changes.getChange(Paths._Address._IdentifyingAddress)!=null) {
                    AdaptationTable table = aContext.getTable();
                    RequestResult tableRequestResult = table.createRequestResult(Paths._Address._MDMAccountId.format() + " = '" + String.valueOf(aContext.getOccurrenceContext().getValue(Paths._Address._MDMAccountId))+"' and ("+Paths._Address._IdentifyingAddress.format()+" = 'Y')");
                    String currentIdentifyingAddress = aContext.getAdaptationOccurrence().getString(Paths._Address._IdentifyingAddress);
                    Object currentMdmAddressId = aContext.getAdaptationOccurrence().get(Paths._Address._MDMAddressId);
                    boolean otherRecordIsIdentifying = false;
                    Integer otherRecordId = null;
                    if(tableRequestResult!=null && tableRequestResult.getSize()>0){
                        for(Adaptation tableRequestResultRecord; (tableRequestResultRecord = tableRequestResult.nextAdaptation()) != null; ){
                            if(!String.valueOf(tableRequestResultRecord.get(Paths._Address._MDMAddressId)).equals(String.valueOf(currentMdmAddressId))){
                                otherRecordId=tableRequestResultRecord.get_int(Paths._Address._MDMAddressId);
                                otherRecordIsIdentifying=true;
                                LOGGER.debug("otherRecordIsIdentifying="+otherRecordIsIdentifying);
                                break;
                            }
                        }
                    }
                    if(otherRecordIsIdentifying && "Y".equals(currentIdentifyingAddress)){
                        throw OperationException.createError("Cannot mark address as identifying address. Address with MDMAddressId "+otherRecordId+" is already marked as identifying address.");
                    }
                }*/
                if(update){
                    aContext.getProcedureContext().doModifyContent(aContext.getAdaptationOccurrence(), valueContextForUpdate);
                }
            }
            ProcedureContext procedureContext = aContext.getProcedureContext();
            Adaptation adaptation = aContext.getAdaptationOccurrence();
            LOGGER.debug("Record Id:" + adaptation.get(objectPrimaryKeyPath) + " timestamp" + LocalDateTime.now());
            int numberOfNonDaqaChanges = 0;
            changeIterator = changes.getChangesIterator();
            while(changeIterator.hasNext()){
                ValueChange change = (ValueChange) changeIterator.next();
                if(!change.getModifiedNode().getPathInSchema().format().contains("DaqaMetaData")){
                    numberOfNonDaqaChanges++;
                }
            }
            ValueChange daqaStateChanges = changes.getChange(stateFieldPath);
            ValueChange daqaTargetChanges = changes.getChange(daqaTargetFieldPath);
            ValueChange daqaMergeOriginChanges = changes.getChange(mergeOriginPath);
            ValueChange daqaTimestampChanges = changes.getChange(timestampPath);
            ValueChange countryChanges = changes.getChange(countryPath);
            ValueChange publishedFieldValueChange = changes.getChange(publishedFieldPath);
            ValueChange assignedToValueChange = changes.getChange(Paths._Account._AssignedTo);
            ValueChange lastPublishedValueChange = changes.getChange(Paths._Account._LastPublished);
            if ("ACCOUNT".equalsIgnoreCase(objectName) && countryChanges != null) {
                List countryList = (List) aContext.getOccurrenceContext().getValue(Paths._Account._Country);
                if (countryList != null && !countryList.isEmpty()) {
                    String countryCode = (String) countryList.get(0);
                    Map<String,Map<String,String>> countryReferenceFieldsMap = applicationCacheUtil.CountryReferenceFieldsMap("BReference");
                    Map<String,String> resultItem = countryReferenceFieldsMap!=null?countryReferenceFieldsMap.get(countryCode):null;
                    if(resultItem!=null){
                    /*String countryReferenceTablePath = "root/CountryReferenceFields";
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("filter", "CountryCode='" + countryCode + "'");
                    OrchestraRestClient orchestraRestClient = (OrchestraRestClient) SpringContext.getApplicationContext().getBean("orchestraRestClient");
                    OrchestraObjectListResponse orchestraObjectListResponse = null;
                    try {
                        orchestraObjectListResponse = orchestraRestClient.get("BReference", "Account", countryReferenceTablePath, parameters);
                    } catch (IOException e) {
                        throw new ApplicationOperationException("Error getting Profile Class and Region for country");
                    }
                    Map<String, String> resultObject = new HashMap<>();
                    if (orchestraObjectListResponse != null && orchestraObjectListResponse.getRows() != null && !orchestraObjectListResponse.getRows().isEmpty()) {
//*/                        //OrchestraObjectResponse objectResponse = orchestraObjectListResponse.getRows().get(0);
                        //Map<String, OrchestraContent> content = objectResponse.getContent();
                        String profileClass = resultItem.get("ProfileClass");//content.get("ProfileClass").getContent() != null ? content.get("ProfileClass").getContent().toString() : null;
                        String region = resultItem.get("Region");//content.get("Region").getContent() != null ? content.get("Region").getContent().toString() : null;
                        ValueContextForUpdate valueContextForUpdate = aContext.getProcedureContext().getContext(aContext.getAdaptationOccurrence().getAdaptationName());
                        valueContextForUpdate.setValue(profileClass, Paths._Account._ProfileClass);
                        valueContextForUpdate.setValue(region, Paths._Account._Region);
                        if("BR".equals(countryCode)){
                            valueContextForUpdate.setValue("Brazil Bank Collection", Paths._Account._PaymentReceiptMethod);
                        }else if("US".equals(countryCode) || "CA".equals(countryCode) || "GU".equals(countryCode) || "SG".equals(countryCode) ||
                                "JP".equals(countryCode) || "TH".equals(countryCode)){
                            valueContextForUpdate.setValue("Lockbox", Paths._Account._PaymentReceiptMethod);
                        }else{
                            valueContextForUpdate.setValue("Manual Payment", Paths._Account._PaymentReceiptMethod);
                        }
                        aContext.getProcedureContext().doModifyContent(aContext.getAdaptationOccurrence(), valueContextForUpdate);
                    }
                }
            }
            if (daqaStateChanges != null) {
                LOGGER.debug("State Before: " + String.valueOf(daqaStateChanges.getValueBefore()));
                LOGGER.debug("State After: " + String.valueOf(daqaStateChanges.getValueAfter()));
            }
            if (daqaTargetChanges != null) {
                LOGGER.debug("target Before: " + String.valueOf(daqaTargetChanges.getValueBefore()));
                LOGGER.debug("target After: " + String.valueOf(daqaTargetChanges.getValueAfter()));
            }
            if (daqaMergeOriginChanges != null) {
                LOGGER.debug("daqaMergeOriginChanges Before: " + String.valueOf(daqaMergeOriginChanges.getValueBefore()));
                LOGGER.debug("daqaMergeOriginChanges After: " + String.valueOf(daqaMergeOriginChanges.getValueAfter()));
            }
            if (daqaTimestampChanges != null) {
                LOGGER.debug("daqaTimestampChanges Before: " + String.valueOf(daqaTimestampChanges.getValueBefore()));
                LOGGER.debug("daqaTimestampChanges After: " + String.valueOf(daqaTimestampChanges.getValueAfter()));
            }
            if (countryChanges != null) {
                LOGGER.debug("country Before: " + String.valueOf(countryChanges.getValueBefore()));
                LOGGER.debug("country After: " + String.valueOf(countryChanges.getValueAfter()));
            }
            if (publishedFieldValueChange != null) {
                LOGGER.debug("publishedFieldValueChange Before: " + String.valueOf(publishedFieldValueChange.getValueBefore()));
                LOGGER.debug("publishedFieldValueChange After: " + String.valueOf(publishedFieldValueChange.getValueAfter()));
            }
            LOGGER.debug("DaqaValues.........");
            LOGGER.debug("daqastate = " + String.valueOf(adaptation.get(stateFieldPath)));
            LOGGER.debug("daqatarget = " + String.valueOf(adaptation.get(daqaTargetFieldPath)));
            LOGGER.debug("daqamergeorigin = " + String.valueOf(adaptation.get(mergeOriginPath)));
            LOGGER.debug("daqatimestamp = " + String.valueOf(adaptation.get(timestampPath)));
            String publishedValue = adaptation.get(publishedFieldPath) != null ? String.valueOf(adaptation.get(publishedFieldPath)) : null;
            LOGGER.debug("publishedValue=" + publishedValue);
            if (!(publishedFieldValueChange != null && publishedFieldValueChange.getValueAfter() != null && "U".equalsIgnoreCase(publishedFieldValueChange.getValueAfter().toString()))) {
                LOGGER.debug("executing trigger");
                if (daqaStateChanges != null && "Merged".equalsIgnoreCase(String.valueOf(daqaStateChanges.getValueAfter())) && lovsToMerge != null) {
                    LOGGER.debug("DAQA STATE CHANGED TO MERGED!!");
                    Adaptation resultRecord = searchByMdmId(procedureContext,
                            Integer.valueOf(String.valueOf(adaptation.get(daqaTargetFieldPath))), adaptation.getContainerTable());
                    ValueContextForUpdate valueContextForUpdate = procedureContext.getContext(resultRecord.getAdaptationName());
                    for (String fieldName : lovsToMerge) {
                        Path fieldPath = Path.parse("./" + fieldName);
                        Set finalValues = new HashSet<String>();
                        finalValues.addAll(adaptation.getList(fieldPath));
                        if (resultRecord != null && resultRecord.getList(fieldPath) != null && !resultRecord.getList(fieldPath).isEmpty()) {
                            finalValues.addAll(resultRecord.getList(fieldPath));
                        }
                        valueContextForUpdate.setValue(new ArrayList<String>(finalValues), fieldPath);
                    }
                    procedureContext.doModifyContent(resultRecord, valueContextForUpdate);
                }

                if (!(numberOfChanges<=2 && assignedToValueChange!=null) && publishedFieldValueChange == null && lastPublishedValueChange==null && publishedValue != null && "Y".equalsIgnoreCase(publishedValue) && numberOfNonDaqaChanges>0) {
                    LOGGER.debug("Updating published flag");
                    ValueContextForUpdate valueContextForUpdate = procedureContext.getContext(adaptation.getAdaptationName());
                    valueContextForUpdate.setValue("U", publishedFieldPath);
                    procedureContext.doModifyContent(adaptation, valueContextForUpdate);
                    LOGGER.debug("publishedValue updated");
                }
            } else {
                LOGGER.debug("Nothing to update");
            }
            String username = aContext.getSession().getUserReference().getUserId();
/*        if("reduser".equalsIgnoreCase(username)) {
            ApplicationContext context = SpringContext.getApplicationContext();
            EmailHtmlSender emailHtmlSender = (EmailHtmlSender) context.getBean("emailHtmlSender");
            String primaryKey = aContext.getAdaptationOccurrence().get(objectPrimaryKeyPath).toString();
            Context thymeleafContext = new Context();
            thymeleafContext.setVariable("timezone", TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));
            thymeleafContext.setVariable("objectName", objectName);
            thymeleafContext.setVariable("primaryKey", primaryKey);
            String subject = "Record updated in MDM by reduser";
            String htmlBody = objectName + " with primary key " + primaryKey + " was updated in MDM by reduser";
            String emailTo = String.valueOf(AppUtil.getMailProperty("email.to"));
            EmailStatus emailStatus = emailHtmlSender.sendEmail(emailTo,subject , htmlBody);
            if (emailStatus.isError()) {
                LOGGER.error("Error while sending email - " + emailStatus.getErrorMessage());
            }
        }*/
            List<String> sourceValues = new ArrayList<>();
            String detectionSource = "";
            boolean fieldChanged = false;
            LOGGER.debug("hbm languageDetectionSourceFields=" + languageDetectionSourceFields);
            if (languageDetectionSourceFields != null && aContext.getChanges() != null) {
                LOGGER.debug("bm something changed");
                for (Path path : languageDetectionSourceFields) {
                    LOGGER.debug("path " + path + " change" + aContext.getChanges().getChange(path));
                    if (aContext.getChanges().getChange(path) != null) {
                        fieldChanged = true;
                        break;
                    }
                }
                if (fieldChanged) {
                    LOGGER.debug("FIELD CHANGED");
                    for (Path path : languageDetectionSourceFields) {
                        String value = aContext.getOccurrenceContext().getValue(path) != null ? String.valueOf(aContext.getOccurrenceContext().getValue(path)) : null;
                        if (value != null) {
                            sourceValues.add(value);
                        }
                    }
                    if (!sourceValues.isEmpty()) {
                        detectionSource = StringUtils.join(sourceValues, " ");
                        String locale = getLocale(languageDetector, detectionSource.trim());
                        ValueContextForUpdate valueContextForUpdate = aContext.getProcedureContext().getContext(aContext.getAdaptationOccurrence().getAdaptationName());
                        valueContextForUpdate.setValue(locale, localeFieldPath);
                        aContext.getProcedureContext().doModifyContent(aContext.getAdaptationOccurrence(), valueContextForUpdate);
                    }
                }
            }
        }
    }

    private List<Adaptation> searchByTargetValue(ProcedureContext procedureContext,Integer mdmId,AdaptationTable adaptationTable) {
        Set<String> countries = new HashSet<>();
        int retryCount = 0;
        List<Adaptation> resultList = new ArrayList<>();
        LOGGER.debug("Searching by targetvalue.......");
        final String conditionString = daqaTargetFieldPath.format()+" = '"+String.valueOf(mdmId)+"'";
        LOGGER.debug("condition ="+conditionString);
        List<OrchestraObject> searchResultList = new ArrayList<>();
        try {
            do {
                final RequestResult tableRequestResult = adaptationTable.createRequestResult(conditionString);
                if (tableRequestResult != null && !tableRequestResult.isEmpty()) {
                    for (Adaptation tableRequestResultRecord; (tableRequestResultRecord = tableRequestResult.nextAdaptation()) != null; ) {
                        LOGGER.debug("Record found: " + String.valueOf(tableRequestResultRecord.get(objectPrimaryKeyPath)));
                        resultList.add(tableRequestResultRecord);
                    }
                } else {
                    retryCount++;
                    Thread.sleep(2000);
                    LOGGER.debug("Retry Attempt = "+retryCount);
                }
            } while ((resultList == null || resultList.isEmpty()) && retryCount < 3);
        }catch(InterruptedException e) {
            LOGGER.error("Error searching..",e);
        }
        return resultList;
    }

    private Adaptation searchByMdmId(ProcedureContext procedureContext,Integer mdmId,AdaptationTable adaptationTable) {
        Set<String> countries = new HashSet<>();
        int retryCount = 0;
        Adaptation resultRecord = null;
        List<Adaptation> resultList = new ArrayList<>();
        LOGGER.debug("Searching by MdmId.......");
        final String conditionString = objectPrimaryKeyPath.format()+" = "+mdmId;
        LOGGER.debug("condition ="+conditionString);
        List<OrchestraObject> searchResultList = new ArrayList<>();
        try {
            do {
                final RequestResult tableRequestResult = adaptationTable.createRequestResult(conditionString);
                if (tableRequestResult != null && !tableRequestResult.isEmpty()) {
                    for (Adaptation tableRequestResultRecord; (tableRequestResultRecord = tableRequestResult.nextAdaptation()) != null; ) {
                        LOGGER.debug("Record found: " + String.valueOf(tableRequestResultRecord.get(objectPrimaryKeyPath)));
                        resultList.add(tableRequestResultRecord);
                    }
                } else {
                    retryCount++;
                    Thread.sleep(2000);
                    LOGGER.debug("Retry Attempt = "+retryCount);
                }
            } while ((resultList == null || resultList.isEmpty()) && retryCount < 3);
        }catch(InterruptedException e) {
            LOGGER.error("Error searching..",e);
        }
        if(resultList!=null && !resultList.isEmpty()){
            resultRecord = resultList.get(0);
        }
        return resultRecord;
    }

    private String getLocale(LanguageDetector languageDetector, String text) {
        LOGGER.debug("in getLocale text="+text);
        String detectedLanguage = null;
        TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();
        TextObject textObject = textObjectFactory.forText(text);
        com.google.common.base.Optional<LdLocale> lang = languageDetector.detectWithMinimalConfidence(0,textObject);
        if(lang.isPresent()){
            LdLocale ldLocale = lang.get();
            detectedLanguage = ldLocale.getLanguage();
            if("ja".equalsIgnoreCase(detectedLanguage)||"ko".equalsIgnoreCase(detectedLanguage) || "zh".equalsIgnoreCase(detectedLanguage)
                    || "zh-CN".equalsIgnoreCase(detectedLanguage) || "zh-TW".equalsIgnoreCase(detectedLanguage)){
                detectedLanguage="ja";
            }else{
                detectedLanguage=null;
            }
            LOGGER.info("Detected Locale: "+ ldLocale);
        }else{
            LOGGER.error("Language could not be detected. May be because of probability of detected language is less than minimal confidence 0.999");
        }
        return detectedLanguage;
    }

    private void validateStateAndProvince(String countryCode,String currentState,String currentProvince,boolean local) throws OperationException {
        ApplicationCacheUtil applicationCacheUtil = (ApplicationCacheUtil)SpringContext.getApplicationContext().getBean("applicationCacheUtil");
        Map<String,String> territoryTypeMap = applicationCacheUtil.getTerritoryTypeMap("BReference");
        HashSet<String> options = new HashSet<>();
        if("STATE".equalsIgnoreCase(territoryTypeMap.get(countryCode))){
            options = applicationCacheUtil.getOptionsList("BReference",countryCode,"STATE");
            if(options!=null && !options.contains(currentState) && StringUtils.isNotBlank(currentState)){
                if(("JP".equals(countryCode) || "RU".equals(countryCode)) && local){
                    throw OperationException.createError("State Local Language value is invalid");
                }else if(!local){
                    throw OperationException.createError("State value is invalid");
                }
            }else if(options==null && !local){
                throw OperationException.createError("Error getting state options");
            }
        }else if("PROVINCE".equalsIgnoreCase(territoryTypeMap.get(countryCode))){
            options = applicationCacheUtil.getOptionsList("BReference",countryCode,"PROVINCE");
            if(options!=null && !options.contains(currentProvince) && StringUtils.isNotBlank(currentProvince)){
                if(("CN".equals(countryCode) || "KR".equals(countryCode)) && local){
                    throw OperationException.createError("Province Local Language value is invalid");
                }else if(!local){
                    throw OperationException.createError("Province value is invalid");
                }
            }else if(options==null && !local){
                throw OperationException.createError("Error getting province options");
            }
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setDaqaTargetFieldPath(Path daqaTargetFieldPath) {
        this.daqaTargetFieldPath = daqaTargetFieldPath;
    }

    public void setObjectPrimaryKeyPath(Path objectPrimaryKeyPath) {
        this.objectPrimaryKeyPath = objectPrimaryKeyPath;
    }

    public void setStateFieldPath(Path stateFieldPath) {
        this.stateFieldPath = stateFieldPath;
    }

    public void setMergeOriginPath(Path mergeOriginPath) {
        this.mergeOriginPath = mergeOriginPath;
    }

    public void setTimestampPath(Path timestampPath) {
        this.timestampPath = timestampPath;
    }

    public void setCountryPath(Path countryPath) {
        this.countryPath = countryPath;
    }

    public Path getPublishedFieldPath() {
        return publishedFieldPath;
    }

    public void setPublishedFieldPath(Path publishedFieldPath) {
        this.publishedFieldPath = publishedFieldPath;
    }

    public List<String> getLovsToMerge() {
        return lovsToMerge;
    }

    public void setLovsToMerge(List<String> lovsToMerge) {
        this.lovsToMerge = lovsToMerge;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public List<Path> getLanguageDetectionSourceFields() {
        return languageDetectionSourceFields;
    }

    public void setLanguageDetectionSourceFields(List<Path> languageDetectionSourceFields) {
        this.languageDetectionSourceFields = languageDetectionSourceFields;
    }

    public Path getLocaleFieldPath() {
        return localeFieldPath;
    }

    public void setLocaleFieldPath(Path localeFieldPath) {
        this.localeFieldPath = localeFieldPath;
    }

    protected boolean validateByteLength(String fieldName,String value,int bytes) throws OperationException {
        if(value!=null){
            try {
                final byte[] utf8Bytes = value.getBytes("UTF-8");
                if(utf8Bytes.length>bytes){
                    throw OperationException.createError(fieldName+" length exceeds "+bytes+" byte limit");
                }
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("Error while getting byte length of string",e);
            }
        }
        return true;
    }
}
