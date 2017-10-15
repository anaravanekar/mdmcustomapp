package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;

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
            setInitialized(true);
        }
    }
}
