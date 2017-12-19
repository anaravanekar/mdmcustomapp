package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.RequestResult;
import com.orchestranetworks.schema.trigger.*;
import com.orchestranetworks.service.OperationException;
import com.orchestranetworks.service.ValueContextForUpdate;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.SpringContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.exception.ApplicationRuntimeException;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraContent;
import com.sereneast.orchestramdm.keysight.mdmcustom.rest.client.OrchestraRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BusinessPurposeTrigger extends TableTrigger {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessPurposeTrigger.class);

    @Override
    public void setup(TriggerSetupContext triggerSetupContext) {
    }

    @Override
    public void handleNewContext(NewTransientOccurrenceContext newContext) {
        LOGGER.debug("handleNewContext called");
        newContext.getOccurrenceContextForUpdate().setValue(newContext.getOccurrenceContext().getValue(Paths._BusinessPurpose._MDMAddressId),Paths._BusinessPurpose._Location);
    }

    @Override
    public void handleBeforeCreate(BeforeCreateOccurrenceContext aContext) throws OperationException {
        LOGGER.debug("handleBeforeCreate called");
    }

    @Override
    public void handleAfterCreate(AfterCreateOccurrenceContext aContext) throws OperationException {
        LOGGER.debug("handleAfterCreate called");
        boolean update = false;
        ValueContextForUpdate valueContextForUpdate = aContext.getProcedureContext().getContext(aContext.getAdaptationOccurrence().getAdaptationName());
        if(aContext.getOccurrenceContext().getValue(Paths._BusinessPurpose._MDMAddressId)!=null) {
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
        }
        if(update) {
            aContext.getProcedureContext().doModifyContent(aContext.getAdaptationOccurrence(), valueContextForUpdate);
        }
    }

    @Override
    public void handleBeforeModify(BeforeModifyOccurrenceContext var1) throws OperationException {
        LOGGER.debug("handleBeforeModify called");
    }

    @Override
    public void handleAfterModify(AfterModifyOccurrenceContext var1) throws OperationException {
        LOGGER.debug("handleAfterModify called");
    }

    @Override
    public void handleBeforeTransactionCommit(BeforeTransactionCommitContext aContext) throws OperationException {
        LOGGER.debug("handleBeforeTransactionCommit called");
       /* if(aContext.getOccurrenceContext().getValue(Paths._BusinessPurpose._MDMAddressId)!=null) {
            AdaptationTable addressTable = aContext.getTable().getContainerAdaptation().getTable(Paths._Address.getPathInSchema());
            RequestResult addressRequestResult = addressTable.createRequestResult(Paths._Address._MDMAddressId.format() + " = " + Integer.valueOf(aContext.getOccurrenceContext().getValue(Paths._BusinessPurpose._MDMAddressId).toString()));
            if (aContext.getOccurrenceContext().getValue(Paths._BusinessPurpose._OperatingUnit) != null) {
                List<String> operatingUnits = (List<String>)aContext.getOccurrenceContext().getValue(Paths._BusinessPurpose._OperatingUnit);
                List<String> addressOperatingUnits = addressRequestResult.nextAdaptation().getList(Paths._Address._OperatingUnit);
                if (addressOperatingUnits == null) {
                    addressOperatingUnits = new ArrayList<>();
                }
                List<String> finalOuList = Stream.concat(operatingUnits.stream(), addressOperatingUnits.stream())
                        .collect(Collectors.toList());
                List<OrchestraContent> ouContentList = new ArrayList<>();
                for (String ou : finalOuList) {
                    ouContentList.add(new OrchestraContent(ou));
                }
                OrchestraRestClient orchestraRestClient = (OrchestraRestClient) SpringContext.getApplicationContext().getBean("orchestraRestClient");
                try {
                    orchestraRestClient.updateField(aContext.getAdaptationHome().getKey().format(), "Account", "root/Address/" + aContext.getOccurrenceContext().getValue(Paths._BusinessPurpose._MDMAddressId).toString() + "/OperatingUnit", new OrchestraContent(ouContentList), null);
                } catch (IOException e) {
                    throw new ApplicationRuntimeException("Error udpating operating units on address");
                }
            }
        }*/
    }

    @Override
    public void handleBeforeTransactionCancel(BeforeTransactionCancelContext var1) {
        LOGGER.debug("handleBeforeTransactionCancel called");
    }
}
