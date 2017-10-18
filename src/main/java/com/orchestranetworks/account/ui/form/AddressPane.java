package com.orchestranetworks.account.ui.form;

import com.orchestranetworks.ui.form.UIFormContext;
import com.orchestranetworks.ui.form.UIFormPane;
import com.orchestranetworks.ui.form.UIFormPaneWriter;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;

public class AddressPane implements UIFormPane{
	public static final String CELL_STYLE = "width:80%; padding:5px; vertical-align:top;text-align:right";
	
	@Override
	public void writePane(UIFormPaneWriter writer, UIFormContext context) {
		writer.add("<table>");
       
		
		writer.add("<tr>");
		writer.add("<td ");
		
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._RelatedAddress);
	
		writer.endTableFormRow();
	
		writer.add("</td>");
		writer.add("</tr>");
		
		writer.add("</table>");
		
	}

}
