package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.base.text.UserMessage;
import com.orchestranetworks.instance.ValueContext;
import com.orchestranetworks.instance.ValueContextForValidation;
import com.orchestranetworks.schema.Constraint;
import com.orchestranetworks.schema.ConstraintContext;
import com.orchestranetworks.schema.Path;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.SpringContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ApplicationCacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;

public class StateConstraintLocal implements Constraint {

    private static final Logger LOGGER = LoggerFactory.getLogger(StateConstraintLocal.class);

    @Override
    public void checkOccurrence(Object o, ValueContextForValidation valueContextForValidation) {
        LOGGER.debug("StateConstraintLocal.checkOccurrence-> ");
        String countryCode = valueContextForValidation.getValue(Path.PARENT.add(Paths._Address._Country)) != null ?
                String.valueOf(valueContextForValidation.getValue(Path.PARENT.add(Paths._Address._Country))) : null;
        String currentState = valueContextForValidation.getValue(Path.PARENT.add(Paths._Address._StateLocalLanguage)) != null ?
                String.valueOf(valueContextForValidation.getValue(Path.PARENT.add(Paths._Address._StateLocalLanguage))) : null;
        ApplicationCacheUtil applicationCacheUtil = (ApplicationCacheUtil) SpringContext.getApplicationContext().getBean("applicationCacheUtil");
        Map<String, String> territoryTypeMap = applicationCacheUtil.getTerritoryTypeMap("BReference");
        if(StringUtils.isNotBlank(countryCode) && "STATE".equalsIgnoreCase(territoryTypeMap.get(countryCode))) {
            String errorMessage = applicationCacheUtil.validateState(countryCode,currentState,true);
            if(StringUtils.isNotBlank(errorMessage)){
                LOGGER.debug("message="+errorMessage);
                UserMessage message = UserMessage.createError(errorMessage);
                valueContextForValidation.addMessage(message);
            }
        }
        LOGGER.debug("<-StateConstraintLocal.checkOccurrence ");
    }

    @Override
    public void setup(ConstraintContext constraintContext) {

    }

    @Override
    public String toUserDocumentation(Locale locale, ValueContext valueContext) {
        return null;
    }
}
