package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.schema.trigger.AfterCreateOccurrenceContext;
import com.orchestranetworks.schema.trigger.AfterModifyOccurrenceContext;
import com.orchestranetworks.schema.trigger.TableTrigger;
import com.orchestranetworks.schema.trigger.TriggerSetupContext;
import com.orchestranetworks.service.OperationException;
import com.orchestranetworks.service.ValueContextForUpdate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public class ProspectTrigger extends TableTrigger {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProspectTrigger.class);

    @Override
    public void setup(TriggerSetupContext triggerSetupContext) {
    }

    @Override
    public void handleAfterCreate(AfterCreateOccurrenceContext aContext) throws OperationException{
        if("CMDReference".equalsIgnoreCase(aContext.getAdaptationHome().getKey().getName())
                && "Prospect".equalsIgnoreCase(aContext.getOccurrenceContext().getAdaptationInstance().getAdaptationName().getStringName())) {
            LOGGER.info("in handleAfterCreate is Prospect dataset");
            if(aContext.getTable().getTablePath().format().contains("Account")
                    && StringUtils.isNotBlank(String.valueOf(aContext.getOccurrenceContext().getValue(Path.parse("./AccountName"))))){
                LOGGER.info("in handleAfterCreate is Account path");
                List<LanguageProfile> languageProfiles = null;
                try {
                    languageProfiles = new LanguageProfileReader().readAllBuiltIn();
                } catch (IOException e) {
                    throw OperationException.createError("Error initializing language detector",e);
                }
                LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                        .withProfiles(languageProfiles)
                        .build();
                LOGGER.info("Prospect Account name is "+String.valueOf(aContext.getOccurrenceContext().getValue(Path.parse("./AccountName"))));
                String locale = getLocale(languageDetector, String.valueOf(aContext.getOccurrenceContext().getValue(Path.parse("./AccountName"))));
                LOGGER.info("Prospect Account Locale is "+locale);
                ValueContextForUpdate valueContextForUpdate = aContext.getProcedureContext().getContext(aContext.getAdaptationOccurrence().getAdaptationName());
                valueContextForUpdate.setValue(locale, Path.parse("./Locale"));
                aContext.getProcedureContext().doModifyContent(aContext.getAdaptationOccurrence(), valueContextForUpdate);
            }
        }
    }

    @Override
    public void handleAfterModify(AfterModifyOccurrenceContext aContext) throws OperationException {
        LOGGER.info("in handleAfterModify");
        LOGGER.info("dataset name : "+aContext.getAdaptationOccurrence().getAdaptationName().getStringName());
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        boolean update = false;
        if("CMDReference".equalsIgnoreCase(aContext.getAdaptationHome().getKey().getName())
                && "Prospect".equalsIgnoreCase(aContext.getOccurrenceContext().getAdaptationInstance().getAdaptationName().getStringName())) {
            LOGGER.info("in if updating record...");
            ValueContextForUpdate valueContextForUpdate = aContext.getProcedureContext().getContext(aContext.getAdaptationOccurrence().getAdaptationName());
            if(aContext.getTable().getTablePath().format().contains("Account") && aContext.getChanges().getChange(Path.parse("./AccountName"))!=null
                    && StringUtils.isNotBlank(String.valueOf(aContext.getOccurrenceContext().getValue(Path.parse("./AccountName"))))){
                LOGGER.info("in handleAfterModify is Account path");
                List<LanguageProfile> languageProfiles = null;
                try {
                    languageProfiles = new LanguageProfileReader().readAllBuiltIn();
                } catch (IOException e) {
                    throw OperationException.createError("Error initializing language detector",e);
                }
                LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                        .withProfiles(languageProfiles)
                        .build();
                String locale = getLocale(languageDetector, String.valueOf(aContext.getOccurrenceContext().getValue(Path.parse("./AccountName"))));
                valueContextForUpdate.setValue(locale, Path.parse("./Locale"));
                update = true;
            }
            if(aContext.getChanges().getChange(Path.parse("./LastActionBy"))==null && aContext.getChanges().getChange(Path.parse("./LastModifiedDate"))==null) {
                valueContextForUpdate.setValue(aContext.getSession().getUserReference().getUserId(), Path.parse("./LastActionBy"));
                valueContextForUpdate.setValue(Date.from(utc.toInstant()), Path.parse("./LastModifiedDate"));
                update = true;
            }
            if(update) {
                aContext.getProcedureContext().doModifyContent(aContext.getAdaptationOccurrence(), valueContextForUpdate);
            }
        }
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

}
