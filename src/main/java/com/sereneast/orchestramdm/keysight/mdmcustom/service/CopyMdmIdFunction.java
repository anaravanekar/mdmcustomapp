package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.Adaptation;
import com.orchestranetworks.schema.ValueFunction;
import com.orchestranetworks.schema.ValueFunctionContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import org.apache.commons.lang3.StringUtils;

public class CopyMdmIdFunction implements ValueFunction {

    @Override
    public Object getValue(Adaptation adaptation) {
        String taxPayerId = adaptation.getString(Paths._Account._TaxpayerId);
        if(StringUtils.isBlank(taxPayerId)){
            int mdmId = adaptation.get_int(Paths._Account._AccountName);
            return String.valueOf(mdmId);
        }else{
            return taxPayerId;
        }
    }

    @Override
    public void setup(ValueFunctionContext valueFunctionContext) {

    }
}
