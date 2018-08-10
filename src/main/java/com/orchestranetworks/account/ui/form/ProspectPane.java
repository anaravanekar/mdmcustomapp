package com.orchestranetworks.account.ui.form;

import com.orchestranetworks.schema.Path;
import com.orchestranetworks.ui.form.UIFormContext;
import com.orchestranetworks.ui.form.UIFormPane;
import com.orchestranetworks.ui.form.UIFormPaneWriter;
import com.sereneast.orchestramdm.keysight.mdmcustom.SpringContext;
import com.sereneast.orchestramdm.keysight.mdmcustom.config.properties.RestProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProspectPane implements UIFormPane {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProspectPane.class);

	@Override
	public void writePane(UIFormPaneWriter writer, UIFormContext context) {
		writer.startTableFormRow();
		writer.addFormRow(Path.parse("./AssignedTo"));
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
		writer.addJS("function saveAssignment(dataSpace, newAssignment, table, primaryKey) { var xhr = new XMLHttpRequest(); xhr.open('POST', '"+protocol+"://"+host+":"+port+"/mdmcustomapp/' + table + '/updateAssignment/' + dataSpace + '/' + primaryKey + '/' + newAssignment.key); xhr.setRequestHeader('Content-Type', 'application/json'); xhr.onload = function() { if (xhr.status === 200) { document.getElementById(\"divLoading\").classList.remove(\"show\"); } else { document.getElementById(\"divLoading\").classList.remove(\"show\"); } }; xhr.send(); document.getElementById(\"divLoading\").classList.add(\"show\"); }");

	}
}
