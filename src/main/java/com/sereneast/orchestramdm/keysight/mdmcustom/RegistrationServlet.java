/*
 * Copyright Orchestra Networks 2000-2008. All rights reserved.
 */
package com.sereneast.orchestramdm.keysight.mdmcustom;

import com.orchestranetworks.module.ModuleRegistrationServlet;
import com.orchestranetworks.module.ModuleServiceRegistrationContext;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.service.ServiceKey;
import com.sereneast.orchestramdm.keysight.mdmcustom.service.RecordSampleServiceDeclaration;
import com.sereneast.orchestramdm.keysight.mdmcustom.service.RecordUpdateShowUpdatedByService;
import com.sereneast.orchestramdm.keysight.mdmcustom.service.RecordWithToolbarSampleService;
import com.sereneast.orchestramdm.keysight.mdmcustom.service.TableViewSampleServiceDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
@SuppressWarnings("serial")
public class RegistrationServlet extends ModuleRegistrationServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationServlet.class);
	@Override
	public void handleServiceRegistration(ModuleServiceRegistrationContext aContext)
	{

		LOGGER.info("In RegistrationServlet. Registering module: mdmcustomapp.");
		// Register custom user service declaration.
		aContext.registerUserService(new RecordSampleServiceDeclaration(
				ServiceKey.forModuleServiceName("mdmcustomapp",RecordUpdateShowUpdatedByService.class.getSimpleName()),
				RecordUpdateShowUpdatedByService.class,
				"Record Update Sample",
				"This user service implements a form that updates a record.",
				"<ul style='margin:0;padding:1em'><li>Create at least one record for table <b>User</b> from the <b>Directory</b> sample data set,</li>"
						+ "<li>From <b>User</b> table view, select exactly one record,</li>"
						+ "<li>Run user service <b>[User Service API] Record Update Sample</b>.</li></ul>",
				toPath("/root/Account")));
		aContext.registerUserService(new TableViewSampleServiceDeclaration(
				ServiceKey.forModuleServiceName("mdmcustomapp",RecordWithToolbarSampleService.class.getSimpleName()),
				RecordWithToolbarSampleService.class,
				"Publish Records",
				"Publish records",
				"",-1,
				toPath("/root/Account")));
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