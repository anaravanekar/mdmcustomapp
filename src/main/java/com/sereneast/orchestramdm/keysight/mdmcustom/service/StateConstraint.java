package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.base.text.UserMessage;
import com.orchestranetworks.instance.ValueContext;
import com.orchestranetworks.instance.ValueContextForValidation;
import com.orchestranetworks.schema.*;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.SpringContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ApplicationCacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;

public class StateConstraint implements Constraint, ConstraintOnNull {

    private static final Logger LOGGER = LoggerFactory.getLogger(StateConstraint.class);

    @Override
    public void checkNull(ValueContextForValidation valueContextForValidation) {
        Object countryValue = valueContextForValidation.getValue(Path.PARENT.add(Paths._Address._Country));
        if(countryValue!=null && ("BR".equals(String.valueOf(countryValue)) || "US".equals(String.valueOf(countryValue))
                || "IN".equals(String.valueOf(countryValue)) || "MX".equals(String.valueOf(countryValue)))){
            UserMessage message = UserMessage.createError("State is required for Brazil, United States, India and Mexico");
            valueContextForValidation.addMessage(message);
        }
    }

    @Override
    public void checkOccurrence(Object o, ValueContextForValidation valueContextForValidation) {
        String countryCode = valueContextForValidation.getValue(Path.PARENT.add(Paths._Address._Country)) != null ?
                String.valueOf(valueContextForValidation.getValue(Path.PARENT.add(Paths._Address._Country))) : null;
        String currentState = valueContextForValidation.getValue(Path.PARENT.add(Paths._Address._AddressState)) != null ?
                String.valueOf(valueContextForValidation.getValue(Path.PARENT.add(Paths._Address._AddressState))) : null;
        ApplicationCacheUtil applicationCacheUtil = (ApplicationCacheUtil) SpringContext.getApplicationContext().getBean("applicationCacheUtil");
        Map<String, String> territoryTypeMap = applicationCacheUtil.getTerritoryTypeMap("BReference");
        if(StringUtils.isNotBlank(countryCode) && "STATE".equalsIgnoreCase(territoryTypeMap.get(countryCode))) {
            String errorMessage = applicationCacheUtil.validateState(countryCode,currentState,false);
            if(StringUtils.isNotBlank(errorMessage)){
                UserMessage message = UserMessage.createError(errorMessage);
                valueContextForValidation.addMessage(message);
            }
        }
    }

    @Override
    public void setup(ConstraintContext constraintContext) {
        LOGGER.info("StateConstraint.setup-> schemaNode{}",constraintContext.getSchemaNode().toString());
        LOGGER.info("constraintContext.getSchemaNode().getTableNode().getNode(Paths._Address._Country)={}",constraintContext.getSchemaNode().getTableNode().getNode(Paths._Address._Country));
        constraintContext.addDependencyToModify(constraintContext.getSchemaNode().getTableNode().getNode(Paths._Address._Country));
        constraintContext.addDependencyToModify(constraintContext.getSchemaNode().getTableNode().getNode(Paths._Address._AddressState));
    }

    @Override
    public String toUserDocumentation(Locale locale, ValueContext valueContext) {
        return null;
    }
}
