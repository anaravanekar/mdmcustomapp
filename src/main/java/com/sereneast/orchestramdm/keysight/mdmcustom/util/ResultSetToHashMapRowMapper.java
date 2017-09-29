package com.sereneast.orchestramdm.keysight.mdmcustom.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

public class ResultSetToHashMapRowMapper implements RowMapper<Map<String,Object>> {

	@Override
	public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
		Map<String,Object> item = new LinkedHashMap<String, Object>();
		ResultSetMetaData metaData = rs.getMetaData();
		int count = metaData.getColumnCount();
		List<Map<String,String>> columns = new ArrayList<Map<String,String>>();
		for (int i = 1; i <= count; i++) {
			Map<String,String> column = new HashMap<>();
			column.put("data",metaData.getColumnLabel(i));
			columns.add(column);
			Object value = rs.getObject(metaData.getColumnLabel(i));
			SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
			SimpleDateFormat sdf2 = new SimpleDateFormat( "yyyy-MM-dd" );
			if("TIMESTAMP".equals(metaData.getColumnTypeName(i))){
				Timestamp timestamp = rs.getTimestamp(metaData.getColumnLabel(i));
				value = timestamp!=null?sdf.format(new Date(timestamp.getTime())):null;
			}else if("DATE".equals(metaData.getColumnTypeName(i))){
				value = value!=null?sdf2.format(value):null;
			}
		   item.put(metaData.getColumnLabel(i), value);
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
//			System.out.println("\nColumns: " + mapper.writeValueAsString(columns));
		}catch(Exception e){
			e.printStackTrace();
		}
		columns = new ArrayList<Map<String,String>>();
		return item;
	}
}