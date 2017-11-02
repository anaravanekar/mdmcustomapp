package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.RequestResult;
import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.*;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.SpringContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.RestProperties;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ApplicationCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.Locale;

public class ClusterViewService implements UserService<TableViewEntitySelection>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterViewService.class);

    private ApplicationContext applicationContext;

    private String objectName;

    private ApplicationCacheUtil applicationCacheUtil;

    private ObjectKey objectKey;

    public ClusterViewService() {
    }

    @Override
    public void setupObjectContext(
            UserServiceSetupObjectContext<TableViewEntitySelection> aContext,
            UserServiceObjectContextBuilder aBuilder)
    {
        LOGGER.debug("In Setup Object Context");
        LOGGER.debug("Dataspace name/label="+aContext.getEntitySelection().getDataspace().getLabelOrName(Locale.ENGLISH));
        LOGGER.debug("Dataspace label="+aContext.getEntitySelection().getDataspace().getLabel());
        RequestResult result = aContext.getEntitySelection().getSelectedRecords().execute();
        Adaptation selectedRecord = result.nextAdaptation();
        objectKey = ObjectKey.forName("SELECTED_ACCOUNT");
        aBuilder.registerRecordOrDataSet(objectKey,selectedRecord);
    }

    /**
     * Sets up the display.
     */
    @Override
    public void setupDisplay(
            UserServiceSetupDisplayContext<TableViewEntitySelection> aContext,
            UserServiceDisplayConfigurator aConfigurator)

    {
        LOGGER.debug("In Setup Display");
        // Sets content.
        aConfigurator.setContent(new UserServicePane()
        {
            @Override
            public void writePane(UserServicePaneContext aContext, UserServicePaneWriter aWriter)
            {
                ClusterViewService.this.writeForm(aContext, aWriter);
            }
        });
        aConfigurator.setLeftButtons(aConfigurator.newCloseButton());
    }

    @Override
    public void validate(UserServiceValidateContext<TableViewEntitySelection> aContext)
    {
        // Not used.

    }

    @Override
    public UserServiceEventOutcome processEventOutcome(
            UserServiceProcessEventOutcomeContext<TableViewEntitySelection> aContext,
            UserServiceEventOutcome anEventOutcome)
    {
        return anEventOutcome;
    }

    private void writeForm(UserServicePaneContext aContext, UserServicePaneWriter aWriter){
        LOGGER.debug("In writeform");
        Adaptation adaptation = (Adaptation) aContext.getValueContext(objectKey).getValue();
        String clusterId = adaptation.get(Paths._Account._DaqaMetaData_ClusterId)!=null?String.valueOf(adaptation.get(Paths._Account._DaqaMetaData_ClusterId)):"";
        String divId = "clusterViewIframe";
        RestProperties restProperties = (RestProperties) SpringContext.getApplicationContext().getBean("restProperties");
        String protocol = "true".equals(restProperties.getOrchestra().getSsl())?"https":"http";
        String host = restProperties.getOrchestra().getHost();
        String port = restProperties.getOrchestra().getPort();
        String src = protocol+"://"+host+":"+port+"/mdmcustomapp/clusterView?clusterId="+clusterId;
        aWriter.add("<iframe ");
        aWriter.addSafeAttribute("id", divId);
        aWriter.addSafeAttribute("src", src);
        aWriter.addSafeAttribute("width", "100%");
        aWriter.addSafeAttribute("height", "100%");
        aWriter.add("></iframe>");
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
    public ApplicationCacheUtil getApplicationCacheUtil() {
        return applicationCacheUtil;
    }

    public void setApplicationCacheUtil(ApplicationCacheUtil applicationCacheUtil) {
        this.applicationCacheUtil = applicationCacheUtil;
    }

    public ObjectKey getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(ObjectKey objectKey) {
        this.objectKey = objectKey;
    }
}
