package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.orchestranetworks.ui.UICSSClasses;
import com.orchestranetworks.ui.selection.DatasetEntitySelection;
import com.orchestranetworks.userservice.*;
import com.sereneast.orchestramdm.keysight.mdmcustom.SpringContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ApplicationCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RefreshCacheService implements UserService<DatasetEntitySelection> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshCacheService.class);

    @Override
    public void setupObjectContext(UserServiceSetupObjectContext<DatasetEntitySelection> userServiceSetupObjectContext, UserServiceObjectContextBuilder userServiceObjectContextBuilder) {
        // Not used.
    }

    @Override
    public void setupDisplay(UserServiceSetupDisplayContext<DatasetEntitySelection> userServiceSetupDisplayContext, UserServiceDisplayConfigurator userServiceDisplayConfigurator) {
        LOGGER.debug("In Setup Display");
        // Sets content.
        userServiceDisplayConfigurator.setContent(new UserServicePane()
        {
            @Override
            public void writePane(UserServicePaneContext aContext, UserServicePaneWriter aWriter)
            {
                RefreshCacheService.this.writeForm(aContext, aWriter);
            }
        });
        userServiceDisplayConfigurator.setLeftButtons(userServiceDisplayConfigurator.newCloseButton());
    }

    @Override
    public void validate(UserServiceValidateContext<DatasetEntitySelection> userServiceValidateContext) {
        // Not used.
    }

    @Override
    public UserServiceEventOutcome processEventOutcome(UserServiceProcessEventOutcomeContext<DatasetEntitySelection> userServiceProcessEventOutcomeContext, UserServiceEventOutcome userServiceEventOutcome) {
        return userServiceEventOutcome;
    }

    private void writeForm(UserServicePaneContext aContext, UserServicePaneWriter aWriter){
        LOGGER.debug("In writeform");
        String urlForClose = aWriter.getURLForEndingService();
        LOGGER.debug("urlForClose="+urlForClose);

        String divId = "refreshCacheDiv";
        String loadingDivId = "divLoading";
        aWriter.add("<div ");
        aWriter.addSafeAttribute("id", divId);
        aWriter.add("></div>");
        aWriter.add("<div ");
        aWriter.addSafeAttribute("id", loadingDivId);
        aWriter.add("></div>");

        aWriter.addJS_cr();
        aWriter.addJS_cr("function callAjax(url, targetDivId) {");
        aWriter.addJS_cr("  var ajaxHandler = new EBX_AJAXResponseHandler();");

        aWriter.addJS_cr("  ajaxHandler.handleAjaxResponseSuccess = function(responseContent) {");
        aWriter.addJS_cr("    var element = document.getElementById(targetDivId);");
        aWriter.addJS_cr("    element.innerHTML = responseContent;");
        aWriter.addJS_cr("    document.getElementById(\"divLoading\").classList.remove(\"show\");");
        aWriter.addJS_cr("  };");

        aWriter.addJS_cr("  ajaxHandler.handleAjaxResponseFailed = function(responseContent) {");
        aWriter.addJS_cr("    var element = document.getElementById(targetDivId);");
        aWriter.addJS_cr("    element.innerHTML = \"<span class='" + UICSSClasses.TEXT.ERROR
                + "'>An error occurred in processing the request.</span>\";");
        aWriter.addJS_cr("    document.getElementById(\"divLoading\").classList.remove(\"show\");");
        aWriter.addJS_cr("  }");

        aWriter.addJS_cr("  ajaxHandler.sendRequest(url);");
        aWriter.addJS_cr("document.getElementById(\"divLoading\").classList.add(\"show\");");
        aWriter.addJS_cr("}");

        // Generate the URL of the Ajax callback.
        String url = aWriter.getURLForAjaxRequest((userServiceAjaxContext, userServiceAjaxResponse) -> {
            RefreshCacheService.this.ajaxCallback(userServiceAjaxContext, userServiceAjaxResponse,aContext);
        });
        aWriter.addJS("ebx_confirm({question: \"Do you want to refresh Cache?\", jsCommandYes: \"callAjax('"+url+"','"+divId+"');\", labelYes: \"Yes\", labelNo: \"No\", jsCommandNo: \"window.location.href='"+urlForClose+"';\" });");

    }

    private void ajaxCallback(UserServiceAjaxContext ajaxContext,UserServiceAjaxResponse anAjaxResponse,UserServicePaneContext aContext) {
        LOGGER.debug("Refresh Cache Ajax Callback");
        String finalMessage = "";
        try {
            ApplicationCacheUtil applicationCacheUtil = (ApplicationCacheUtil) SpringContext.getApplicationContext().getBean("applicationCacheUtil");
            applicationCacheUtil.evictAllCacheEntries();
            finalMessage = "Cache refreshed successfully";
        }catch (RuntimeException e){
            finalMessage = "Error refreshing cache "+e.getMessage();
        }
        anAjaxResponse.getWriter().add(finalMessage);
    }
}
