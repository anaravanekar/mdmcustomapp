package com.orchestranetworks.account.ui.form;

import com.orchestranetworks.ui.form.UIFormContext;
import com.orchestranetworks.ui.form.UIFormPane;
import com.orchestranetworks.ui.form.UIFormPaneWriter;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.AppUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

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

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_AssignedTo);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_AssignedTo);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_Published);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_Published);
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
//		writer.addLabel(_SiteId);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
//		writer.addWidget(_SiteId);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_Country);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_Country);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_AddressLine1);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_AddressLine1);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_AddressLine2);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_AddressLine2);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_AddressLine3);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_AddressLine3);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_AddressLine4);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_AddressLine4);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_City);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_City);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_PostalCode);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_PostalCode);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_AddressState);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_AddressState);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_Province);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_Province);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_County);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_County);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_NLSLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_NLSLanguage);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_AddressLine1LocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_AddressLine1LocalLanguage);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_AddressLine2LocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_AddressLine2LocalLanguage);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_AddressLine3LocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_AddressLine3LocalLanguage);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_AddressLine4LocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_AddressLine4LocalLanguage);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_CityLocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_CityLocalLanguage);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_PostalLocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_PostalLocalLanguage);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_StateLocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_StateLocalLanguage);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_ProvinceLocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_ProvinceLocalLanguage);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_CountyLocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_CountyLocalLanguage);
		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_CountryLocalLanguage);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_CountryLocalLanguage);
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

/*		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_PrimaryPurpose);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_PrimaryPurpose);
		writer.add("</td></tr>");*/

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

/*		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_SubSegment);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_SubSegment);
		writer.add("</td></tr>");*/

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
		writer.addLabel(_MDMAccountId);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_MDMAccountId);
		writer.add("</td></tr>");

		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_InternalAccountId);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_InternalAccountId);
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

/*		writer.add("<tr><td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(_KeysightSFAAddressId);
		writer.add("</td><td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(_KeysightSFAAddressId);
		writer.add("</td></tr>");*/

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
		//writer.addJS("alert('calculatedFields called');");
		writer.addJS("var xhr = new XMLHttpRequest();");
		String protocol = "true".equals(((Map)((Map) AppUtil.getAllPropertiesMap().get("keysight")).get("orchestraRest")).get("ssl").toString())?"https":"http";
		String host = ((Map)((Map) AppUtil.getAllPropertiesMap().get("keysight")).get("orchestraRest")).get("host").toString();
		String port = ((Map)((Map) AppUtil.getAllPropertiesMap().get("keysight")).get("orchestraRest")).get("port").toString();
		writer.addJS("xhr.open('GET', '"+protocol+"://"+host+":"+port+"/mdmcustomapp/calculatedFields/country/BReference/Account/'+countryCode);");
		writer.addJS("xhr.setRequestHeader('Content-Type', 'application/json');");
		writer.addJS("xhr.onload = function() {");
		writer.addJS("if (xhr.status === 200) {");
		writer.addJS("var calculatedFieldsJson = JSON.parse(xhr.responseText);");
		writer.addJS("if(calculatedFieldsJson && calculatedFieldsJson.hasOwnProperty('OperatingUnit')){");
		writer.addJS("var value = calculatedFieldsJson.OperatingUnit;");
		/*writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(Paths._Address._OperatingUnit).format()).addJS("\", ").addJS(
				"value").addJS(");");*/
		writer.addJS_setNodeValue("value",Paths._Address._OperatingUnit);
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
		writer.addJS("if(calculatedFieldsJson && calculatedFieldsJson.hasOwnProperty('AddressState')){");
		writer.addJS("var value = calculatedFieldsJson.AddressState;");
		writer.addJS_setNodeValue("value",Paths._Address._AddressState);
		writer.addJS("alert('setnodevalue called');");
/*		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(Paths._Address._AddressState).format()).addJS("\", ").addJS(
				"value").addJS(");")*/;
		writer.addJS("}");writer.addJS("else{");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(Paths._Address._AddressState).format()).addJS("\", ").addJS(
				"null").addJS(");");
		writer.addJS("}");
		writer.addJS("if(calculatedFieldsJson && calculatedFieldsJson.hasOwnProperty('Province')){");
		writer.addJS("var value = calculatedFieldsJson.Province;");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(Paths._Address._Province).format()).addJS("\", ").addJS(
				"value").addJS(");");
		writer.addJS("}");writer.addJS("else{");
		writer.addJS("ebx_form_setValue(\"").addJS(writer.getPrefixedPath(Paths._Address._Province).format()).addJS("\", ").addJS(
				"null").addJS(");");
		writer.addJS("}");
		writer.addJS("}");
		writer.addJS("};");
		writer.addJS("xhr.send();");
		writer.addJS("}");
	}
}
