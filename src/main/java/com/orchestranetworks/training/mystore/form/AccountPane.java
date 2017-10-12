package com.orchestranetworks.training.mystore.form;

import com.orchestranetworks.ui.form.UIFormContext;
import com.orchestranetworks.ui.form.UIFormPane;
import com.orchestranetworks.ui.form.UIFormPaneWriter;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;

public class AccountPane implements UIFormPane {
	public static final String CELL_STYLE = "width:80%; padding:5px; vertical-align:top;text-align:right;";
	public static final String CELL_STYLE_LEFT = "width:80%; padding:5px; vertical-align:top;";
	public static final String ADDRESS_STYLE = "margin:10px";
	
	@Override
	public void writePane(UIFormPaneWriter writer, UIFormContext context) {
		
		writer.add("<table>");
		
		 writer.add("<tr>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._AssignedTo);
		writer.endTableFormRow();
		writer.add("</td>");
		writer.add("<td>");
		writer.add("</td>");
		writer.add("</tr>");
		// row end 
		
		 writer.add("<tr>");
		 writer.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		 writer.startTableFormRow();
		 writer.addFormRow(Paths._Account._CustomerType);
		 writer.endTableFormRow();
		 writer.add("</td>");
		 writer.add("<td>");
		 writer.add("</td>");
		 writer.add("</tr>");
		 writer.add("<tr>");
		
	
		writer.add("<td style=\"" + CELL_STYLE + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._AccountName);
		writer.endTableFormRow();
		writer.add("</td>");
		writer.add("<td style=\"" + CELL_STYLE + "\">");
	
		writer.add("</td>");
		writer.add("</tr>");
		
		 writer.add("<tr>");
		 writer.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		 writer.startTableFormRow();
		 writer.addFormRow(Paths._Account._Country);
		 writer.endTableFormRow();
		 writer.add("</td>");
		 writer.add("</tr>");
		 writer.add("<tr>");
		 
		 writer.add("<tr>");
		 writer.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		 writer.startTableFormRow();
		 writer.addFormRow(Paths._Account._Notes);
		 writer.endTableFormRow();
		 writer.add("</td>");
		 writer.add("<td style=\"" + CELL_STYLE + "\">");
			
			writer.add("</td>");
		 writer.add("</tr>");
		 
		 writer.add("<tr>");
		writer.add("<td style=\"" + CELL_STYLE + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._AccountType);
		writer.endTableFormRow();
		writer.add("</td>");
		writer.add("<td style=\"" + CELL_STYLE + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._AccountDescription);
		writer.endTableFormRow();
		writer.add("</td>");
		writer.add("</tr>");
		 
		writer.add("<td style=\"" + CELL_STYLE + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._Alias);
		writer.endTableFormRow();
		writer.add("</td>");
		writer.add("<td style=\"" + CELL_STYLE + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._NamePronunciation);
		writer.endTableFormRow();
		writer.add("</td>");
		writer.add("</tr>");
		
		 writer.add("<tr>");
		 writer.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		 writer.startTableFormRow();
		 writer.addFormRow(Paths._Account._Status);
		 writer.endTableFormRow();
		 writer.add("</td>");
		 writer.add("<td style=\"" + CELL_STYLE + "\">");
			
			writer.add("</td>");
		 writer.add("</tr>");
		 writer.add("<tr>");
		 
	    writer.add("<td style=\"" + CELL_STYLE + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._SystemName);
		writer.endTableFormRow();
		writer.add("</td>");
		writer.add("<td style=\"" + CELL_STYLE + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._SystemId);
		writer.endTableFormRow();
		writer.add("</td>");
		writer.add("</tr>");
			
		writer.add("<tr>");
		writer.add("<td colspan=\"2\">");
		writer.add("<div style=\"" + ADDRESS_STYLE + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._RelatedAddress);
		writer.endTableFormRow();
		writer.add("</div>");
		writer.add("</tr>");
			
		 writer.add("<tr>");
		 writer.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		 writer.startTableFormRow();
		 writer.addFormRow(Paths._Account._RegistryId);
		 writer.endTableFormRow();
		 writer.add("</td>");
		 writer.add("<td style=\"" + CELL_STYLE + "\">");
			
		 writer.add("</td>");
		 writer.add("</tr>");
		 writer.add("<tr>");
			 
	    writer.add("<td style=\"" + CELL_STYLE + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._GroupingId);
		writer.endTableFormRow();
		writer.add("</td>");
		writer.add("<td style=\"" + CELL_STYLE + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._GroupingDescription);
		writer.endTableFormRow();
		writer.add("</td>");
		writer.add("</tr>");
		
		 writer.add("<tr>");
		 writer.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		 writer.startTableFormRow();
		 writer.addFormRow(Paths._Account._ISGClassification);
		 writer.endTableFormRow();
		 writer.add("</td>");
		 writer.add("<td style=\"" + CELL_STYLE + "\">");
			
			writer.add("</td>");
		 writer.add("</tr>");
		 writer.add("<tr>");
		
		writer.add("<td style=\"" + CELL_STYLE + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._TaxpayerId);
		writer.endTableFormRow();
		writer.add("</td>");
		writer.add("<td style=\"" + CELL_STYLE + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._TaxRegistrationNumber);
		writer.endTableFormRow();
		writer.add("</td>");
		writer.add("</tr>");
		
		 writer.add("<tr>");
		 writer.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		 writer.startTableFormRow();
		 writer.addFormRow(Paths._Account._Region);
		 writer.endTableFormRow();
		 writer.add("</td>");
		 writer.add("<td style=\"" + CELL_STYLE + "\">");
			
		 writer.add("</td>");
		 writer.add("</tr>");
		 writer.add("<tr>");
		 
		 writer.add("<tr>");
		 writer.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		 writer.startTableFormRow();
		 writer.addFormRow(Paths._Account._Classification);
		 writer.endTableFormRow();
		 writer.add("</td>");
		 writer.add("<td style=\"" + CELL_STYLE + "\">");
			
			writer.add("</td>");
		 writer.add("</tr>");
		 writer.add("<tr>");
		 
		 writer.add("<tr>");
		 writer.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		 writer.startTableFormRow();
		 writer.addFormRow(Paths._Account._SalesChannel);
		 writer.endTableFormRow();
		 writer.add("</td>");
		 writer.add("<td style=\"" + CELL_STYLE + "\">");
			
			writer.add("</td>");
		 writer.add("</tr>");
		 writer.add("<tr>");
				 
	    writer.add("<td style=\"" + CELL_STYLE + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._NLSLanguageCode);
		writer.endTableFormRow();
		writer.add("</td>");
		writer.add("<td style=\"" + CELL_STYLE + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._NameLocalLanguage);
		writer.endTableFormRow();
		writer.add("</td>");
		writer.add("</tr>");
		
		 writer.add("<tr>");
		 writer.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		 writer.startTableFormRow();
		 writer.addFormRow(Paths._Account._CustomerScreening);
		 writer.endTableFormRow();
		 writer.add("</td>");
		 writer.add("<td style=\"" + CELL_STYLE + "\">");
			
			writer.add("</td>");
		 writer.add("</tr>");
		 writer.add("<tr>");
		 
		 writer.add("<tr>");
		 writer.add("<td  colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 writer.startTableFormRow();
		 writer.addFormRow(Paths._Account._LastCreditReviewDate);
		 writer.endTableFormRow();
		 writer.add("</td>");
		 writer.add("<td style=\"" + CELL_STYLE + "\">");
			
			writer.add("</td>");
		 writer.add("</tr>");
		 writer.add("<tr>");
		 
		 writer.add("<tr>");
		 writer.add("<td  colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 writer.startTableFormRow();
		 writer.addFormRow(Paths._Account._NextCreditReviewDate);
		 writer.endTableFormRow();
		 writer.add("</td>");
		 writer.add("<td style=\"" + CELL_STYLE + "\">");
			
			writer.add("</td>");
		 writer.add("</tr>");
		 writer.add("<tr>");
		 
		 writer.add("<tr>");
		 writer.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		 writer.startTableFormRow();
		 writer.addFormRow(Paths._Account._CreditReviewCycle);
		 writer.endTableFormRow();
		 writer.add("</td>");
		 writer.add("<td style=\"" + CELL_STYLE + "\">");
			
			writer.add("</td>");
		 writer.add("</tr>");
		 writer.add("<tr>");
		 
		 writer.add("<tr>");
		 writer.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		 writer.startTableFormRow();
		 writer.addFormRow(Paths._Account._ParentParty);
		 writer.endTableFormRow();
		 writer.add("</td>");
		 writer.add("<td style=\"" + CELL_STYLE + "\">");
			
			writer.add("</td>");
		 writer.add("</tr>");
		 writer.add("<tr>");
		 
		 writer.add("<tr>");
		 writer.add("<td  colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 writer.startTableFormRow();
		 writer.addFormRow(Paths._Account._EmgLastTrans);
		 writer.endTableFormRow();
		 writer.add("</td>");
		 writer.add("<td style=\"" + CELL_STYLE + "\">");
			
			writer.add("</td>");
		 writer.add("</tr>");
		 writer.add("<tr>");
					 
	    writer.add("<td style=\"" + CELL_STYLE + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._PaymentReceiptMethod);
		writer.endTableFormRow();
		writer.add("</td>");
		writer.add("<td style=\"" + CELL_STYLE + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._PrimaryPayment);
		writer.endTableFormRow();
		writer.add("</td>");
		writer.add("</tr>");
						
	    writer.add("<td style=\"" + CELL_STYLE_LEFT + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._PaymentStartDate);
		writer.endTableFormRow();
		writer.add("</td>");
		writer.add("<td style=\"" + CELL_STYLE_LEFT + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._PaymentEndDate);
		writer.endTableFormRow();
		writer.add("</td>");
		writer.add("</tr>");
							
	    writer.add("<td style=\"" + CELL_STYLE + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._Published);
		writer.endTableFormRow();
		writer.add("</td>");
		writer.add("<td style=\"" + CELL_STYLE + "\">");
		writer.startTableFormRow();
		writer.addFormRow(Paths._Account._BatchCode);
		writer.endTableFormRow();
		writer.add("</td>");
		writer.add("</tr>");
								
		 writer.add("<tr>");
		 writer.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		 writer.startTableFormRow();
		 writer.addFormRow(Paths._Account._DaqaMetaData_TargetRecord);
		 writer.endTableFormRow();
		 writer.add("</td>");
		 writer.add("<td style=\"" + CELL_STYLE + "\">");
			
			writer.add("</td>");
		 writer.add("</tr>");
		 writer.add("<tr>");		

				
		writer.add("</table>");
	

}
}
