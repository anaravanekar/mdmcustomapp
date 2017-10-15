package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.RequestResult;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.schema.trigger.*;
import com.orchestranetworks.service.OperationException;
import com.orchestranetworks.service.ProcedureContext;
import com.orchestranetworks.service.ValueContextForUpdate;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TableTriggerForCountry extends TableTrigger {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublishService.class);

    private Path daqaTargetFieldPath = Paths._Account._DaqaMetaData_TargetRecord;

    private Path objectPrimaryKeyPath = Paths._Account._MDMAccountId;

    private Path stateFieldPath = Paths._Account._DaqaMetaData_State;

    private Path mergeOriginPath = Paths._Account._DaqaMetaData_MergeOrigin;

    private Path timestampPath = Paths._Account._DaqaMetaData_Timestamp;

    private Path countryPath = Paths._Account._Country;

    private boolean initialized;

    @Override
    public void setup(TriggerSetupContext triggerSetupContext) {

    }

    public void initialize(){
    }

    public void handleAfterModify(AfterModifyOccurrenceContext aContext) throws OperationException {
        LOGGER.debug("TableTriggerForCountry handleAfterModify called...");
        initialize();
        ProcedureContext procedureContext = aContext.getProcedureContext();
        Adaptation adaptation = aContext.getAdaptationOccurrence();
        ValueChanges changes = aContext.getChanges();
        ValueChange daqaStateChanges = changes.getChange(stateFieldPath);
        ValueChange daqaTargetChanges = changes.getChange(daqaTargetFieldPath);
        ValueChange daqaMergeOriginChanges = changes.getChange(mergeOriginPath);
        ValueChange daqaTimestampChanges = changes.getChange(timestampPath);
        ValueChange countryChanges = changes.getChange(countryPath);
        if(daqaStateChanges!=null) {
            LOGGER.debug("State Before: " + String.valueOf(daqaStateChanges.getValueBefore()));
            LOGGER.debug("State After: " + String.valueOf(daqaStateChanges.getValueAfter()));
        }
        if(daqaTargetChanges!=null) {
            LOGGER.debug("target Before: " + String.valueOf(daqaTargetChanges.getValueBefore()));
            LOGGER.debug("target After: " + String.valueOf(daqaTargetChanges.getValueAfter()));
        }
        if(daqaMergeOriginChanges!=null) {
            LOGGER.debug("daqaMergeOriginChanges Before: " + String.valueOf(daqaMergeOriginChanges.getValueBefore()));
            LOGGER.debug("daqaMergeOriginChanges After: " + String.valueOf(daqaMergeOriginChanges.getValueAfter()));
        }
        if(daqaTimestampChanges!=null) {
            LOGGER.debug("daqaTimestampChanges Before: " + String.valueOf(daqaTimestampChanges.getValueBefore()));
            LOGGER.debug("daqaTimestampChanges After: " + String.valueOf(daqaTimestampChanges.getValueAfter()));
        }
        if (countryChanges != null) {
            LOGGER.debug("country Before: " + String.valueOf(countryChanges.getValueBefore()));
            LOGGER.debug("country After: " + String.valueOf(countryChanges.getValueAfter()));
        }
        LOGGER.debug("DaqaValues.........");
        LOGGER.debug("daqastate = "+String.valueOf(adaptation.get(stateFieldPath)));
        LOGGER.debug("daqatarget = "+String.valueOf(adaptation.get(daqaTargetFieldPath)));
        LOGGER.debug("daqamergeorigin = "+String.valueOf(adaptation.get(mergeOriginPath)));
        LOGGER.debug("daqatimestamp = "+String.valueOf(adaptation.get(timestampPath)));

       /* if("Golden".equalsIgnoreCase(String.valueOf(adaptation.get(stateFieldPath))) ){
            Set<String> goldenCountriesFinal = new HashSet<>();
            List<String> goldenCountriesCurrent = adaptation.getList(countryPath);
            Set<String> searchResultCountriesSet = new HashSet<>();
            List<Adaptation> searchResultList = searchByTargetValue(procedureContext,
                    Integer.valueOf(String.valueOf(adaptation.get(objectPrimaryKeyPath))),adaptation.getContainerTable());
            goldenCountriesFinal.addAll(goldenCountriesCurrent);
            if (searchResultList!=null && !searchResultList.isEmpty()) {
               for(Adaptation resultRecord: searchResultList) {
                   List<String> resultRecordCountries = resultRecord.getList(countryPath);
                   if(resultRecordCountries != null && !resultRecordCountries.isEmpty()) {
                       searchResultCountriesSet.addAll(resultRecordCountries);
                   }
               }
            }
            goldenCountriesFinal.addAll(searchResultCountriesSet);
            LOGGER.debug("Golden countries current:"+String.valueOf(goldenCountriesCurrent));
            LOGGER.debug("Search countries :"+String.valueOf(searchResultCountriesSet));
            LOGGER.debug("Golden countries final:"+String.valueOf(goldenCountriesFinal));
            ValueContextForUpdate valueContextForUpdate = procedureContext.getContext(adaptation.getAdaptationName());
            valueContextForUpdate.setValue(new ArrayList<String>(goldenCountriesFinal), countryPath);
            procedureContext.doModifyContent(adaptation, valueContextForUpdate);
        }else */
       if(daqaStateChanges!=null && "Merged".equalsIgnoreCase(String.valueOf(daqaStateChanges.getValueAfter()))){
            LOGGER.debug("DAQA STATE CHANGED TO MERGED!!");
            Set<String> goldenCountriesFinal = new HashSet<>();
            List<String> currentRecordCountries = adaptation.getList(countryPath);
            Set<String> searchResultCountriesSet = new HashSet<>();
            Adaptation resultRecord = searchByMdmId(procedureContext,
                    Integer.valueOf(String.valueOf(adaptation.get(daqaTargetFieldPath))),adaptation.getContainerTable());
            goldenCountriesFinal.addAll(currentRecordCountries);
            if (resultRecord!=null) {
                List<String> resultRecordCountries = resultRecord.getList(countryPath);
                if(resultRecordCountries != null && !resultRecordCountries.isEmpty()) {
                    searchResultCountriesSet.addAll(resultRecordCountries);
                }
            }
            goldenCountriesFinal.addAll(searchResultCountriesSet);
            LOGGER.debug("Current record countries:"+String.valueOf(currentRecordCountries));
            LOGGER.debug("Search countries :"+String.valueOf(searchResultCountriesSet));
            LOGGER.debug("Golden countries final:"+String.valueOf(goldenCountriesFinal));
            ValueContextForUpdate valueContextForUpdate = procedureContext.getContext(resultRecord.getAdaptationName());
            valueContextForUpdate.setValue(new ArrayList<String>(goldenCountriesFinal), countryPath);
            procedureContext.doModifyContent(resultRecord, valueContextForUpdate);
        }
    }

    private List<Adaptation> searchByTargetValue(ProcedureContext procedureContext,Integer mdmId,AdaptationTable adaptationTable) {
        Set<String> countries = new HashSet<>();
        int retryCount = 0;
        List<Adaptation> resultList = new ArrayList<>();
        LOGGER.debug("Searching by targetvalue.......");
        final String conditionString = daqaTargetFieldPath.format()+" = '"+String.valueOf(mdmId)+"'";
        LOGGER.debug("condition ="+conditionString);
        List<OrchestraObject> searchResultList = new ArrayList<>();
        try {
            do {
                final RequestResult tableRequestResult = adaptationTable.createRequestResult(conditionString);
                if (tableRequestResult != null && !tableRequestResult.isEmpty()) {
                    for (Adaptation tableRequestResultRecord; (tableRequestResultRecord = tableRequestResult.nextAdaptation()) != null; ) {
                        LOGGER.debug("Record found: " + String.valueOf(tableRequestResultRecord.get(objectPrimaryKeyPath)));
                        resultList.add(tableRequestResultRecord);
                    }
                } else {
                    retryCount++;
                    Thread.sleep(2000);
                    LOGGER.debug("Retry Attempt = "+retryCount);
                }
            } while ((resultList == null || resultList.isEmpty()) && retryCount < 3);
        }catch(InterruptedException e) {
            LOGGER.error("Error searching..",e);
        }
        return resultList;
    }

    private Adaptation searchByMdmId(ProcedureContext procedureContext,Integer mdmId,AdaptationTable adaptationTable) {
        Set<String> countries = new HashSet<>();
        int retryCount = 0;
        Adaptation resultRecord = null;
        List<Adaptation> resultList = new ArrayList<>();
        LOGGER.debug("Searching by MdmId.......");
        final String conditionString = objectPrimaryKeyPath.format()+" = "+mdmId;
        LOGGER.debug("condition ="+conditionString);
        List<OrchestraObject> searchResultList = new ArrayList<>();
        try {
            do {
                final RequestResult tableRequestResult = adaptationTable.createRequestResult(conditionString);
                if (tableRequestResult != null && !tableRequestResult.isEmpty()) {
                    for (Adaptation tableRequestResultRecord; (tableRequestResultRecord = tableRequestResult.nextAdaptation()) != null; ) {
                        LOGGER.debug("Record found: " + String.valueOf(tableRequestResultRecord.get(objectPrimaryKeyPath)));
                        resultList.add(tableRequestResultRecord);
                    }
                } else {
                    retryCount++;
                    Thread.sleep(2000);
                    LOGGER.debug("Retry Attempt = "+retryCount);
                }
            } while ((resultList == null || resultList.isEmpty()) && retryCount < 3);
        }catch(InterruptedException e) {
            LOGGER.error("Error searching..",e);
        }
        if(resultList!=null && !resultList.isEmpty()){
            resultRecord = resultList.get(0);
        }
        return resultRecord;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setDaqaTargetFieldPath(Path daqaTargetFieldPath) {
        this.daqaTargetFieldPath = daqaTargetFieldPath;
    }

    public void setObjectPrimaryKeyPath(Path objectPrimaryKeyPath) {
        this.objectPrimaryKeyPath = objectPrimaryKeyPath;
    }

    public void setStateFieldPath(Path stateFieldPath) {
        this.stateFieldPath = stateFieldPath;
    }

    public void setMergeOriginPath(Path mergeOriginPath) {
        this.mergeOriginPath = mergeOriginPath;
    }

    public void setTimestampPath(Path timestampPath) {
        this.timestampPath = timestampPath;
    }

    public void setCountryPath(Path countryPath) {
        this.countryPath = countryPath;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}
