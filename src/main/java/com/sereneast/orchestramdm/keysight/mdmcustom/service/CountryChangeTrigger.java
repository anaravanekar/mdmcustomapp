package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.RequestResult;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.schema.trigger.*;
import com.orchestranetworks.service.OperationException;
import com.orchestranetworks.service.ValueContextForUpdate;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.exception.ApplicationRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountryChangeTrigger extends TableTrigger {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryChangeTrigger.class);
    private Path countryFieldPath;
    private Path operatingUnitFieldPath;
    private Path regimeCodeFieldPath;
    private Path stateFieldPath;
    private Path regionFieldPath;
    private Path provinceFieldPath;
    private Path profileClassFieldPath;
    private boolean initialized;

    private void updateRelatedFields(ValueContextForUpdate valueContextForUpdate, Adaptation container,String countryCode) {
        LOGGER.debug("updating related fields");
        if(!initialized){
            throw new ApplicationRuntimeException("Error in CountryChangeTrigger. Object not initialized.");
        }
        LOGGER.debug("country code:"+countryCode);
        LOGGER.debug("container:"+container);
        String operatingUnit = null;
        String regimeCode = null;
        String region = null;
        String profileClass = null;
        String state = null;
        String province = null;
        if(StringUtils.isNotBlank(countryCode)) {
            AdaptationTable operatingUnitTable = container.getTable(Paths._OperatingUnit.getPathInSchema());
            final RequestResult operatingUnitTableRequestResult = operatingUnitTable.createRequestResult(Paths._OperatingUnit._CountryCode.format()+ " = '"+countryCode+"'");
            if (operatingUnitTableRequestResult != null && !operatingUnitTableRequestResult.isEmpty()) {
                Adaptation record = operatingUnitTableRequestResult.nextAdaptation();
                operatingUnit = record.getString(Paths._OperatingUnit._OperatingUnit);
            }
            AdaptationTable regimeCodeTable = container.getTable(Paths._RegimeCode.getPathInSchema());
            final RequestResult regimeCodeTableRequestResult = regimeCodeTable.createRequestResult(Paths._RegimeCode._CountryCode.format()+ " = '"+countryCode+"'");
            if (regimeCodeTableRequestResult != null && !regimeCodeTableRequestResult.isEmpty()) {
                regimeCode = regimeCodeTableRequestResult.nextAdaptation().getString(Paths._RegimeCode._TaxRegimeCode);
            }
            AdaptationTable stateTable = container.getTable(Paths._State.getPathInSchema());
            final RequestResult stateTableRequestResult = stateTable.createRequestResult(Paths._State._CountryCode.format()+ " = '"+countryCode+"'");
            if (stateTableRequestResult != null && !stateTableRequestResult.isEmpty()) {
                state = stateTableRequestResult.nextAdaptation().getString(Paths._State._State);
            }
            AdaptationTable provinceTable = container.getTable(Paths._Province.getPathInSchema());
            final RequestResult provinceTableRequestResult = provinceTable.createRequestResult(Paths._Province._CountryCode.format()+ " = '"+countryCode+"'");
            if (provinceTableRequestResult != null && !provinceTableRequestResult.isEmpty()) {
                province = provinceTableRequestResult.nextAdaptation().getString(Paths._Province._Province);
            }
        }
        valueContextForUpdate.setValue(operatingUnit, operatingUnitFieldPath);
        valueContextForUpdate.setValue(regimeCode, regimeCodeFieldPath);
        valueContextForUpdate.setValue(state,stateFieldPath);
        valueContextForUpdate.setValue(province,provinceFieldPath);
//        valueContextForUpdate.setValue(profileClass,profileClassFieldPath);
       /* try {
            procedureContext.doModifyContent(adaptationOccurrence, valueContextForUpdate);
        }catch(Exception e){
            throw new ApplicationOperationException("Error updating related fields after Country field change",e);
        }*/
    }

    @Override
    public void handleBeforeCreate(BeforeCreateOccurrenceContext bContext) throws OperationException {
        LOGGER.debug("before create");
        ValueContextForUpdate valueContextForUpdate = bContext.getOccurrenceContextForUpdate();
        Adaptation container = bContext.getOccurrenceContext().getAdaptationTable().getContainerAdaptation();
        String country=bContext.getOccurrenceContext().getValue(countryFieldPath)!=null?bContext.getOccurrenceContext().getValue(countryFieldPath).toString():null;
        if(StringUtils.isNotBlank(country)) {
            LOGGER.debug("Country not null");
            updateRelatedFields(valueContextForUpdate, container, country);
        }
    }

    @Override
    public void handleNewContext(NewTransientOccurrenceContext bContext) {
        LOGGER.debug("handleNewContext...");
        ValueContextForUpdate valueContextForUpdate = bContext.getOccurrenceContextForUpdate();
        Adaptation container = bContext.getOccurrenceContext().getAdaptationTable().getContainerAdaptation();
        String country=bContext.getOccurrenceContext().getValue(countryFieldPath)!=null?bContext.getOccurrenceContext().getValue(countryFieldPath).toString():null;
        if(StringUtils.isNotBlank(country)) {
            LOGGER.debug("Country not null");
            updateRelatedFields(valueContextForUpdate, container, country);
        }
    }

    @Override
    public void handleBeforeModify(BeforeModifyOccurrenceContext bContext) throws OperationException {
        LOGGER.debug("before modify........");
        ValueContextForUpdate valueContextForUpdate = bContext.getOccurrenceContextForUpdate();
        Adaptation container = bContext.getTable().getContainerAdaptation();
        ValueChanges changes = bContext.getChanges();
        if(changes.getChange(countryFieldPath)!=null) {
            LOGGER.debug("Country field changed");
            String country = changes.getChange(countryFieldPath).getValueAfter()!=null?changes.getChange(countryFieldPath).getValueAfter().toString():null;
            updateRelatedFields(valueContextForUpdate, container,country);
        }
    }

 /*   @Override
    public void handleAfterCreate(AfterCreateOccurrenceContext aContext) throws OperationException {
        LOGGER.debug("before create....");
        Adaptation adaptationOccurrence = aContext.getAdaptationOccurrence();
        ProcedureContext procedureContext = aContext.getProcedureContext();
        updateRelatedFields(procedureContext,adaptationOccurrence);
    }

    @Override
    public void handleAfterModify(AfterModifyOccurrenceContext aContext) throws OperationException {
        LOGGER.debug("before modify.....");
        Adaptation adaptationOccurrence = aContext.getAdaptationOccurrence();
        ProcedureContext procedureContext = aContext.getProcedureContext();
        ValueChanges changes = aContext.getChanges();
        if(changes.getChange(countryFieldPath)!=null) {
            LOGGER.debug("Country value changed");
            updateRelatedFields(procedureContext, adaptationOccurrence);
        }
    }*/

    @Override
    public void setup(TriggerSetupContext triggerSetupContext) {

    }

    public Path getCountryFieldPath() {
        return countryFieldPath;
    }

    public void setCountryFieldPath(Path countryFieldPath) {
        this.countryFieldPath = countryFieldPath;
    }

    public Path getOperatingUnitFieldPath() {
        return operatingUnitFieldPath;
    }

    public void setOperatingUnitFieldPath(Path operatingUnitFieldPath) {
        this.operatingUnitFieldPath = operatingUnitFieldPath;
    }

    public Path getRegimeCodeFieldPath() {
        return regimeCodeFieldPath;
    }

    public void setRegimeCodeFieldPath(Path regimeCodeFieldPath) {
        this.regimeCodeFieldPath = regimeCodeFieldPath;
    }

    public Path getStateFieldPath() {
        return stateFieldPath;
    }

    public void setStateFieldPath(Path stateFieldPath) {
        this.stateFieldPath = stateFieldPath;
    }

    public Path getRegionFieldPath() {
        return regionFieldPath;
    }

    public void setRegionFieldPath(Path regionFieldPath) {
        this.regionFieldPath = regionFieldPath;
    }

    public Path getProvinceFieldPath() {
        return provinceFieldPath;
    }

    public void setProvinceFieldPath(Path provinceFieldPath) {
        this.provinceFieldPath = provinceFieldPath;
    }

    public Path getProfileClassFieldPath() {
        return profileClassFieldPath;
    }

    public void setProfileClassFieldPath(Path profileClassFieldPath) {
        this.profileClassFieldPath = profileClassFieldPath;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}
