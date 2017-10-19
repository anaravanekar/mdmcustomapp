/**
 * 
 */
package com.orchestranetworks.account.ui.form;

import com.orchestranetworks.ui.UIFormLabelSpec;
import com.orchestranetworks.ui.form.UIForm;
import com.orchestranetworks.ui.form.UIFormBody;
import com.orchestranetworks.ui.form.UIFormContext;
import com.orchestranetworks.ui.form.UIFormHeader;

public class AddressUIForm extends UIForm{
	
	@Override
	public void defineHeader(final UIFormHeader header, final UIFormContext context) {
	
	super.defineHeader(header, context);
	
	header.setTitle(new UIFormLabelSpec("Address"));
	
	}
	
	@Override
	public void defineBody(final UIFormBody body, final UIFormContext context) {
		super.defineBody(body, context);		
	
		AddressPane pane=new AddressPane();

		body.setContent(pane);
		
	}
	
	
}
