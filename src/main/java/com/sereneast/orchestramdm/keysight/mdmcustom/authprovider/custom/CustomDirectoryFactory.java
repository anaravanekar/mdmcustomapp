package com.sereneast.orchestramdm.keysight.mdmcustom.authprovider.custom;

import com.onwbp.adaptation.AdaptationHome;
import com.orchestranetworks.service.directory.Directory;
import com.orchestranetworks.service.directory.DirectoryDefaultFactory;

/* Default factory for custom directory 
 * @see com.orchestranetworks.service.directory.DirectoryDefaultFactory#createDirectory(com.onwbp.adaptation.AdaptationHome)
 */
public class CustomDirectoryFactory extends DirectoryDefaultFactory {
	/* Create a custom directory without an external directory. 
	 * A simple extension of the DirectoryDefaultFactory.
	 * 
	 * @see com.orchestranetworks.service.directory.DirectoryDefaultFactory#createDirectory(com.onwbp.adaptation.AdaptationHome)
	 */
	public CustomDirectoryFactory(){
		super();
		System.out.println("Reached in Constructor LdapActiveDirectory");
	}
	@Override
	public Directory createDirectory(AdaptationHome aHome) throws Exception {
		System.out.println("Reached in LdapActiveDirectory");

		// Returns a base directory with no external secondary
		CustomDirectory dir = new CustomDirectory(aHome);
		return dir;
	}
}
