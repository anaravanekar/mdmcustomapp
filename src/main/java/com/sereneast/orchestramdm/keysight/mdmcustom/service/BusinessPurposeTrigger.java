package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.RequestResult;
import com.orchestranetworks.schema.trigger.AfterCreateOccurrenceContext;
import com.orchestranetworks.schema.trigger.NewTransientOccurrenceContext;
import com.orchestranetworks.schema.trigger.TableTrigger;
import com.orchestranetworks.schema.trigger.TriggerSetupContext;
import com.orchestranetworks.service.OperationException;
import com.orchestranetworks.service.ValueContextForUpdate;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BusinessPurposeTrigger extends TableTrigger {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublishService.class);

    @Override
    public void setup(TriggerSetupContext triggerSetupContext) {
    }

    public void handleNewContext(NewTransientOccurrenceContext newContext) {
        newContext.getOccurrenceContextForUpdate().setValue(newContext.getOccurrenceContext().getValue(Paths._BusinessPurpose._MDMAddressId),Paths._BusinessPurpose._Location);
    }

    public void handleAfterCreate(AfterCreateOccurrenceContext aContext) throws OperationException {
        if(aContext.getOccurrenceContext().getValue(Paths._BusinessPurpose._MDMAddressId)!=null) {
            boolean update = false;
            ValueContextForUpdate valueContextForUpdate = aContext.getProcedureContext().getContext(aContext.getAdaptationOccurrence().getAdaptationName());
            Object addressId = aContext.getOccurrenceContext().getValue(Paths._BusinessPurpose._MDMAddressId);
            String purposeId = String.valueOf(aContext.getOccurrenceContext().getValue(Paths._BusinessPurpose._MDMPurposeId));
            String condition = Paths._BusinessPurpose._MDMAddressId.format() + " = '" + String.valueOf(addressId)+"'";
            AdaptationTable table = aContext.getTable();//getAdaptationOccurrence().getContainer().getTable(Paths._Address.getPathInSchema());
            RequestResult tableRequestResult = table.createRequestResult(condition);
            if(tableRequestResult==null || tableRequestResult.isEmpty() || tableRequestResult.getSize()==1){
                if(!String.valueOf(tableRequestResult.nextAdaptation().get(Paths._BusinessPurpose._MDMPurposeId)).equals(purposeId)){
                    return;
                }
                valueContextForUpdate.setValue("Y", Paths._BusinessPurpose._Primary);
                update = true;
            }
            if(aContext.getOccurrenceContext().getValue(Paths._BusinessPurpose._Location)==null){
                valueContextForUpdate.setValue(aContext.getOccurrenceContext().getValue(Paths._BusinessPurpose._MDMAddressId), Paths._BusinessPurpose._Location);
                update = true;
            }
            if(update) {
                aContext.getProcedureContext().doModifyContent(aContext.getAdaptationOccurrence(), valueContextForUpdate);
            }
        }
    }
}
