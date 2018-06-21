package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationName;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.RequestResult;
import com.orchestranetworks.addon.daqa.TableContext;
import com.orchestranetworks.addon.daqa.crosswalk.CrosswalkExecutionResult;
import com.orchestranetworks.addon.daqa.crosswalk.CrosswalkOperations;
import com.orchestranetworks.addon.daqa.crosswalk.CrosswalkOperationsFactory;
import com.orchestranetworks.addon.daqa.crosswalk.CrosswalkResultPaths;
import com.orchestranetworks.instance.HomeKey;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.service.*;
import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.*;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.SpringContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraContent;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObject;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObjectList;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.RestResponse;
import com.sereneast.orchestramdm.keysight.mdmcustom.rest.client.OrchestraRestClient;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ApplicationCacheUtil;
import org.apache.commons.lang3.StringUtils;
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
    private static final String passWord = "Keysight123gOJm2KPTPV0c7hFjAVdaE5ccW";
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

        ApplicationCacheUtil applicationCacheUtil = (ApplicationCacheUtil) SpringContext.getApplicationContext().getBean("applicationCacheUtil");
        Map<String,Map<String,String>> lookup = applicationCacheUtil.getLookupValues("BReference");
        Map<String,String> sfdcToMdmMapping = lookup.get("MAPPING_SFDC_TO_MDM");
        if(sfdcToMdmMapping!=null && !sfdcToMdmMapping.isEmpty()) {
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
            java.nio.file.Path path = java.nio.file.Paths.get(System.getProperty("ebx.home"), "Account"+time+".csv");
            if(!Files.exists(path)){
                try {
                    Files.createFile(path);
                } catch (IOException e) {
                    LOGGER.error("Error creating temporary file",e);
                }
            }
            tokenUrl = environmentUrl + "/services/oauth2/token?grant_type=password";
            String loginUrl = tokenUrl + "&client_id=" + clientId + "&client_secret=" + clientSecret + "&username=" + userName + "&password=" + passWord;
            HttpPost httpPost = new HttpPost(loginUrl);
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = null;
            try
            {
                response = httpclient.execute(httpPost);
            } catch (IOException ioexception) {}
            String getResult = null;
            try {
                getResult = EntityUtils.toString(response.getEntity());
            } catch (IOException e) {}

            JSONObject jsonObject = null;
            String accessToken = null;
            String instanceUrl = null;

            try {
                jsonObject = (JSONObject) new JSONTokener(getResult).nextValue();
                accessToken = jsonObject.getString("access_token");
                instanceUrl = jsonObject.getString("instance_url");
            } catch (JSONException jsonexp) { }

            baseUri = instanceUrl + R_EndPoint + Version ;
            authHeader = new BasicHeader("Authorization", "OAuth " + accessToken) ;
            httpPost.releaseConnection();
            HttpClient httpClient = HttpClientBuilder.create().build();
            StringBuilder builder = new StringBuilder();
            for (String key : sfdcToMdmMapping.keySet()) {
                builder.append(key);
                builder.append("+,+");
            }
            //String uri = baseUri + "/query?q=Select+Id+,+Name+,+AccountNumber+,+Site+From+Account+WHERE+CreatedDate+=+LAST_N_MONTHS:1";
            String uri = baseUri + "/query?q=Select+"+builder.toString().substring(0,builder.toString().length()-3)+"+From+Account+WHERE+CreatedDate+=+LAST_N_MONTHS:1";
            LOGGER.info("uri="+uri);
            try{

                HttpGet httpget = new HttpGet(uri);
                httpget.addHeader(authHeader);
                httpget.addHeader(ppHeader);

                response = httpClient.execute(httpget);
                getResult = EntityUtils.toString(response.getEntity());
                JSONObject json = new JSONObject(getResult);

                JSONArray jarr = json.getJSONArray("records");
                LOGGER.info("Records fetched from SFDC : "+jarr.length());
                boolean header = false;
                for(int i = 0 ; i < jarr.length(); i++){
                    /*System.out.println("REC\n"+json.getJSONArray("records").getJSONObject(i));
                    accountid = json.getJSONArray("records").getJSONObject(i).getString("Id");
                    accountname = json.getJSONArray("records").getJSONObject(i).getString("Name");
                    System.out.println("The Returned Account Details are " + i + ". " + accountid + " Account Name is " + accountname);*/
                    StringBuilder record = new StringBuilder();
                    if(!header) {
                        for (String key : sfdcToMdmMapping.keySet()) {
                            record.append("/"+sfdcToMdmMapping.get(key));
                            record.append(";");
                        }
                        record.append("/PaymentStartDate;");
                        record.append("/SystemName;");
                        record.deleteCharAt(record.length()-1);
                        record.append('\r');
                        record.append('\n');
                        Files.write(path, record.toString().getBytes(), StandardOpenOption.APPEND);
                        header = true;
                        record = new StringBuilder();
                    }
                    for (String key : sfdcToMdmMapping.keySet()) {
                        if("Profile_Class__c".equals(key)){
                            Map<String,Map<String,String>> countryReferenceFieldsMap = applicationCacheUtil.CountryReferenceFieldsMap("BReference");
                            Map<String,String> resultItem = countryReferenceFieldsMap!=null?
                                    countryReferenceFieldsMap.get(String.valueOf(jarr.getJSONObject(i).get("Country_Code__c"))):null;
                            String value = jarr.getJSONObject(i).get(key)!=null && !"null".equals(String.valueOf(jarr.getJSONObject(i).get(key)))?String.valueOf(jarr.getJSONObject(i).get(key)):"";
                            if(StringUtils.isNotBlank(value) || resultItem==null) {
                                record.append(jarr.getJSONObject(i).get(key)!=null && !"null".equals(String.valueOf(jarr.getJSONObject(i).get(key)))?String.valueOf(jarr.getJSONObject(i).get(key)):"");
                            }else{
                                record.append(resultItem.get("ProfileClass"));
                            }
                        }else if("Type".equals(key)){
                            String value = jarr.getJSONObject(i).get(key)!=null && !"null".equals(String.valueOf(jarr.getJSONObject(i).get(key)))?String.valueOf(jarr.getJSONObject(i).get(key)):"";
                            if("INTERNAL".equals(value.toUpperCase())){
                                record.append("I");
                            }else{
                                record.append("R");
                            }
                        }else if("Status__c".equals(key)){
                            record.append("Prospect");
                        }else if(jarr.getJSONObject(i).get(key)!=null && StringUtils.contains(jarr.getJSONObject(i).get(key).toString(),';')){
                            record.append(StringUtils.wrap(jarr.getJSONObject(i).get(key)!=null && !"null".equals(String.valueOf(jarr.getJSONObject(i).get(key)))?String.valueOf(jarr.getJSONObject(i).get(key)):"",'"'));
                        }else{
                            record.append(jarr.getJSONObject(i).get(key)!=null && !"null".equals(String.valueOf(jarr.getJSONObject(i).get(key)))?String.valueOf(jarr.getJSONObject(i).get(key)):"");
                        }
                        record.append(";");
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String today = sdf.format(new Date());
                    record.append(today+";");
                    record.append("SFDC;");
                    record.deleteCharAt(record.length()-1);
                    record.append('\r');
                    record.append('\n');
                    Files.write(path, record.toString().getBytes(), StandardOpenOption.APPEND);
                    LOGGER.info("Record written to file : \n"+record.toString());
                }

                //Address
                path = java.nio.file.Paths.get(System.getProperty("ebx.home"), "Address"+time+".csv");
                if(!Files.exists(path)){
                    try {
                        Files.createFile(path);
                    } catch (IOException e) {
                        LOGGER.error("Error creating temporary file",e);
                    }
                }
                sfdcToMdmMapping = lookup.get("MAPPING_SFDC_TO_MDM_ADDRESS");
                builder = new StringBuilder();
                for (String key : sfdcToMdmMapping.keySet()) {
                    builder.append(key);
                    builder.append("+,+");
                }
                uri = baseUri + "/query?q=Select+"+builder.toString().substring(0,builder.toString().length()-3)+"+From+Address__c+WHERE+CreatedDate+=+LAST_N_MONTHS:1";
                LOGGER.info("address uri="+uri);
                httpget = new HttpGet(uri);
                httpget.addHeader(authHeader);
                httpget.addHeader(ppHeader);

                response = httpClient.execute(httpget);
                getResult = EntityUtils.toString(response.getEntity());
                json = new JSONObject(getResult);

                jarr = json.getJSONArray("records");
                LOGGER.info("Address Records fetched from SFDC : "+jarr.length());
                header = false;
                for(int i = 0 ; i < jarr.length(); i++){
                    /*System.out.println("REC\n"+json.getJSONArray("records").getJSONObject(i));
                    accountid = json.getJSONArray("records").getJSONObject(i).getString("Id");
                    accountname = json.getJSONArray("records").getJSONObject(i).getString("Name");
                    System.out.println("The Returned Account Details are " + i + ". " + accountid + " Account Name is " + accountname);*/
                    StringBuilder record = new StringBuilder();
                    if(!header) {
                        for (String key : sfdcToMdmMapping.keySet()) {
                            record.append("/"+sfdcToMdmMapping.get(key));
                            record.append(";");
                        }
                        record.append("/RPLCheck;");
                        record.append("/SystemName;");
                        record.deleteCharAt(record.length()-1);
                        record.append('\r');
                        record.append('\n');
                        Files.write(path, record.toString().getBytes(), StandardOpenOption.APPEND);
                        header = true;
                        record = new StringBuilder();
                    }
                    for (String key : sfdcToMdmMapping.keySet()) {
                        if(jarr.getJSONObject(i).get(key)!=null && StringUtils.contains(jarr.getJSONObject(i).get(key).toString(),';')){
                            record.append(StringUtils.wrap(jarr.getJSONObject(i).get(key)!=null && !"null".equals(String.valueOf(jarr.getJSONObject(i).get(key)))?String.valueOf(jarr.getJSONObject(i).get(key)):"",'"'));
                        }else{
                            record.append(jarr.getJSONObject(i).get(key)!=null && !"null".equals(String.valueOf(jarr.getJSONObject(i).get(key)))?String.valueOf(jarr.getJSONObject(i).get(key)):"");
                        }
                        record.append(";");
                    }
                    record.append("rplcheck;");
                    record.append("SFDC;");
                    record.deleteCharAt(record.length()-1);
                    record.append('\r');
                    record.append('\n');
                    Files.write(path, record.toString().getBytes(), StandardOpenOption.APPEND);
                    LOGGER.info("Address Record written to file : \n"+record.toString());
                }
            }catch (IOException e) {}

            final Path accountPath = java.nio.file.Paths.get(System.getProperty("ebx.home"), "Account"+time+".csv");
            final Path addressPath = java.nio.file.Paths.get(System.getProperty("ebx.home"), "Address"+time+".csv");
            Procedure procedure = procedureContext -> {

                LOGGER.info("accountPath csv file exists? " + accountPath.toFile().exists());
                AdaptationTable table = Repository.getDefault().lookupHome(HomeKey.forBranchName("CMDReference")).findAdaptationOrNull(AdaptationName.forName("Prospect")).getTable(Paths._Account.getPathInSchema());
                LOGGER.info("table=" + table.toString());

                ExportImportCSVSpec csvSpec = new ExportImportCSVSpec();
                csvSpec.setFieldSeparator(';');
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
            } else {
                LOGGER.info("Account Import Procedure successful");
            }

            procedure = procedureContext -> {

                LOGGER.info("addressPath csv file exists? " + addressPath.toFile().exists());
                AdaptationTable table = Repository.getDefault().lookupHome(HomeKey.forBranchName("CMDReference")).findAdaptationOrNull(AdaptationName.forName("Prospect")).getTable(Paths._Address.getPathInSchema());
                LOGGER.info("table=" + table.toString());

                ExportImportCSVSpec csvSpec = new ExportImportCSVSpec();
                csvSpec.setFieldSeparator(';');
                csvSpec.setHeader(ExportImportCSVSpec.Header.PATH_IN_TABLE);
                ImportSpec importSpec = new ImportSpec();
                importSpec.setSourceFile(addressPath.toFile());
                importSpec.setTargetAdaptationTable(table);
                importSpec.setImportMode(ImportSpecMode.UPDATE_OR_INSERT);
                importSpec.setCSVSpec(csvSpec);
                procedureContext.doImport(importSpec);
            };
            svc = ProgrammaticService.createForSession(aContext.getSession(), Repository.getDefault().lookupHome(HomeKey.forBranchName("CMDReference")));
            result = null;
            result = svc.execute(procedure);
            if (result == null || result.hasFailed()) {
                LOGGER.info("Address Import Procedure failed");
            } else {
                LOGGER.info("Address Import Procedure successful");
            }
            procedure = procedureContext -> {
                AdaptationTable table = Repository.getDefault().lookupHome(HomeKey.forBranchName("CMDReference")).findAdaptationOrNull(AdaptationName.forName("Prospect")).getTable(Paths._Account.getPathInSchema());
                AdaptationTable targetTable = Repository.getDefault().lookupHome(HomeKey.forBranchName("CMDReference")).findAdaptationOrNull(AdaptationName.forName("Account")).getTable(Paths._Account.getPathInSchema());
                TableContext context = new TableContext(table, procedureContext);
                CrosswalkOperations operations = CrosswalkOperationsFactory.getCrosswalkOperations();
                List<AdaptationTable> tableList = new ArrayList<>();
                tableList.add(targetTable);
                CrosswalkExecutionResult crosswalkResult = operations.executeCrosswalk(context,tableList);
                RequestResult requestResult = crosswalkResult.getCrosswalkResults();
                LOGGER.info("Account crosswalk result size : "+requestResult.getSize());
                OrchestraObjectList orchestraObjectList = new OrchestraObjectList();
                List<OrchestraObject> rows = new ArrayList<>();
                try{
                    Adaptation record;
                    while((record = requestResult.nextAdaptation()) != null){
                        OrchestraObject orchestraObject = new OrchestraObject();
                        Map<String, OrchestraContent> jsonFieldsMap = new HashMap<>();
                        jsonFieldsMap.put("SystemId",new OrchestraContent(record.get(CrosswalkResultPaths._Crosswalk._SourceRecord)));
                        jsonFieldsMap.put("MDMAccountId",new OrchestraContent(record.get(CrosswalkResultPaths._Crosswalk._MatchingDetail01_Record)));
                        jsonFieldsMap.put("Score",new OrchestraContent(record.get(CrosswalkResultPaths._Crosswalk._MatchingDetail01_Score)));
                        orchestraObject.setContent(jsonFieldsMap);
                        rows.add(orchestraObject);
                    }
                }finally{
                    requestResult.close();
                }
                orchestraObjectList.setRows(rows);
                try{
                    ObjectMapper mapper = new ObjectMapper();
                    OrchestraRestClient orchestraRestClient = (OrchestraRestClient) SpringContext.getApplicationContext().getBean("orchestraRestClient");
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("updateOrInsert", "true");
                    RestResponse restResponse = null;
                    LOGGER.info("Updating crosswalk results: \n"+mapper.writeValueAsString(orchestraObjectList));
                    restResponse = orchestraRestClient.promote("CMDReference", "Prospect", "root/Account", orchestraObjectList, parameters);
                    if(restResponse.getStatus()!=200 && restResponse.getStatus()!=201){
                        LOGGER.error("Error updating crosswalk results: "+String.valueOf(mapper.writeValueAsString(restResponse.getResponseBody())));
                    }
                }catch(IOException e){
                    LOGGER.error("Error updating crosswalk results",e);
                }
            };
            svc = ProgrammaticService.createForSession(aContext.getSession(), Repository.getDefault().lookupHome(HomeKey.forBranchName("CMDReference")));
            result = null;
            result = svc.execute(procedure);
            if (result == null || result.hasFailed()) {
                LOGGER.info("Account execute crosswalk Procedure failed");
            } else {
                LOGGER.info("Account execute crosswalk Procedure successful");
            }
            procedure = procedureContext -> {
                AdaptationTable table = Repository.getDefault().lookupHome(HomeKey.forBranchName("CMDReference")).findAdaptationOrNull(AdaptationName.forName("Prospect")).getTable(Paths._Address.getPathInSchema());
                AdaptationTable targetTable = Repository.getDefault().lookupHome(HomeKey.forBranchName("CMDReference")).findAdaptationOrNull(AdaptationName.forName("Account")).getTable(Paths._Address.getPathInSchema());
                TableContext context = new TableContext(table, procedureContext);
                CrosswalkOperations operations = CrosswalkOperationsFactory.getCrosswalkOperations();
                List<AdaptationTable> tableList = new ArrayList<>();
                tableList.add(targetTable);
                CrosswalkExecutionResult crosswalkResult = operations.executeCrosswalk(context,tableList);
                RequestResult requestResult = crosswalkResult.getCrosswalkResults();
                LOGGER.info("Address crosswalk result size : "+requestResult.getSize());
                OrchestraObjectList orchestraObjectList = new OrchestraObjectList();
                List<OrchestraObject> rows = new ArrayList<>();
                try{
                    Adaptation record;
                    while((record = requestResult.nextAdaptation()) != null){
                        OrchestraObject orchestraObject = new OrchestraObject();
                        Map<String, OrchestraContent> jsonFieldsMap = new HashMap<>();
                        jsonFieldsMap.put("SystemId",new OrchestraContent(record.get(CrosswalkResultPaths._Crosswalk._SourceRecord)));
                        jsonFieldsMap.put("MDMAddressId",new OrchestraContent(record.get(CrosswalkResultPaths._Crosswalk._MatchingDetail01_Record)));
                        jsonFieldsMap.put("Score",new OrchestraContent(record.get(CrosswalkResultPaths._Crosswalk._MatchingDetail01_Score)));
                        orchestraObject.setContent(jsonFieldsMap);
                        rows.add(orchestraObject);
                    }
                }finally{
                    requestResult.close();
                }
                orchestraObjectList.setRows(rows);
                try{
                    ObjectMapper mapper = new ObjectMapper();
                    OrchestraRestClient orchestraRestClient = (OrchestraRestClient) SpringContext.getApplicationContext().getBean("orchestraRestClient");
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("updateOrInsert", "true");
                    RestResponse restResponse = null;
                    LOGGER.info("Updating address crosswalk results: \n"+mapper.writeValueAsString(orchestraObjectList));
                    restResponse = orchestraRestClient.promote("CMDReference", "Prospect", "root/Address", orchestraObjectList, parameters);
                    if(restResponse.getStatus()!=200 && restResponse.getStatus()!=201){
                        LOGGER.error("Error updating address crosswalk results: "+String.valueOf(mapper.writeValueAsString(restResponse.getResponseBody())));
                    }
                }catch(IOException e){
                    LOGGER.error("Error updating address crosswalk results",e);
                }
            };
            svc = ProgrammaticService.createForSession(aContext.getSession(), Repository.getDefault().lookupHome(HomeKey.forBranchName("CMDReference")));
            result = null;
            result = svc.execute(procedure);
            if (result == null || result.hasFailed()) {
                LOGGER.info("Address execute crosswalk Procedure failed");
            } else {
                LOGGER.info("Address execute crosswalk Procedure successful");
            }
           /* String urlSfdcDs = aWriter.getURLForSelection(
                    Repository.getDefault().lookupHome(HomeKey.forBranchName("SFDCProspect"))).replaceAll("Spaces","");
            LOGGER.info("Url for sfdc Account datset : "+urlSfdcDs);
            aWriter.addJS("window.location.href='"+urlSfdcDs+"';");*/
        }else{
            String urlEnding = aWriter.getURLForEndingService();
            LOGGER.info("Url for ending service : "+urlEnding);
            aWriter.addJS("window.location.href='"+urlEnding+"';");
        }
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
