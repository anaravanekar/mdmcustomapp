/**
 * 
 */
package com.orchestranetworks.training.mystore.form;

import com.orchestranetworks.ui.UIFormLabelSpec;
import com.orchestranetworks.ui.form.UIForm;
import com.orchestranetworks.ui.form.UIFormBody;
import com.orchestranetworks.ui.form.UIFormContext;
import com.orchestranetworks.ui.form.UIFormHeader;
import com.orchestranetworks.ui.form.UIFormPaneWithTabs;

/**
 * @author rbarfa
 *
 */
public class AccountUIForm  extends UIForm{
	
	
	
	@Override
	public void defineBody(final UIFormBody body, final UIFormContext context) {
		super.defineBody(body, context);		
		//final UIFormPaneWithTabs tabs = new UIFormPaneWithTabs();
		AccountPane pane=new AccountPane();
		
		//AddressPane addressPane=new AddressPane();
		//tabs.addTab("Account",pane);
		//tabs.addTab("RelatedAddresses",addressPane);
		body.setContent(pane);
		
	}
	
	
}
