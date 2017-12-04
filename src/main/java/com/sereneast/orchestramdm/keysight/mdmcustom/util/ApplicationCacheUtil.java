package com.sereneast.orchestramdm.keysight.mdmcustom.util;

import com.orchestranetworks.schema.Path;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.SpringContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraContent;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObjectListResponse;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObjectResponse;
import com.sereneast.orchestramdm.keysight.mdmcustom.rest.client.OrchestraRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
						&& !key.contains("Source") && !key.contains("Locale") && !key.contains("LastPublished") && !key.contains("RegistryId")
						&& !(Paths._Address.class.getName().equals(className) && key.contains("AccountName"))) {
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

}
