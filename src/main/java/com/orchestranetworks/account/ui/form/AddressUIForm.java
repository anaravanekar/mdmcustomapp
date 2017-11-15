/**
 * 
 */
package com.orchestranetworks.account.ui.form;

import com.orchestranetworks.ui.UIFormLabelSpec;
import com.orchestranetworks.ui.form.*;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddressUIForm extends UIForm{
	private static final Logger LOGGER = LoggerFactory.getLogger(AddressUIForm.class);
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

	@Override
	public void defineBottomBar(UIFormBottomBar aBottomBar, UIFormContext context){
		if(context!=null && context.getCurrentRecord()!=null) {
			LOGGER.debug("session userid:" + context.getSession().getUserReference().getUserId());
			LOGGER.debug("assignedto:" + context.getCurrentRecord().getString(Paths._Address._AssignedTo));
			if (StringUtils.isNotBlank(context.getCurrentRecord().getString(Paths._Address._AssignedTo))
					&& !context.getSession().getUserReference().getUserId().equals(context.getCurrentRecord().getString(Paths._Address._AssignedTo))) {
				aBottomBar.setAllButtonsNotDisplayable();
				aBottomBar.setCloseButtonDisplayable(true);
				return;
			}
		}
	}
}
