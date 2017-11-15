package com.orchestranetworks.account.ui.form;

import com.onwbp.base.text.UserMessageString;
import com.orchestranetworks.ui.UIButtonSpecJSAction;
import com.orchestranetworks.ui.form.UIFormContext;
import com.orchestranetworks.ui.form.UIFormPane;
import com.orchestranetworks.ui.form.UIFormPaneWriter;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.SpringContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.RestProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.sereneast.orchestramdm.keysight.mdmcustom.Paths._Account.*;
import static com.sereneast.orchestramdm.keysight.mdmcustom.Paths._Address._LastPublished;

public class AccountPane implements UIFormPane {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountPane.class);
	public static final String CELL_STYLE_LEFT = "width:25%;  vertical-align:top;";
	public static final String CELL_STYLE_RIGHT = "width:25%; vertical-align:top;text-align:right;";
	public static final String CELL_STYLE_LEFT_HALF = "width:12%;  vertical-align:top;";
	public static final String CELL_STYLE_RIGHT_HALF = "width:12%; vertical-align:top;text-align:right;";
	public static final String TABLE_STYLE = "width:100%";

	@Override
	public void writePane(UIFormPaneWriter writer, UIFormContext context) {
		String currentUserId = context.getSession().getUserReference().getUserId();
		String openedByUser = context.getValueContext()!=null && context.getValueContext().getValue(Paths._Account._AssignedTo)!=null?context.getValueContext().getValue(Paths._Account._AssignedTo).toString():null;
		LOGGER.debug("currentUsereId:"+currentUserId);
		LOGGER.debug("openedByUser:"+openedByUser);
		if(StringUtils.isNotBlank(openedByUser) && !currentUserId.equalsIgnoreCase(openedByUser)) {
			writer.add("<div");writer.addSafeAttribute("style", "margin-left: 5px;");writer.add(">");writer.add("<b><font color=\"red\">Note: This record is currently being edited by " +openedByUser+". Any changes made cannot be saved.</font></b>");writer.add("</div>");
		}

		writer.add("<table width=\"50%\" >");

		if(StringUtils.isNotBlank(openedByUser) && !currentUserId.equalsIgnoreCase(openedByUser)) {
			UserMessageString buttonLabel = new UserMessageString();
			buttonLabel.setString(Locale.ENGLISH,"Save Assigned To");
			String mdmdAccountId = String.valueOf(context.getCurrentRecord().get(_MDMAccountId));
			String dataSpace = context.getCurrentRecord().getHome().getKey().format();
			writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_AssignedTo);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_AssignedTo);writer.add("</td>");
			writer.add("<td colspan=\"2\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_LEFT + "\"><font color=\"#606060\">");writer.addButtonJavaScript(new UIButtonSpecJSAction(buttonLabel,"saveAssignment('"+dataSpace+"',ebx_form_getValue(\""+writer.getPrefixedPath(_AssignedTo).format()+"\"),'account',"+mdmdAccountId+")"));writer.add("</td>");writer.add("</tr>");
		}else{
			writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_AssignedTo);writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_AssignedTo);writer.add("</td></tr>");
		}

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_MDMAccountId);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_MDMAccountId);writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_RegistryId);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_RegistryId);writer.add("</td>");writer.add("</tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_AccountName);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_AccountName);writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_CustomerType);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_CustomerType);writer.add("</td>");writer.add("</tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_NLSLanguageCode);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_NLSLanguageCode);writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_NameLocalLanguage);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_NameLocalLanguage);writer.add("</td>");writer.add("</tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_NamePronunciation);writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_NamePronunciation);writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_Country);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_Country);writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_AccountType);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_AccountType);writer.add("</td>");writer.add("</tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_Status);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_Status);writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_Published);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_Published);writer.add("</td>");writer.add("</tr>");

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date lastPublishedDate = !context.isCreatingRecord() && context.getCurrentRecord()!=null?context.getCurrentRecord().getDate(_LastPublished):null;
		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_ProfileClass);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_ProfileClass);writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_LastPublished);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");if(lastPublishedDate!=null){writer.add(sdf.format(lastPublishedDate));}writer.add("</td>");writer.add("</tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_Classification);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_Classification);writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_ISGClassification);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_ISGClassification);writer.add("</td>");writer.add("</tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_CustomerScreening);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_CustomerScreening);writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_CustomerCategory);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_CustomerCategory);writer.add("</td>");writer.add("</tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_PaymentReceiptMethod);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_PaymentReceiptMethod);writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_PrimaryPayment);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_PrimaryPayment);writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_PaymentStartDate);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_PaymentStartDate);writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_PaymentEndDate);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_PaymentEndDate);writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_ParentParty);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_ParentParty);writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_SalesChannel);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_SalesChannel);writer.add("</td>");writer.add("</tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_TaxpayerId);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_TaxpayerId);writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_TaxRegistrationNumber);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_TaxRegistrationNumber);writer.add("</td>");writer.add("</tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_GroupingId);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_GroupingId);writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_GroupingDescription);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_GroupingDescription);writer.add("</td>");writer.add("</tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_EmgLastTrans);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_EmgLastTrans);writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_Reference);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_Reference);writer.add("</td>");writer.add("</tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_Notes);writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_Notes);writer.add("</td></tr>");
		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_Region);writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_Region);writer.add("</td></tr>");
		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_RelatedAddress);writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_RelatedAddress);writer.add("</td></tr>");
		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_InternalAccountId);writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_InternalAccountId);writer.add("</td></tr>");
		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_SystemName);writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_SystemName);writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_SystemId);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_SystemId);writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_RMTId);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_RMTId);writer.add("</td>");writer.add("</tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_BatchCode);writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_BatchCode);writer.add("</td></tr>");
		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_MergedTargetRecord);writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_MergedTargetRecord);writer.add("</td></tr>");

		writer.add("</table>");

		RestProperties restProperties = (RestProperties) SpringContext.getApplicationContext().getBean("restProperties");
		String protocol = "true".equals(restProperties.getOrchestra().getSsl())?"https":"http";
		String host = restProperties.getOrchestra().getHost();
		String port = restProperties.getOrchestra().getPort();

/*		writer.addJS("function calculatedFields(countryCode){");
		writer.addJS("console.log('calculatedFields called');");
		writer.addJS("var countryFieldValue=ebx_form_getValue(\""+writer.getPrefixedPath(_Country).format()+"\");");
		writer.addJS("console.log('countryFieldValue='+JSON.stringify(countryFieldValue));");
		writer.addJS("console.log('countryCode passed ='+countryCode);");
		writer.addJS("countryCode=countryFieldValue[0].key;");
		writer.addJS("console.log('countryCode new ='+countryCode);");
		writer.addJS("var xhr = new XMLHttpRequest();");
		writer.addJS("xhr.open('GET', '"+protocol+"://"+host+":"+port+"/mdmcustomapp/calculatedFields/country/BReference/Account/'+countryCode);");
		writer.addJS("xhr.setRequestHeader('Content-Type', 'application/json');");
		writer.addJS("xhr.onload = function() {");
		writer.addJS("if (xhr.status === 200) {");
		writer.addJS("var calculatedFieldsJson = JSON.parse(xhr.responseText);");
		writer.addJS("if(calculatedFieldsJson && calculatedFieldsJson.hasOwnProperty('Region')){");
		writer.addJS("var value = calculatedFieldsJson.Region;");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(_Region).format()).addJS("\", ").addJS(
				"value").addJS(");");
		writer.addJS("}");writer.addJS("else{");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(_Region).format()).addJS("\", ").addJS(
				"null").addJS(");");
		writer.addJS("}");
		writer.addJS("if(calculatedFieldsJson && calculatedFieldsJson.hasOwnProperty('ProfileClass')){");
//		writer.addJS("var value = calculatedFieldsJson.ProfileClass;");
		writer.addJS("var value = {\"key\":calculatedFieldsJson.ProfileClass,\"label\":calculatedFieldsJson.ProfileClass};");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(_ProfileClass).format()).addJS("\", ").addJS(
				"value").addJS(");");
		writer.addJS("}");writer.addJS("else{");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(_ProfileClass).format()).addJS("\", ").addJS(
				"null").addJS(");");
		writer.addJS("}");
		writer.addJS("}");
		writer.addJS("};");
		writer.addJS("xhr.send();");
		writer.addJS("}");*/

		writer.add("<div ");
		writer.addSafeAttribute("id", "divLoading");
		writer.add("></div>");

		writer.addJS("function saveAssignment(dataSpace,newAssignment,table,primaryKey){");
		writer.addJS("var xhr = new XMLHttpRequest();");
		writer.addJS("xhr.open('POST', '"+protocol+"://"+host+":"+port+"/mdmcustomapp/'+table+'/updateAssignment/'+dataSpace+'/'+primaryKey+'/'+newAssignment.key);");
		writer.addJS("xhr.setRequestHeader('Content-Type', 'application/json');");
		writer.addJS("xhr.onload = function() {");
		writer.addJS("if (xhr.status === 200) {");
//		writer.addJS("console.log('update assingment successful');");
		writer.addJS_cr("    document.getElementById(\"divLoading\").classList.remove(\"show\");");
		writer.addJS("}else{");
		writer.addJS_cr("    document.getElementById(\"divLoading\").classList.remove(\"show\");");
		writer.addJS("}");
		writer.addJS("};");
		writer.addJS("xhr.send();");
		writer.addJS_cr("document.getElementById(\"divLoading\").classList.add(\"show\");");
		writer.addJS("}");
	}
}
