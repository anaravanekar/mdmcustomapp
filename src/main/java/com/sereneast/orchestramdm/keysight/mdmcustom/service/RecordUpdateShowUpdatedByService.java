/*
 * Copyright Orchestra Networks 2000-2016. All rights reserved.
 */
package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.Adaptation;
import com.orchestranetworks.service.Procedure;
import com.orchestranetworks.service.ProcedureResult;
import com.orchestranetworks.service.ProgrammaticService;
import com.orchestranetworks.service.ValueContextForUpdate;
import com.orchestranetworks.ui.selection.RecordEntitySelection;
import com.orchestranetworks.userservice.*;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths._Account;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.ModifiedByStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class RecordUpdateShowUpdatedByService implements UserService<RecordEntitySelection>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RecordUpdateShowUpdatedByService.class);
	private final static ObjectKey accountObjectKey = ObjectKey.forName("account");
	private final static ObjectKey addressObjectKey = ObjectKey.forName("address");

	public static final String CELL_STYLE = "width:80%; padding:5px; vertical-align:top;text-align:right;";
	public static final String CELL_STYLE_LEFT = "width:80%; padding:5px; vertical-align:top;";
	public static final String ADDRESS_STYLE = "margin:10px";

	public RecordUpdateShowUpdatedByService()
	{
	}

	/**
	 * Sets up the object context.
	 */
	@Override
	public void setupObjectContext(
			UserServiceSetupObjectContext<RecordEntitySelection> aContext,
			UserServiceObjectContextBuilder aBuilder)
	{
		LOGGER.info("Setup Object Context called: "+aContext.getSession().getUserReference().getUserId());
		if (aContext.isInitialDisplay())
		{
			LOGGER.info("is Initial Display: "+aContext.getSession().getUserReference().getUserId());
			Adaptation record = aContext.getEntitySelection().getRecord();
/*			Procedure procedure = aContext1 -> {
                ValueContextForUpdate valueContextForUpdate = aContext1.getContext(record.getAdaptationName());
                valueContextForUpdate.setValue(aContext1.getSession().getUserReference().getUserId(), _Account._AccountName);//TODO change
				aContext1.doModifyContent(record,valueContextForUpdate);
            };
			ProgrammaticService svc = ProgrammaticService.createForSession(aContext.getSession(), record.getHome());
			ProcedureResult result = svc.execute(procedure);
			if (result.hasFailed()) {
				LOGGER.info("proc failed "+result.getExceptionFullMessage(Locale.ENGLISH));
			}else{
				LOGGER.info("proc success ");
			}*/
			aBuilder.registerRecordOrDataSet(accountObjectKey, record);
		}
	}
	/**
	 * Sets up the displays.
	 */
	@Override
	public void setupDisplay(
			UserServiceSetupDisplayContext<RecordEntitySelection> aContext,
			UserServiceDisplayConfigurator aConfigurator)
	{
		/*aConfigurator.setDefaultButtons(new UserServiceEvent()
		{
			@Override
			public UserServiceEventOutcome processEvent(UserServiceEventContext aContext)
			{
				return RecordUpdateShowUpdatedByService.this.onSave(aContext);
			}
		});*/

		aConfigurator.setContent(new UserServicePane()
		{
			@Override
			public void writePane(UserServicePaneContext aContext, UserServicePaneWriter aWriter)
			{
				RecordUpdateShowUpdatedByService.this.writePane(aContext, aWriter);
			}
		});
	}

	@Override
	public void validate(UserServiceValidateContext<RecordEntitySelection> aContext)
	{
		// Do nothing.
	}

	@Override
	public UserServiceEventOutcome processEventOutcome(
			UserServiceProcessEventOutcomeContext<RecordEntitySelection> aContext,
			UserServiceEventOutcome anEventOutcome)
	{
		return anEventOutcome;
	}

	/**
	 * Displays the pane.
	 */
	private void writePane(UserServicePaneContext aContext, UserServicePaneWriter aWriter)
	{
		String userId = aContext.getSession().getUserReference().getUserId();
		String className = aContext.getValueContext(accountObjectKey).getValue().getClass().getSimpleName();
		String rec = aContext.getValueContext(accountObjectKey).toString();
		String node = aContext.getValueContext(accountObjectKey).getNode().toString();
		String openedByUser = aContext.getValueContext(accountObjectKey).getValue(_Account._AssignedTo)!=null?aContext.getValueContext(accountObjectKey).getValue(_Account._AssignedTo).toString():null;//ModifiedByStore.processRecordModifiedBy("GET",aContext.getValueContext(accountObjectKey).getValue(_Account._MDMAccountId).toString(),null);
		LOGGER.info("\n\n\n\nuserId: "+userId+" className: "+className+" rec: "+rec+" node: "+node);
		if(openedByUser!=null) {
			aWriter.add("<b>Note: Record has been opened for modification by : " +openedByUser+"</b>");
		}else{
			/*String recordId = aContext.getValueContext(accountObjectKey).getValue(_Account._MDMAccountId).toString();
			LOGGER.info("\n\n\nrecordId: "+recordId);
			ModifiedByStore.processRecordModifiedBy("PUT",recordId,userId);
			aWriter.add("userId: "+userId+" recid: " +recordId);*/
		}

//		aWriter.startTableFormRow();
		aWriter.setCurrentObject(accountObjectKey);
		aWriter.add("<table>");

		aWriter.add("<tr>");
		aWriter.add("<td colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._AssignedTo);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td>");
		aWriter.add("</td>");
		aWriter.add("</tr>");
		// row end

		aWriter.add("<tr>");
		aWriter.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._CustomerType);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td>");
		aWriter.add("</td>");
		aWriter.add("</tr>");
		aWriter.add("<tr>");


		aWriter.add("<td style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._AccountName);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");

		aWriter.add("</td>");
		aWriter.add("</tr>");

		aWriter.add("<tr>");
		aWriter.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._Country);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("</tr>");
		aWriter.add("<tr>");

		aWriter.add("<tr>");
		aWriter.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._Notes);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");

		aWriter.add("</td>");
		aWriter.add("</tr>");

		aWriter.add("<tr>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._AccountType);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._AccountDescription);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("</tr>");

		aWriter.add("<td style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._Alias);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._NamePronunciation);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("</tr>");

		aWriter.add("<tr>");
		aWriter.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._Status);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");

		aWriter.add("</td>");
		aWriter.add("</tr>");
		aWriter.add("<tr>");

		aWriter.add("<td style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._SystemName);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._SystemId);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("</tr>");

		aWriter.add("<tr>");
		aWriter.add("<td colspan=\"2\">");
		aWriter.add("<div style=\"" + ADDRESS_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._RelatedAddress);
		aWriter.endTableFormRow();
		aWriter.add("</div>");
		aWriter.add("</tr>");

		aWriter.add("<tr>");
		aWriter.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._RegistryId);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");

		aWriter.add("</td>");
		aWriter.add("</tr>");
		aWriter.add("<tr>");

		aWriter.add("<td style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._GroupingId);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._GroupingDescription);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("</tr>");

		aWriter.add("<tr>");
		aWriter.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._ISGClassification);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");

		aWriter.add("</td>");
		aWriter.add("</tr>");
		aWriter.add("<tr>");

		aWriter.add("<td style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._TaxpayerId);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._TaxRegistrationNumber);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("</tr>");

		aWriter.add("<tr>");
		aWriter.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._Region);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");

		aWriter.add("</td>");
		aWriter.add("</tr>");
		aWriter.add("<tr>");

		aWriter.add("<tr>");
		aWriter.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._Classification);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");

		aWriter.add("</td>");
		aWriter.add("</tr>");
		aWriter.add("<tr>");

		aWriter.add("<tr>");
		aWriter.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._SalesChannel);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");

		aWriter.add("</td>");
		aWriter.add("</tr>");
		aWriter.add("<tr>");

		aWriter.add("<td style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._NLSLanguageCode);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._NameLocalLanguage);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("</tr>");

		aWriter.add("<tr>");
		aWriter.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._CustomerScreening);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");

		aWriter.add("</td>");
		aWriter.add("</tr>");
		aWriter.add("<tr>");

		aWriter.add("<tr>");
		aWriter.add("<td  colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._LastCreditReviewDate);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");

		aWriter.add("</td>");
		aWriter.add("</tr>");
		aWriter.add("<tr>");

		aWriter.add("<tr>");
		aWriter.add("<td  colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._NextCreditReviewDate);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");

		aWriter.add("</td>");
		aWriter.add("</tr>");
		aWriter.add("<tr>");

		aWriter.add("<tr>");
		aWriter.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._CreditReviewCycle);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");

		aWriter.add("</td>");
		aWriter.add("</tr>");
		aWriter.add("<tr>");

		aWriter.add("<tr>");
		aWriter.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._ParentParty);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");

		aWriter.add("</td>");
		aWriter.add("</tr>");
		aWriter.add("<tr>");

		aWriter.add("<tr>");
		aWriter.add("<td  colspan=\"1\" style=\"" + CELL_STYLE_LEFT + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._EmgLastTrans);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");

		aWriter.add("</td>");
		aWriter.add("</tr>");
		aWriter.add("<tr>");

		aWriter.add("<td style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._PaymentReceiptMethod);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._PrimaryPayment);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("</tr>");

		aWriter.add("<td style=\"" + CELL_STYLE_LEFT + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._PaymentStartDate);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE_LEFT + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._PaymentEndDate);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("</tr>");

		aWriter.add("<td style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._Published);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._BatchCode);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("</tr>");

		aWriter.add("<tr>");
		aWriter.add("<td  colspan=\"1\" style=\"" + CELL_STYLE + "\">");
		aWriter.startTableFormRow();
		aWriter.addFormRow(Paths._Account._DaqaMetaData_TargetRecord);
		aWriter.endTableFormRow();
		aWriter.add("</td>");
		aWriter.add("<td style=\"" + CELL_STYLE + "\">");

		aWriter.add("</td>");
		aWriter.add("</tr>");
		aWriter.add("<tr>");


		aWriter.add("</table>");
//		aWriter.endTableFormRow();

	}

	/**
	 * <p>This method is called when use clicks "save" or "save and close" buttons.</p>
	 *
	 * <p>The returned value is used only for if user clicks on button "save
	 * and close" and ignored if user clicks on button "save".</p>
	 *
	 * <p>When user clicks button "save and close", return code will either
	 * close current user service or redisplay it perhaps because of an error.</p>
	 */
	private UserServiceEventOutcome onSave(UserServiceEventContext aContext)
	{
		ProcedureResult result = aContext.save(addressObjectKey);
		if (result.hasFailed())
		{
			aContext.addError("Save failed");
			return null; //Redisplay the service.
		}

		return UserServiceNext.nextClose(); // The user service can close.
	}
}
