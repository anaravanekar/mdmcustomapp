package com.orchestranetworks.account.ui.form;

import com.orchestranetworks.ui.form.UIFormContext;
import com.orchestranetworks.ui.form.UIFormPane;
import com.orchestranetworks.ui.form.UIFormPaneWriter;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;

public class AccountPane implements UIFormPane {
	public static final String CELL_STYLE_LEFT = "width:25%;  vertical-align:top;";
	public static final String CELL_STYLE_RIGHT = "width:25%; vertical-align:top;text-align:right;";
	public static final String TABLE_STYLE = "width:100%";
	
	@Override
	public void writePane(UIFormPaneWriter writer, UIFormContext context) {
		
		writer.add("<table width=\"50%\" >");
		
		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._AssignedTo);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._AssignedTo);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_RIGHT + "\">");
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 writer.add("</td>");	
		writer.add("</tr>");
		
		
		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._CustomerType);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._CustomerType);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_RIGHT + "\">");
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 writer.add("</td>");	
		writer.add("</tr>");
		// row end 
		
		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._AccountName);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._AccountName);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_RIGHT + "\">");
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 writer.add("</td>");	
		writer.add("</tr>");
		// row end 
		
		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._Country);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._Country);
		writer.add("</td>");
		
		writer.add("</tr>");
		// row end 
		
		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._Notes);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._Notes);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_RIGHT + "\">");
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 writer.add("</td>");	
		writer.add("</tr>");
		// row end 

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._AccountType);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._AccountType);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._AccountDescription);
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 writer.addWidget(Paths._Account._AccountDescription);
		 writer.add("</td>");	
		writer.add("</tr>");
		// row end 
		 
		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._Alias);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_LEFT + "\"><font color=\"#606060\">");
		writer.addWidget(Paths._Account._Alias);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"> <font color=\"#606060\">");
		writer.addLabel(Paths._Account._NamePronunciation);
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 writer.addWidget(Paths._Account._NamePronunciation);
		 writer.add("</td>");	
		writer.add("</tr>");
		 
		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._Status);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._Status);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_RIGHT + "\">");
		
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		
		 writer.add("</td>");	
		writer.add("</tr>");
		
		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._SystemName);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._SystemName);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._SystemId);
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 writer.addWidget(Paths._Account._SystemId);
		 writer.add("</td>");	
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._RelatedAddress);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._RelatedAddress);
		writer.add("</td>");
		
		writer.add("</tr>");
			

		
		
		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._RegistryId);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._RegistryId);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_RIGHT + "\">");
	
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 
		 writer.add("</td>");	
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._ProfileClass);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._ProfileClass);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_RIGHT + "\">");

		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");

		writer.add("</td>");
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._GroupingId);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._GroupingId);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._GroupingDescription);
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 writer.addWidget(Paths._Account._GroupingDescription);
		 writer.add("</td>");	
		writer.add("</tr>");

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._ISGClassification);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._ISGClassification);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\">");
		
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 
		 writer.add("</td>");	
		writer.add("</tr>");	 

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._TaxpayerId);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._TaxpayerId);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._TaxRegistrationNumber);
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 writer.addWidget(Paths._Account._TaxRegistrationNumber);
		 writer.add("</td>");	
		writer.add("</tr>");	 

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._Region);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._Region);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_RIGHT + "\">");
		
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 writer.add("</td>");	
		writer.add("</tr>");	 

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._Classification);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._Classification);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\">");
		
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 writer.add("</td>");	
		writer.add("</tr>");	

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._SalesChannel);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._SalesChannel);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_RIGHT + "\">");
		
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 writer.add("</td>");	
		writer.add("</tr>");	
		 
		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._NLSLanguageCode);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._NLSLanguageCode);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._NameLocalLanguage);
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 writer.addWidget(Paths._Account._NameLocalLanguage);
		 writer.add("</td>");	
		writer.add("</tr>");	
				 

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._CustomerScreening);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._CustomerScreening);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_RIGHT + "\">");
		
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 
		 writer.add("</td>");	
		writer.add("</tr>");	
		
		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._LastCreditReviewDate);
		writer.add("</td>");
		writer.add("<td colspan=\"2\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._LastCreditReviewDate);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_RIGHT + "\">");
		
		 writer.add("</td>");
	
		writer.add("</tr>");	
		
		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._NextCreditReviewDate);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._NextCreditReviewDate);
		writer.add("</td>");
		writer.add("<td colspan=\"2\" style=\"" + CELL_STYLE_RIGHT + "\">");
		
	
		 
		 writer.add("</td>");	
		writer.add("</tr>");		 

		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._CreditReviewCycle);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._CreditReviewCycle);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_RIGHT + "\">");
		
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 
		 writer.add("</td>");	
		writer.add("</tr>");	
		
		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._ParentParty);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._ParentParty);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_RIGHT + "\">");
		
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 
		 writer.add("</td>");	
		writer.add("</tr>");	
		 
		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._EmgLastTrans);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._EmgLastTrans);
				
		 
		 writer.add("</td>");	
		writer.add("</tr>");	
		 
		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._PaymentReceiptMethod);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._PaymentReceiptMethod);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._PrimaryPayment);
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 writer.addWidget(Paths._Account._PrimaryPayment);
		 writer.add("</td>");	
		writer.add("</tr>");	
		
		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._PaymentStartDate);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._PaymentStartDate);
	
		
		
		 writer.add("</td>");	
		writer.add("</tr>");	
	    
		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._Published);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._Published);
		writer.add("</td>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._BatchCode);
		 writer.add("</td>");
		 writer.add("<td colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		 writer.addWidget(Paths._Account._BatchCode);
		 writer.add("</td>");	
		writer.add("</tr>");	
		
		writer.add("<tr>");
		writer.add("<td colspan=\"1\" nowrap=\"nowrap\" style=\"" + CELL_STYLE_RIGHT + "\"><font color=\"#606060\">");
		writer.addLabel(Paths._Account._MergedTargetRecord);
		writer.add("</td>");
		writer.add("<td colspan=\"3\" style=\"" + CELL_STYLE_LEFT + "\">");
		writer.addWidget(Paths._Account._MergedTargetRecord);
		writer.add("</td>");
		writer.add("</tr>");
		
					
		writer.add("</table>");
	

}
}
