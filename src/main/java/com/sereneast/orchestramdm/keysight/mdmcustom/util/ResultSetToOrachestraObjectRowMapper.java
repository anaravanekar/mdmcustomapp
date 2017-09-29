package com.sereneast.orchestramdm.keysight.mdmcustom.util;


import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.AccountJobProperties;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.AddressJobProperties;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.JobProperties;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraContent;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
@Scope("prototype")
public class ResultSetToOrachestraObjectRowMapper implements RowMapper<OrchestraObject> {

    @Autowired
    private AccountJobProperties accountJobProperties;

    @Autowired
    private AddressJobProperties addressJobProperties;

    private String jobName;

    @Override
    public OrchestraObject mapRow(ResultSet rs, int rowNum) throws SQLException {
        JobProperties jobProperties = null;
        if("account".equalsIgnoreCase(jobName)){
            jobProperties = accountJobProperties;
        }else if("address".equalsIgnoreCase(jobName)){
            jobProperties = addressJobProperties;
        }
        OrchestraObject orchestraObject = new OrchestraObject();
        Map<String,OrchestraContent> fields = new HashMap<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int count = metaData.getColumnCount();
        for (int i = 1; i <= count; i++) {
            if(jobProperties.getMapping().containsKey(metaData.getColumnLabel(i).toLowerCase())){
                if("dbPrimaryKey".equalsIgnoreCase(jobProperties.getMapping().get(metaData.getColumnLabel(i).toLowerCase()))){
                    orchestraObject.setDbPrimaryKey(rs.getInt(metaData.getColumnLabel(i)));
                }else {
                    fields.put(jobProperties.getMapping().get(metaData.getColumnLabel(i).toLowerCase()), new OrchestraContent(rs.getObject(metaData.getColumnLabel(i))));
                }
            }
        }
        orchestraObject.setContent(fields);
        return orchestraObject;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}
