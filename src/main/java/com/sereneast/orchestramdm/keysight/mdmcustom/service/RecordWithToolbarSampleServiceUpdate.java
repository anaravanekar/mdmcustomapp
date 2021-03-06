/*
 * Copyright Orchestra Networks 2000-2016. All rights reserved.
 */
package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.Request;
import com.onwbp.adaptation.RequestResult;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.ui.UICSSClasses;
import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.*;
import com.sereneast.orchestramdm.keysight.mdmcustom.Paths;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraContent;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObject;
import com.sereneast.orchestramdm.keysight.mdmcustom.model.OrchestraObjectList;
import com.sereneast.orchestramdm.keysight.mdmcustom.util.ApplicationCacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This user service implements a form that allows user to update a selected
 * set of records using toolbar's next and previous buttons.
 *
 * <p>
 * To execute this user service:
 * <ul>
 *  <li>Create a data set using packaged data model
 *      <i>/WEB-INF/ebx/schemas/userservice/Directory.xsd</i>,</li>
 *  <li>From table <i>User</i>, select one or more rows and run user service
 *      <i>[UserService API] Display selected records</i>.</li>
 * </ul>
 * </p>
 */
public class RecordWithToolbarSampleServiceUpdate implements UserService<TableViewEntitySelection>,ApplicationContextAware
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RecordWithToolbarSampleServiceUpdate.class);
	private ApplicationContext applicationContext;
	private String objectName;
	private final static String TOOLBAR_NAME = "Publish";

	private final static ObjectKey accountObjectKey = ObjectKey.forName("account");

	private int recordIndex;
	private boolean isNextRecord;
	private boolean isFetchCurrentRecord;

	private ApplicationCacheUtil applicationCacheUtil;

	public RecordWithToolbarSampleServiceUpdate()
	{
	}

	/**
	 * The object context is modified when a different record is selected, for
	 * example after a click on next or on current record.
	 */
	@Override
	public void setupObjectContext(
			UserServiceSetupObjectContext<TableViewEntitySelection> aContext,
			UserServiceObjectContextBuilder aBuilder)

	{
		if (aContext.isInitialDisplay())
		{
			RequestResult requestResult = aContext.getEntitySelection().getSelectedRecords().execute();
			LOGGER.debug("no of records selected = "+requestResult.getSize());
			this.fetchFirstRecord(aContext.getEntitySelection(), aBuilder);
			this.isFetchCurrentRecord = false;
			return;
		}

		if (!this.isFetchCurrentRecord)
		{
			return;
		}
		this.isFetchCurrentRecord = false;

		this.fetchCurrentRecord(aContext.getEntitySelection(), aBuilder);
	}

	/**
	 * Sets up the display.
	 */
	@Override
	public void setupDisplay(
			UserServiceSetupDisplayContext<TableViewEntitySelection> aContext,
			UserServiceDisplayConfigurator aConfigurator)

	{
		if (this.recordIndex < 0)
		{
			// Sets content to error page.
			aConfigurator.setContent(new UserServicePane()
			{
				@Override
				public void writePane(UserServicePaneContext aContext, UserServicePaneWriter aWriter)
				{
					RecordWithToolbarSampleServiceUpdate.this.writeError(aContext, aWriter);
				}
			});

			aConfigurator.setLeftButtons(aConfigurator.newCloseButton());
			return;
		}

		if (this.recordIndex >= 1)
		{
			aConfigurator.setToolbarPreviousCallback(new UserServiceEvent()
			{
				@Override
				public UserServiceEventOutcome processEvent(UserServiceEventContext aContext)
				{
					return RecordWithToolbarSampleServiceUpdate.this.onPrevious(aContext);
				}
			});
		}

		if (this.isNextRecord)
		{
			aConfigurator.setToolbarNextCallback(new UserServiceEvent()
			{
				@Override
				public UserServiceEventOutcome processEvent(UserServiceEventContext aContext)
				{
					return RecordWithToolbarSampleServiceUpdate.this.onNext(aContext);
				}
			});
		}

		// Set bottom bar.
		aConfigurator.setDefaultButtons(new UserServiceEvent()
		{
			@Override
			public UserServiceEventOutcome processEvent(UserServiceEventContext aContext)
			{
				return RecordWithToolbarSampleServiceUpdate.this.onSave(aContext);
			}
		});

		// Sets content.
		aConfigurator.setContent(new UserServicePane()
		{
			@Override
			public void writePane(UserServicePaneContext aContext, UserServicePaneWriter aWriter)
			{
				RecordWithToolbarSampleServiceUpdate.this.writeForm(aContext, aWriter);
			}
		});
	}

	@Override
	public void validate(UserServiceValidateContext<TableViewEntitySelection> aContext)
	{
		// Not used.

	}

	@Override
	public UserServiceEventOutcome processEventOutcome(
			UserServiceProcessEventOutcomeContext<TableViewEntitySelection> aContext,
			UserServiceEventOutcome anEventOutcome)
	{
		return anEventOutcome;
	}

	/**
	 * Displays the form.
	 */
	private void writeForm(UserServicePaneContext aContext, UserServicePaneWriter aWriter)
	{
		try {
			List<OrchestraObject> orchestraObjects = new ArrayList<OrchestraObject>();
			List<Adaptation> adaptations = aContext.getValueContext(accountObjectKey).getAdaptationTable().selectOccurrences(null);
			LOGGER.info("Selected records size : " + adaptations.size());
			OrchestraObject orchestraObject = new OrchestraObject();
			Map<String, OrchestraContent> jsonFieldsMap = new HashMap<>();
			Map<String, Path> pathFieldsMap = null;
			if(applicationCacheUtil==null){
				LOGGER.debug("Initializing application cache util");
				applicationCacheUtil = new ApplicationCacheUtil();
			}
			if ("ACCOUNT".equals(objectName)) {
				pathFieldsMap = applicationCacheUtil.getObjectDirectFields(Paths._Account.class.getName());
			} else if ("ADDRESS".equals(objectName)) {
				pathFieldsMap = applicationCacheUtil.getObjectDirectFields(Paths._Address.class.getName());
			}
			for (Adaptation adaptation : adaptations) {
				for (String fieldName : pathFieldsMap.keySet()) {
					LOGGER.debug(fieldName);
					LOGGER.debug(String.valueOf(pathFieldsMap.get(fieldName)));
					jsonFieldsMap.put(fieldName, new OrchestraContent(adaptation.get(pathFieldsMap.get(fieldName))));
				}
			}
			orchestraObject.setContent(jsonFieldsMap);
			orchestraObjects.add(orchestraObject);
			ObjectMapper mapper = new ObjectMapper();
			LOGGER.info("Selected records: \n" + mapper.writeValueAsString(orchestraObjects));
			OrchestraObjectList rows = new OrchestraObjectList();
			rows.setRows(orchestraObjects);
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("updateOrInsert", "true");
			try {
				/*OrchestraResponseDetails responseDetails = orchestraRestClient.insert(DATA_SPACE, DATA_SET, PATH, rows, parameters);
				final Procedure procedure = new Procedure() {
					public void execute(final ProcedureContext procedureContext) throws Exception {
						LOGGER.info("Deleting records...");
						for (Adaptation adaptation : adaptations) {
							final AdaptationName adaptationName = adaptation.getAdaptationName();
							LOGGER.info("To delete: "+adaptationName);
							procedureContext.doDelete(adaptationName, false);
						}
					}
				};
				LOGGER.info("Executing procedure");
				context.execute(procedure);
				LOGGER.info("Procedure Executed");*/
			} catch (Exception e) {
				LOGGER.error("Error making rest call ", e);
			}
			Files.write(java.nio.file.Paths.get("publishedrecords.txt"), mapper.writeValueAsString(rows).getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
			aWriter.add("Publish successful");
		}catch (ClassNotFoundException | IllegalAccessException | IOException e){
			LOGGER.error("Error publishing ",e);
		}
	}

	/**
	 * Displays the an error pane.
	 */
	private void writeError(UserServicePaneContext aContext, UserServicePaneWriter aWriter)
	{
		aWriter.add("<div ");
		aWriter.addSafeAttribute("class", UICSSClasses.CONTAINER_WITH_TEXT_PADDING);
		aWriter.add(">");
		aWriter.add("<span ");
		aWriter.addSafeAttribute("class", UICSSClasses.TEXT.ERROR);
		aWriter.add(">");
		aWriter.add("Error: no record found.");
		aWriter.add("</span>");
		aWriter.add("</div>");
	}

	/**
	 * <p>This method is called when use clicks "save" or "save and close" buttons.</p>
	 *
	 * <p>The returned value is used only for if user clicks on button "save and close" and ignored if user clicks on button "save".</p>
	 *
	 * <p>When user clicks button "save and close", return code will either close current user service or redisplay it
	 * perhaps because of an error.</p>
	 */
	private UserServiceEventOutcome onSave(UserServiceEventContext aContext)
	{
		return UserServiceNext.nextClose(); // The user service can close.
	}

	private UserServiceEventOutcome onPrevious(UserServiceEventContext aContext)
	{
		if (this.recordIndex > 0)
		{
			--this.recordIndex;
			this.isFetchCurrentRecord = true;
		}

		return null;
	}

	private UserServiceEventOutcome onNext(UserServiceEventContext aContext)
	{
		++this.recordIndex;
		this.isFetchCurrentRecord = true;

		return null;
	}

	private void fetchFirstRecord(
			TableViewEntitySelection anEntitySelection,
			UserServiceObjectContextBuilder aBuilder)
	{
		Request request = anEntitySelection.getSelectedRecords();
		RequestResult result = request.execute();
		try
		{
			Adaptation record = result.nextAdaptation();
			if (record == null)
			{
				this.isNextRecord = false;
				return;
			}

			aBuilder.registerRecordOrDataSet(accountObjectKey, record);
			this.recordIndex = 0;

			this.isNextRecord = (result.nextAdaptation() != null);
		}
		finally
		{
			result.close();
		}
	}

	private void fetchCurrentRecord(
			TableViewEntitySelection anEntitySelection,
			UserServiceObjectContextBuilder aBuilder)
	{
		Request request = anEntitySelection.getSelectedRecords();
		RequestResult result = request.execute();
		try
		{
			Adaptation record = null;
			int index = -1;
			while (index < this.recordIndex)
			{
				Adaptation rec = result.nextAdaptation();
				if (rec == null)
				{
					break;
				}

				record = rec;
				++index;
			}

			if (record == null)
			{
				aBuilder.unregisterObject(accountObjectKey);
				this.recordIndex = -1;
				this.isNextRecord = false;
			}
			else
			{
				aBuilder.registerRecordOrDataSet(accountObjectKey, record);
				this.recordIndex = index;
				this.isNextRecord = (result.nextAdaptation() != null);
			}
		}
		finally
		{
			result.close();
		}
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName=objectName;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		LOGGER.debug("Setting application context");
		this.applicationContext = applicationContext;
		if(applicationContext!=null) {
			LOGGER.debug("Got application context initializing cacheutil");
			applicationCacheUtil = (ApplicationCacheUtil) applicationContext.getBean("applicationCacheUtil");
		}

	}
}
