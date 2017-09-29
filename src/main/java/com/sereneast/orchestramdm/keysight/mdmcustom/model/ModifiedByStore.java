package com.sereneast.orchestramdm.keysight.mdmcustom.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ModifiedByStore {
    private static Map<String,String> recordModifiedBy;
    private static Integer lock=100;
    private static final Logger LOGGER = LoggerFactory.getLogger(ModifiedByStore.class);

    public static String processRecordModifiedBy(String operation,String recordId, String userId) {
        synchronized (lock) {
            LOGGER.info("\n\noperation: "+operation+" recordId: "+recordId+" userId: "+userId);
            LOGGER.info("\n\nrecordModifiedBy: "+recordModifiedBy);
            if(recordModifiedBy==null){
                recordModifiedBy = new HashMap<String,String>();
            }
            LOGGER.info("\n\nrecordModifiedByString: "+recordModifiedBy.toString());
            if("GET".equals(operation)){
                return recordModifiedBy.get(recordId);
            }else if("PUT".equals(operation)) {
                if (recordModifiedBy.get(recordId) != null) {
                    return recordModifiedBy.get(recordId);
                } else {
                    recordModifiedBy.put(recordId,userId);
                }
            }else if("REMOVE".equals(operation)){
                recordModifiedBy.remove(recordId);
                return null;
            }
            return null;
        }
    }
}
