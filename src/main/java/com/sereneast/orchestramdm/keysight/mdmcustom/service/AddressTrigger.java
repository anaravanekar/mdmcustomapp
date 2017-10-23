package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.AppUtil;

import java.util.List;
import java.util.Map;

public class AddressTrigger extends TableTriggerForCountry{
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
        }
    }
}
