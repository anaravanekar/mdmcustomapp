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
import com.sereneast.orchestramdm.keysight.mdmcustom.email.EmailHtmlSender;
import com.sereneast.orchestramdm.keysight.mdmcustom.email.EmailStatus;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObject;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.AppUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
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
        //get application context
        String username = aContext.getSession().getUserReference().getUserId();
        if("reduser".equalsIgnoreCase(username)) {
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
        }
        /*List<String> sourceValues = new ArrayList<>();
        String detectionSource = "";
        LOGGER.debug("hbc languageDetectionSourceFields="+languageDetectionSourceFields);
        if(languageDetectionSourceFields!=null){
            for(Path path:languageDetectionSourceFields){
                LOGGER.debug("bc path "+path);
                String value = aContext.getOccurrenceContext().getValue(path)!=null?String.valueOf(aContext.getOccurrenceContext().getValue(path)):null;
                LOGGER.debug("value="+value);
                if(value!=null){
                    sourceValues.add(value);
                }
            }
            if(!sourceValues.isEmpty()){
                detectionSource = StringUtils.join(sourceValues, " ");
                String locale = getLocale(languageDetector, detectionSource.trim());
                ValueContextForUpdate valueContextForUpdate = aContext.getProcedureContext().getContext(aContext.getAdaptationOccurrence().getAdaptationName());
                valueContextForUpdate.setValue(locale,localeFieldPath);
                aContext.getProcedureContext().doModifyContent(aContext.getAdaptationOccurrence(),valueContextForUpdate);
            }
        }*/
    }

    public void handleAfterModify(AfterModifyOccurrenceContext aContext) throws OperationException {
        LOGGER.debug("GenericTrigger handleAfterModify called...");
        initialize();
        ProcedureContext procedureContext = aContext.getProcedureContext();
        Adaptation adaptation = aContext.getAdaptationOccurrence();
        LOGGER.debug("Record Id:"+adaptation.get(objectPrimaryKeyPath)+" timestamp"+ LocalDateTime.now());
        ValueChanges changes = aContext.getChanges();
        ValueChange daqaStateChanges = changes.getChange(stateFieldPath);
        ValueChange daqaTargetChanges = changes.getChange(daqaTargetFieldPath);
        ValueChange daqaMergeOriginChanges = changes.getChange(mergeOriginPath);
        ValueChange daqaTimestampChanges = changes.getChange(timestampPath);
        ValueChange countryChanges = changes.getChange(countryPath);
        ValueChange publishedFieldValueChange = changes.getChange(publishedFieldPath);
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
        if(!(publishedFieldValueChange!=null && publishedFieldValueChange.getValueAfter()!=null && "U".equalsIgnoreCase(publishedFieldValueChange.getValueAfter().toString()))) {
            LOGGER.debug("executing trigger");
            if (daqaStateChanges != null && "Merged".equalsIgnoreCase(String.valueOf(daqaStateChanges.getValueAfter())) && lovsToMerge!=null) {
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
            if (publishedFieldValueChange==null && publishedValue != null && "Y".equalsIgnoreCase(publishedValue)) {
                LOGGER.debug("Updating published flag");
                ValueContextForUpdate valueContextForUpdate = procedureContext.getContext(adaptation.getAdaptationName());
                valueContextForUpdate.setValue("U", publishedFieldPath);
                procedureContext.doModifyContent(adaptation, valueContextForUpdate);
                LOGGER.debug("publishedValue updated");
            }
        }else{
            LOGGER.debug("Nothing to update");
        }
        String username = aContext.getSession().getUserReference().getUserId();
        if("reduser".equalsIgnoreCase(username)) {
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
        }
     /*   List<String> sourceValues = new ArrayList<>();
        String detectionSource = "";
        boolean fieldChanged = false;
        LOGGER.debug("hbm languageDetectionSourceFields="+languageDetectionSourceFields);
        if(languageDetectionSourceFields!=null && aContext.getChanges()!=null){
            LOGGER.debug("bm something changed");
            for(Path path:languageDetectionSourceFields){
                LOGGER.debug("path "+path+" change"+aContext.getChanges().getChange(path));
                if(aContext.getChanges().getChange(path)!=null){
                    fieldChanged=true;
                    break;
                }
            }
            if(fieldChanged) {
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
                    valueContextForUpdate.setValue(locale,localeFieldPath);
                    aContext.getProcedureContext().doModifyContent(aContext.getAdaptationOccurrence(),valueContextForUpdate);
                }
            }
        }*/
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
            LOGGER.info("Detected Locale: "+ ldLocale);
        }else{
            LOGGER.error("Language could not be detected. May be because of probability of detected language is less than minimal confidence 0.999");
        }
        return detectedLanguage;
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
}
