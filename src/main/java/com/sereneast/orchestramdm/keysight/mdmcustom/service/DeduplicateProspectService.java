package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onwbp.adaptation.*;
import com.onwbp.base.text.UserMessageString;
import com.orchestranetworks.addon.daqa.TableContext;
import com.orchestranetworks.addon.daqa.crosswalk.*;
import com.orchestranetworks.dataservices.rest.RESTEncodingHelper;
import com.orchestranetworks.instance.HomeCreationSpec;
import com.orchestranetworks.instance.HomeKey;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.service.*;
import com.orchestranetworks.ui.UICSSClasses;
import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.*;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.SpringContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.exception.ApplicationRuntimeException;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraContent;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObject;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObjectList;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.RestResponse;
import com.sereneast.orchestramdm.keysight.mdmcustom.rest.client.OrchestraRestClient;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ApplicationCacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

public class DeduplicateProspectService implements UserService<TableViewEntitySelection>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DeduplicateProspectService.class);

    private ApplicationContext applicationContext;

    private String objectName;

    private ApplicationCacheUtil applicationCacheUtil;

    private ObjectKey objectKey;


    private static final String clientId = "3MVG9Yb5IgqnkB4rvpE8ANt2MeTDwtPA5fbyHRPR1daVOo9fHEjRLChgZ7Kv1fR69MUN3KcYe2Nsbj2xIEwvY";
    private static final String clientSecret = "1447597324437005395";
    private static final String redirecturi = "1447597324437005395";
    private static String tokenUrl = null;
    private static final String environmentUrl = "https://test.salesforce.com";
    private static final String userName = "mstr_user@keysight.com.prd.test1";
    private static final String passWord = "Keysight123LT5xWyT8y3dqjp9FBKBFSzdON";//"Keysight123gOJm2KPTPV0c7hFjAVdaE5ccW";
    private static final String accessToken = null;
    private static final String instanceUrl= null;
    private static String R_EndPoint = "/services/data" ;
    private static String Version = "/v32.0" ;
    private static String baseUri;
    private static Header authHeader;
    private static Header ppHeader = new BasicHeader("X-PrettyPrint", "1");
    private static  String accountid;
    private static  String accountname;
    private static  String accountnumber;
    private static  String accountsite;

    public DeduplicateProspectService() {
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
                DeduplicateProspectService.this.writeForm(aContext, aWriter);
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

    private void writeForm(UserServicePaneContext aContext, UserServicePaneWriter aWriter) {
        LOGGER.info("In writeform");
        String urlForClose = aWriter.getURLForEndingService();
        LOGGER.info("urlForClose=" + urlForClose);


        String divId = "publishMessageDiv";
        String loadingDivId = "divLoading";
        aWriter.add("<div ");
        aWriter.addSafeAttribute("id", divId);
        aWriter.addSafeAttribute("style", "height:100%;");
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
            DeduplicateProspectService.this.ajaxCallback(userServiceAjaxContext, userServiceAjaxResponse,aContext);
        });

        aWriter.addJS("callAjax('"+url+"','"+divId+"');");
    }

    private void ajaxCallback(UserServiceAjaxContext ajaxContext,UserServiceAjaxResponse anAjaxResponse,UserServicePaneContext aContext) {
        LOGGER.debug("In ajaxCallback");
        String finalMessage = "";
        try {
            deduplicateSfdcProspects(aContext);
            anAjaxResponse.getWriter().add("<div class=\"custom-success-image\"></div>");
            anAjaxResponse.getWriter().add("<div class=\"custom-success-header\">Success!</div>");
            anAjaxResponse.getWriter().add("<div class=\"custom-success-message\">" + finalMessage);
            anAjaxResponse.getWriter().add("</div>");
            String urlSfdcDs = anAjaxResponse.getWriter().getURLForSelection(Repository.getDefault().lookupHome(HomeKey.forBranchName("CMDReference")).findAdaptationOrNull(AdaptationName.forName("Prospect")));
            LOGGER.info("Url for Prospect datset : "+urlSfdcDs);
            //anAjaxResponse.getWriter().addJS("window.location.href='"+urlSfdcDs+"';");
        }catch(ApplicationRuntimeException e){
            finalMessage = e.getMessage();
            anAjaxResponse.getWriter().add("<div class=\"custom-error-image\"></div>");
            anAjaxResponse.getWriter().add("<div class=\"custom-error-header\">Oh No! Something went wrong.</div>");
            anAjaxResponse.getWriter().add("<div class=\"custom-error-message\">" + finalMessage);
            anAjaxResponse.getWriter().add("<br> Stack trace : <br>" + ExceptionUtils.getStackTrace(e));
            anAjaxResponse.getWriter().add("</div>");
        }
    }

    private void deduplicateSfdcProspects(UserServicePaneContext aContext) {
        ApplicationCacheUtil applicationCacheUtil = (ApplicationCacheUtil) SpringContext.getApplicationContext().getBean("applicationCacheUtil");
        Map<String,Map<String,String>> lookup = applicationCacheUtil.getLookupValues("BReference");
        Map<String,String> sfdcToMdmMapping = lookup.get("MAPPING_SFDC_TO_MDM");
        int sfdcCountAccount = 0;
        int sfdcCountAddress = 0;
        if(sfdcToMdmMapping!=null && !sfdcToMdmMapping.isEmpty()) {
            try {
/*            try {
                if(aContext.getRepository().lookupHome(HomeKey.forBranchName("SFDCProspect"))!=null) {
                    aContext.getRepository().closeHome(aContext.getRepository().lookupHome(HomeKey.forBranchName("SFDCProspect")), aContext.getSession());
                    aContext.getRepository().getPurgeDelegate().markHomeForHistoryPurge(aContext.getRepository().lookupHome(HomeKey.forBranchName("SFDCProspect")), aContext.getSession());
                    aContext.getRepository().deleteHome(aContext.getRepository().lookupHome(HomeKey.forBranchName("SFDCProspect")), aContext.getSession());
                }
                HomeCreationSpec homeSpec = new HomeCreationSpec();
                homeSpec.setParent(aContext.getRepository().lookupHome(HomeKey.forBranchName("CMDReference")));
                homeSpec.setKey(HomeKey.forBranchName("SFDCProspect"));
                homeSpec.setOwner(Profile.forUser("admin"));
                UserMessageString label = new UserMessageString();
                label.setString(Locale.ENGLISH,"SFDC Prospect");
                homeSpec.setLabel(label);
                UserMessageString description = new UserMessageString();
                description.setString(Locale.ENGLISH,"Dataspace for SFDC Prospects");
                homeSpec.setDescription(description);
                try {
                    aContext.getRepository().createHome(homeSpec,aContext.getSession());
                } catch (OperationException e) {
                    LOGGER.error("Error creating dataspace",e);
                }
            } catch (OperationException e) {
                LOGGER.error("Error deleting dataspace",e);
            }*/

                String time = String.valueOf((new Date()).getTime());
                java.nio.file.Path path = java.nio.file.Paths.get(System.getProperty("ebx.home"), "Account" + time + ".csv");
                if (!Files.exists(path)) {
                    try {
                        Files.createFile(path);
                    } catch (IOException e) {
                        LOGGER.error("Error creating temporary file", e);
                        throw new ApplicationRuntimeException("Error in Deduplicate Prospect Service", e);
                    }
                }
                tokenUrl = environmentUrl + "/services/oauth2/token?grant_type=password";
                String loginUrl = tokenUrl + "&client_id=" + clientId + "&client_secret=" + clientSecret + "&username=" + userName + "&password=" + passWord;
                HttpPost httpPost = new HttpPost(loginUrl);
                DefaultHttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = null;
                try {
                    response = httpclient.execute(httpPost);
                } catch (IOException ioexception) {
                    throw new ApplicationRuntimeException("Error in Deduplicate Prospect Service", ioexception);
                }
                String getResult = null;
                try {
                    getResult = EntityUtils.toString(response.getEntity());
                } catch (IOException e) {
                    throw new ApplicationRuntimeException("Error in Deduplicate Prospect Service", e);
                }

                JSONObject jsonObject = null;
                String accessToken = null;
                String instanceUrl = null;

                try {
                    jsonObject = (JSONObject) new JSONTokener(getResult).nextValue();
                    accessToken = jsonObject.getString("access_token");
                    instanceUrl = jsonObject.getString("instance_url");
                } catch (JSONException jsonexp) {
                    throw new ApplicationRuntimeException("Error in Deduplicate Prospect Service", jsonexp);
                }

                baseUri = instanceUrl + R_EndPoint + Version;
                authHeader = new BasicHeader("Authorization", "OAuth " + accessToken);
                httpPost.releaseConnection();
                HttpClient httpClient = HttpClientBuilder.create().build();
                StringBuilder builder = new StringBuilder();
                for (String key : sfdcToMdmMapping.keySet()) {
                    builder.append(key);
                    builder.append("+,+");
                }
                HashSet<String> sfdcAccountIds = new HashSet<>();
                HashSet<String> sfdcAddressIds = new HashSet<>();
                //String uri = baseUri + "/query?q=Select+Id+,+Name+,+AccountNumber+,+Site+From+Account+WHERE+CreatedDate+=+LAST_N_MONTHS:1";
                //String uri = baseUri + "/query?q=Select+"+builder.toString().substring(0,builder.toString().length()-3)+"+From+Account+WHERE+Status__c+=+'Prospect'+And+CreatedDate+=+LAST_N_MONTHS:1";
                String uri = baseUri + "/query?q=Select+" + builder.toString().substring(0, builder.toString().length() - 3) + "+From+Account+WHERE+" + String.valueOf(lookup.get("SFDC_FILTER_ACCOUNT").get("SFDC_FILTER_ACCOUNT"));
                LOGGER.info("Account query url =" + uri);
                try {

                    HttpGet httpget = new HttpGet(uri);
                    httpget.addHeader(authHeader);
                    httpget.addHeader(ppHeader);

                    response = httpClient.execute(httpget);
                    getResult = EntityUtils.toString(response.getEntity());
                    JSONObject json = new JSONObject(getResult);

                    JSONArray jarr = json.getJSONArray("records");
                    LOGGER.info("Records fetched from SFDC : " + jarr.length());
                    sfdcCountAccount = jarr.length();
                    boolean header = false;
                    for (int i = 0; i < jarr.length(); i++) {
                    /*System.out.println("REC\n"+json.getJSONArray("records").getJSONObject(i));
                    accountid = json.getJSONArray("records").getJSONObject(i).getString("Id");
                    accountname = json.getJSONArray("records").getJSONObject(i).getString("Name");
                    System.out.println("The Returned Account Details are " + i + ". " + accountid + " Account Name is " + accountname);*/
                        StringBuilder record = new StringBuilder();
                        if (!header) {
                            for (String key : sfdcToMdmMapping.keySet()) {
                                record.append("/" + sfdcToMdmMapping.get(key));
                                record.append(";");
                            }
                            record.append("/SystemName;");
                            record.append("/MatchExclusion");
                            record.append('\r');
                            record.append('\n');
                            Files.write(path, record.toString().getBytes(), StandardOpenOption.APPEND);
                            header = true;
                            record = new StringBuilder();
                        }
                        for (String key : sfdcToMdmMapping.keySet()) {
                            if ("Profile_Class__c".equals(key)) {
                                Map<String, Map<String, String>> countryReferenceFieldsMap = applicationCacheUtil.CountryReferenceFieldsMap("BReference");
                                Map<String, String> resultItem = countryReferenceFieldsMap != null ?
                                        countryReferenceFieldsMap.get(String.valueOf(jarr.getJSONObject(i).get("Country_Code__c"))) : null;
                                String value = jarr.getJSONObject(i).get(key) != null && !"null".equals(String.valueOf(jarr.getJSONObject(i).get(key))) ? String.valueOf(jarr.getJSONObject(i).get(key)) : "";
                                if (StringUtils.isNotBlank(value) || resultItem == null) {
                                    record.append(jarr.getJSONObject(i).get(key) != null && !"null".equals(String.valueOf(jarr.getJSONObject(i).get(key))) ? String.valueOf(jarr.getJSONObject(i).get(key)) : "");
                                } else {
                                    record.append(resultItem.get("ProfileClass"));
                                }
                            } else if ("Type".equals(key)) {
                                String value = jarr.getJSONObject(i).get(key) != null && !"null".equals(String.valueOf(jarr.getJSONObject(i).get(key))) ? String.valueOf(jarr.getJSONObject(i).get(key)) : "";
                                if ("INTERNAL".equals(value.toUpperCase())) {
                                    record.append("I");
                                } else {
                                    record.append("R");
                                }
                            } else if ("Status__c".equals(key)) {
                                record.append("Prospect");
                            } else if (jarr.getJSONObject(i).get(key) != null && StringUtils.containsAny(jarr.getJSONObject(i).get(key).toString(), ';', '^', '\r', '\n')) {
                                if("Address__C".equals(key)){
                                    record.append(StringUtils.wrap(jarr.getJSONObject(i).get(key) != null && !"null".equals(String.valueOf(jarr.getJSONObject(i).get(key))) ? String.valueOf(jarr.getJSONObject(i).get(key)).replaceAll("<br>"," ") : "", '^'));
                                }else{
                                    record.append(StringUtils.wrap(jarr.getJSONObject(i).get(key) != null && !"null".equals(String.valueOf(jarr.getJSONObject(i).get(key))) ? String.valueOf(jarr.getJSONObject(i).get(key)) : "", '^'));
                                }
                            } else {
                                if("Address__C".equals(key)) {
                                    record.append(jarr.getJSONObject(i).get(key) != null && !"null".equals(String.valueOf(jarr.getJSONObject(i).get(key))) ? String.valueOf(jarr.getJSONObject(i).get(key)).replaceAll("<br>"," ") : "");
                                }else{
                                    record.append(jarr.getJSONObject(i).get(key) != null && !"null".equals(String.valueOf(jarr.getJSONObject(i).get(key))) ? String.valueOf(jarr.getJSONObject(i).get(key)) : "");
                                }
                            }
                            record.append(";");
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String today = sdf.format(new Date());
                        //record.append(today+";");
                        record.append("SFDC;");
                        record.append("N");
                        record.append('\r');
                        record.append('\n');
                        Files.write(path, record.toString().getBytes(), StandardOpenOption.APPEND);
                        if (jarr.getJSONObject(i).get("Id") != null && !"null".equals(String.valueOf(jarr.getJSONObject(i).get("Id")))) {
                            sfdcAccountIds.add(String.valueOf(jarr.getJSONObject(i).get("Id")));
                        }
                        LOGGER.info("Record written to file : \n" + record.toString());
                    }

                    //Address
                    path = java.nio.file.Paths.get(System.getProperty("ebx.home"), "Address" + time + ".csv");
                    if (!Files.exists(path)) {
                        try {
                            Files.createFile(path);
                        } catch (IOException e) {
                            LOGGER.error("Error creating temporary file", e);
                            throw new ApplicationRuntimeException("Error in Deduplicate Prospect Service", e);
                        }
                    }
                    sfdcToMdmMapping = lookup.get("MAPPING_SFDC_TO_MDM_ADDRESS");
                    builder = new StringBuilder();
                    for (String key : sfdcToMdmMapping.keySet()) {
                        builder.append(key);
                        builder.append("+,+");
                    }
                    //uri = baseUri + "/query?q=Select+"+builder.toString().substring(0,builder.toString().length()-3)+"+From+Address__c+WHERE+Status__c+=+'Prospect'+And+CreatedDate+=+LAST_N_MONTHS:1";
                    uri = baseUri + "/query?q=Select+" + builder.toString().substring(0, builder.toString().length() - 3) + "+From+Address__c+WHERE+" + String.valueOf(lookup.get("SFDC_FILTER_ADDRESS").get("SFDC_FILTER_ADDRESS"));
                    LOGGER.info("Address query url =" + uri);
                    httpget = new HttpGet(uri);
                    httpget.addHeader(authHeader);
                    httpget.addHeader(ppHeader);

                    response = httpClient.execute(httpget);
                    getResult = EntityUtils.toString(response.getEntity());
                    json = new JSONObject(getResult);

                    jarr = json.getJSONArray("records");
                    LOGGER.info("Address Records fetched from SFDC : " + jarr.length());
                    sfdcCountAddress = jarr.length();
                    header = false;
                    for (int i = 0; i < jarr.length(); i++) {
                    /*System.out.println("REC\n"+json.getJSONArray("records").getJSONObject(i));
                    accountid = json.getJSONArray("records").getJSONObject(i).getString("Id");
                    accountname = json.getJSONArray("records").getJSONObject(i).getString("Name");
                    System.out.println("The Returned Account Details are " + i + ". " + accountid + " Account Name is " + accountname);*/
                        StringBuilder record = new StringBuilder();
                        if (!header) {
                            for (String key : sfdcToMdmMapping.keySet()) {
                                record.append("/" + sfdcToMdmMapping.get(key));
                                record.append(";");
                            }
                            record.append("/SystemName;");
                            record.append("/Address;");
                            record.append("/MatchExclusion");
                            record.append('\r');
                            record.append('\n');
                            Files.write(path, record.toString().getBytes(), StandardOpenOption.APPEND);
                            header = true;
                            record = new StringBuilder();
                        }
                        for (String key : sfdcToMdmMapping.keySet()) {
                            if (jarr.getJSONObject(i).get(key) != null && StringUtils.containsAny(jarr.getJSONObject(i).get(key).toString(), ';', '^', '\r', '\n')) {
                                record.append(StringUtils.wrap(jarr.getJSONObject(i).get(key) != null && !"null".equals(String.valueOf(jarr.getJSONObject(i).get(key))) ? String.valueOf(jarr.getJSONObject(i).get(key)) : "", '^'));
                            } else {
                                record.append(jarr.getJSONObject(i).get(key) != null && !"null".equals(String.valueOf(jarr.getJSONObject(i).get(key))) ? String.valueOf(jarr.getJSONObject(i).get(key)) : "");
                            }
                            record.append(";");
                        }
                        record.append("SFDC;");
                        String concatenated = getConcatenatedAddress(jarr.getJSONObject(i));
                        if (StringUtils.containsAny(concatenated, ';', '^', '\r', '\n')) {
                            concatenated = StringUtils.wrap(concatenated, '^');
                        }
                        record.append(concatenated);
                        record.append(";");
                        record.append("N");
                        record.append('\r');
                        record.append('\n');
                        Files.write(path, record.toString().getBytes(), StandardOpenOption.APPEND);
                        if (jarr.getJSONObject(i).get("Id") != null && !"null".equals(String.valueOf(jarr.getJSONObject(i).get("Id")))) {
                            sfdcAddressIds.add(String.valueOf(jarr.getJSONObject(i).get("Id")));
                        }
                        LOGGER.info("Address Record written to file : \n" + record.toString());
                    }
                } catch (IOException e) {
                    throw new ApplicationRuntimeException("Error in Deduplicate Prospect Service", e);
                }

                OrchestraRestClient orchestraRestClient = (OrchestraRestClient) SpringContext.getApplicationContext().getBean("orchestraRestClient");
                LOGGER.info("Deleting existing records in Prospect");
                try {
                    if (applicationCacheUtil.getLookupValues("BReference").get("DeleteExistingProspect") != null && "Y".equals(applicationCacheUtil.getLookupValues("BReference").get("DeleteExistingProspect").get("DeleteExistingProspect"))) {
                        LOGGER.info("Deleting existing records in Prospect");
                        orchestraRestClient.delete("BCMDReference", "Prospect", "root/Account", null);
                        orchestraRestClient.delete("BCMDReference", "Prospect", "root/Address", null);
                    }
                } catch (Exception e) {
                    throw new ApplicationRuntimeException("Error in Deduplicate Prospect Service. Error deleting existing records in Prospect", e);
                }

                //doSfdcProspect(aContext,aWriter,time);

                final Path accountPath = java.nio.file.Paths.get(System.getProperty("ebx.home"), "Account" + time + ".csv");
                final Path addressPath = java.nio.file.Paths.get(System.getProperty("ebx.home"), "Address" + time + ".csv");
                if(sfdcCountAccount>0) {
                    Procedure procedure = procedureContext -> {

                        LOGGER.info("accountPath csv file exists? " + accountPath.toFile().exists());
                        AdaptationTable table = Repository.getDefault().lookupHome(HomeKey.forBranchName("CMDReference")).findAdaptationOrNull(AdaptationName.forName("Prospect")).getTable(Paths._Account.getPathInSchema());
                        LOGGER.info("table=" + table.toString());

                        ExportImportCSVSpec csvSpec = new ExportImportCSVSpec();
                        csvSpec.setFieldSeparator(';');
                        csvSpec.setTextDelimiter('^');
                        csvSpec.setHeader(ExportImportCSVSpec.Header.PATH_IN_TABLE);
                        ImportSpec importSpec = new ImportSpec();
                        importSpec.setSourceFile(accountPath.toFile());
                        importSpec.setTargetAdaptationTable(table);
                        importSpec.setImportMode(ImportSpecMode.UPDATE_OR_INSERT);
                        importSpec.setCSVSpec(csvSpec);
                        procedureContext.doImport(importSpec);
                    };
                    ProgrammaticService svc = ProgrammaticService.createForSession(aContext.getSession(), Repository.getDefault().lookupHome(HomeKey.forBranchName("CMDReference")));
                    ProcedureResult result = null;
                    result = svc.execute(procedure);
                    if (result == null || result.hasFailed()) {
                        LOGGER.info("Account Import Procedure failed");
                        throw new ApplicationRuntimeException("Account file import failed", result.getException());
                    } else {
                        LOGGER.info("Account Import Procedure successful");
                        if (!sfdcAccountIds.isEmpty()) {
                            ObjectMapper mapper = new ObjectMapper();
                            String[] accountPolicies = {"Prospect", "Prospect_JP", "Prospect_Asian"};
                            AdaptationFilter[] filters = {new NonAsianFilter(), new JapanFilter(), new AsianFilter()};
                            for (int i = 0; i < 3; i++) {
                                OrchestraObject orchestraObject = orchestraRestClient.getById("Bebx-addon-daqa", "ebx-addon-daqa-configuration-v2", "root/DataQualityConfiguration/CrosswalkPolicy/CrosswalkMatchingPolicy", RESTEncodingHelper.encodePrimaryKey(PrimaryKey.parseString(accountPolicies[i])), null);
                                if (orchestraObject != null && orchestraObject.getContent() != null) {
                                    LOGGER.info("Policy " + accountPolicies[i] + " exists.");
                                    for (String policy : accountPolicies) {
                                        if (!policy.equals(accountPolicies[i])) {
                                            OrchestraObject otherPolicyObject = orchestraRestClient.getById("Bebx-addon-daqa", "ebx-addon-daqa-configuration-v2", "root/DataQualityConfiguration/CrosswalkPolicy/CrosswalkMatchingPolicy", RESTEncodingHelper.encodePrimaryKey(PrimaryKey.parseString(policy)), null);
                                            if (otherPolicyObject != null && otherPolicyObject.getContent() != null) {
                                                LOGGER.info("Policy " + policy + " exists. Disabling it ...");
                                                javax.ws.rs.core.Response response1 = orchestraRestClient.updateField("Bebx-addon-daqa", "ebx-addon-daqa-configuration-v2", "root/DataQualityConfiguration/CrosswalkPolicy/CrosswalkMatchingPolicy/" + RESTEncodingHelper.encodePrimaryKey(PrimaryKey.parseString(policy)) + "/active", new OrchestraContent(false), null);
                                                if (response1.getStatus() >= 300) {
                                                    throw new ApplicationRuntimeException("Error deactivating crosswalk policy " + policy + ". Response JSON : " + mapper.writeValueAsString(response1.readEntity(String.class)));
                                                }
                                            }
                                        } else {
                                            LOGGER.info("Activating Policy - " + policy);
                                            javax.ws.rs.core.Response response1 = orchestraRestClient.updateField("Bebx-addon-daqa", "ebx-addon-daqa-configuration-v2", "root/DataQualityConfiguration/CrosswalkPolicy/CrosswalkMatchingPolicy/" + RESTEncodingHelper.encodePrimaryKey(PrimaryKey.parseString(policy)) + "/active", new OrchestraContent(true), null);
                                            if (response1.getStatus() >= 300) {
                                                throw new ApplicationRuntimeException("Error activating crosswalk policy " + policy + ". Response JSON : " + mapper.writeValueAsString(response1.readEntity(String.class)));
                                            }
                                        }
                                    }
                                    LOGGER.info("Running crosswalk for " + accountPolicies[i]);
                                    runCrosswalkAccount(aContext, sfdcAccountIds, filters[i]);
                                } else {
                                    LOGGER.info("Policy " + accountPolicies[i] + " not found.");
                                }
                            }
                        }
                    }
                }
                if(sfdcCountAddress>0) {
                    Procedure procedure = procedureContext -> {

                        LOGGER.info("addressPath csv file exists? " + addressPath.toFile().exists());
                        AdaptationTable table = Repository.getDefault().lookupHome(HomeKey.forBranchName("CMDReference")).findAdaptationOrNull(AdaptationName.forName("Prospect")).getTable(Paths._Address.getPathInSchema());
                        LOGGER.info("table=" + table.toString());

                        ExportImportCSVSpec csvSpec = new ExportImportCSVSpec();
                        csvSpec.setFieldSeparator(';');
                        csvSpec.setTextDelimiter('^');
                        csvSpec.setHeader(ExportImportCSVSpec.Header.PATH_IN_TABLE);
                        ImportSpec importSpec = new ImportSpec();
                        importSpec.setSourceFile(addressPath.toFile());
                        importSpec.setTargetAdaptationTable(table);
                        importSpec.setImportMode(ImportSpecMode.UPDATE_OR_INSERT);
                        importSpec.setCSVSpec(csvSpec);
                        procedureContext.doImport(importSpec);
                    };
                    ProgrammaticService svc = ProgrammaticService.createForSession(aContext.getSession(), Repository.getDefault().lookupHome(HomeKey.forBranchName("CMDReference")));
                    ProcedureResult result = null;
                    result = svc.execute(procedure);
                    if (result == null || result.hasFailed()) {
                        LOGGER.info("Address Import Procedure failed");
                        throw new ApplicationRuntimeException("Address file import failed", result.getException());
                    } else {
                        LOGGER.info("Address Import Procedure successful");
                        if (!sfdcAddressIds.isEmpty()) {
                            OrchestraObject resultObject = orchestraRestClient.getById("Bebx-addon-daqa", "ebx-addon-daqa-configuration-v2", "root/DataQualityConfiguration/CrosswalkPolicy/CrosswalkMatchingPolicy", RESTEncodingHelper.encodePrimaryKey(PrimaryKey.parseString("Prospect_Address")), null);
                            if ((resultObject != null) && (resultObject.getContent() != null)
                                    && "true".equals(resultObject.getContent().get("active").getContent())) {
                                runCrosswalkAddress(aContext, sfdcAddressIds);
                            }
                        }
                    }
                }
            } catch(Exception ex){
                throw new ApplicationRuntimeException("Error in de-duplicate prospects", ex);
            }
        }
    }

    private void runCrosswalkAccount(UserServicePaneContext aContext, HashSet<String> sfdcAccountIds,AdaptationFilter filter){
        OrchestraObjectList orchestraObjectList = new OrchestraObjectList();
        Procedure procedure = procedureContext -> {
            AdaptationTable table = Repository.getDefault().lookupHome(HomeKey.forBranchName("CMDReference")).findAdaptationOrNull(AdaptationName.forName("Prospect")).getTable(Paths._Account.getPathInSchema());
            AdaptationTable targetTable = Repository.getDefault().lookupHome(HomeKey.forBranchName("CMDReference")).findAdaptationOrNull(AdaptationName.forName("Account")).getTable(Paths._Account.getPathInSchema());
            TableContext context = new TableContext(table, procedureContext);
            List<String> targetDataspaces = new ArrayList<>(Collections.singletonList("CMDReference"));
            List<String> targetDatasets = new ArrayList<>(Collections.singletonList("Account"));
            CrosswalkContext crosswalkContext = new CrosswalkContext(null,filter,targetDataspaces,targetDatasets);
            CrosswalkOperations operations = CrosswalkOperationsFactory.getCrosswalkOperations();
            List<AdaptationTable> tableList = new ArrayList<>();
            tableList.add(targetTable);
            CrosswalkExecutionResult crosswalkResult = filter!=null?operations.executeCrosswalk(context,crosswalkContext):operations.executeCrosswalk(context, tableList);
            RequestResult requestResult = crosswalkResult.getCrosswalkResults();
            LOGGER.info("Account crosswalk result size : " + requestResult.getSize());
            List<OrchestraObject> rows = new ArrayList<>();
            try {
                Adaptation record;
                while ((record = requestResult.nextAdaptation()) != null) {
                    if (sfdcAccountIds.contains(String.valueOf(record.get(CrosswalkResultPaths._Crosswalk._SourceRecord)))) {
                        for(int i=1;i<=20;i++) {
                            if(record.get((com.orchestranetworks.schema.Path)CrosswalkResultPaths._Crosswalk.class.getDeclaredField("_MatchingDetail"+String.format("%02d", i)+"_Record").get(null))!=null) {
                                OrchestraObject orchestraObject = new OrchestraObject();
                                Map<String, OrchestraContent> jsonFieldsMap = new HashMap<>();
                                jsonFieldsMap.put("SystemId", new OrchestraContent(record.get(CrosswalkResultPaths._Crosswalk._SourceRecord)));
                                jsonFieldsMap.put("MDMAccountId", new OrchestraContent(record.get((com.orchestranetworks.schema.Path)CrosswalkResultPaths._Crosswalk.class.getDeclaredField("_MatchingDetail" + String.format("%02d", i) + "_Record").get(null))));
                                jsonFieldsMap.put("Score", new OrchestraContent(record.get((com.orchestranetworks.schema.Path)CrosswalkResultPaths._Crosswalk.class.getDeclaredField("_MatchingDetail" + String.format("%02d", i) + "_Score").get(null))));
                                orchestraObject.setContent(jsonFieldsMap);
                                rows.add(orchestraObject);
                            }
                        }
                    }
                }
            } finally {
                requestResult.close();
            }
            orchestraObjectList.setRows(rows);
        };
        ProgrammaticService svc = ProgrammaticService.createForSession(aContext.getSession(), Repository.getDefault().lookupHome(HomeKey.forBranchName("CMDReference")));
        ProcedureResult result = null;
        result = svc.execute(procedure);
        if (result == null || result.hasFailed()) {
            throw new ApplicationRuntimeException("Execute crosswalk Procedure failed for Account", result.getException());
        } else {
            LOGGER.info("Account execute crosswalk Procedure successful");
            Runnable updateCrosswalkResultsAccount = () -> {
                try {
                    LOGGER.info("Update crosswalk results Account Begin");
                    ObjectMapper mapper = new ObjectMapper();
                    OrchestraRestClient restClient = (OrchestraRestClient) SpringContext.getApplicationContext().getBean("orchestraRestClient");
                    for (OrchestraObject orchestraObject : orchestraObjectList.getRows()) {
                        Map<String, OrchestraContent> jsonFieldsMap = orchestraObject.getContent();
                        OrchestraObject mdmAccount = restClient.getById("BCMDReference", "Account", "root/Account", RESTEncodingHelper.encodePrimaryKey(PrimaryKey.parseString(String.valueOf(jsonFieldsMap.get("MDMAccountId").getContent()))), null);
                        jsonFieldsMap.put("MDMAccountName", mdmAccount.getContent().get("AccountName"));
                        jsonFieldsMap.put("MDMAlternateAccountName", mdmAccount.getContent().get("NameLocalLanguage"));
                    }
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("updateOrInsert", "true");
                    RestResponse restResponse = null;
                    LOGGER.info("Updating crosswalk results: \n" + mapper.writeValueAsString(orchestraObjectList));
                    restResponse = restClient.post("BCMDReference", "Prospect", "root/MDMMatch", orchestraObjectList, parameters, 300000, null);
                    if (restResponse.getStatus() != 200 && restResponse.getStatus() != 201) {
                        LOGGER.error("Error updating crosswalk results: " + String.valueOf(mapper.writeValueAsString(restResponse.getResponseBody())));
                    }
                } catch (IOException e) {
                    LOGGER.error("Error updating crosswalk results", e);
                    throw new ApplicationRuntimeException("Error updating crosswalk results", e);
                }
            };
            new Thread(updateCrosswalkResultsAccount).start();
        }
    }

    private void runCrosswalkAddress(UserServicePaneContext aContext, HashSet<String> sfdcAddressIds){
        OrchestraObjectList orchestraObjectListAddress = new OrchestraObjectList();
        Procedure procedure = procedureContext -> {
            AdaptationTable table = Repository.getDefault().lookupHome(HomeKey.forBranchName("CMDReference")).findAdaptationOrNull(AdaptationName.forName("Prospect")).getTable(Paths._Address.getPathInSchema());
            AdaptationTable targetTable = Repository.getDefault().lookupHome(HomeKey.forBranchName("CMDReference")).findAdaptationOrNull(AdaptationName.forName("Account")).getTable(Paths._Address.getPathInSchema());
            TableContext context = new TableContext(table, procedureContext);
            CrosswalkOperations operations = CrosswalkOperationsFactory.getCrosswalkOperations();
            List<AdaptationTable> tableList = new ArrayList<>();
            tableList.add(targetTable);
            CrosswalkExecutionResult crosswalkResult = operations.executeCrosswalk(context, tableList);
            RequestResult requestResult = crosswalkResult.getCrosswalkResults();
            LOGGER.info("Address crosswalk result size : " + requestResult.getSize());
            List<OrchestraObject> rows = new ArrayList<>();
            try {
                Adaptation record;
                while ((record = requestResult.nextAdaptation()) != null) {
                    if (sfdcAddressIds.contains(String.valueOf(record.get(CrosswalkResultPaths._Crosswalk._SourceRecord)))) {
                        OrchestraObject orchestraObject = new OrchestraObject();
                        Map<String, OrchestraContent> jsonFieldsMap = new HashMap<>();
                        jsonFieldsMap.put("SystemId", new OrchestraContent(record.get(CrosswalkResultPaths._Crosswalk._SourceRecord)));
                        jsonFieldsMap.put("MDMAddressId", new OrchestraContent(record.get(CrosswalkResultPaths._Crosswalk._MatchingDetail01_Record)));
                        jsonFieldsMap.put("Score", new OrchestraContent(record.get(CrosswalkResultPaths._Crosswalk._MatchingDetail01_Score)));
                        orchestraObject.setContent(jsonFieldsMap);
                        rows.add(orchestraObject);
                    }
                }
            } finally {
                requestResult.close();
            }
            orchestraObjectListAddress.setRows(rows);
        };
        ProgrammaticService svc = ProgrammaticService.createForSession(aContext.getSession(), Repository.getDefault().lookupHome(HomeKey.forBranchName("CMDReference")));
        ProcedureResult result = null;
        result = svc.execute(procedure);
        if (result == null || result.hasFailed()) {
            throw new ApplicationRuntimeException("Execute crosswalk Procedure failed for Address", result.getException());
        } else {
            LOGGER.info("Address execute crosswalk Procedure successful");
            Runnable updateCrosswalkResultsAddress = () -> {
                try {
                    LOGGER.info("Update crosswalk results Address Begin");
                    ObjectMapper mapper = new ObjectMapper();
                    OrchestraRestClient restClient = (OrchestraRestClient) SpringContext.getApplicationContext().getBean("orchestraRestClient");
                    for (OrchestraObject orchestraObject : orchestraObjectListAddress.getRows()) {
                        Map<String, OrchestraContent> jsonFieldsMap = orchestraObject.getContent();
                        OrchestraObject mdmAddress = restClient.getById("BCMDReference", "Account", "root/Address", RESTEncodingHelper.encodePrimaryKey(PrimaryKey.parseString(String.valueOf(jsonFieldsMap.get("MDMAddressId").getContent()))), null);
                        jsonFieldsMap.put("MDMAccountId", mdmAddress.getContent().get("MDMAccountId"));
                        jsonFieldsMap.put("MDMAccountName", mdmAddress.getContent().get("MDMAccountName"));
                        jsonFieldsMap.put("MDMAddress", mdmAddress.getContent().get("Address"));
                    }
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("updateOrInsert", "true");
                    RestResponse restResponse = null;
                    LOGGER.info("Updating address crosswalk results: \n" + mapper.writeValueAsString(orchestraObjectListAddress));
                    restResponse = restClient.post("BCMDReference", "Prospect", "root/Address", orchestraObjectListAddress, parameters, 300000, null);
                    if (restResponse.getStatus() != 200 && restResponse.getStatus() != 201) {
                        LOGGER.error("Error updating address crosswalk results: " + String.valueOf(mapper.writeValueAsString(restResponse.getResponseBody())));
                    }
                } catch (IOException e) {
                    LOGGER.error("Error updating address crosswalk results", e);
                    throw new ApplicationRuntimeException("Error in Deduplicate Prospect Service", e);
                }
            };
            new Thread(updateCrosswalkResultsAddress).start();
        }
    }

    private String getConcatenatedAddress(JSONObject jsonObject) {
        List<String> addresses = new ArrayList<>();
        for(int i=1;i<=4;i++){
            if(StringUtils.isNotBlank(String.valueOf(jsonObject.get("Address_Line_"+i+"__c")))
                    && !"null".equals(String.valueOf(jsonObject.get("Address_Line_"+i+"__c")))){
                addresses.add(String.valueOf(jsonObject.get("Address_Line_"+i+"__c")));
            }
        }
        if(!addresses.isEmpty()){
            return StringUtils.join(addresses, " ");
        }else{
            return "";
        }
    }

    public ObjectKey getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(ObjectKey objectKey) {
        this.objectKey = objectKey;
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



    private void doSfdcProspect(UserServicePaneContext aContext, UserServicePaneWriter aWriter, String time) {
        ApplicationCacheUtil applicationCacheUtil = (ApplicationCacheUtil) SpringContext.getApplicationContext().getBean("applicationCacheUtil");
        Map<String,Map<String,String>> lookup = applicationCacheUtil.getLookupValues("BReference");
        Map<String,String> sfdcToMdmMapping = lookup.get("MAPPING_SFDC_TO_MDM");
        if(sfdcToMdmMapping!=null && !sfdcToMdmMapping.isEmpty()) {
            try {
                if(aContext.getRepository().lookupHome(HomeKey.forBranchName("SFDCProspect"))!=null) {
                    aContext.getRepository().closeHome(aContext.getRepository().lookupHome(HomeKey.forBranchName("SFDCProspect")), aContext.getSession());
                    aContext.getRepository().getPurgeDelegate().markHomeForHistoryPurge(aContext.getRepository().lookupHome(HomeKey.forBranchName("SFDCProspect")), aContext.getSession());
                    aContext.getRepository().deleteHome(aContext.getRepository().lookupHome(HomeKey.forBranchName("SFDCProspect")), aContext.getSession());
                }
                HomeCreationSpec homeSpec = new HomeCreationSpec();
                homeSpec.setParent(aContext.getRepository().lookupHome(HomeKey.forBranchName("CMDReference")));
                homeSpec.setKey(HomeKey.forBranchName("SFDCProspect"));
                homeSpec.setOwner(Profile.forUser("admin"));
                UserMessageString label = new UserMessageString();
                label.setString(Locale.ENGLISH,"SFDC Prospect");
                homeSpec.setLabel(label);
                UserMessageString description = new UserMessageString();
                description.setString(Locale.ENGLISH,"Dataspace for SFDC Prospects");
                homeSpec.setDescription(description);
                try {
                    aContext.getRepository().createHome(homeSpec,aContext.getSession());
                } catch (OperationException e) {
                    LOGGER.error("Error creating dataspace",e);
                    throw new ApplicationRuntimeException("Error in Deduplicate Prospect Service",e);
                }
            } catch (OperationException e) {
                LOGGER.error("Error deleting dataspace",e);
                throw new ApplicationRuntimeException("Error in Deduplicate Prospect Service",e);
            }

            final Path processPolicyPath = java.nio.file.Paths.get(System.getProperty("ebx.home"), "ProcessPolicy.csv");
            final Path accountPath = java.nio.file.Paths.get(System.getProperty("ebx.home"), "Account"+time+".csv");
            final Path addressPath = java.nio.file.Paths.get(System.getProperty("ebx.home"), "Address"+time+".csv");

            Procedure procedure = procedureContext -> {

                LOGGER.info("processpolicy csv file exists? " + processPolicyPath.toFile().exists());
                AdaptationTable table = Repository.getDefault().lookupHome(
                        HomeKey.forBranchName("ebx-addon-daqa")).findAdaptationOrNull(
                        AdaptationName.forName("ebx-addon-daqa-configuration-v2")).getTable(
                        com.orchestranetworks.schema.Path.parse("/root").add("DataQualityConfiguration").add("MatchingPolicy").add("MatchingPolicy"));
                LOGGER.info("table=" + table.toString());

                ExportImportCSVSpec csvSpec = new ExportImportCSVSpec();
                csvSpec.setFieldSeparator(';');
                csvSpec.setTextDelimiter('^');
                csvSpec.setHeader(ExportImportCSVSpec.Header.LABEL);
                ImportSpec importSpec = new ImportSpec();
                importSpec.setSourceFile(processPolicyPath.toFile());
                importSpec.setTargetAdaptationTable(table);
                importSpec.setImportMode(ImportSpecMode.UPDATE_OR_INSERT);
                importSpec.setCSVSpec(csvSpec);
                procedureContext.doImport(importSpec);
            };
            ProgrammaticService svc = ProgrammaticService.createForSession(aContext.getSession(), Repository.getDefault().lookupHome(HomeKey.forBranchName("ebx-addon-daqa")));
            ProcedureResult result = null;
            result = svc.execute(procedure);
            if (result == null || result.hasFailed()) {
                LOGGER.info("ProcessPolicy Import Procedure failed");
            } else {
                LOGGER.info("ProcessPolicy Import Procedure successful");
            }


            procedure = procedureContext -> {

                LOGGER.info("accountPath csv file exists? " + accountPath.toFile().exists());
                AdaptationTable table = Repository.getDefault().lookupHome(HomeKey.forBranchName("SFDCProspect")).findAdaptationOrNull(AdaptationName.forName("Account")).getTable(Paths._Account.getPathInSchema());
                LOGGER.info("table=" + table.toString());

                ExportImportCSVSpec csvSpec = new ExportImportCSVSpec();
                csvSpec.setFieldSeparator(';');
                csvSpec.setTextDelimiter('^');
                csvSpec.setHeader(ExportImportCSVSpec.Header.PATH_IN_TABLE);
                ImportSpec importSpec = new ImportSpec();
                importSpec.setSourceFile(accountPath.toFile());
                importSpec.setTargetAdaptationTable(table);
                importSpec.setImportMode(ImportSpecMode.UPDATE_OR_INSERT);
                importSpec.setCSVSpec(csvSpec);
                procedureContext.doImport(importSpec);
            };
            svc = ProgrammaticService.createForSession(aContext.getSession(), Repository.getDefault().lookupHome(HomeKey.forBranchName("SFDCProspect")));
            result = null;
            result = svc.execute(procedure);
            if (result == null || result.hasFailed()) {
                LOGGER.info("Account Import Procedure failed");
            } else {
                LOGGER.info("Account Import Procedure successful");
            }

            procedure = procedureContext -> {

                LOGGER.info("addressPath csv file exists? " + addressPath.toFile().exists());
                AdaptationTable table = Repository.getDefault().lookupHome(HomeKey.forBranchName("SFDCProspect")).findAdaptationOrNull(AdaptationName.forName("Account")).getTable(Paths._Address.getPathInSchema());
                LOGGER.info("table=" + table.toString());

                ExportImportCSVSpec csvSpec = new ExportImportCSVSpec();
                csvSpec.setFieldSeparator(';');
                csvSpec.setTextDelimiter('^');
                csvSpec.setHeader(ExportImportCSVSpec.Header.PATH_IN_TABLE);
                ImportSpec importSpec = new ImportSpec();
                importSpec.setSourceFile(addressPath.toFile());
                importSpec.setTargetAdaptationTable(table);
                importSpec.setImportMode(ImportSpecMode.UPDATE_OR_INSERT);
                importSpec.setCSVSpec(csvSpec);
                procedureContext.doImport(importSpec);
            };
            svc = ProgrammaticService.createForSession(aContext.getSession(), Repository.getDefault().lookupHome(HomeKey.forBranchName("SFDCProspect")));
            result = null;
            result = svc.execute(procedure);
            if (result == null || result.hasFailed()) {
                LOGGER.info("Address Import Procedure failed");
            } else {
                LOGGER.info("Address Import Procedure successful");
            }
        }else{
            String urlEnding = aWriter.getURLForEndingService();
            LOGGER.info("Url for ending service : "+urlEnding);
            aWriter.addJS("window.location.href='"+urlEnding+"';");
        }
    }

}
