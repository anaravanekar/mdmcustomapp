package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.orchestranetworks.service.OperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

public class GenericLengthConstraint {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericLengthConstraint.class);

    public String validateByteLength(String value,int bytes) {
        if(value!=null){
            try {
                final byte[] utf8Bytes = value.getBytes("UTF-8");
                if(utf8Bytes.length>bytes){
                    return "Length exceeds "+bytes+" byte limit";
                }
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("Error while getting byte length of string",e);
            }
        }
        return null;
    }
}
