package com.sereneast.orchestramdm.keysight.mdmcustom.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.ApplicationProperties;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ResultSetToHashMapRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private NamedParameterJdbcTemplate oracleDbNamedParameterJdbcTemplate;

    @RequestMapping(path="/accountsses", method= RequestMethod.GET)
    public String getAllEmployees(){
        String query = "select * from ( select * from EBX_MDM_ACCOUNT order by ACCOUNTNAME) where ROWNUM <= 1000";
        List<Map<String, Object>> resultList = null;
        String result = "[]";
        try{
            resultList = oracleDbNamedParameterJdbcTemplate.query(query,new ResultSetToHashMapRowMapper());
            Map<String,Object> data = new HashMap<String,Object>();
            data.put("data",resultList);
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            result = mapper.writeValueAsString(resultList);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("",e);
        }
        return result;
    }
}
