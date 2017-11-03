/*
 * Copyright Orchestra Networks 2000-2008. All rights reserved.
 */
package com.sereneast.orchestramdm.keysight.mdmcustom;

import com.orchestranetworks.module.ModuleContextOnRepositoryStartup;
import com.orchestranetworks.module.ModuleRegistrationServlet;
import com.orchestranetworks.module.ModuleServiceRegistrationContext;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.service.OperationException;
import com.orchestranetworks.service.ServiceKey;
import com.sereneast.orchestramdm.keysight.mdmcustom.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
@SuppressWarnings("serial")
public class RegistrationServlet extends ModuleRegistrationServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationServlet.class);

	@Override
	public void handleRepositoryStartup(ModuleContextOnRepositoryStartup aContext) throws OperationException {
		aContext.addPackagedStyleSheetResource("loading.css");
	}

	@Override
	public void handleServiceRegistration(ModuleServiceRegistrationContext aContext)
	{

		LOGGER.info("In RegistrationServlet. Registering module: mdmcustomapp.");
		// Register custom user service declaration.
		aContext.registerUserService(new TableViewSampleServiceDeclaration(
				ServiceKey.forModuleServiceName("mdmcustomapp",AccountPublishService.class.getSimpleName()),
				AccountPublishService.class,
				"Promote and Publish",
				"Promote and Publish",
				"",-1,
				toPath("/root/Account")));
		aContext.registerUserService(new TableViewSampleServiceDeclaration(
				ServiceKey.forModuleServiceName("mdmcustomapp",AddressPublishService.class.getSimpleName()),
				AddressPublishService.class,
				"Promote and Publish",
				"Promote and Publish",
				"",-1,
				toPath("/root/Address")));
		aContext.registerUserService(new TableViewSampleServiceDeclaration(
				ServiceKey.forModuleServiceName("mdmcustomapp",AlignForeignKeysAccountService.class.getSimpleName()),
				AlignForeignKeysAccountService.class,
				"Align foreign keys",
				"Align foreign keys",
				"",-1,
				toPath("/root/Account")));
		aContext.registerUserService(new TableViewSampleServiceDeclaration(
				ServiceKey.forModuleServiceName("mdmcustomapp",AlignForeignKeysAddressService.class.getSimpleName()),
				AlignForeignKeysAddressService.class,
				"Align foreign keys",
				"Align foreign keys",
				"",-1,
				toPath("/root/Address")));
		aContext.registerUserService(new TableViewSampleServiceDeclaration(
				ServiceKey.forModuleServiceName("mdmcustomapp",ClusterViewService.class.getSimpleName()),
				ClusterViewService.class,
				"Cluster View",
				"Cluster View",
				"",1,
				toPath("/root/Account")));
		aContext.registerUserService(new TableViewSampleServiceDeclaration(
				ServiceKey.forModuleServiceName("mdmcustomapp",CustomMasterDataViewService.class.getSimpleName()),
				CustomMasterDataViewService.class,
				"Custom Master Data View",
				"Custom Master Data View",
				"",-2,
				new Path[]{Paths._Account.getPathInSchema(),Paths._Address.getPathInSchema()}));
/*		aContext.registerUserService(new TableViewSampleServiceDeclaration(
				ServiceKey.forModuleServiceName("mdmcustomapp",AccountPublishRecursiveService.class.getSimpleName()),
				AccountPublishRecursiveService.class,
				"Promote and Publish Recursive",
				"Promote and Publish Recursive",
				"",-1,
				toPath("/root/Account")));
		aContext.registerUserService(new TableViewSampleServiceDeclaration(
				ServiceKey.forModuleServiceName("mdmcustomapp",AddressPublishRecursiveService.class.getSimpleName()),
				AddressPublishRecursiveService.class,
				"Promote and Publish Recursive",
				"Promote and Publish Recursive",
				"",-1,
				toPath("/root/Address")));*/
/*		aContext.registerUserService(new RecordSampleServiceDeclaration(
				ServiceKey.forModuleServiceName("mdmcustomapp",RecordUpdateShowUpdatedByService.class.getSimpleName()),
				RecordUpdateShowUpdatedByService.class,
				"Update Record",
				"Update Record",
				"",
				toPath("/root/Account")));*/
	}
	/*public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ModulesRegister.registerWebApp(this, config);
	}

	public void destroy() {
		ModulesRegister.unregisterWebApp(this, this.getServletConfig());
	}*/

	private static Path[] toPath(String... paths)
	{
		Path[] parsedPaths = new Path[paths.length];
		for (int i = 0; i < paths.length; ++i)
		{
			parsedPaths[i] = Path.parse(paths[i]);
		}

		return parsedPaths;
	}
}