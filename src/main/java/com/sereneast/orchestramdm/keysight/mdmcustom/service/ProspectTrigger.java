package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.orchestranetworks.schema.Path;
import com.orchestranetworks.schema.trigger.AfterCreateOccurrenceContext;
import com.orchestranetworks.schema.trigger.AfterModifyOccurrenceContext;
import com.orchestranetworks.schema.trigger.TableTrigger;
import com.orchestranetworks.schema.trigger.TriggerSetupContext;
import com.orchestranetworks.service.OperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProspectTrigger extends TableTrigger {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProspectTrigger.class);

    @Override
    public void setup(TriggerSetupContext triggerSetupContext) {
    }

    @Override
    public void handleAfterCreate(AfterCreateOccurrenceContext aContext) throws OperationException{
        if("CMDReference".equalsIgnoreCase(aContext.getAdaptationHome().getKey().getName())
                && "Prospect".equalsIgnoreCase(aContext.getAdaptationOccurrence().getAdaptationName().getStringName())) {
        }
    }

    @Override
    public void handleAfterModify(AfterModifyOccurrenceContext aContext) throws OperationException {
        if("CMDReference".equalsIgnoreCase(aContext.getAdaptationHome().getKey().getName())
                && "Prospect".equalsIgnoreCase(aContext.getAdaptationOccurrence().getAdaptationName().getStringName())) {
            aContext.getProcedureContext().getContext(aContext.getAdaptationOccurrence().getAdaptationName()).setValue(
                    aContext.getSession().getUserReference().getUserId(), Path.parse("./LastActionBy"));
        }
    }
}
