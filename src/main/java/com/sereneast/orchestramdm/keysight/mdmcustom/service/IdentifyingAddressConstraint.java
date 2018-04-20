package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.RequestResult;
import com.onwbp.base.text.UserMessage;
import com.orchestranetworks.instance.ValueContext;
import com.orchestranetworks.instance.ValueContextForValidation;
import com.orchestranetworks.schema.Constraint;
import com.orchestranetworks.schema.ConstraintContext;
import com.orchestranetworks.schema.InvalidSchemaException;
import com.orchestranetworks.schema.Path;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class IdentifyingAddressConstraint implements Constraint {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdentifyingAddressConstraint.class);

    @Override
    public void checkOccurrence(Object o, ValueContextForValidation aContext) {
        Object mdmAccountId = aContext.getValue(Path.PARENT.add(Paths._Address._MDMAccountId));
        if(mdmAccountId!=null) {
            AdaptationTable table = aContext.getAdaptationTable();
            RequestResult tableRequestResult = table.createRequestResult(Paths._Address._MDMAccountId.format() + " = '" + String.valueOf(mdmAccountId) + "' and (" + Paths._Address._IdentifyingAddress.format() + " = 'Y')");
            String currentIdentifyingAddress = aContext.getValue(Path.PARENT.add(Paths._Address._IdentifyingAddress)) != null ? aContext.getValue(Path.PARENT.add(Paths._Address._IdentifyingAddress)).toString() : null;
            Object currentMdmAddressId = aContext.getValue(Path.PARENT.add(Paths._Address._MDMAddressId));
            boolean otherRecordIsIdentifying = false;
            Integer otherRecordId = null;
            if (tableRequestResult != null && tableRequestResult.getSize() > 0) {
                LOGGER.debug("Record(s) found. Size=" + tableRequestResult.getSize());
                for (Adaptation tableRequestResultRecord; (tableRequestResultRecord = tableRequestResult.nextAdaptation()) != null; ) {
                    if (!String.valueOf(tableRequestResultRecord.get(Paths._Address._MDMAddressId)).equals(String.valueOf(currentMdmAddressId))) {
                        otherRecordId = tableRequestResultRecord.get_int(Paths._Address._MDMAddressId);
                        otherRecordIsIdentifying = true;
                        LOGGER.debug("otherRecordIsIdentifying=" + otherRecordIsIdentifying);
                        break;
                    }
                }
            }
            if (otherRecordIsIdentifying && "Y".equals(currentIdentifyingAddress)) {
                UserMessage message = UserMessage.createError("Cannot mark address as identifying address. Address with MDMAddressId " + otherRecordId + " is already marked as identifying address.");
                aContext.addMessage(message);
            }
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
