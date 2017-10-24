package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.orchestranetworks.schema.Path;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.AppUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddressTrigger extends GenericTrigger {

    @Override
    public void initialize() {
        if(!isInitialized()){
            setCountryPath(Paths._Address._Country);
            setDaqaTargetFieldPath(Paths._Address._DaqaMetaData_TargetRecord);
            setMergeOriginPath(Paths._Address._DaqaMetaData_MergeOrigin);
            setObjectPrimaryKeyPath(Paths._Address._MDMAddressId);
            setStateFieldPath(Paths._Address._DaqaMetaData_State);
            setTimestampPath(Paths._Address._DaqaMetaData_Timestamp);
            setPublishedFieldPath(Paths._Address._Published);
            setObjectName("Address");
            List<String> lovsToMerge = (List<String>) ((Map)((Map)((Map)((Map) AppUtil.getAllPropertiesMap().get("keysight")).get("matching"))).get("lovsToMerge")).get("address");
            setLovsToMerge(lovsToMerge);
            setInitialized(true);
         /*   try {
                List<LanguageProfile> languageProfiles = null;
                languageProfiles = new LanguageProfileReader().readAllBuiltIn();
                LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                        .withProfiles(languageProfiles)
                        .build();
                GenericTrigger.languageDetector = languageDetector;
            }catch (IOException e){
                throw new ApplicationRuntimeException("Error intializing language detector",e);
            }*/
            List<Path> languageDetectionSourceFields = new ArrayList<>();
            languageDetectionSourceFields.add(Paths._Address._AddressLine1);
            languageDetectionSourceFields.add(Paths._Address._AddressLine2);
            languageDetectionSourceFields.add(Paths._Address._AddressLine3);
            languageDetectionSourceFields.add(Paths._Address._AddressLine4);
            setLanguageDetectionSourceFields(languageDetectionSourceFields);
//            setLocaleFieldPath(Paths._Address._Locale);
        }
    }
}
