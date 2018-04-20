package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.base.text.UserMessage;
import com.orchestranetworks.instance.ValueContext;
import com.orchestranetworks.instance.ValueContextForValidation;
import com.orchestranetworks.schema.Constraint;
import com.orchestranetworks.schema.ConstraintContext;
import com.orchestranetworks.schema.ConstraintOnNull;
import com.orchestranetworks.schema.Path;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.SpringContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ApplicationCacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;

public class ProvinceConstraint implements Constraint, ConstraintOnNull {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProvinceConstraint.class);

    @Override
    public void checkNull(ValueContextForValidation valueContextForValidation){
        Object countryValue = valueContextForValidation.getValue(Path.PARENT.add(Paths._Address._Country));
        if(countryValue!=null && ("CA".equals(String.valueOf(countryValue)))){
            UserMessage message = UserMessage.createError("Province is required for Canada");
            valueContextForValidation.addMessage(message);
        }
    }

    @Override
    public void checkOccurrence(Object o, ValueContextForValidation valueContextForValidation) {
/*        String countryCode = valueContextForValidation.getValue(Path.PARENT.add(Paths._Address._Country)) != null ?
                String.valueOf(valueContextForValidation.getValue(Path.PARENT.add(Paths._Address._Country))) : null;
        String currentProvince = valueContextForValidation.getValue(Path.PARENT.add(Paths._Address._Province)) != null ?
                String.valueOf(valueContextForValidation.getValue(Path.PARENT.add(Paths._Address._Province))) : null;
        ApplicationCacheUtil applicationCacheUtil = (ApplicationCacheUtil) SpringContext.getApplicationContext().getBean("applicationCacheUtil");
        Map<String, String> territoryTypeMap = applicationCacheUtil.getTerritoryTypeMap("BReference");
        if(StringUtils.isNotBlank(countryCode) && "PROVINCE".equalsIgnoreCase(territoryTypeMap.get(countryCode))) {
            String errorMessage = applicationCacheUtil.validateProvince(countryCode,currentProvince,false);
            if(StringUtils.isNotBlank(errorMessage)){
                UserMessage message = UserMessage.createError(errorMessage);
                valueContextForValidation.addMessage(message);
            }
        }*/
    }

    @Override
    public void setup(ConstraintContext constraintContext) {

    }

    @Override
    public String toUserDocumentation(Locale locale, ValueContext valueContext) {
        return null;
    }
}
