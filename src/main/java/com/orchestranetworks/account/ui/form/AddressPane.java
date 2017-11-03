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

import static com.sereneast.orchestramdm.keysight.mdmcustom.Paths._Account._AssignedTo;
import static com.sereneast.orchestramdm.keysight.mdmcustom.Paths._Address.*;

public class AddressPane implements UIFormPane {
	private static final Logger LOGGER = LoggerFactory.getLogger(AddressPane.class);
	public static final String CELL_STYLE_LEFT = "width:25%;  vertical-align:top;";
	public static final String CELL_STYLE_RIGHT = "width:25%; vertical-align:top;text-align:right;";
	public static final String TABLE_STYLE = "width:100%";

	@Override
	public void writePane(UIFormPaneWriter writer, UIFormContext context) {
		String currentUserId = context.getSession().getUserReference().getUserId();
		String openedByUser = context.getValueContext().getValue(Paths._Address._AssignedTo)!=null?context.getValueContext().getValue(Paths._Address._AssignedTo).toString():null;
		LOGGER.debug("currentUsereId:"+currentUserId);
		LOGGER.debug("openedByUser:"+openedByUser);
		if(StringUtils.isNotBlank(openedByUser) && !currentUserId.equalsIgnoreCase(openedByUser)) {
			writer.add("<div");writer.addSafeAttribute("style", "margin-left: 5px;");writer.add(">");writer.add("<b><font color=\"red\">Note: This record is currently being edited by " +openedByUser+". Any changes made cannot be saved.</font></b>");writer.add("</div>");
		}

		writer.add("<table width=\"50%\" >");

		if(StringUtils.isNotBlank(openedByUser) && !currentUserId.equalsIgnoreCase(openedByUser)) {
			UserMessageString buttonLabel = new UserMessageString();
			buttonLabel.setString(Locale.ENGLISH,"Save Assigned To");
			String dataSpace = context.getCurrentRecord().getHome().getKey().format();
			String mdmdAddressId = String.valueOf(context.getCurrentRecord().get(_MDMAddressId));
			writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");writer.addLabel(_AssignedTo);writer.add("</td>");writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addWidget(_AssignedTo);writer.add("</td>");
			writer.add("<td colspan=\"2\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_LEFT + "\">");writer.addButtonJavaScript(new UIButtonSpecJSAction(buttonLabel,"saveAssignment('"+dataSpace+"',ebx_form_getValue(\""+writer.getPrefixedPath(_AssignedTo).format()+"\"),'address',"+mdmdAddressId+")"));writer.add("</td>");
			writer.add("</tr>");
		}else {
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
		if(lastPublishedDate!=null){
			writer.add(sdf.format(lastPublishedDate));
		}
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_OperatingUnit);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
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

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_RMTId);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_RMTId);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_MDMAccountId);
		writer.add("</td><td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_MDMAccountId);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_InternalAccountId);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_InternalAccountId);
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
		writer.addWidget(_InternalAddressId);
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

		writer.add("<tr>");
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

		writer.add("<tr>");
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

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_BusinessNumber);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_BusinessNumber);
		writer.add("</td></tr>");

		writer.add("<tr>");
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

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_SpecialHandling);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_SpecialHandling);
		writer.add("</td></tr>");

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
		writer.addLabel(_ParentSystemId);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_ParentSystemId);
		writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_BatchCode);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_BatchCode);
		writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_MergedTargetRecord);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_MergedTargetRecord);
		writer.add("</td></tr>");

		writer.add("</table>");

		writer.addJS("function calculatedFields(countryCode){");
		writer.addJS("var stateValue=ebx_form_getValue(\""+writer.getPrefixedPath(_AddressState).format()+"\");");
		writer.addJS("console.log('stateValue='+stateValue);");
		writer.addJS("console.log('stateValue json ='+JSON.stringify(stateValue));");
		writer.addJS("var xhr = new XMLHttpRequest();");
		RestProperties restProperties = (RestProperties) SpringContext.getApplicationContext().getBean("restProperties");
		String protocol = "true".equals(restProperties.getOrchestra().getSsl())?"https":"http";
		String host = restProperties.getOrchestra().getHost();
		String port = restProperties.getOrchestra().getPort();
		writer.addJS("xhr.open('GET', '"+protocol+"://"+host+":"+port+"/mdmcustomapp/calculatedFields/country/BReference/Account/'+countryCode);");
		writer.addJS("xhr.setRequestHeader('Content-Type', 'application/json');");
		writer.addJS("xhr.onload = function() {");
		writer.addJS("if (xhr.status === 200) {");
		writer.addJS("var calculatedFieldsJson = JSON.parse(xhr.responseText);");
		writer.addJS("if(calculatedFieldsJson && calculatedFieldsJson.hasOwnProperty('OperatingUnit')){");
		writer.addJS("var value = {\"key\":calculatedFieldsJson.OperatingUnit,\"label\":calculatedFieldsJson.OperatingUnit};");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(Paths._Address._OperatingUnit).format()).addJS("\", ").addJS(
				"value").addJS(");");
		writer.addJS("}");writer.addJS("else{");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(Paths._Address._OperatingUnit).format()).addJS("\", ").addJS(
				"null").addJS(");");
		writer.addJS("}");
		writer.addJS("if(calculatedFieldsJson && calculatedFieldsJson.hasOwnProperty('TaxRegimeCode')){");
		writer.addJS("var value = calculatedFieldsJson.TaxRegimeCode;");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(Paths._Address._TaxRegimeCode).format()).addJS("\", ").addJS(
				"value").addJS(");");
		writer.addJS("}");writer.addJS("else{");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(Paths._Address._TaxRegimeCode).format()).addJS("\", ").addJS(
				"null").addJS(");");
		writer.addJS("}");
		writer.addJS("var value = {\"key\":null,\"label\":null};");
		writer.addJS("var valueNd = {\"key\":\"[not defined]\",\"label\":\"[not defined]\"};");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(_AddressState).format()).addJS("\", ").addJS(
				"null").addJS(");");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(_Province).format()).addJS("\", ").addJS(
				"null").addJS(");");
		writer.addJS("}");
		writer.addJS("};");
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
	}
}
