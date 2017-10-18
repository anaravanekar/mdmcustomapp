package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.Adaptation;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.schema.ValueFunction;
import com.orchestranetworks.schema.ValueFunctionContext;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AddressFieldFunction implements ValueFunction {

    @Override
    public Object getValue(Adaptation adaptation) {
        List<String> addresses = new ArrayList<>();
        for(int i=1;i<=4;i++){
            Path addresslinePath = Path.parse("./AddressLine"+i);
            if(StringUtils.isNotBlank(adaptation.getString(addresslinePath))){
                addresses.add(adaptation.getString(addresslinePath).trim());
            }
        }
        if(!addresses.isEmpty()){
            return StringUtils.join(addresses, " ");
        }else{
            return null;
        }
    }

    @Override
    public void setup(ValueFunctionContext valueFunctionContext) {

    }
}
