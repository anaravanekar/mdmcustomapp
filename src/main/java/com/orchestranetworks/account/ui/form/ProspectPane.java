package com.orchestranetworks.account.ui.form;

import com.onwbp.base.text.UserMessageString;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.ui.UIButtonSpecJSAction;
import com.orchestranetworks.ui.UIFormLabelSpec;
import com.orchestranetworks.ui.base.Size;
import com.orchestranetworks.ui.form.UIFormContext;
import com.orchestranetworks.ui.form.UIFormPane;
import com.orchestranetworks.ui.form.UIFormPaneWriter;
import com.orchestranetworks.ui.form.UIFormRow;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.SpringContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.RestProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import static com.sereneast.orchestramdm.keysight.mdmcustom.Paths._Account._AssignedTo;
import static com.sereneast.orchestramdm.keysight.mdmcustom.Paths._Account._SystemId;

public class ProspectPane implements UIFormPane {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProspectPane.class);
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
		writer.startTableFormRow();
		if(!context.isCreatingRecord() && StringUtils.isNotBlank(openedByUser) && !currentUserId.equalsIgnoreCase(openedByUser)) {
			UserMessageString buttonLabel = new UserMessageString();
			buttonLabel.setString(Locale.ENGLISH, "Save Assigned To");
			String systemId = String.valueOf(context.getCurrentRecord().get(_SystemId));
			String dataSpace = context.getCurrentRecord().getHome().getKey().format();
			writer.startFormRow(Path.parse("./AssignedTo"));
/*			UIFormRow uiFormRow = writer.newFormRow();
			Size size = new Size(0,Size.Unit.PIXEL);
			uiFormRow.setWidgetWidth(size);
			writer.startFormRow(uiFormRow);
			writer.add("<td class=\"ebx_Input\" colspan=\"1\">");writer.addWidget(_AssignedTo);writer.add("</td>");*/
			writer.add("<td colspan=\"2\" style=\"width:25%;  vertical-align:top;\" nowrap=\"nowrap\" color=\"#606060\">");writer.addButtonJavaScript(new UIButtonSpecJSAction(buttonLabel,"saveAssignmentProspect('"+dataSpace+"',ebx_form_getValue(\""+writer.getPrefixedPath(_AssignedTo).format()+"\"),'account','"+systemId+"')"));writer.add("</td>");
			writer.endFormRow();
		}else{
			writer.addFormRow(Path.parse("./AssignedTo"));
		}
		writer.addFormRow(Path.parse("./SystemName"));
		writer.addFormRow(Path.parse("./SystemId"));
		writer.addFormRow(Path.parse("./AccountNumber"));
		writer.addFormRow(Path.parse("./AccountName"));
		writer.addFormRow(Path.parse("./Status"));
		writer.addFormRow(Path.parse("./AccountType"));
		writer.addFormRow(Path.parse("./AccountClass"));
		writer.addFormRow(Path.parse("./CustomerScreening"));
		writer.addFormRow(Path.parse("./JPKatakanaAccount"));
		writer.addFormRow(Path.parse("./AlternateAccountName"));
		writer.addFormRow(Path.parse("./NameLocalLanguage"));
		writer.addFormRow(Path.parse("./Country"));
		writer.addFormRow(Path.parse("./CreatedBy"));
		writer.addFormRow(Path.parse("./CreatedDate"));
		writer.addFormRow(Path.parse("./PendingInactivation"));
		writer.addFormRow(Path.parse("./LastActionBy"));
		writer.addFormRow(Path.parse("./MatchExclusion"));
		writer.addFormRow(Path.parse("./LastModifiedDate"));
		writer.addFormRow(Path.parse("./Address"));
		writer.addFormRow(Path.parse("./Locale"));
		writer.addFormRow(Path.parse("./MDMMatchCount"));
		writer.addFormRow(Path.parse("./RelatedMDMMatch"));
		writer.endTableFormRow();

		RestProperties restProperties = (RestProperties) SpringContext.getApplicationContext().getBean("restProperties");
		String protocol = "true".equals(restProperties.getOrchestra().getSsl())?"https":"http";
		String host = restProperties.getOrchestra().getHost();
		String port = restProperties.getOrchestra().getPort();
		writer.addJS("var mdmRestProtocol = '"+protocol+"';");
		writer.addJS("var mdmRestHost = '"+host+"';");
		writer.addJS("var mdmRestPort = '"+port+"';");

		//JS FUNCTIONS
		writer.addJS("function saveAssignmentProspect(dataSpace, newAssignment, table, primaryKey) { var xhr = new XMLHttpRequest(); xhr.open('POST', '"+protocol+"://"+host+":"+port+"/mdmcustomapp/' + table + '/updateAssignmentProspect/' + dataSpace + '/Prospect/' + primaryKey + '/' + newAssignment.key); xhr.setRequestHeader('Content-Type', 'application/json'); xhr.onload = function() { if (xhr.status === 200) { document.getElementById(\"divLoading\").classList.remove(\"show\"); } else { document.getElementById(\"divLoading\").classList.remove(\"show\"); } }; xhr.send(); document.getElementById(\"divLoading\").classList.add(\"show\"); }");

	}
}
