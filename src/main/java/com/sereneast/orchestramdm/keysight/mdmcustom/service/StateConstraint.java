package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.base.text.UserMessage;
import com.orchestranetworks.instance.ValueContext;
import com.orchestranetworks.instance.ValueContextForValidation;
import com.orchestranetworks.schema.*;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;

import java.util.Locale;

public class StateConstraint implements Constraint, ConstraintOnNull {
    @Override
    public void checkNull(ValueContextForValidation valueContextForValidation) throws InvalidSchemaException {
        Object countryValue = valueContextForValidation.getValue(Path.PARENT.add(Paths._Address._Country));
        if(countryValue!=null && ("BR".equals(String.valueOf(countryValue)))){
            UserMessage message = UserMessage.createError("State is required when country is Brazil");//UserMessage(Severity.ERROR,"ThePriceMustNotDifferFromTheDefaultPriceMoreThan{0}");
            valueContextForValidation.addMessage(message);
        }
    }

    @Override
    public void checkOccurrence(Object o, ValueContextForValidation valueContextForValidation) throws InvalidSchemaException {

    }

    @Override
    public void setup(ConstraintContext constraintContext) {

    }

    @Override
    public String toUserDocumentation(Locale locale, ValueContext valueContext) throws InvalidSchemaException {
        return null;
    }
}
