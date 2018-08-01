package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.RequestResult;
import com.orchestranetworks.addon.daqa.MatchingEventListener;
import com.orchestranetworks.addon.daqa.MatchingState;
import com.orchestranetworks.addon.daqa.RecordContext;
import com.orchestranetworks.service.OperationException;
import com.orchestranetworks.service.ProcedureContext;
import com.orchestranetworks.service.ValueContextForUpdate;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MatchEventListener implements MatchingEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublishService.class);

    @Override
    public void onStateChange(MatchingState matchingState, RecordContext recordContext) {
        LOGGER.debug("On state change");
        LOGGER.debug("matchingState before ="+matchingState.getValue());
        ProcedureContext procedureContext = recordContext.getProcedureContext();
        Adaptation record = recordContext.getRecord();
        LOGGER.debug("matchingState now ="+recordContext.getState().getValue());
        if(!"GOLDEN".equalsIgnoreCase(recordContext.getState().getValue())){
            Integer targetMdmId = Integer.valueOf(String.valueOf(record.get(Paths._Account._DaqaMetaData_TargetRecord)));
            LOGGER.debug("target mdmid : "+targetMdmId);
            final String condition = Paths._Account._MDMAccountId.format() + " = " + targetMdmId;
            LOGGER.debug("condition=" + condition);
            Adaptation container = record.getContainer();
            AdaptationTable accountTable = container.getTable(Paths._Account.getPathInSchema());
            final RequestResult collectedAccounts = accountTable.createRequestResult(condition);
            if (collectedAccounts != null && !collectedAccounts.isEmpty()) {
                Adaptation goldenRecord = collectedAccounts.nextAdaptation();
                Set<String> finalCountries = new HashSet<>();
                List<String> countries = record.getList(Paths._Account._Country);
                finalCountries.addAll(countries);
                finalCountries.addAll(goldenRecord.getList(Paths._Account._Country));
                LOGGER.debug("account found: " + goldenRecord.getString(Paths._Account._AccountName));
                LOGGER.debug("goldencountries: " + goldenRecord.getString(Paths._Account._AccountName));
                LOGGER.debug("this countries: " + String.valueOf(countries));
                LOGGER.debug("final countries: " + String.valueOf(finalCountries));

                List goldenCountries = goldenRecord.getList(Paths._Account._Country);
                ValueContextForUpdate valueContextForUpdate = procedureContext.getContext(goldenRecord.getAdaptationName());
                valueContextForUpdate.setValue(new ArrayList<>(finalCountries), Paths._Account._Country);
                try {
                    procedureContext.doModifyContent(goldenRecord, valueContextForUpdate);
                } catch (OperationException e) {
                    LOGGER.error("Error event", e);
                }
            }
        }
    }

    @Override
    public void onCreateNewGolden(RecordContext recordContext) {
        LOGGER.debug("On create new golden");
    }

    @Override
    public void onCreateNewPivot(RecordContext recordContext) {
        LOGGER.debug("On create new pivot");
    }

    @Override
    public void onUnmerge(RecordContext recordContext) {

    }
}
