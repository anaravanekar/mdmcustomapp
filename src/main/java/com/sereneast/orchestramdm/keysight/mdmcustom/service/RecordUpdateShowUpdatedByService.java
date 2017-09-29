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
			Procedure procedure = aContext1 -> {
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
			}
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
		aConfigurator.setDefaultButtons(new UserServiceEvent()
		{
			@Override
			public UserServiceEventOutcome processEvent(UserServiceEventContext aContext)
			{
				return RecordUpdateShowUpdatedByService.this.onSave(aContext);
			}
		});

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
		String openedByUser = ModifiedByStore.processRecordModifiedBy("GET",aContext.getValueContext(accountObjectKey).getValue(_Account._MDMAccountId).toString(),null);
		LOGGER.info("\n\n\n\nuserId: "+userId+" className: "+className+" rec: "+rec+" node: "+node);
		if(openedByUser!=null) {
			aWriter.add("Note: Record has been opened for modification by : " +openedByUser);
		}else{
			String recordId = aContext.getValueContext(accountObjectKey).getValue(_Account._MDMAccountId).toString();
			LOGGER.info("\n\n\nrecordId: "+recordId);
			ModifiedByStore.processRecordModifiedBy("PUT",recordId,userId);
			aWriter.add("userId: "+userId+" recid: " +recordId);
		}
//		ModifiedByStore.processRecordModifiedBy("PUT",)
		aWriter.startTableFormRow();

		/*// Display the directory company name data set field in read only mode.
		aWriter.setCurrentObject(accountObjectKey);
		UIWidget company = aWriter.newBestMatching(Paths._Account);
		company.setEditorDisabled(true);*/

		// Display person
		aWriter.setCurrentObject(accountObjectKey);

		aWriter.addFormRow(_Account._AccountName);
		aWriter.addFormRow(_Account._AccountNumber);
		aWriter.addFormRow(_Account._AccountType);
		aWriter.addFormRow(_Account._Alias);
		aWriter.addFormRow(_Account._AccountDescription);
//		aWriter.addFormRow(_Account._CalcAccountName);
		aWriter.addFormRow(_Account._Classification);
		aWriter.addFormRow(_Account._CreditReviewCycle);
		aWriter.addFormRow(_Account._CustomerCategory);
		aWriter.addFormRow(_Account._CustomerScreening);
		aWriter.addFormRow(_Account._CustomerType);
		aWriter.addFormRow(_Account._EmgLastTrans);
		aWriter.addFormRow(_Account._GroupingDescription);
		aWriter.addFormRow(_Account._GroupingId);
		aWriter.addFormRow(_Account._IXIAClassification);
		aWriter.addFormRow(_Account._LastCreditReviewDate);
		aWriter.addFormRow(_Account._MDMAccountId);
		aWriter.addFormRow(_Account._NameLocalLanguage);
		aWriter.addFormRow(_Account._NamePronunciation);
		aWriter.addFormRow(_Account._NextCreditReviewDate);
		aWriter.addFormRow(_Account._NLSLanguageCode);
		aWriter.addFormRow(_Account._ProfileClass);
		aWriter.addFormRow(_Account._Reference);
		aWriter.addFormRow(_Account._Region);
		aWriter.addFormRow(_Account._RegistryId);
		aWriter.addFormRow(_Account._RelatedAddress);
		aWriter.addFormRow(_Account._SalesChannel);
		aWriter.addFormRow(_Account._Status);
		aWriter.addFormRow(_Account._SystemId);
		aWriter.addFormRow(_Account._SystemName);
		aWriter.addFormRow(_Account._TaxpayerId);
		aWriter.endTableFormRow();
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
