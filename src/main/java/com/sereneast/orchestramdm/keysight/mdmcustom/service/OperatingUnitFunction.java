package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.RequestResult;
import com.orchestranetworks.schema.ValueFunction;
import com.orchestranetworks.schema.ValueFunctionContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import org.apache.commons.lang3.StringUtils;

public class OperatingUnitFunction implements ValueFunction {

    @Override
    public Object getValue(Adaptation adaptation) {
        String countryCode = adaptation.getString(Paths._Address._Country);
        if(StringUtils.isNotBlank(countryCode)) {
            Adaptation container = adaptation.getContainer();
            AdaptationTable operatingUnitTable = container.getTable(Paths._OperatingUnit.getPathInSchema());
            final RequestResult operatingUnitTableRequestResult = operatingUnitTable.createRequestResult(Paths._OperatingUnit._CountryCode.format()+ " = '"+countryCode+"'");
            if (operatingUnitTableRequestResult != null && !operatingUnitTableRequestResult.isEmpty()) {
                return operatingUnitTableRequestResult.nextAdaptation().getString(Paths._OperatingUnit._OperatingUnit);
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
