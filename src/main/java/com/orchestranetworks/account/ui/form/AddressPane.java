 package com.orchestranetworks.account.ui.form;

import com.orchestranetworks.schema.Path;
import com.orchestranetworks.ui.form.UIFormContext;
import com.orchestranetworks.ui.form.UIFormPane;
import com.orchestranetworks.ui.form.UIFormPaneWriter;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.exception.ApplicationRuntimeException;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.AppUtil;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ApplicationCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AddressPane implements UIFormPane {
	private static final Logger LOGGER = LoggerFactory.getLogger(AddressPane.class);
	
	@Override
	public void writePane(UIFormPaneWriter writer, UIFormContext context) {
		try {
			ApplicationCacheUtil applicationCacheUtil = new ApplicationCacheUtil();
			Map<String, Path> addressFields = applicationCacheUtil.getObjectDirectFields(Paths._Address.class.getName());
			if(addressFields!=null){
				for(String key:addressFields.keySet()){
					Path fieldPath = addressFields.get(key);
					writer.startTableFormRow();
					writer.addFormRow(fieldPath);
					writer.endTableFormRow();
				}
			}
			writer.addJS("function calculatedFields(countryCode){");
				//writer.addJS("alert('calculatedFields called');");
				writer.addJS("var xhr = new XMLHttpRequest();");
			String port = ((Map)((Map) AppUtil.getAllPropertiesMap().get("keysight")).get("orchestraRest")).get("port").toString();//"http://localhost:8080/ebx-dataservices/rest/data/v1";
			writer.addJS("xhr.open('GET', 'http://localhost:"+port+"/mdmcustomapp/calculatedFields/country/BReference/Account/'+countryCode);");
				writer.addJS("xhr.setRequestHeader('Content-Type', 'application/json');");
					writer.addJS("xhr.onload = function() {");
					writer.addJS("if (xhr.status === 200) {");
						writer.addJS("var calculatedFieldsJson = JSON.parse(xhr.responseText);");
						writer.addJS("if(calculatedFieldsJson && calculatedFieldsJson.hasOwnProperty('OperatingUnit')){");
							writer.addJS("var value = calculatedFieldsJson.OperatingUnit;");
							writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(Paths._Address._OperatingUnit).format()).addJS("\", ").addJS(
								"value").addJS(");");
						writer.addJS("}");
						writer.addJS("if(calculatedFieldsJson && calculatedFieldsJson.hasOwnProperty('TaxRegimeCode')){");
							writer.addJS("var value = calculatedFieldsJson.TaxRegimeCode;");
							writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(Paths._Address._TaxRegimeCode).format()).addJS("\", ").addJS(
									"value").addJS(");");
						writer.addJS("}");
						writer.addJS("if(calculatedFieldsJson && calculatedFieldsJson.hasOwnProperty('AddressState')){");
							writer.addJS("var value = calculatedFieldsJson.AddressState;");
							writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(Paths._Address._AddressState).format()).addJS("\", ").addJS(
									"value").addJS(");");
						writer.addJS("}");
						writer.addJS("if(calculatedFieldsJson && calculatedFieldsJson.hasOwnProperty('Province')){");
							writer.addJS("var value = calculatedFieldsJson.Province;");
							writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(Paths._Address._Province).format()).addJS("\", ").addJS(
									"value").addJS(");");
						writer.addJS("}");
					writer.addJS("}");
				writer.addJS("};");
				writer.addJS("xhr.send();");
			writer.addJS("}");
		}catch(IllegalAccessException | ClassNotFoundException e){
			LOGGER.error("Error generating address pane",e);
			throw new ApplicationRuntimeException("Error generating address pane",e);
		}
}
}
