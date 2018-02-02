package com.sereneast.orchestramdm.keysight.mdmcustom.util;

import com.orchestranetworks.schema.Path;
import com.orchestranetworks.ui.form.UIFormPaneWriter;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.SpringContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraContent;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObjectListResponse;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObjectResponse;
import com.sereneast.orchestramdm.keysight.mdmcustom.rest.client.OrchestraRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.*;

@Service
@DependsOn("cacheManager")
public class ApplicationCacheUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationCacheUtil.class);

	@Cacheable(cacheNames="mainCache",key="{#root.methodName, #className}")
	public Map<String,Path> getObjectDirectFields(String className) throws IllegalAccessException, ClassNotFoundException {
		Map<String,Path> fields = new HashMap<>();
		Field[] accountPathFields = Class.forName(className).getDeclaredFields();
		for(Field pathField: accountPathFields){
//			LOGGER.debug(String.valueOf(!Modifier.isPrivate(pathField.getModifiers())));
			if(!Modifier.isPrivate(pathField.getModifiers())) {
				Path path = (Path)pathField.get(null);
				String key = path.format().replaceAll("\\.\\/", "");
				if(!key.contains("DaqaMetaData") && !key.contains("MergedTargetRecord") && !key.contains("RelatedAddress") && !key.contains("RelatedBusinessPurpose")
						&& !key.contains("Notes") && !key.contains("AssignedTo") && !key.contains("BatchCode")
						&& !key.contains("Source") && !key.contains("Locale") && !key.contains("LastPublished") && !key.contains("RegistryId") && !key.contains("FirstOperatingUnit")
						&& !(Paths._Address.class.getName().equals(className) && key.contains("AccountName")) && !key.contains("DefaultTaxRegistration")
						&& !key.contains("AccountLocked") && !key.contains("AddressLocked") && !key.contains("ErrorMessage")) {
//					LOGGER.debug(String.valueOf(path));
//					LOGGER.debug(key);
					fields.put(key,path);
				}
			}
		}
		return fields;
	}

	@Cacheable(cacheNames="mainCache",key="{#root.methodName, #tableName, #fieldNameForValue}")
	public List<Map<String,String>> getOptionsToDisplay(String tableName,String fieldNameForValue) {
		int retryCount = 0;
		List<Map<String,String>> resultList = new ArrayList<>();
		OrchestraRestClient orchestraRestClient = (OrchestraRestClient) SpringContext.getApplicationContext().getBean("orchestraRestClient");
		Map<String, String> parameters = new HashMap<>();
		parameters.put("filter", "Display='Y'");
		parameters.put("pageSize", "unbounded");
		OrchestraObjectListResponse orchestraObjectListResponse = null;
		do{
			retryCount++;
			try {
				orchestraObjectListResponse = orchestraRestClient.get("BReference", "Account", "root/"+tableName, parameters);
				if (orchestraObjectListResponse != null && orchestraObjectListResponse.getRows() != null && !orchestraObjectListResponse.getRows().isEmpty()) {
					for (OrchestraObjectResponse orchestraObjectResponse : orchestraObjectListResponse.getRows()) {
						Map<String, OrchestraContent> record = orchestraObjectResponse.getContent();
						Map<String, String> resultItem = new HashMap<>();
						resultItem.put(fieldNameForValue,record.get(fieldNameForValue).getContent().toString());
						resultItem.put("label",record.get("label").getContent()!=null?record.get("label").getContent().toString():"");
						resultList.add(resultItem);
					}
				}
			} catch (Exception e) {
				LOGGER.error("Error getting options to display", e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					LOGGER.error("Error getting options to display", e);
				}
			}
		}while((orchestraObjectListResponse == null || orchestraObjectListResponse.getRows()==null) && retryCount<3);
		return resultList;
	}

	@Cacheable(cacheNames="mainCache",key="{#root.methodName, #dataSpace}")
	public Map<String,Map<String,String>> CountryReferenceFieldsMap(String dataSpace) {
		int retryCount = 0;
		Map<String,Map<String,String>> resultMap = new HashMap<>();
		OrchestraRestClient orchestraRestClient = (OrchestraRestClient) SpringContext.getApplicationContext().getBean("orchestraRestClient");
		Map<String, String> parameters = new HashMap<>();
		parameters.put("pageSize", "unbounded");
		OrchestraObjectListResponse orchestraObjectListResponse = null;
		do{
			retryCount++;
			try {
				orchestraObjectListResponse = orchestraRestClient.get(dataSpace, "Account", "root/CountryReferenceFields", parameters);
				if (orchestraObjectListResponse != null && orchestraObjectListResponse.getRows() != null && !orchestraObjectListResponse.getRows().isEmpty()) {
					for (OrchestraObjectResponse orchestraObjectResponse : orchestraObjectListResponse.getRows()) {
						Map<String, OrchestraContent> record = orchestraObjectResponse.getContent();
						Map<String, String> resultItem = new HashMap<>();
						resultItem.put("CountryCode",record.get("CountryCode").getContent().toString());
						resultItem.put("Country",record.get("Country").getContent().toString());
						resultItem.put("OperatingUnit",record.get("OperatingUnit").getContent().toString());
						resultItem.put("Region",record.get("Region").getContent().toString());
						resultItem.put("ProfileClass",record.get("ProfileClass").getContent().toString());
						resultItem.put("RegimeCode",record.get("RegimeCode").getContent().toString());
						resultMap.put(record.get("CountryCode").getContent().toString(),resultItem);
					}
				}
			} catch (Exception e) {
				LOGGER.error("Error getting options to display", e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					LOGGER.error("Error getting options to display", e);
				}
			}
		}while((orchestraObjectListResponse == null || orchestraObjectListResponse.getRows()==null) && retryCount<3);
		return resultMap;
	}

	@Cacheable(cacheNames="mainCache",key="{#root.methodName, #dataSpace}", unless="#result == null")
	public Map<String,String> getTerritoryTypeMap(String dataSpace) {
		int retryCount = 0;
		Map<String,String> resultMap = new HashMap<>();
		OrchestraRestClient orchestraRestClient = (OrchestraRestClient) SpringContext.getApplicationContext().getBean("orchestraRestClient");
		Map<String, String> parameters = new HashMap<>();
		parameters.put("pageSize", "unbounded");
		OrchestraObjectListResponse orchestraObjectListResponse = null;
		do{
			retryCount++;
			try {
				//state
				orchestraObjectListResponse = orchestraRestClient.get(dataSpace, "Account", "root/State", parameters);
				if (orchestraObjectListResponse != null && orchestraObjectListResponse.getRows() != null && !orchestraObjectListResponse.getRows().isEmpty()) {
					for (OrchestraObjectResponse orchestraObjectResponse : orchestraObjectListResponse.getRows()) {
						Map<String, OrchestraContent> record = orchestraObjectResponse.getContent();
						Map<String, String> resultItem = new HashMap<>();
						resultMap.put(record.get("CountryCode").getContent().toString(),"STATE");
					}
				}
				//province
				orchestraObjectListResponse = orchestraRestClient.get(dataSpace, "Account", "root/Province", parameters);
				if (orchestraObjectListResponse != null && orchestraObjectListResponse.getRows() != null && !orchestraObjectListResponse.getRows().isEmpty()) {
					for (OrchestraObjectResponse orchestraObjectResponse : orchestraObjectListResponse.getRows()) {
						Map<String, OrchestraContent> record = orchestraObjectResponse.getContent();
						Map<String, String> resultItem = new HashMap<>();
						resultMap.put(record.get("CountryCode").getContent().toString(),"PROVINCE");
					}
				}
			} catch (Exception e) {
				LOGGER.error("Error getting state/province options", e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					LOGGER.error("Error getting state/province options", e);
				}
			}
		}while((orchestraObjectListResponse == null || orchestraObjectListResponse.getRows()==null) && retryCount<3);
		return resultMap;
	}

	@Cacheable(cacheNames="mainCache",key="{#root.methodName, #dataSpace, #countryCode}", unless="#result == null")
	public List<Map<String,String>> getStateOptions(String dataSpace,String countryCode) {
		int retryCount = 0;
		List<Map<String,String>> options = new ArrayList<>();
		OrchestraRestClient orchestraRestClient = (OrchestraRestClient) SpringContext.getApplicationContext().getBean("orchestraRestClient");
		Map<String, String> parameters = new HashMap<>();
		parameters.put("pageSize", "unbounded");
		parameters.put("filter", "CountryCode='"+countryCode+"'");
		OrchestraObjectListResponse orchestraObjectListResponse = null;
		do{
			retryCount++;
			try {
				orchestraObjectListResponse = orchestraRestClient.get(dataSpace, "Account", "root/State", parameters);
				if (orchestraObjectListResponse != null && orchestraObjectListResponse.getRows() != null && !orchestraObjectListResponse.getRows().isEmpty()) {
					for (OrchestraObjectResponse orchestraObjectResponse : orchestraObjectListResponse.getRows()) {
						Map<String, OrchestraContent> record = orchestraObjectResponse.getContent();
						Map<String, String> resultItem = new HashMap<>();
						resultItem.put("Option",record.get("State").getContent().toString());
						resultItem.put("OptionValue",record.get("StateCode").getContent().toString());
						options.add(resultItem);
					}
				}
			} catch (Exception e) {
				LOGGER.error("Error getting State options to display", e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					LOGGER.error("Error getting State options to display", e);
				}
			}
		}while((orchestraObjectListResponse == null || orchestraObjectListResponse.getRows()==null) && retryCount<3);
		if(options!=null && options.isEmpty()){
			options = null;
		}
		return options;
	}

	@Cacheable(cacheNames="mainCache",key="{#root.methodName, #dataSpace, #countryCode}", unless="#result == null")
	public List<Map<String,String>> getProvinceOptions(String dataSpace,String countryCode) {
		int retryCount = 0;
		List<Map<String,String>> options = new ArrayList<>();
		OrchestraRestClient orchestraRestClient = (OrchestraRestClient) SpringContext.getApplicationContext().getBean("orchestraRestClient");
		Map<String, String> parameters = new HashMap<>();
		parameters.put("pageSize", "unbounded");
		parameters.put("filter", "CountryCode='"+countryCode+"'");
		OrchestraObjectListResponse orchestraObjectListResponse = null;
		do{
			retryCount++;
			try {
				orchestraObjectListResponse = orchestraRestClient.get(dataSpace, "Account", "root/Province", parameters);
				if (orchestraObjectListResponse != null && orchestraObjectListResponse.getRows() != null && !orchestraObjectListResponse.getRows().isEmpty()) {
					for (OrchestraObjectResponse orchestraObjectResponse : orchestraObjectListResponse.getRows()) {
						Map<String, OrchestraContent> record = orchestraObjectResponse.getContent();
						Map<String, String> resultItem = new HashMap<>();
						resultItem.put("Option",record.get("Province").getContent().toString());
						resultItem.put("OptionValue",record.get("ProvinceCode").getContent().toString());
						options.add(resultItem);
					}
				}
			} catch (Exception e) {
				LOGGER.error("Error getting Province options to display", e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					LOGGER.error("Error getting Province options to display", e);
				}
			}
		}while((orchestraObjectListResponse == null || orchestraObjectListResponse.getRows()==null) && retryCount<3);
		if(options!=null && options.isEmpty()){
			options = null;
		}
		return options;
	}

	@Cacheable(cacheNames="mainCache",key="{#root.methodName, #dataSpace, #countryCode, #territoryType}", unless="#result == null")
	public HashSet<String> getOptionsList(String dataSpace,String countryCode,String territoryType) {
		int retryCount = 0;
		HashSet<String> options = new HashSet<>();
		OrchestraRestClient orchestraRestClient = (OrchestraRestClient) SpringContext.getApplicationContext().getBean("orchestraRestClient");
		Map<String, String> parameters = new HashMap<>();
		parameters.put("pageSize", "unbounded");
		parameters.put("filter", "CountryCode='"+countryCode+"'");
		OrchestraObjectListResponse orchestraObjectListResponse = null;
		do{
			retryCount++;
			try {
				if("STATE".equalsIgnoreCase(territoryType)) {
					orchestraObjectListResponse = orchestraRestClient.get(dataSpace, "Account", "root/State", parameters);
				}else if("PROVINCE".equalsIgnoreCase(territoryType)){
					orchestraObjectListResponse = orchestraRestClient.get(dataSpace, "Account", "root/Province", parameters);
				}
				if (orchestraObjectListResponse != null && orchestraObjectListResponse.getRows() != null && !orchestraObjectListResponse.getRows().isEmpty()) {
					for (OrchestraObjectResponse orchestraObjectResponse : orchestraObjectListResponse.getRows()) {
						Map<String, OrchestraContent> record = orchestraObjectResponse.getContent();
						if("STATE".equalsIgnoreCase(territoryType)) {
							options.add(record.get("StateCode").getContent().toString());
						}else if("PROVINCE".equalsIgnoreCase(territoryType)){
							options.add(record.get("ProvinceCode").getContent().toString());
						}
					}
				}
			} catch (Exception e) {
				LOGGER.error("Error getting State options to display", e);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					LOGGER.error("Error getting State options to display", e);
				}
			}
		}while((orchestraObjectListResponse == null || orchestraObjectListResponse.getRows()==null) && retryCount<3);
		if(options!=null && options.isEmpty()){
			options = null;
		}
		return options;
	}

	@CacheEvict(value="mainCache",allEntries=true)
	public void evictAllCacheEntries(){
		LOGGER.debug("Cache evicted at "+Instant.now().toString());
	}

	@Cacheable(cacheNames="mainCache",key="{#root.methodName, #className}")
	public Map<String,String> getPrefixedPaths(String className, UIFormPaneWriter writer) throws IllegalAccessException, ClassNotFoundException {
		Map<String,String> perefixedPaths = new HashMap<>();
		Field[] accountPathFields = Class.forName(className).getDeclaredFields();
		for(Field pathField: accountPathFields){
			if(!Modifier.isPrivate(pathField.getModifiers())) {
				Path path = (Path)pathField.get(null);
				String key = path.format().replaceAll("\\.\\/", "");
				if(!key.contains("DaqaMetaData") && !key.contains("MergedTargetRecord") && !key.contains("RelatedAddress") && !key.contains("RelatedBusinessPurpose")){
					perefixedPaths.put(key,writer.getPrefixedPath(path).format());
				}
			}
		}
		return perefixedPaths;
	}
}
