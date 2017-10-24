package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.orchestranetworks.schema.Path;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.exception.ApplicationRuntimeException;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.AppUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountTrigger extends GenericTrigger {

    @Override
    public void initialize() {
        if(!isInitialized()){
            setCountryPath(Paths._Account._Country);
            setDaqaTargetFieldPath(Paths._Account._DaqaMetaData_TargetRecord);
            setMergeOriginPath(Paths._Account._DaqaMetaData_MergeOrigin);
            setObjectPrimaryKeyPath(Paths._Account._MDMAccountId);
            setStateFieldPath(Paths._Account._DaqaMetaData_State);
            setTimestampPath(Paths._Account._DaqaMetaData_Timestamp);
            setPublishedFieldPath(Paths._Account._Published);
            setObjectName("Account");
            List<String> lovsToMerge = (List<String>) ((Map)((Map)((Map)((Map) AppUtil.getAllPropertiesMap().get("keysight")).get("matching"))).get("lovsToMerge")).get("account");
            setLovsToMerge(lovsToMerge);
            setInitialized(true);
            try {
                List<LanguageProfile> languageProfiles = null;
                languageProfiles = new LanguageProfileReader().readAllBuiltIn();
                LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                        .withProfiles(languageProfiles)
                        .build();
                GenericTrigger.languageDetector = languageDetector;
            }catch (IOException e){
                throw new ApplicationRuntimeException("Error intializing language detector",e);
            }
            List<Path> languageDetectionSourceFields = new ArrayList<>();
            languageDetectionSourceFields.add(Paths._Account._AccountName);
            setLanguageDetectionSourceFields(languageDetectionSourceFields);
//            setLocaleFieldPath(Paths._Account._Locale);
        }
    }
}
