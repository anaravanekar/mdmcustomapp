package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.Adaptation;
import com.orchestranetworks.addon.daqa.MatchingOperations;
import com.orchestranetworks.addon.daqa.MatchingOperationsFactory;
import com.orchestranetworks.addon.daqa.RecordContext;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.service.*;
import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.*;
import com.sereneast.orchestramdm.keysight.mdmcustom.exception.ApplicationRuntimeException;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraContent;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObject;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ApplicationCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.*;

public class RealignForeignKeysService implements UserService<TableViewEntitySelection>{
    private static final Logger LOGGER = LoggerFactory.getLogger(RealignForeignKeysService.class);

    private ApplicationContext applicationContext;

    private String objectName;

    private Path objectPrimaryKeyPath;

    private Class objectPrimaryKeyType;

    private ApplicationCacheUtil applicationCacheUtil;

    private List<ObjectKey> objectKeys = new ArrayList<>();

    public RealignForeignKeysService() {
    }

    @Override
    public void setupObjectContext(
            UserServiceSetupObjectContext<TableViewEntitySelection> aContext,
            UserServiceObjectContextBuilder aBuilder) {
    }

    /**
     * Sets up the display.
     */
    @Override
    public void setupDisplay(
            UserServiceSetupDisplayContext<TableViewEntitySelection> aContext,
            UserServiceDisplayConfigurator aConfigurator) {
        LOGGER.debug("In Setup Display");
        // Sets content.
        aConfigurator.setContent(new UserServicePane()
        {
            @Override
            public void writePane(UserServicePaneContext aContext, UserServicePaneWriter aWriter)
            {
                RealignForeignKeysService.this.writeForm(aContext, aWriter);
            }
        });
        aConfigurator.setLeftButtons(aConfigurator.newCloseButton());
    }

    @Override
    public void validate(UserServiceValidateContext<TableViewEntitySelection> aContext) {
        // Not used.
    }

    @Override
    public UserServiceEventOutcome processEventOutcome(
            UserServiceProcessEventOutcomeContext<TableViewEntitySelection> aContext,
            UserServiceEventOutcome anEventOutcome) {
        return anEventOutcome;
    }

    private void writeForm(UserServicePaneContext aContext, UserServicePaneWriter aWriter){
        LOGGER.debug("In writeform");
        String finalMessage = null;
        try {
            for (ObjectKey objectKey : objectKeys) {
                LOGGER.debug("Getting adaptation for objectKey=" + objectKey.getName());
                Adaptation record = (Adaptation) aContext.getValueContext(objectKey).getValue();
                Procedure procedure = procedureContext -> {
                    RecordContext recordContext = new RecordContext(record,procedureContext);
                    MatchingOperations operations = MatchingOperationsFactory.getMatchingOperations();
                    operations.alignForeignKeys(recordContext);
                };
                ProgrammaticService svc = ProgrammaticService.createForSession(aContext.getSession(), record.getHome());
                ProcedureResult result = null;
                result = svc.execute(procedure);
                if (result == null || result.hasFailed()){
                    LOGGER.info("Align foreign keys operation failed " + result.getExceptionFullMessage(Locale.ENGLISH));
                    finalMessage = "Align foreign keys operation failed";
                    throw new ApplicationRuntimeException("Error aligning foreign keys");
                }else{
                    LOGGER.info("Align foreign keys successful");
                    finalMessage = "Foreign keys aligned successfully";
                }
            }
        }catch(ApplicationRuntimeException e){
            finalMessage = e.getMessage();
        }
        aWriter.add(finalMessage);
    }

    private OrchestraObject getCopy(OrchestraObject orchestraObject) {
        OrchestraObject resultObject = new OrchestraObject();
        Map<String,OrchestraContent> contents = new HashMap<>();
        for(String key:orchestraObject.getContent().keySet()){
            contents.put(key,orchestraObject.getContent().get(key));
        }
        resultObject.setContent(contents);
        return resultObject;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Path getObjectPrimaryKeyPath() {
        return objectPrimaryKeyPath;
    }

    public void setObjectPrimaryKeyPath(Path objectPrimaryKeyPath) {
        this.objectPrimaryKeyPath = objectPrimaryKeyPath;
    }

    public Class getObjectPrimaryKeyType() {
        return objectPrimaryKeyType;
    }

    public void setObjectPrimaryKeyType(Class objectPrimaryKeyType) {
        this.objectPrimaryKeyType = objectPrimaryKeyType;
    }

    public ApplicationCacheUtil getApplicationCacheUtil() {
        return applicationCacheUtil;
    }

    public void setApplicationCacheUtil(ApplicationCacheUtil applicationCacheUtil) {
        this.applicationCacheUtil = applicationCacheUtil;
    }

    public List<ObjectKey> getObjectKeys() {
        return objectKeys;
    }

    public void setObjectKeys(List<ObjectKey> objectKeys) {
        this.objectKeys = objectKeys;
    }

}
