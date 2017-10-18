/**
 * 
 */
package com.orchestranetworks.account.ui.form;

import com.orchestranetworks.account.ui.form.AccountPane;
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
	public void defineHeader(final UIFormHeader header, final UIFormContext context) {
	
	super.defineHeader(header, context);
	
	header.setTitle(new UIFormLabelSpec("Account"));
	
	}
	
	@Override
	public void defineBody(final UIFormBody body, final UIFormContext context) {
		super.defineBody(body, context);		
	
		AccountPane pane=new AccountPane();

		body.setContent(pane);
		
	}
	
	
}
