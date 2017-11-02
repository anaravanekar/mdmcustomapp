package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.*;
import com.sereneast.orchestramdm.keysight.mdmcustom.SpringContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.RestProperties;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ApplicationCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class CustomMasterDataViewService implements UserService<TableViewEntitySelection>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomMasterDataViewService.class);

    private ApplicationContext applicationContext;

    private String objectName;

    private ApplicationCacheUtil applicationCacheUtil;

    private ObjectKey objectKey;

    public CustomMasterDataViewService() {
    }

    @Override
    public void setupObjectContext(
            UserServiceSetupObjectContext<TableViewEntitySelection> aContext,
            UserServiceObjectContextBuilder aBuilder)
    {
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
                CustomMasterDataViewService.this.writeForm(aContext, aWriter);
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
        String urlForClose = aWriter.getURLForEndingService();
        LOGGER.debug("urlForClose="+urlForClose);
        RestProperties restProperties = (RestProperties) SpringContext.getApplicationContext().getBean("restProperties");
        String protocol = "true".equals(restProperties.getOrchestra().getSsl())?"https":"http";
        String host = restProperties.getOrchestra().getHost();
        String port = restProperties.getOrchestra().getPort();
        String customUiUrl = protocol+"://"+host+":"+port+"/mdmcustomapp/homePageNew";
        aWriter.addJS("function openCustomView(){var win = window.open('"+customUiUrl+"', \"Custom Master Data View\", \"height=screen.width,width=screen.height\"); win.moveTo(0,0); win.resizeTo(screen.width,screen.height);} openCustomView(); window.location.href='"+urlForClose+"';");
        /*aWriter.addJS("" +
                "function openCustomView(){\n" +
                "            var win = window.open("+customUiUrl+", \"Custom Master Data View\", \"height=\"+screen.width+\",width=\"+screen.height);\n" +
                "            win.moveTo(0,0);\n" +
                "            win.resizeTo(screen.width,screen.height);\n" +
                "        }" +
                "");
        aWriter.addJS("openCustomView();");
        aWriter.addJS("window.location.href='"+urlForClose+"';");*/
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
