package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;

public class CountryChangeTriggerAddress extends CountryChangeTrigger {

    public CountryChangeTriggerAddress(){
        setCountryFieldPath(Paths._Address._Country);
        setOperatingUnitFieldPath(Paths._Address._OperatingUnit);
        setRegimeCodeFieldPath(Paths._Address._TaxRegimeCode);
        setStateFieldPath(Paths._Address._AddressState);
        setProvinceFieldPath(Paths._Address._Province);

        setInitialized(true);
    }
}
