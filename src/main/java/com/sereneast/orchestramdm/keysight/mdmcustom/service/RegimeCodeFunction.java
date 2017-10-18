package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.RequestResult;
import com.orchestranetworks.schema.ValueFunction;
import com.orchestranetworks.schema.ValueFunctionContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import org.apache.commons.lang3.StringUtils;

public class RegimeCodeFunction implements ValueFunction {

    @Override
    public Object getValue(Adaptation adaptation) {
        String countryCode = adaptation.getString(Paths._Address._Country);
        if(StringUtils.isNotBlank(countryCode)) {
            Adaptation container = adaptation.getContainer();
            AdaptationTable regimeCodeTable = container.getTable(Paths._RegimeCode.getPathInSchema());
            final RequestResult regimeCodeTableRequestResult = regimeCodeTable.createRequestResult(Paths._RegimeCode._CountryCode.format()+ " = '"+countryCode+"'");
            if (regimeCodeTableRequestResult != null && !regimeCodeTableRequestResult.isEmpty()) {
                return regimeCodeTableRequestResult.nextAdaptation().getString(Paths._RegimeCode._TaxRegimeCode);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    @Override
    public void setup(ValueFunctionContext valueFunctionContext) {

    }
}
