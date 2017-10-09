package com.sereneast.orchestramdm.keysight.mdmcustom.util;

import com.orchestranetworks.schema.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

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
				if(!key.contains("DaqaMetaData") && !key.contains("MergedTargetRecord")) {
//					LOGGER.debug(String.valueOf(path));
//					LOGGER.debug(key);
					fields.put(key,path);
				}
			}
		}
		return fields;
	}

}
