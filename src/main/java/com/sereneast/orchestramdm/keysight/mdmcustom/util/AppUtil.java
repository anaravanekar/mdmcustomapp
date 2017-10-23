/**
 * 
 */
package com.sereneast.orchestramdm.keysight.mdmcustom.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.gson.Gson;
import com.sereneast.orchestramdm.keysight.mdmcustom.exception.ApplicationRuntimeException;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.pagination.PaginationCriteria;


/**
 * The Class AppUtil.
 *
 *
 */
public class AppUtil {
	
	/**
	 * Checks if is collection empty.
	 *
	 * @param collection the collection
	 * @return true, if is collection empty
	 */
	private static boolean isCollectionEmpty(Collection<?> collection) {
		if (collection == null || collection.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if is object empty.
	 *
	 * @param object the object
	 * @return true, if is object empty
	 */
	public static boolean isObjectEmpty(Object object) {
		if(object == null) return true;
		else if(object instanceof String) {
			if (((String)object).trim().length() == 0) {
				return true;
			}
		} else if(object instanceof Collection) {
			return isCollectionEmpty((Collection<?>)object);
		}
		return false;
	}
	
	/**
	 * Gets the bean to json string.
	 *
	 * @param beanClass the bean class
	 * @return the bean to json string
	 */
	public static String getBeanToJsonString(Object beanClass) {
		return new Gson().toJson(beanClass);
	}
	
	/**
	 * Gets the bean to json string.
	 *
	 * @param beanClasses the bean classes
	 * @return the bean to json string
	 */
	public static String getBeanToJsonString(Object... beanClasses) {
		StringBuilder stringBuilder = new StringBuilder();
		for (Object beanClass : beanClasses) {
			stringBuilder.append(getBeanToJsonString(beanClass));
			stringBuilder.append(", ");
		}
		return stringBuilder.toString();
	}

	/**
	 * Concatenate.
	 *
	 * @param listOfItems the list of items
	 * @param separator the separator
	 * @return the string
	 */
	public String concatenate(List<String> listOfItems, String separator) {
		StringBuilder sb = new StringBuilder();
		Iterator<String> stit = listOfItems.iterator();
		
		while (stit.hasNext()) {
			sb.append(stit.next());
			if(stit.hasNext()) {
				sb.append(separator);
			}
		}
		
		return sb.toString();		
	}
	
	/**
	 * Builds the paginated query.
	 *
	 * @param baseQuery the base query
	 * @param paginationCriteria the pagination criteria
	 * @return the string
	 */
	public static String buildPaginatedQuery(String baseQuery, PaginationCriteria paginationCriteria) {
		//String queryTemplate = "SELECT FILTERED_ORDERD_RESULTS.* FROM (SELECT BASEINFO.* FROM ( %s ) BASEINFO %s  %s ) FILTERED_ORDERD_RESULTS LIMIT %d, %d";
		StringBuilder sb = new StringBuilder("SELECT FILTERED_ORDERD_RESULTS.* FROM (SELECT BASEINFO.* FROM ( #BASE_QUERY# ) BASEINFO #WHERE_CLAUSE#  #ORDER_CLASUE# ) FILTERED_ORDERD_RESULTS LIMIT #PAGE_NUMBER#, #PAGE_SIZE#");
		String finalQuery = null;
		if(!AppUtil.isObjectEmpty(paginationCriteria)) {
			finalQuery = sb.toString().replaceAll("#BASE_QUERY#", baseQuery)
							.replaceAll("#WHERE_CLAUSE#", ((AppUtil.isObjectEmpty(paginationCriteria.getFilterByClause())) ? "" : " WHERE ") + paginationCriteria.getFilterByClause())
								.replaceAll("#ORDER_CLASUE#", paginationCriteria.getOrderByClause())
									.replaceAll("#PAGE_NUMBER#", paginationCriteria.getPageNumber().toString())
										.replaceAll("#PAGE_SIZE#", paginationCriteria.getPageSize().toString());
		}
		return (null == finalQuery) ?  baseQuery : finalQuery;
	}

	public static String buildPaginatedQueryOracle(String baseQuery, PaginationCriteria paginationCriteria) {
		//String queryTemplate = "SELECT FILTERED_ORDERD_RESULTS.* FROM (SELECT BASEINFO.* FROM ( %s ) BASEINFO %s  %s ) FILTERED_ORDERD_RESULTS LIMIT %d, %d";
		StringBuilder sb = new StringBuilder(baseQuery);
		String finalQuery = null;
		Integer pageNumber = paginationCriteria.getPageNumber();
		Integer pageSize = paginationCriteria.getPageSize();
		if(!AppUtil.isObjectEmpty(paginationCriteria)) {
			finalQuery = sb.toString()
					.replaceAll("#WHERE_CLAUSE#", ((AppUtil.isObjectEmpty(paginationCriteria.getFilterByClause())) ? "" : " WHERE ") + paginationCriteria.getFilterByClause())
					.replaceAll("#ORDER_CLASUE#", paginationCriteria.getOrderByClause())
					.replaceAll("#START#", String.valueOf(pageNumber + 1))
					.replaceAll("#LIMIT#", String.valueOf(pageNumber + 1 + pageSize));
		}
		return (null == finalQuery) ?  baseQuery : finalQuery;
	}

	public static Map<String,Object> getAllPropertiesMap(){
		ObjectMapper yamlmapper = new ObjectMapper(new YAMLFactory());
		ClassLoader classLoader = AppUtil.class.getClassLoader();
		File file = new File(classLoader.getResource("application.yml").getFile());
		try {
			Map<String,Object> properties = yamlmapper.readValue(file,Map.class);
			return properties;
		} catch (IOException e) {
			throw new ApplicationRuntimeException("Error reading properties from application.yml",e);
		}
	}

	public static Object getMailProperty(String key){
		ClassLoader classLoader = AppUtil.class.getClassLoader();
		File file = new File(classLoader.getResource("mail.properties").getFile());
		String rootPath = file.getPath();
		String mailConfigPath = rootPath;
		Properties mailProperties = new Properties();
		try {
			mailProperties.load(new FileInputStream(mailConfigPath));
		} catch (IOException e) {
			throw new ApplicationRuntimeException("Error configuring email",e);
		}
		return mailProperties.get(key);
	}
}
