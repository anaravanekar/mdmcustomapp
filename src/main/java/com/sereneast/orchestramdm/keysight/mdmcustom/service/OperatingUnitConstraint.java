package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.RequestResult;
import com.onwbp.base.text.UserMessage;
import com.orchestranetworks.instance.ValueContext;
import com.orchestranetworks.instance.ValueContextForValidation;
import com.orchestranetworks.schema.Constraint;
import com.orchestranetworks.schema.ConstraintContext;
import com.orchestranetworks.schema.Path;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class OperatingUnitConstraint implements Constraint {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperatingUnitConstraint.class);

    @Override
    public void checkOccurrence(Object o, ValueContextForValidation valueContextForValidation) {
        LOGGER.debug("OperatingUnitConstraint.checkOccurrence->");
        if(o!=null) {
            LOGGER.debug("o not null. o="+o);
            // List addressOus = (List)o;
            //LOGGER.debug("addressOus="+addressOus.size());
            List<String> ousWithNoBp = new ArrayList<>();
            AdaptationTable table = valueContextForValidation.getAdaptationInstance().getTable(Paths._BusinessPurpose.getPathInSchema());
            RequestResult bpResult = table.createRequestResult(Paths._BusinessPurpose._MDMAddressId.format() + " = '" + valueContextForValidation.getValue(Path.PARENT.add(Paths._Address._MDMAddressId)) + "'");
            HashSet<String> bpOus = new HashSet<>();
            if (bpResult != null && !bpResult.isEmpty()) {
                for (Adaptation bpAdapatation; (bpAdapatation = bpResult.nextAdaptation()) != null; ) {
                    if (bpAdapatation.getList(Paths._BusinessPurpose._OperatingUnit) != null) {
                        List<String> ous = bpAdapatation.getList(Paths._BusinessPurpose._OperatingUnit);
                        if (ous != null) {
                            bpOus.addAll(ous);
                        }
                    }
                }
                LOGGER.debug("bpOus="+bpOus.size());
                //for(Object addressOu:addressOus){
                if(!bpOus.contains(String.valueOf(o))){
                    ousWithNoBp.add(String.valueOf(o));
                }
                // }
                LOGGER.debug("ousWithNoBp="+ousWithNoBp.size());
                if(!ousWithNoBp.isEmpty()){
                    UserMessage message = UserMessage.createError("No Business Purpose exists for Operating Units "+StringUtils.join(ousWithNoBp, ','));
                    valueContextForValidation.addMessage(message);
                }
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
