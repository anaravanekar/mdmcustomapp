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

public class CountryConstraint implements Constraint {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryConstraint.class);

    @Override
    public void checkOccurrence(Object o, ValueContextForValidation valueContextForValidation) {
        String countryCode = o!=null?String.valueOf(o):null;/*valueContextForValidation.getValue(Path.PARENT.add(Paths._Address._Country)) != null ?
                String.valueOf(valueContextForValidation.getValue(Path.PARENT.add(Paths._Address._Country))) : null;*/
        String currentState = valueContextForValidation.getValue(Path.PARENT.add(Paths._Address._AddressState)) != null ?
                String.valueOf(valueContextForValidation.getValue(Path.PARENT.add(Paths._Address._AddressState))) : null;
        ApplicationCacheUtil applicationCacheUtil = (ApplicationCacheUtil) SpringContext.getApplicationContext().getBean("applicationCacheUtil");
        Map<String, String> territoryTypeMap = applicationCacheUtil.getTerritoryTypeMap("BReference");
        if(StringUtils.isNotBlank(countryCode) && StringUtils.isNotBlank(currentState) && "STATE".equalsIgnoreCase(territoryTypeMap.get(countryCode))) {
            String errorMessage = applicationCacheUtil.validateState(countryCode,currentState,false);
            if(StringUtils.isNotBlank(errorMessage)){
                UserMessage message = UserMessage.createError(errorMessage);
                valueContextForValidation.addMessage(message);
            }
        }
    }

    @Override
    public void setup(ConstraintContext constraintContext) {
        /*LOGGER.info("StateConstraint.setup-> schemaNode{}",constraintContext.getSchemaNode().toString());
        LOGGER.info("constraintContext.getSchemaNode().getTableNode().getNode(Paths._Address._Country)={}",constraintContext.getSchemaNode().getTableNode().getNode(Paths._Address._Country));
        constraintContext.addDependencyToModify(constraintContext.getSchemaNode().getTableNode().getNode(Paths._Address._Country));
        constraintContext.addDependencyToModify(constraintContext.getSchemaNode().getTableNode().getNode(Paths._Address._AddressState));*/
    }

    @Override
    public String toUserDocumentation(Locale locale, ValueContext valueContext) {
        return null;
    }
}
