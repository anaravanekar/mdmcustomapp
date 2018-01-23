package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.RequestResult;
import com.orchestranetworks.schema.trigger.*;
import com.orchestranetworks.service.OperationException;
import com.orchestranetworks.service.ValueContextForUpdate;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
        if("CMDReference".equalsIgnoreCase(aContext.getAdaptationHome().getKey().getName())) {
            boolean update = false;
            ValueContextForUpdate valueContextForUpdate = aContext.getProcedureContext().getContext(aContext.getAdaptationOccurrence().getAdaptationName());
            if (aContext.getOccurrenceContext().getValue(Paths._BusinessPurpose._MDMAddressId) != null) {
                if (aContext.getAdaptationOccurrence().getList(Paths._BusinessPurpose._Primary) == null ||
                        aContext.getAdaptationOccurrence().getList(Paths._BusinessPurpose._Primary).isEmpty()) {
                    List<String> bpExistsOus = new ArrayList<>();
                    List<String> primaryForOus = new ArrayList<>();
                    List thisOus = aContext.getAdaptationOccurrence().getList(Paths._BusinessPurpose._OperatingUnit);
                    thisOus = thisOus != null ? thisOus : new ArrayList();
                    Object addressId = aContext.getOccurrenceContext().getValue(Paths._BusinessPurpose._MDMAddressId);
                    String purposeId = String.valueOf(aContext.getOccurrenceContext().getValue(Paths._BusinessPurpose._MDMPurposeId));
                    String condition = Paths._Address._MDMAddressId.format() + " = " + String.valueOf(addressId) + "";
                    String mdmAccountId = null;
                    AdaptationTable bpTable = aContext.getTable();
                    AdaptationTable addressTable = aContext.getTable().getContainerAdaptation().getTable(Paths._Address.getPathInSchema());
                    RequestResult thisAddressRequestResult = addressTable.createRequestResult(condition);
                    if (thisAddressRequestResult != null && !thisAddressRequestResult.isEmpty()) {
                        mdmAccountId = thisAddressRequestResult.nextAdaptation().getString(Paths._Address._MDMAccountId);
                        String accountIdCondition = Paths._Address._MDMAccountId.format() + " = '" + mdmAccountId + "'";
                        RequestResult allAddressesRequestResult = addressTable.createRequestResult(accountIdCondition);
                        if (allAddressesRequestResult != null && !allAddressesRequestResult.isEmpty()) {
                            for (Adaptation address; (address = allAddressesRequestResult.nextAdaptation()) != null; ) {
                                String bpCondition = Paths._BusinessPurpose._BusinessPurpose.format() + " = '" + aContext.getAdaptationOccurrence().getString(Paths._BusinessPurpose._BusinessPurpose) + "'"
                                        + " and (" + Paths._BusinessPurpose._MDMAddressId.format() + " = '" + address.get(Paths._Address._MDMAddressId).toString() + "')";
                                RequestResult bpRequestResult = bpTable.createRequestResult(bpCondition);
                                if (bpRequestResult != null && !bpRequestResult.isEmpty()) {
                                    for (Adaptation bp; (bp = bpRequestResult.nextAdaptation()) != null; ) {
                                        List resultBpOu = bp.getList(Paths._BusinessPurpose._OperatingUnit);
                                        String resultPurposeId = String.valueOf(bp.get(Paths._BusinessPurpose._MDMPurposeId));
                                        if (!purposeId.equals(resultPurposeId) && !thisOus.isEmpty() && resultBpOu != null && !resultBpOu.isEmpty()) {
                                            for (Object ou : thisOus) {
                                                if (resultBpOu.contains(ou)) {
                                                    bpExistsOus.add(String.valueOf(ou));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    for (Object ou : thisOus) {
                        if (!bpExistsOus.contains(String.valueOf(ou))) {
                            primaryForOus.add(String.valueOf(ou));
                        }
                    }
                    if (!primaryForOus.isEmpty()) {
                        valueContextForUpdate.setValue(primaryForOus, Paths._BusinessPurpose._Primary);
                        update = true;
                    }
                }
                if (aContext.getOccurrenceContext().getValue(Paths._BusinessPurpose._Location) == null) {
                    valueContextForUpdate.setValue(aContext.getOccurrenceContext().getValue(Paths._BusinessPurpose._MDMAddressId), Paths._BusinessPurpose._Location);
                    update = true;
                }
            }
            if (update) {
                aContext.getProcedureContext().doModifyContent(aContext.getAdaptationOccurrence(), valueContextForUpdate);
            }
        }
    }

    @Override
    public void handleBeforeModify(BeforeModifyOccurrenceContext var1) throws OperationException {
        LOGGER.debug("handleBeforeModify called");
    }

    @Override
    public void handleAfterModify(AfterModifyOccurrenceContext aContext) throws OperationException {
        LOGGER.debug("handleAfterModify called");
        if("CMDReference".equalsIgnoreCase(aContext.getAdaptationHome().getKey().getName())) {
            boolean update = false;
            ValueContextForUpdate valueContextForUpdate = aContext.getProcedureContext().getContext(aContext.getAdaptationOccurrence().getAdaptationName());
            if (aContext.getChanges() != null && aContext.getChanges().getChange(Paths._BusinessPurpose._OperatingUnit) != null) {
                List<String> bpExistsOus = new ArrayList<>();
                List<String> primaryForOus = aContext.getAdaptationOccurrence().getList(Paths._BusinessPurpose._Primary);
                HashSet<String> primaryForOusSet = primaryForOus != null ? new HashSet<>(primaryForOus) : new HashSet<>();
                List<String> oUsBefore = aContext.getChanges().getChange(Paths._BusinessPurpose._OperatingUnit).getValueBefore() != null ? (List) aContext.getChanges().getChange(Paths._BusinessPurpose._OperatingUnit).getValueBefore() : null;
                HashSet<String> oUsBeforeSet = oUsBefore != null ? new HashSet<>(oUsBefore) : new HashSet<>();
                List<String> currentOus = aContext.getAdaptationOccurrence().getList(Paths._BusinessPurpose._OperatingUnit);
                HashSet<String> currentOusSet = currentOus != null ? new HashSet<>(currentOus) : new HashSet<>();
                HashSet<String> oUsRemovedSet = new HashSet<>(oUsBeforeSet);
                oUsRemovedSet.removeAll(new HashSet<>(currentOusSet));
                List<String> existingOusRemoved = aContext.getAdaptationOccurrence().getList(Paths._BusinessPurpose._RemovedOperatingUnits);
                existingOusRemoved = existingOusRemoved != null ? existingOusRemoved : new ArrayList<>();
                HashSet<String> oUsAddedSet = new HashSet<>(currentOusSet);
                oUsAddedSet.removeAll(new HashSet<>(oUsBeforeSet));
                primaryForOusSet.removeAll(oUsRemovedSet);
                List<String> thisOus = new ArrayList<>(oUsAddedSet);
                Object addressId = aContext.getOccurrenceContext().getValue(Paths._BusinessPurpose._MDMAddressId);
                String purposeId = String.valueOf(aContext.getOccurrenceContext().getValue(Paths._BusinessPurpose._MDMPurposeId));
                String condition = Paths._Address._MDMAddressId.format() + " = " + String.valueOf(addressId) + "";
                String mdmAccountId = null;
                AdaptationTable bpTable = aContext.getTable();
                AdaptationTable addressTable = aContext.getTable().getContainerAdaptation().getTable(Paths._Address.getPathInSchema());
                RequestResult thisAddressRequestResult = addressTable.createRequestResult(condition);
                if (thisAddressRequestResult != null && !thisAddressRequestResult.isEmpty()) {
                    mdmAccountId = thisAddressRequestResult.nextAdaptation().getString(Paths._Address._MDMAccountId);
                    String accountIdCondition = Paths._Address._MDMAccountId.format() + " = '" + mdmAccountId + "'";
                    RequestResult allAddressesRequestResult = addressTable.createRequestResult(accountIdCondition);
                    if (allAddressesRequestResult != null && !allAddressesRequestResult.isEmpty()) {
                        for (Adaptation address; (address = allAddressesRequestResult.nextAdaptation()) != null; ) {
                            String bpCondition = Paths._BusinessPurpose._BusinessPurpose.format() + " = '" + aContext.getAdaptationOccurrence().getString(Paths._BusinessPurpose._BusinessPurpose) + "'"
                                    + " and (" + Paths._BusinessPurpose._MDMAddressId.format() + " = '" + address.get(Paths._Address._MDMAddressId).toString() + "')";
                            RequestResult bpRequestResult = bpTable.createRequestResult(bpCondition);
                            if (bpRequestResult != null && !bpRequestResult.isEmpty()) {
                                for (Adaptation bp; (bp = bpRequestResult.nextAdaptation()) != null; ) {
                                    List resultBpOu = bp.getList(Paths._BusinessPurpose._OperatingUnit);
                                    String resultPurposeId = String.valueOf(bp.get(Paths._BusinessPurpose._MDMPurposeId));
                                    if (!purposeId.equals(resultPurposeId) && !thisOus.isEmpty() && resultBpOu != null && !resultBpOu.isEmpty()) {
                                        for (Object ou : thisOus) {
                                            if (resultBpOu.contains(ou)) {
                                                bpExistsOus.add(String.valueOf(ou));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!oUsRemovedSet.isEmpty()) {
                    oUsRemovedSet.addAll(existingOusRemoved);
                    valueContextForUpdate.setValue(new ArrayList<>(oUsRemovedSet), Paths._BusinessPurpose._RemovedOperatingUnits);
                    update = true;
                }
                for (Object ou : thisOus) {
                    if (!bpExistsOus.contains(String.valueOf(ou))) {
                        primaryForOusSet.add(String.valueOf(ou));
                    }
                }
                if (!primaryForOusSet.isEmpty()) {
                    primaryForOus = new ArrayList<>(primaryForOusSet);
                    valueContextForUpdate.setValue(primaryForOus, Paths._BusinessPurpose._Primary);
                    update = true;
                }
            }
            if (update) {
                aContext.getProcedureContext().doModifyContent(aContext.getAdaptationOccurrence(), valueContextForUpdate);
            }
        }
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
