/**
 *
 */
package com.orchestranetworks.account.ui.form;

import com.orchestranetworks.ui.UIFormLabelSpec;
import com.orchestranetworks.ui.form.UIForm;
import com.orchestranetworks.ui.form.UIFormBody;
import com.orchestranetworks.ui.form.UIFormContext;
import com.orchestranetworks.ui.form.UIFormHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BusinessPurposeUIForm extends UIForm{
	private static final Logger LOGGER = LoggerFactory.getLogger(BusinessPurposeUIForm.class);
	@Override
	public void defineHeader(final UIFormHeader header, final UIFormContext context) {

		super.defineHeader(header, context);

		header.setTitle(new UIFormLabelSpec("Business Purpose"));

	}

	@Override
	public void defineBody(final UIFormBody body, final UIFormContext context) {
		super.defineBody(body, context);

		BusinessPurposePane pane=new BusinessPurposePane();

		body.setContent(pane);

	}
}
