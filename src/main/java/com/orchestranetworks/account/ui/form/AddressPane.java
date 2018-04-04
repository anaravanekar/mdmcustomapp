package com.orchestranetworks.account.ui.form;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.RequestResult;
import com.onwbp.base.text.UserMessageString;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.ui.UIButtonSpecJSAction;
import com.orchestranetworks.ui.form.UIFormContext;
import com.orchestranetworks.ui.form.UIFormPane;
import com.orchestranetworks.ui.form.UIFormPaneWriter;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.SpringContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.RestProperties;
import com.sereneast.orchestramdm.keysight.mdmcustom.exception.ApplicationRuntimeException;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ApplicationCacheUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.sereneast.orchestramdm.keysight.mdmcustom.Paths._Address.*;

public class AddressPane implements UIFormPane {
	private static final Logger LOGGER = LoggerFactory.getLogger(AddressPane.class);
	public static final String CELL_STYLE_LEFT = "width:25%;  vertical-align:top;";
	public static final String CELL_STYLE_RIGHT = "width:25%; vertical-align:top;text-align:right;";
	public static final String TABLE_STYLE = "width:100%";

	@Override
	public void writePane(UIFormPaneWriter writer, UIFormContext context) {
		String accountLocalName = "";
		if(!context.isCreatingRecord() && context.getCurrentRecord()!=null){
			StringBuilder textToAppend = new StringBuilder();

			String accountName = null;
			if(StringUtils.isNotBlank(context.getCurrentRecord().getString(_MDMAccountId))) {
				/*AdaptationTable accountTable = context.getCurrentDataSet().getTable(Paths._Account.getPathInSchema());
				final RequestResult requestResult = accountTable.createRequestResult(Paths._Account._MDMAccountId.format() + " = " + context.getCurrentRecord().getString(_MDMAccountId));
				if (requestResult != null && !requestResult.isEmpty()) {
					Adaptation record = requestResult.nextAdaptation();*/
					accountName = context.getCurrentRecord().getString(Path.parse("./MDMAccountName"));//record.getString(Paths._Account._AccountName);
					accountLocalName = context.getCurrentRecord().getString(Path.parse("./MDMNameLocalLanguage"))!=null?context.getCurrentRecord().getString(Path.parse("./MDMNameLocalLanguage")):"";//record.getString(Paths._Account._NameLocalLanguage)!=null?record.getString(Paths._Account._NameLocalLanguage):"";
					textToAppend.append(" of ").append(accountName);
//				}
			}
			/*if(StringUtils.isNotBlank(context.getCurrentRecord().getString(_AccountName))){
				textToAppend.append("    ").append(context.getCurrentRecord().getString(_AccountName));
			}*/
			//textToAppend.append(context.getCurrentRecord()!=null?context.getCurrentRecord().getString(_):"");
			/*String internalAccountId = StringUtils.isNotBlank(context.getCurrentRecord().getString(Paths._Account._InternalAccountId))?context.getCurrentRecord().getString(Paths._Account._InternalAccountId):null;
			if(internalAccountId!=null){
				textToAppend.append("    Internal Account Id: ").append(internalAccountId);
			}*/

			if(StringUtils.isNotBlank(textToAppend.toString())) {
				writer.addJS("var textToAppendToHeader=\"" + StringEscapeUtils.escapeEcmaScript(textToAppend.toString()) + "\";");
				writer.addJS("appendToFormHeader(textToAppendToHeader);");
			}
		}

		ApplicationCacheUtil applicationCacheUtil = (ApplicationCacheUtil)SpringContext.getApplicationContext().getBean("applicationCacheUtil");

		//get territorytypes for countries
		Map<String,String> territoryTypeMap = applicationCacheUtil.getTerritoryTypeMap("BReference");
		String territoryTypeMapJsonString = "{}";
		try{
			ObjectMapper mapper = new ObjectMapper();
			territoryTypeMapJsonString = mapper.writeValueAsString(territoryTypeMap);
		}catch (Exception e){
			throw new ApplicationRuntimeException("Error getting territory types for countries");
		}

		//operatingUnit custom html
/*
		StringBuilder operatingUnitSelectBox = new StringBuilder();
		StringBuilder operatingUnitSelectBoxOptions = new StringBuilder();
		operatingUnitSelectBoxOptions.append("<option value=\"\"></option>");
		List<Map<String,String>> operatingUnitValues = applicationCacheUtil.getOptionsToDisplay("OperatingUnit","OperatingUnit");
		if(operatingUnitValues!=null) {
			operatingUnitValues.sort(Comparator.comparing(
					m -> m.get("OperatingUnit"),
					Comparator.nullsLast(Comparator.naturalOrder()))
			);
			boolean isCurrentValueValidForDisplay = false;
			for (Map<String, String> entry : operatingUnitValues) {
				if (!context.isCreatingRecord() && entry.get("OperatingUnit").equals(context.getCurrentRecord().getString(_OperatingUnit))) {
					isCurrentValueValidForDisplay = true;
					operatingUnitSelectBoxOptions.append("<option value=\"" + entry.get("OperatingUnit") + "\" selected>" + entry.get("label") + "</option>");
				} else {
					operatingUnitSelectBoxOptions.append("<option value=\"" + entry.get("OperatingUnit") + "\">" + entry.get("label") + "</option>");
				}
			}
			if (!context.isCreatingRecord() && !isCurrentValueValidForDisplay) {
				operatingUnitSelectBoxOptions.append("<option value=\"" + context.getCurrentRecord().getString(_OperatingUnit) + "\" selected>DO NOT USE: " + context.getCurrentRecord().getString(_OperatingUnit) + "</option>");
			}
			String operatingUnitPrefixedPath = writer.getPrefixedPath(_OperatingUnit).format();
		}
		operatingUnitSelectBox.append("<select id=\"OperatingUnitCustom\" onchange=\"changeDropDownValue('"+writer.getPrefixedPath(_OperatingUnit).format()+"',this.value,this.options.item(this.selectedIndex).text)\">").append(operatingUnitSelectBoxOptions).append("</select>");
*/

		String currentUserId = context.getSession().getUserReference().getUserId();
		String openedByUser = context.getValueContext()!=null && context.getValueContext().getValue(Paths._Address._AssignedTo)!=null?context.getValueContext().getValue(Paths._Address._AssignedTo).toString():null;
		LOGGER.debug("currentUsereId:"+currentUserId);
		LOGGER.debug("openedByUser:"+openedByUser);
		if(StringUtils.isNotBlank(openedByUser) && !currentUserId.equalsIgnoreCase(openedByUser)) {
			writer.add("<div");writer.addSafeAttribute("style", "margin-left: 5px;");writer.add(">");writer.add("<b><font color=\"red\">Note: This record is currently being edited by " +openedByUser+". Any changes made cannot be saved.</font></b>");writer.add("</div>");
		}

		writer.add("<table width=\"50%\" >");

		if(!context.isCreatingRecord() && StringUtils.isNotBlank(openedByUser) && !currentUserId.equalsIgnoreCase(openedByUser)) {
			UserMessageString buttonLabel = new UserMessageString();
			buttonLabel.setString(Locale.ENGLISH,"Save Assigned To");
			String dataSpace = context.getCurrentRecord().getHome().getKey().format();
			String mdmdAddressId = String.valueOf(context.getCurrentRecord().get(_MDMAddressId));
			writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_AssignedTo);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_AssignedTo);//writer.add("</td>");
			writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\">");writer.addButtonJavaScript(new UIButtonSpecJSAction(buttonLabel,"saveAssignment('"+dataSpace+"',ebx_form_getValue(\""+writer.getPrefixedPath(_AssignedTo).format()+"\"),'address',"+mdmdAddressId+")"));writer.add("<font color=\"#606060\"><span style=\"padding-left:50px;\">Account Local Language Name</span></font>");writer.add("</td>");
			writer.add("<td colspan=\"1\" style=\"padding-top:2px;" + CELL_STYLE_LEFT + "\">");writer.add("<span>"+accountLocalName+"</span>");writer.add("</td>");
			writer.add("</tr>");
		}else if(!context.isCreatingRecord()){
			writer.add("<tr>");
			writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.addLabel(_AssignedTo);
			writer.add("</td>");
			writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
			writer.addWidget(_AssignedTo);
			writer.add("</td>");
			writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.add("<span>Account Local Language Name</span>");
			writer.add("</td>");
			writer.add("<td colspan=\"1\" style=\"padding-top:2px;" + CELL_STYLE_LEFT + "\">");
			writer.add("<span>"+accountLocalName+"</span>");
			writer.add("</td>");
			writer.add("</tr>");
		} else {
			writer.add("<tr>");
			writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.addLabel(_AssignedTo);
			writer.add("</td>");
			writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
			writer.addWidget(_AssignedTo);
			writer.add("</td>");
			writer.add("</tr>");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date lastPublishedDate = !context.isCreatingRecord() && context.getCurrentRecord()!=null?context.getCurrentRecord().getDate(_LastPublished):null;
		String published = !context.isCreatingRecord() && context.getCurrentRecord()!=null?context.getCurrentRecord().getString(_Published):null;
		if(!context.isCreatingRecord()) {
			writer.add("<tr>");
			writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.addLabel(_Published);
			writer.add("</td>");
			writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
			writer.addWidget(_Published);
			writer.add("</td>");
			writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.addLabel(_LastPublished);
			writer.add("</td>");
			writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
			if (lastPublishedDate != null) {
				writer.add(sdf.format(lastPublishedDate));
			}
			writer.add("</td>");
			writer.add("</tr>");
		}

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_OperatingUnit);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		//writer.add("<td colspan=\"1\" style=\"padding-left:5px;" + CELL_STYLE_LEFT + "\">");
		//writer.add(operatingUnitSelectBox.toString());writer.add("<div style=\"display:none;\">");writer.addWidget(_OperatingUnit);writer.add("</div>");
		writer.addWidget(_OperatingUnit);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_Status);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_Status);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_RPLCheck);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_RPLCheck);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_Reference);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_Reference);
		writer.add("</td>");
		writer.add("</tr>");

		String systemId = !context.isCreatingRecord() && context.getCurrentRecord()!=null?context.getCurrentRecord().getString(_SystemId):null;
		if(!context.isCreatingRecord()) {
			writer.add("<tr>");
			writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.addLabel(_SystemName);
			writer.add("</td>");
			writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
			writer.addWidget(_SystemName);
			writer.add("</td>");
			writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.addLabel(_SystemId);
			writer.add("</td>");
			writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
			writer.addWidget(_SystemId);
			writer.add("</td>");
			writer.add("</tr>");
		}else{
			writer.add("<tr>");
			writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.addLabel(_SystemName);
			writer.add("</td>");
			writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
			writer.addWidget(_SystemName);
			writer.add("</td>");
			writer.add("</tr>");
		}

		String internalAccountId = !context.isCreatingRecord() && StringUtils.isNotBlank(context.getCurrentRecord().getString(Paths._Address._InternalAccountId))?context.getCurrentRecord().getString(Paths._Address._InternalAccountId):null;
		String internalAddressId = !context.isCreatingRecord() && StringUtils.isNotBlank(context.getCurrentRecord().getString(Paths._Address._InternalAddressId))?context.getCurrentRecord().getString(Paths._Address._InternalAddressId):null;
		if(internalAccountId==null) {
			writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.addLabel(_RMTId);
			writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
			writer.addWidget(_RMTId);
			writer.add("</td>");
			writer.add("</tr>");

			if (!context.isCreatingRecord()) {
				String mdmAccountId = context.getCurrentRecord() != null ? context.getCurrentRecord().getString(_MDMAccountId) : null;
				writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
				writer.addLabel(_MDMAccountId);
				writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
				writer.add("<span>" + mdmAccountId + "</span>");
				writer.add("</td></tr>");
			} else {
				writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
				writer.addLabel(_MDMAccountId);
				writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
				writer.addWidget(_MDMAccountId);
				writer.add("</td></tr>");
			}


			writer.add("<tr>");
			writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.addLabel(_MDMAddressId);
			writer.add("</td>");
			writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
			writer.addWidget(_MDMAddressId);
			writer.add("</td>");
			writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.addLabel(_InternalAddressId);
			writer.add("</td>");
			writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
			if(StringUtils.isBlank(published)){
				writer.addWidget(_InternalAddressId);
			}else{
				writer.add(internalAddressId);
			}
			writer.add("</td>");
			writer.add("</tr>");
		}else{
			writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.addLabel(_RMTId);
			writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
			writer.addWidget(_RMTId);
			writer.add("</td>");
			writer.add("</tr>");


			String mdmAccountId = context.getCurrentRecord() != null ? context.getCurrentRecord().getString(_MDMAccountId) : "";
			writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.addLabel(_InternalAccountId);
			writer.add("</td><td colspan=\"1\" style=\"padding-top:2px; " + CELL_STYLE_LEFT + "\">");
			writer.add("<b>"+internalAccountId+"</b>");
			writer.add("</td>");
			writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.addLabel(_MDMAccountId);
			writer.add("</td><td colspan=\"1\" style=\" "  + CELL_STYLE_LEFT + "\">");
			writer.add("<span>" + mdmAccountId + "</span>");
//			writer.addWidget(_MDMAccountId);
			writer.add("</td>");
			writer.add("</tr>");

			writer.add("<tr>");
			writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.addLabel(_MDMAddressId);
			writer.add("</td>");
			writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
			writer.addWidget(_MDMAddressId);
			writer.add("</td>");
			writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.addLabel(_InternalAddressId);
			writer.add("</td>");
			writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
			if(StringUtils.isBlank(published)){
				writer.addWidget(_InternalAddressId);
			}else{
				writer.add(internalAddressId);
			}
			writer.add("</td>");
			writer.add("</tr>");
		}

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_IdentifyingAddress);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_IdentifyingAddress);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_NLSLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_NLSLanguage);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_Country);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_Country);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_CountryLocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_CountryLocalLanguage);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_AddressLine1);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_AddressLine1);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_AddressLine1LocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_AddressLine1LocalLanguage);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_AddressLine2);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_AddressLine2);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_AddressLine2LocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_AddressLine2LocalLanguage);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_AddressLine3);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_AddressLine3);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_AddressLine3LocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_AddressLine3LocalLanguage);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_AddressLine4);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_AddressLine4);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_AddressLine4LocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_AddressLine4LocalLanguage);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_City);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_City);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_CityLocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_CityLocalLanguage);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_PostalCode);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_PostalCode);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_PostalLocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_PostalLocalLanguage);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_AddressState);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"padding-left:5px;" + CELL_STYLE_LEFT + "\">");
		writer.add("<div id='stateCustomDiv'><select id=\"customStateSelect\" onChange=\"var sv=null;if(this.value){sv=this.value;}ebx_form_setValue('"+writer.getPrefixedPath(_AddressState).format()+"',sv)\"><option value=\"\">[not defined]</option></select></div>");writer.add("<div id='stateStandardDiv' style=\"display:none;\">");writer.addWidget(_AddressState);writer.add("</div>");
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_StateLocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"padding-left:5px;" + CELL_STYLE_LEFT + "\">");
		writer.add("<div id='stateLocalCustomDiv'><input type=\"text\" selectBoxOptions=\"\" id=\"customStateLocalSelect\" oninput=\"changeStateLocalStandard(this.value)\"></div>");writer.add("<div id='stateLocalStandardDiv' style=\"display:none;\">");writer.addWidget(_StateLocalLanguage);writer.add("</div>");
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_Province);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"padding-left:5px;" + CELL_STYLE_LEFT + "\">");
		writer.add("<div id='provinceCustomDiv'><select id=\"customProvinceSelect\" onChange=\"var pv=null;if(this.value){pv=this.value;}ebx_form_setValue('"+writer.getPrefixedPath(_Province).format()+"',pv)\"><option value=\"\">[not defined]</option></select></div>");writer.add("<div id='provinceStandardDiv' style=\"display:none;\">");writer.addWidget(_Province);writer.add("</div>");
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_ProvinceLocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"padding-left:5px;" + CELL_STYLE_LEFT + "\">");
		writer.add("<div id='provinceLocalCustomDiv'><input type=\"text\" selectBoxOptions=\"\" id=\"customProvinceLocalSelect\" oninput=\"changeProvinceLocalStandard(this.value)\"></div>");writer.add("<div id='provinceLocalStandardDiv' style=\"display:none;\">");writer.addWidget(_ProvinceLocalLanguage);writer.add("</div>");
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_County);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_County);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_CountyLocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_CountyLocalLanguage);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_SendAcknowledgement);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_SendAcknowledgement);
		writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_InvoiceCopies);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_InvoiceCopies);
		writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_RelatedBusinessPurpose);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_RelatedBusinessPurpose);
		writer.add("</td></tr>");

/*		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_Location);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_Location);
		writer.add("</td></tr>");*/

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_BillToLocation);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_BillToLocation);
		writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_RevenueRecognition);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_RevenueRecognition);
		writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_OrgSegment);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_OrgSegment);
		writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_SubSegmet);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_SubSegmet);
		writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_DemandClass);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_DemandClass);
		writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_ContextValue);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_ContextValue);
		writer.add("</td></tr>");

		String contextValue = !context.isCreatingRecord() && context.getCurrentRecord()!=null?context.getCurrentRecord().getString(_ContextValue):null;
		writer.add("<tr class=\"korea_info\" style=\"display:"+("JA.KR.ARXCUDCI.VAT".equalsIgnoreCase(contextValue)?"table-row":"none")+";\">");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_TaxablePerson);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_TaxablePerson);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_TaxCertificateDate);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_TaxCertificateDate);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr class=\"korea_info\" style=\"display:"+("JA.KR.ARXCUDCI.VAT".equalsIgnoreCase(contextValue)?"table-row":"none")+";\">");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_IndustryClassification);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_IndustryClassification);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_IndustrySubclassification);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_IndustrySubclassification);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr class=\"korea_info\" style=\"display:"+("JA.KR.ARXCUDCI.VAT".equalsIgnoreCase(contextValue)?"table-row":"none")+";\"><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_BusinessNumber);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_BusinessNumber);
		writer.add("</td></tr>");

		writer.add("<tr class=\"malaysia_info\" style=\"display:"+("XXAT.MYM.CUST.SITE.INFO".equalsIgnoreCase(contextValue)?"table-row":"none")+";\">");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_AddressSiteCategory);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_AddressSiteCategory);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_ATS);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_ATS);
		writer.add("</td>");
		writer.add("</tr>");

		//brazil
		writer.add("<tr class=\"brazil_info\" style=\"display:"+("JL.BR.ARXCUDCI.Additional".equalsIgnoreCase(contextValue)?"table-row":"none")+";\">");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_InscriptionType);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_InscriptionType);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_InsciptionNumber);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_InsciptionNumber);
		writer.add("</td>");
		writer.add("</tr>");

		//brazil
		writer.add("<tr class=\"brazil_info\" style=\"display:"+("JL.BR.ARXCUDCI.Additional".equalsIgnoreCase(contextValue)?"table-row":"none")+";\">");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_InscriptionBranch);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_InscriptionBranch);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_InscriptionDigit);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_InscriptionDigit);
		writer.add("</td>");
		writer.add("</tr>");

		//brazil
		writer.add("<tr class=\"brazil_info\" style=\"display:"+("JL.BR.ARXCUDCI.Additional".equalsIgnoreCase(contextValue)?"table-row":"none")+";\">");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_StateInscription);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_StateInscription);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_TaxRegistrationNumber);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_TaxRegistrationNumber);
		writer.add("</td>");
		/*writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_DefaultTaxRegistration);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_DefaultTaxRegistration);
		writer.add("</td>");*/
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_Source);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_Source);
		writer.add("</td>");
		/*writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_TaxRegistrationActive);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_TaxRegistrationActive);
		writer.add("</td>");*/
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_TaxRegimeCode);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_TaxRegimeCode);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_Tax);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_Tax);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_TaxEffectiveFrom);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_TaxEffectiveFrom);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_TaxEffectiveTo);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_TaxEffectiveTo);
		writer.add("</td>");
		writer.add("</tr>");

		if(!context.isCreatingRecord()) {
			writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.addLabel(_DaqaMetaData_State);
			writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
			writer.addWidget(_DaqaMetaData_State);
			writer.add("</td></tr>");

			writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.addLabel(_DaqaMetaData_ClusterId);
			writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
			writer.addWidget(_DaqaMetaData_ClusterId);
			writer.add("</td></tr>");

			writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.addLabel(_MergedTargetRecord);
			writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
			writer.addWidget(_MergedTargetRecord);
			writer.add("</td></tr>");
		}

		writer.add("</table>");

		writer.add("<div ");
		writer.addSafeAttribute("id", "divLoading");
		writer.add("></div>");

		RestProperties restProperties = (RestProperties) SpringContext.getApplicationContext().getBean("restProperties");
		String protocol = "true".equals(restProperties.getOrchestra().getSsl())?"https":"http";
		String host = restProperties.getOrchestra().getHost();
		String port = restProperties.getOrchestra().getPort();

		//GLOBAL JS VARIABLES
		try {
			Map<String, String> prefixedPaths = applicationCacheUtil.getPrefixedPaths(Paths._Address.class.getName(),writer);
			ObjectMapper mapper = new ObjectMapper();
			writer.addJS("var addressPrefixedPaths = "+mapper.writeValueAsString(prefixedPaths)+";");
		} catch (IllegalAccessException | ClassNotFoundException | JsonProcessingException e) {
			throw new ApplicationRuntimeException("Error geting prefixed paths for address",e);
		}
		writer.addJS("var territoryTypeMap = "+territoryTypeMapJsonString+";");
		writer.addJS("var mdmRestProtocol = '"+protocol+"';");
		writer.addJS("var mdmRestHost = '"+host+"';");
		writer.addJS("var mdmRestPort = '"+port+"';");
		writer.addJS("var arrowImage = '"+protocol+"://"+host+":"+port+"/mdmcustomapp/www/common/stylesheets/select_arrow.gif';");	// Regular arrow
		writer.addJS("var arrowImageOver = '"+protocol+"://"+host+":"+port+"/mdmcustomapp/www/common/stylesheets/select_arrow_over.gif';");	// Mouse over
		writer.addJS("var arrowImageDown = '"+protocol+"://"+host+":"+port+"/mdmcustomapp/www/common/stylesheets/select_arrow_down.gif';");	// Mouse down
		//JS FUNCTION CALLS
		writer.addJS("populateLovsOnLoad();");
		if(!context.isCreatingRecord() && "MERGED".equalsIgnoreCase(context.getCurrentRecord().getString(Paths._Address._DaqaMetaData_State))){
			writer.addJS("hideCreate();");
		}

		//JS FUNCTIONS
		writer.addJS("function saveAssignment(dataSpace, newAssignment, table, primaryKey) { var xhr = new XMLHttpRequest(); xhr.open('POST', '"+protocol+"://"+host+":"+port+"/mdmcustomapp/' + table + '/updateAssignment/' + dataSpace + '/' + primaryKey + '/' + newAssignment.key); xhr.setRequestHeader('Content-Type', 'application/json'); xhr.onload = function() { if (xhr.status === 200) { document.getElementById(\"divLoading\").classList.remove(\"show\"); } else { document.getElementById(\"divLoading\").classList.remove(\"show\"); } }; xhr.send(); document.getElementById(\"divLoading\").classList.add(\"show\"); }");
		writer.addJS("function updateRelatedOptions(countryCode, currentStateValue, currentProvinceValue) { if (!countryCode) { return; } var urlForOptions = '"+protocol+"://"+host+":"+port+"/mdmcustomapp/selectOptions/BReference/'; var statePrefixedPath = addressPrefixedPaths.AddressState; var provincePrefixedPath = addressPrefixedPaths.Province;  var stateSelect = document.getElementById(\"customStateSelect\"); var provinceSelect = document.getElementById(\"customProvinceSelect\"); var blankOption = new Option(\"[not defined]\", \"\"); if (territoryTypeMap[countryCode] === \"STATE\") { urlForOptions = urlForOptions + \"state/\" + countryCode; toggeleStandardFields(false); } else if (territoryTypeMap[countryCode] === \"PROVINCE\") { urlForOptions = urlForOptions + \"province/\" + countryCode; toggeleStandardFields(false); } else { while (provinceSelect.options.length > 1) { provinceSelect.remove(1); } while (stateSelect.options.length > 1) { stateSelect.remove(1); } ebx_form_setValue(statePrefixedPath, null); ebx_form_setValue(provincePrefixedPath, null); toggeleStandardFields(true); try { ebx_form_setValue(statePrefixedPath, currentStateValue); ebx_form_setValue(provincePrefixedPath, currentProvinceValue); } catch (err) {} return; } var xhr = new XMLHttpRequest(); xhr.open(\"GET\", urlForOptions); xhr.onload = function() { if (xhr.status === 200) { var obj = JSON.parse(xhr.responseText); var selectOptions = obj.options; if (territoryTypeMap[countryCode] === \"STATE\") { while (provinceSelect.options.length > 1) { provinceSelect.remove(1); } while (stateSelect.options.length > 1) { stateSelect.remove(1); } if (selectOptions) { var i; for (i = 0; i < selectOptions.length; i++) { var newOption = new Option(selectOptions[i].Option, selectOptions[i].OptionValue); stateSelect.options.add(newOption); } } ebx_form_setValue(statePrefixedPath, null); ebx_form_setValue(provincePrefixedPath, null); if (currentStateValue) { try { document.getElementById('customStateSelect').value = currentStateValue; ebx_form_setValue(statePrefixedPath, currentStateValue); } catch (err) {} } } else if (territoryTypeMap[countryCode] === \"PROVINCE\") { while (provinceSelect.options.length > 1) { provinceSelect.remove(1); } while (stateSelect.options.length > 1) { stateSelect.remove(1); } if (selectOptions) { var i; for (i = 0; i < selectOptions.length; i++) { var newOption = new Option(selectOptions[i].Option, selectOptions[i].OptionValue); provinceSelect.options.add(newOption); } } ebx_form_setValue(statePrefixedPath, null); ebx_form_setValue(provincePrefixedPath, null); if (currentProvinceValue) { try { document.getElementById('customProvinceSelect').value = currentProvinceValue; ebx_form_setValue(provincePrefixedPath, currentProvinceValue); } catch (err) {} } } else { while (provinceSelect.options.length > 1) { provinceSelect.remove(1); } while (stateSelect.options.length > 1) { stateSelect.remove(1); } ebx_form_setValue(statePrefixedPath, null); ebx_form_setValue(provincePrefixedPath, null); } } else { while (provinceSelect.options.length > 1) { provinceSelect.remove(1); } while (stateSelect.options.length > 1) { stateSelect.remove(1); } ebx_form_setValue(statePrefixedPath, null); ebx_form_setValue(provincePrefixedPath, null); try { ebx_form_setValue(statePrefixedPath, currentStateValue); ebx_form_setValue(provincePrefixedPath, currentProvinceValue); } catch (err) {} } }; xhr.send(); }");
		writer.addJS("function updateRelatedLocalOptions(countryCode, currentStateValue, currentProvinceValue) {     if (!countryCode) {         return;     }     var urlForOptions = '"+protocol+"://"+host+":"+port+"/mdmcustomapp/selectOptionsLocal/BReference/';     var statePrefixedPath = addressPrefixedPaths.StateLocalLanguage;     var provincePrefixedPath = addressPrefixedPaths.ProvinceLocalLanguage;     var stateSelect = document.getElementById(\"customStateLocalSelect\");     var provinceSelect = document.getElementById(\"customProvinceLocalSelect\");     var blankOption = new Option(\"[not defined]\", \"\");     if (territoryTypeMap[countryCode] === \"STATE\" && (\"JP\" === countryCode || \"RU\" === countryCode)) {         urlForOptions = urlForOptions + \"state/\" + countryCode;         toggleStandardFieldsLocal(false);     } else if (territoryTypeMap[countryCode] === \"PROVINCE\" && (\"CN\" === countryCode || \"KR\" === countryCode)) {         urlForOptions = urlForOptions + \"province/\" + countryCode;         toggleStandardFieldsLocal(false);     } else {         provinceSelect.setAttribute(\"selectBoxOptions\", \"\");         stateSelect.setAttribute(\"selectBoxOptions\", \"\");         ebx_form_setValue(statePrefixedPath, null);         ebx_form_setValue(provincePrefixedPath, null);         toggleStandardFieldsLocal(true);         try {             ebx_form_setValue(statePrefixedPath, currentStateValue);             ebx_form_setValue(provincePrefixedPath, currentProvinceValue);         } catch (err) {}         return;     }     var xhr = new XMLHttpRequest();     xhr.open(\"GET\", urlForOptions);     xhr.onload = function() {         if (xhr.status === 200) {             var obj = JSON.parse(xhr.responseText);             var selectOptions = obj.options;             if (territoryTypeMap[countryCode] === \"STATE\") {                 var stateSelect = document.getElementById(\"customStateLocalSelect\");                 stateSelect.parentNode.removeChild(stateSelect);                 var att = document.createAttribute(\"selectBoxOptions\");                 att.value = selectOptions.join([separator = ';']);                 var idAttr = document.createAttribute(\"id\");                 idAttr.value = \"customStateLocalSelect\";                 var onChangeAttr = document.createAttribute(\"oninput\");                 onChangeAttr.value = 'changeStateLocalStandard(this.value)';                 var newElement = document.createElement(\"input\");                 newElement.setAttributeNode(att);                 newElement.setAttributeNode(idAttr);                 document.getElementById(\"stateLocalCustomDiv\").appendChild(newElement);                 createEditableSelect(newElement);                 ebx_form_setValue(statePrefixedPath, null);                 ebx_form_setValue(provincePrefixedPath, null);                 if (currentStateValue) {                     try {                         document.getElementById('customStateLocalSelect').value = currentStateValue;                         ebx_form_setValue(statePrefixedPath, currentStateValue);                     } catch (err) {}                 }             } else if (territoryTypeMap[countryCode] === \"PROVINCE\") {                 var stateSelect = document.getElementById(\"customProvinceLocalSelect\");                 stateSelect.parentNode.removeChild(provinceSelect);                 var att = document.createAttribute(\"selectBoxOptions\");                 att.value = selectOptions.join([separator = ';']);                 var idAttr = document.createAttribute(\"id\");                 idAttr.value = \"customProvinceLocalSelect\";                 var onChangeAttr = document.createAttribute(\"oninput\");                 onChangeAttr.value = 'changeProvinceLocalStandard(this.value)';                 var newElement = document.createElement(\"input\");                 newElement.setAttributeNode(att);                 newElement.setAttributeNode(idAttr);                 document.getElementById(\"provinceLocalCustomDiv\").appendChild(newElement);                 createEditableSelect(newElement);                 ebx_form_setValue(statePrefixedPath, null);                 ebx_form_setValue(provincePrefixedPath, null);                 if (currentProvinceValue) {                     try {                         document.getElementById('customProvinceLocalSelect').value = currentProvinceValue;                         ebx_form_setValue(provincePrefixedPath, currentProvinceValue);                     } catch (err) {}                 }             } else {                 provinceSelect.setAttribute(\"selectBoxOptions\", \"\");             \tstateSelect.setAttribute(\"selectBoxOptions\", \"\");                 ebx_form_setValue(statePrefixedPath, null);                 ebx_form_setValue(provincePrefixedPath, null);             }         } else {             provinceSelect.setAttribute(\"selectBoxOptions\", \"\");             stateSelect.setAttribute(\"selectBoxOptions\", \"\");             ebx_form_setValue(statePrefixedPath, null);             ebx_form_setValue(provincePrefixedPath, null);             try {                 ebx_form_setValue(statePrefixedPath, currentStateValue);                 ebx_form_setValue(provincePrefixedPath, currentProvinceValue);             } catch (err) {}         }     };     xhr.send(); }");
		writer.addJS("function calculatedFields(countryCode) { updateRelatedOptions(countryCode, null, null); updateRelatedLocalOptions(countryCode, null, null); var stateValue = ebx_form_getValue(addressPrefixedPaths.AddressState); var xhr = new XMLHttpRequest(); xhr.open('GET', '"+protocol+"://"+host+":"+port+"/mdmcustomapp/calculatedFields/country/BReference/Account/' + countryCode); xhr.setRequestHeader('Content-Type', 'application/json'); xhr.onload = function() { if (xhr.status === 200) { var calculatedFieldsJson = JSON.parse(xhr.responseText); if (calculatedFieldsJson && calculatedFieldsJson.hasOwnProperty('TaxRegimeCode')) { var valueTax = calculatedFieldsJson.TaxRegimeCode; ebx_form_setValue(addressPrefixedPaths.TaxRegimeCode, valueTax); } else { ebx_form_setValue(addressPrefixedPaths.TaxRegimeCode, null); } } var valueOne = { \"key\": \"1\", \"label\": \"One copy\" }; var valueTwo = { \"key\": \"2\", \"label\": \"Two copies\" }; var valueThree = { \"key\": \"3\", \"label\": \"Three copies\" }; var valueFour = { \"key\": \"4\", \"label\": \"Four copies\" }; var valueFive = { \"key\": \"5\", \"label\": \"Five copies\" }; var valueIndia = { \"key\": \"0.3\", \"label\": \"Suppress because a Government Invoice number is required\" }; var ic = { \"key\": \"0.2\", \"label\": \"Suppress because of special format requirements\" }; var sk = { \"key\": \"N\", \"label\": \"N\" }; var sky = { \"key\": \"Y\", \"label\": \"Y\" }; if (\"US\" === countryCode) { ebx_form_setValue(addressPrefixedPaths.InvoiceCopies, valueTwo); ebx_form_setValue(addressPrefixedPaths.SendAcknowledgement, sky); }else if (\"KR\" === countryCode) { ebx_form_setValue(addressPrefixedPaths.InvoiceCopies, valueThree); ebx_form_setValue(addressPrefixedPaths.SendAcknowledgement, sky); }else if (\"IN\" === countryCode) { ebx_form_setValue(addressPrefixedPaths.InvoiceCopies, valueIndia); ebx_form_setValue(addressPrefixedPaths.SendAcknowledgement, sky); }else if (\"JP\" === countryCode) { ebx_form_setValue(addressPrefixedPaths.SendAcknowledgement, sk); ebx_form_setValue(addressPrefixedPaths.InvoiceCopies, ic); }else if (\"PL\" === countryCode) { ebx_form_setValue(addressPrefixedPaths.SendAcknowledgement, sk); ebx_form_setValue(addressPrefixedPaths.InvoiceCopies, valueOne); }else if (\"GU\" === countryCode || \"VE\" === countryCode || \"PR\" === countryCode) { ebx_form_setValue(addressPrefixedPaths.InvoiceCopies, valueTwo); ebx_form_setValue(addressPrefixedPaths.SendAcknowledgement, sky); }else if (\"PH\" === countryCode) { ebx_form_setValue(addressPrefixedPaths.InvoiceCopies, valueThree); ebx_form_setValue(addressPrefixedPaths.SendAcknowledgement, sky); }else if (\"MX\" === countryCode) { ebx_form_setValue(addressPrefixedPaths.InvoiceCopies, valueFour); ebx_form_setValue(addressPrefixedPaths.SendAcknowledgement, sky); }else if (\"AR\" === countryCode) { ebx_form_setValue(addressPrefixedPaths.InvoiceCopies, valueFive); ebx_form_setValue(addressPrefixedPaths.SendAcknowledgement, sky); } else { ebx_form_setValue(addressPrefixedPaths.InvoiceCopies, valueOne); ebx_form_setValue(addressPrefixedPaths.SendAcknowledgement, sky); } }; xhr.send(); }");
	}
}
