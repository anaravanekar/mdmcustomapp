/*
 * Copyright Orchestra Networks 2000-2017. All rights reserved.
 */
package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.orchestranetworks.schema.Path;
import com.orchestranetworks.service.ServiceKey;
import com.orchestranetworks.ui.selection.RecordEntitySelection;
import com.orchestranetworks.userservice.UserService;
import com.orchestranetworks.userservice.declaration.ActivationContextOnRecord;
import com.orchestranetworks.userservice.declaration.UserServiceDeclaration;

/**
 * Declaration of a table service.
 */
public final class RecordSampleServiceDeclaration
	extends
	SampleServiceDeclarationAbstract<RecordEntitySelection, ActivationContextOnRecord>
	implements UserServiceDeclaration.OnRecord
{
	private final Path[] tables;

	public RecordSampleServiceDeclaration(
		ServiceKey aServiceKey,
		Class<? extends UserService<RecordEntitySelection>> anImplementationClass,
		String aTitle,
		String aDescription,
		String anInstruction,
		Path... tables)
	{
		super(aServiceKey, anImplementationClass, aTitle, aDescription, anInstruction);

		this.tables = tables.clone();
	}

	@Override
	public void defineActivation(ActivationContextOnRecord aDefinition)
	{
		for (Path tablePath : this.tables)
		{
			aDefinition.includeSchemaNodesMatching(tablePath);
		}
	}
}
