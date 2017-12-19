/*
 * Copyright Orchestra Networks 2000-2017. All rights reserved.
 */
package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.orchestranetworks.schema.types.dataspace.DataspaceSet;
import com.orchestranetworks.service.ServiceKey;
import com.orchestranetworks.ui.selection.DatasetEntitySelection;
import com.orchestranetworks.userservice.UserService;
import com.orchestranetworks.userservice.declaration.ActivationContextOnDataset;
import com.orchestranetworks.userservice.declaration.UserServiceDeclaration;

/**
 * Declaration of a table service.
 */
public final class DatasetViewUserServiceDeclaration
		extends
		SampleServiceDeclarationAbstract<DatasetEntitySelection, ActivationContextOnDataset>
		implements UserServiceDeclaration.OnDataset
{

	public DatasetViewUserServiceDeclaration(
			ServiceKey aServiceKey,
			Class<? extends UserService<DatasetEntitySelection>> anImplementationClass,
			String aTitle,
			String aDescription,
			String anInstruction)
	{
		super(aServiceKey, anImplementationClass, aTitle, aDescription, anInstruction);
	}

	@Override
	public void defineActivation(ActivationContextOnDataset aDefinition)
	{
		aDefinition.includeAllDataspaces(DataspaceSet.DataspaceType.BRANCH);
	}
}
