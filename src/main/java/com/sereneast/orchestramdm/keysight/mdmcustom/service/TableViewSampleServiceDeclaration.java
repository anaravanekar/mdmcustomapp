/*
 * Copyright Orchestra Networks 2000-2017. All rights reserved.
 */
package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.onwbp.adaptation.AdaptationName;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.schema.types.dataset.DatasetSet;
import com.orchestranetworks.schema.types.dataspace.DataspaceSet;
import com.orchestranetworks.service.ServiceKey;
import com.orchestranetworks.ui.selection.TableViewEntitySelection;
import com.orchestranetworks.userservice.UserService;
import com.orchestranetworks.userservice.declaration.ActivationContextOnTableView;
import com.orchestranetworks.userservice.declaration.UserServiceDeclaration;

/**
 * Declaration of a table service.
 */
public final class TableViewSampleServiceDeclaration
extends
        SampleServiceDeclarationAbstract<TableViewEntitySelection, ActivationContextOnTableView>
implements UserServiceDeclaration.OnTableView
{
	private final int selectedRecordCount;
	private final Path[] tables;
	private final AdaptationName dataset;

	/**
	 * Possible values for <code>aSelectedRecordCount</code> are:
	 * <ul>
	 * 	<li> >= 1: Selection count must exactly match this value,</li>
	 *  <li> == 0: Any selection is accepted,</li>
	 *  <li> == -1: At least one record must be selected.
	 * </ul>
	 */
	public TableViewSampleServiceDeclaration(
		ServiceKey aServiceKey,
		Class<? extends UserService<TableViewEntitySelection>> anImplementationClass,
			String aTitle,
			String aDescription,
			String anInstruction,
			int aSelectedRecordCount,
			AdaptationName dataset,
			Path... tables)
	{
		super(aServiceKey, anImplementationClass, aTitle, aDescription, anInstruction);

		this.selectedRecordCount = aSelectedRecordCount;
		this.tables = tables.clone();
		this.dataset = dataset;
	}

	@Override
	public void defineActivation(ActivationContextOnTableView aDefinition)
	{
		// activates the service in all dataspaces except the "Reference" branch.
		aDefinition.includeAllDataspaces(DataspaceSet.DataspaceType.BRANCH);
		aDefinition.excludeDataspacesMatching(Repository.REFERENCE, DataspaceSet.DataspaceChildrenPolicy.NONE);
		aDefinition.includeSchemaNodesMatching(this.tables);
		aDefinition.includeDatasetsMatching(dataset, DatasetSet.DatasetChildrenPolicy.NONE);

		if (this.selectedRecordCount >= 1)
		{
			aDefinition.limitRecordSelection(this.selectedRecordCount);
		}
		else if (this.selectedRecordCount == -1)
		{
			aDefinition.forbidEmptyRecordSelection();
		}
	}
}
