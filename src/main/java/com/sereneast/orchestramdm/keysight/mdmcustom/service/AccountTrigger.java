package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.AppUtil;

import java.util.List;
import java.util.Map;

public class AccountTrigger extends TableTriggerForCountry {

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
        }
    }
}
