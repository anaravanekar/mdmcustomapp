package com.sereneast.orchestramdm.keysight.mdmcustom.authprovider;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class TestActiveDirectory {

	static DirContext ldapContext;
	public static void main (String[] args) throws NamingException
	{
		try
		{
			System.out.println("Test Active Directory");

			Hashtable<String, String> ldapEnv = new Hashtable<String, String>(11);
			ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			ldapEnv.put(Context.PROVIDER_URL,  "ldaps://10.10.10.86:636");
			ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
			ldapEnv.put(Context.SECURITY_PRINCIPAL, "CN=user,CN=Users,DC=astserene,DC=local");
			ldapEnv.put(Context.SECURITY_CREDENTIALS, "serene*123");
			
			ldapContext = new InitialDirContext(ldapEnv);

			// Create the search controls         
			SearchControls searchCtls = new SearchControls();

			//Specify the attributes to return
			String returnedAtts[]={"sn","givenName", "samAccountName"};
			searchCtls.setReturningAttributes(returnedAtts);

			//Specify the search scope
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			//specify the LDAP search filter
			//String searchFilter = "(&(objectClass=user)(samAccountName=user1))";
			String searchFilter  = "(&(objectClass=user)(sAMAccountName=user1)(memberOf=CN=Datasteward,CN=Builtin,DC=astserene,DC=local))";
			//Specify the Base for the search
			//String searchBase = "DC=astserene,DC=local";
			String searchBase = "DC=astserene,DC=local";
			//initialize counter to total the results
			int totalResults = 0;

			// Search for objects using the filter
			NamingEnumeration<SearchResult> answer = ldapContext.search(searchBase, searchFilter, searchCtls);

			//Loop through the search results
			while (answer.hasMoreElements())
			{
				SearchResult sr = (SearchResult)answer.next();

				totalResults++;

				System.out.println(">>>" + sr.getName());
				Attributes attrs = sr.getAttributes();
				System.out.println(">>>>>>" + attrs.get("samAccountName"));
			}

			System.out.println("Total results: " + totalResults);
			ldapContext.close();
		}
		catch (Exception e)
		{
			System.out.println(" Search error: " + e);
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
