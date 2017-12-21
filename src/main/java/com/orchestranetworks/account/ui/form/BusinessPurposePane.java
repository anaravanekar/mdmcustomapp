package com.orchestranetworks.account.ui.form;

import com.orchestranetworks.schema.Path;
import com.orchestranetworks.ui.form.UIFormContext;
import com.orchestranetworks.ui.form.UIFormPane;
import com.orchestranetworks.ui.form.UIFormPaneWriter;
import com.sereneast.orchestramdm.keysight.mdmcustom.SpringContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.RestProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.sereneast.orchestramdm.keysight.mdmcustom.Paths._BusinessPurpose.*;

public class BusinessPurposePane implements UIFormPane {
	private static final Logger LOGGER = LoggerFactory.getLogger(BusinessPurposePane.class);
	public static final String CELL_STYLE_LEFT = "width:25%;  vertical-align:top;";
	public static final String CELL_STYLE_RIGHT = "width:25%; vertical-align:top;text-align:right;";
	public static final String TABLE_STYLE = "width:100%";

	@Override
	public void writePane(UIFormPaneWriter writer, UIFormContext context) {

		writer.add("<table width=\"50%\" >");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_MDMPurposeId);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_MDMPurposeId);
		writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_MDMAddressId);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_MDMAddressId);
		writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_SystemId);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_SystemId);
		writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_SystemName);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_SystemName);
		writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_Primary);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_Primary);
		writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_BusinessPurpose);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_BusinessPurpose);
		writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_OperatingUnit);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_OperatingUnit);
		writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_Status);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_Status);
		writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_Location);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_Location);
		writer.add("</td></tr>");

		if(!context.isCreatingRecord()) {
			writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.addLabel(Path.parse("./BatchCode"));
			writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
			writer.addWidget(Path.parse("./BatchCode"));
			writer.add("</td></tr>");
		}

		writer.add("</table>");

		RestProperties restProperties = (RestProperties) SpringContext.getApplicationContext().getBean("restProperties");
		String protocol = "true".equals(restProperties.getOrchestra().getSsl())?"https":"http";
		String host = restProperties.getOrchestra().getHost();
		String port = restProperties.getOrchestra().getPort();

		writer.addJS("function populateLocation(mdmAddressId){");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(_Location).format()).addJS("\", ").addJS("mdmAddressId.key);");
		writer.addJS("}");

		writer.addJS("function checkIfOuExists(operatingUnit){");
		String dataSpace= context.getCurrentDataSet().getHome().getKey().format();
		writer.addJS("var addressIdObj = ebx_form_getValue(\"").addJS(writer.getPrefixedPath(_MDMAddressId).format()).addJS("\");");
		writer.addJS("if(addressIdObj && operatingUnit){");
		writer.addJS("var xhr = new XMLHttpRequest();");
		writer.addJS("xhr.open('GET', '"+protocol+"://"+host+":"+port+"/mdmcustomapp/checkIfOuExists/"+dataSpace+"/Account/'+addressIdObj.key+'/'+operatingUnit.key);");
		writer.addJS("xhr.setRequestHeader('Content-Type', 'application/json');");
		writer.addJS("xhr.onload = function() {");
		writer.addJS("if (xhr.status === 200) {");
		//writer.addJS("console.log(JSON.parse(xhr.responseText));");
		writer.addJS("if(JSON.parse(xhr.responseText).result===false){alert('Operating Unit '+operatingUnit.key+' does not exist on address.');}");
		//writer.addJS("console.log('ou='+ebx_form_getValue(\""+writer.getPrefixedPath(_OperatingUnit).format()+"\"));");
		writer.addJS("}else{");
		writer.addJS("alert('An error occurred while checking Operating Unit in Address. Please contact your administrator.');");
		writer.addJS("}");
		writer.addJS("};");
		writer.addJS("xhr.send();");
		writer.addJS("}}");
	}
}
