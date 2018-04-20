package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.base.text.UserMessage;
import com.orchestranetworks.instance.ValueContext;
import com.orchestranetworks.instance.ValueContextForValidation;
import com.orchestranetworks.schema.Constraint;
import com.orchestranetworks.schema.ConstraintContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class LocationConstraint extends GenericLengthConstraint implements Constraint {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationConstraint.class);

    @Override
    public void checkOccurrence(Object o, ValueContextForValidation valueContextForValidation) {
        String errorMessage = validateByteLength(String.valueOf(o),40);
        if(StringUtils.isNotBlank(errorMessage)){
            UserMessage message = UserMessage.createError(errorMessage);
            valueContextForValidation.addMessage(message);
        }
    }

    @Override
    public void setup(ConstraintContext constraintContext) {

    }

    @Override
    public String toUserDocumentation(Locale locale, ValueContext valueContext) {
        return null;
    }
}
