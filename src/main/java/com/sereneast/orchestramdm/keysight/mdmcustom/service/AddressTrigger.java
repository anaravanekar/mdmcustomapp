package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;

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
            setInitialized(true);
        }
    }
}
