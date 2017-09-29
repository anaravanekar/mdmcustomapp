/*
 * Copyright Orchestra Networks 2000-2017. All rights reserved.
 */
package com.sereneast.orchestramdm.keysight.mdmcustom.service;

import com.orchestranetworks.service.ServiceKey;
import com.orchestranetworks.ui.selection.EntitySelection;
import com.orchestranetworks.userservice.UserService;
import com.orchestranetworks.userservice.declaration.ActivationContext;
import com.orchestranetworks.userservice.declaration.UserServiceDeclaration;
import com.orchestranetworks.userservice.declaration.UserServicePropertiesDefinitionContext;
import com.orchestranetworks.userservice.declaration.WebComponentDeclarationContext;

/**
 * Declaration of a service.
 */
public abstract class SampleServiceDeclarationAbstract<S extends EntitySelection, U extends ActivationContext<S>>
implements UserServiceDeclaration<S, U>
{
	private final ServiceKey serviceKey;
	private final Class<? extends UserService<S>> implementationClass;
	private final String title;
	private final String description;
	private final String instruction;

	public SampleServiceDeclarationAbstract(
		ServiceKey aServiceKey,
		Class<? extends UserService<S>> anImplementationClass,
			String aTitle,
			String aDescription,
			String anInstruction)
	{
		this.serviceKey = aServiceKey;
		this.implementationClass = anImplementationClass;
		this.title = aTitle;
		this.description = aDescription;
		this.instruction = anInstruction;
	}

	@Override
	public final ServiceKey getServiceKey()
	{
		return this.serviceKey;
	}

	public final Class<? extends UserService<S>> getImplementationClass()
		{
		return this.implementationClass;
		}

	public final String getTitle()
	{
		return this.title;
	}

	public final String getDescription()
	{
		return this.description;
	}

	public final String getInstruction()
	{
		return this.instruction;
	}

	@Override
	public final UserService<S> createUserService()
	{
		try
		{
			return this.implementationClass.newInstance();
		}
		catch (InstantiationException ex)
		{
			throw new RuntimeException(ex);
		}
		catch (IllegalAccessException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	@Override
	public final void defineProperties(UserServicePropertiesDefinitionContext aDefinition)
	{
		aDefinition.setLabel("" + this.title);
		aDefinition.setDescription(this.description);
	}

	@Override
	public final void declareWebComponent(WebComponentDeclarationContext aDeclaration)
	{
		aDeclaration.setAvailableAsPerspectiveAction(true);
	}
}
