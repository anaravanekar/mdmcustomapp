package com.orchestranetworks.account.ui.form;

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
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ApplicationCacheUtil;
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
					accountName = context.getCurrentRecord().getString(Path.parse("./AccountName"));//record.getString(Paths._Account._AccountName);
					accountLocalName = context.getCurrentRecord().getString(Path.parse("./AccountNameLocalLanguage"));//record.getString(Paths._Account._NameLocalLanguage)!=null?record.getString(Paths._Account._NameLocalLanguage):"";
					textToAppend.append(" of ").append(accountName);
//				}
			}
			/*if(StringUtils.isNotBlank(context.getCurrentRecord().getString(_AccountName))){
				textToAppend.append("    ").append(context.getCurrentRecord().getString(_AccountName));
			}*/
			//textToAppend.append(context.getCurrentRecord()!=null?context.getCurrentRecord().getString(_):"");
			String internalAccountId = StringUtils.isNotBlank(context.getCurrentRecord().getString(Paths._Account._InternalAccountId))?context.getCurrentRecord().getString(Paths._Account._InternalAccountId):null;
			if(internalAccountId!=null){
				textToAppend.append("    Internal Account Id: ").append(internalAccountId);
			}

			if(StringUtils.isNotBlank(textToAppend.toString())) {
				writer.addJS("appendToFormHeader(\"" + textToAppend + "\");");
			}
		}

		//operatingUnit custom html
		StringBuilder operatingUnitSelectBox = new StringBuilder();
		StringBuilder operatingUnitSelectBoxOptions = new StringBuilder();
		operatingUnitSelectBoxOptions.append("<option value=\"\"></option>");
		ApplicationCacheUtil applicationCacheUtil = new ApplicationCacheUtil();
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
			if (!isCurrentValueValidForDisplay) {
				operatingUnitSelectBoxOptions.append("<option value=\"" + context.getCurrentRecord().getString(_OperatingUnit) + "\" selected>" + context.getCurrentRecord().getString(_OperatingUnit) + "</option>");
			}
			String operatingUnitPrefixedPath = writer.getPrefixedPath(_OperatingUnit).format();
		}
		operatingUnitSelectBox.append("<select id=\"OperatingUnitCustom\" onchange=\"changeDropDownValue(\""+writer.getPrefixedPath(_OperatingUnit).format()+"\",this.value,this.options.item(this.selectedIndex).text)\">").append(operatingUnitSelectBoxOptions).append("</select>");

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
		writer.add("<td colspan=\"1\" style=\"padding-left:5px;" + CELL_STYLE_LEFT + "\">");
		writer.add(operatingUnitSelectBox.toString());writer.add("<div style=\"display:none;\">");writer.addWidget(_OperatingUnit);writer.add("</div>");
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
		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_RMTId);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_RMTId);
		writer.add("</td>");
		writer.add("</tr>");

		if(!context.isCreatingRecord()) {
			String mdmAccountId = context.getCurrentRecord()!=null?context.getCurrentRecord().getString(_MDMAccountId):null;
			writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
			writer.addLabel(_MDMAccountId);
			writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
			writer.add("<span>"+mdmAccountId+"</span>");
			writer.add("</td></tr>");
		}else {
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
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_MDMAddressId);
		writer.add("</td>");
		writer.add("</tr>");

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
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_AddressState);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_StateLocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_StateLocalLanguage);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_Province);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_Province);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_ProvinceLocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_ProvinceLocalLanguage);
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
		writer.add("<tr class=\"korea_info\" style=\"display:"+("Korean Additional Information".equalsIgnoreCase(contextValue)?"table-row":"none")+";\">");
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

		writer.add("<tr class=\"korea_info\" style=\"display:"+("Korean Additional Information".equalsIgnoreCase(contextValue)?"table-row":"none")+";\">");
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

		writer.add("<tr class=\"korea_info\" style=\"display:"+("Korean Additional Information".equalsIgnoreCase(contextValue)?"table-row":"none")+";\"><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_BusinessNumber);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_BusinessNumber);
		writer.add("</td></tr>");

		writer.add("<tr class=\"malaysia_info\" style=\"display:"+("Malaysia Customer Information".equalsIgnoreCase(contextValue)?"table-row":"none")+";\">");
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

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_TaxRegistrationNumber);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_TaxRegistrationNumber);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_DefaultTaxRegistration);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_DefaultTaxRegistration);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_Source);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_Source);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_TaxRegistrationActive);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_TaxRegistrationActive);
		writer.add("</td>");
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

		RestProperties restProperties = (RestProperties) SpringContext.getApplicationContext().getBean("restProperties");
		String protocol = "true".equals(restProperties.getOrchestra().getSsl())?"https":"http";
		String host = restProperties.getOrchestra().getHost();
		String port = restProperties.getOrchestra().getPort();

		writer.addJS("function calculatedFields(countryCode){");
		writer.addJS("var stateValue=ebx_form_getValue(\""+writer.getPrefixedPath(_AddressState).format()+"\");");
		//writer.addJS("console.log('stateValue='+stateValue);");
		//writer.addJS("console.log('stateValue json ='+JSON.stringify(stateValue));");
		writer.addJS("var xhr = new XMLHttpRequest();");
		writer.addJS("xhr.open('GET', '"+protocol+"://"+host+":"+port+"/mdmcustomapp/calculatedFields/country/BReference/Account/'+countryCode);");
		writer.addJS("xhr.setRequestHeader('Content-Type', 'application/json');");
		writer.addJS("xhr.onload = function() {");
		writer.addJS("if (xhr.status === 200) {");
		writer.addJS("var calculatedFieldsJson = JSON.parse(xhr.responseText);");
		writer.addJS("if(calculatedFieldsJson && calculatedFieldsJson.hasOwnProperty('OperatingUnit')){");
		writer.addJS("var value = {\"key\":calculatedFieldsJson.OperatingUnit,\"label\":calculatedFieldsJson.OperatingUnit};");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(Paths._Address._OperatingUnit).format()).addJS("\", ").addJS(
				"value").addJS(");document.getElementById('OperatingUnitCustom').value=calculatedFieldsJson.OperatingUnit;");
		writer.addJS("}");writer.addJS("else{");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(Paths._Address._OperatingUnit).format()).addJS("\", ").addJS(
				"null").addJS(");document.getElementById('OperatingUnitCustom').value=\"\";");
		writer.addJS("}");
		writer.addJS("if(calculatedFieldsJson && calculatedFieldsJson.hasOwnProperty('TaxRegimeCode')){");
		writer.addJS("var value = calculatedFieldsJson.TaxRegimeCode;");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(Paths._Address._TaxRegimeCode).format()).addJS("\", ").addJS(
				"value").addJS(");");
		writer.addJS("}");writer.addJS("else{");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(Paths._Address._TaxRegimeCode).format()).addJS("\", ").addJS(
				"null").addJS(");");
		writer.addJS("}");
		/*writer.addJS("var value = {\"key\":null,\"label\":null};");
		writer.addJS("var valueNd = {\"key\":\"[not defined]\",\"label\":\"[not defined]\"};");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(_AddressState).format()).addJS("\", ").addJS(
				"null").addJS(");");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(_Province).format()).addJS("\", ").addJS(
				"null").addJS(");");*/

		writer.addJS("}");//if 200

		writer.addJS("var valueOne = {\"key\":\"1\",\"label\":\"One copy\"};");
		writer.addJS("var valueTwo = {\"key\":\"2\",\"label\":\"Two copies\"};");
		writer.addJS("if(\"US\" == countryCode){");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(_InvoiceCopies).format()).addJS("\", ").addJS(
				"valueTwo").addJS(");");
		writer.addJS("}else{");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(_InvoiceCopies).format()).addJS("\", ").addJS(
				"valueOne").addJS(");");
		writer.addJS("}");

		writer.addJS("var sk = {\"key\":\"N\",\"label\":\"N\"};");
		writer.addJS("var ic = {\"key\":\"0.2\",\"label\":\"Suppress because of special format requirements\"};");
		writer.addJS("if(\"JP\" == countryCode){");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(_SendAcknowledgement).format()).addJS("\", ").addJS(
				"sk").addJS(");");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(_InvoiceCopies).format()).addJS("\", ").addJS(
				"ic").addJS(");");
		writer.addJS("}");

		writer.addJS("};");//onload function
		writer.addJS("xhr.send();");
		writer.addJS("}");

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

		writer.addJS("function appendToFormHeader(textToAppend) { var span = document.createElement(\"span\"); var t = document.createTextNode(textToAppend); span.appendChild(t); document.getElementById(\"ebx_WorkspaceHeader\").getElementsByTagName(\"h2\")[0].appendChild(span); }");
		writer.addJS("function toggleAdditionalInfo(contextValue) {     var koreaRows = document.getElementsByClassName(\"korea_info\");     var malaysiaRows = document.getElementsByClassName(\"malaysia_info\");     var i;     if (contextValue === \"Korean Additional Information\") {  clearMalaysiaInfo();       for (i = 0; i < koreaRows.length; i++) {             koreaRows[i].style.display = \"table-row\";         }         for (i = 0; i < malaysiaRows.length; i++) {             malaysiaRows[i].style.display = \"none\";         }     } else if (contextValue === \"Malaysia Customer Information\") {    clearKoreaInfo();     for (i = 0; i < koreaRows.length; i++) {             koreaRows[i].style.display = \"none\";         }         for (i = 0; i < malaysiaRows.length; i++) {             malaysiaRows[i].style.display = \"table-row\";         }     } else {    clearMalaysiaInfo();clearKoreaInfo();     for (i = 0; i < koreaRows.length; i++) {             koreaRows[i].style.display = \"none\";         }         for (i = 0; i < malaysiaRows.length; i++) {             malaysiaRows[i].style.display = \"none\";         }     } }");
		writer.addJS("function clearMalaysiaInfo(){ ebx_form_setValue(\""+writer.getPrefixedPath(_AddressSiteCategory).format()+"\",null); ebx_form_setValue(\""+writer.getPrefixedPath(_ATS).format()+"\",null); }");
		writer.addJS("function clearKoreaInfo(){ ebx_form_setValue(\""+writer.getPrefixedPath(_TaxablePerson).format()+"\",null); ebx_form_setValue(\""+writer.getPrefixedPath(_TaxCertificateDate).format()+"\",null); ebx_form_setValue(\""+writer.getPrefixedPath(_IndustryClassification).format()+"\",null); ebx_form_setValue(\""+writer.getPrefixedPath(_IndustrySubclassification).format()+"\",null); ebx_form_setValue(\""+writer.getPrefixedPath(_BusinessNumber).format()+"\",null); }");
		writer.addJS("function changeDropDownValue(prefixedPath,selectedValue,selectedText){ console.log('selectedValue='+selectedValue+' selectedText='+selectedText);ebx_form_setValue(prefixedPath,{'key':selectedValue,'label':selectedText});}");
	}
}
