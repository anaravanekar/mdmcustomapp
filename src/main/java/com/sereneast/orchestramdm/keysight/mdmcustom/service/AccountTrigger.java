package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.Adaptation;
import com.orchestranetworks.instance.ValueContext;
import com.orchestranetworks.schema.trigger.AfterCreateOccurrenceContext;
import com.orchestranetworks.schema.trigger.TableTrigger;
import com.orchestranetworks.schema.trigger.TriggerSetupContext;
import com.orchestranetworks.service.OperationException;
import com.orchestranetworks.service.ProcedureContext;
import com.orchestranetworks.service.ValueContextForUpdate;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountTrigger extends TableTrigger {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublishService.class);
    @Override
    public void setup(TriggerSetupContext triggerSetupContext) {

    }

    public void handleAfterCreate(AfterCreateOccurrenceContext aContext)
            throws OperationException {
        ValueContext valueContext = aContext.getOccurrenceContext();
        ProcedureContext procedureContext = aContext.getProcedureContext();
        String taxRegistrationNumber = (String)valueContext.getValue(Paths._Account._TaxRegistrationNumber);
        Integer mdmAccountId = (Integer)valueContext.getValue(Paths._Account._MDMAccountId);
        if(StringUtils.isBlank(taxRegistrationNumber)){
            Adaptation adaptation = aContext.getAdaptationOccurrence();
            ValueContextForUpdate valueContextForUpdate = procedureContext.getContext(adaptation.getAdaptationName());
            valueContextForUpdate.setValue(String.valueOf(mdmAccountId),Paths._Account._TaxRegistrationNumber);
            procedureContext.doModifyContent(adaptation, valueContextForUpdate);
        }
    }
}
