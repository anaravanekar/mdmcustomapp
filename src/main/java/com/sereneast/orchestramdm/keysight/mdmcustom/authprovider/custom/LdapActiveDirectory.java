package com.sereneast.orchestramdm.keysight.mdmcustom.authprovider.custom;

import java.text.MessageFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapName;

import com.onwbp.adaptation.Adaptation;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.service.Session;
import com.orchestranetworks.service.UserReference;

public class LdapActiveDirectory implements ExternalDirectory{

	private final static Logger logger = LoggerCustomDirectory.getLogger();

	private static final String PROPERTY_HEADER = "ebx.directory.ldap.";

	private static final String PATH = "path";
	private static final String LDAP_ATTRIBS = "ldapAttrib";
	private static final String USER_PATHS = "userPaths";

	private static final String BASE_DN = "baseDN";
	private static final String BIND_DN = "bindDN";
	private static final String USER_LOGIN = "login";
	private static final String CREDENTIAL = "credential";
	private static final String USER_SEARCH = "search";
	private static final String MEMBERSHIP_BASE = "membershipBase";
	private static final String MEMBERSHIP_FILTER = "membershipFilter";
	private static final String MEMBERSHIP_ROLE = "membershipRole";
	private static final String REQ_TOLOGIN_MEMBERSHIP_BASE = "requiredToLogin.membershipBase";
	private static final String REQ_TOLOGIN_ROLE = "requiredToLogin.role";
	private static final String REQ_TOLOGIN_MEMBERSHIP_FILTER = "requiredToLogin.membershipFilter";
	private static final String ADMIN_GROUP = "adminGroup";
	private static final String READONLY_GROUP = "readOnlyGroup";

	private int ebxToLdapAtts = 5;
	protected Path[] ebxUserPaths = { Path.parse("./email"),
			Path.parse("./lastName"), Path.parse("./firstName"),
			Path.parse("./salutation"), Path.parse("./faxNumber") };

	protected String[] ldapAttrib = { "mail", "sn", "preferredgivenname",
			"personalTitle", "region" };

	private String path = null;

	private LdapName baseDN = null;
	private LdapName bindDN = null;
	private String credential = null;
	private MessageFormat userSearch = null;
	private MessageFormat userLogin = null;
	private LdapName membershipBase = null;
	private MessageFormat membershipRole = null;
	private MessageFormat membershipFilter = null;

	private LdapName reqLogin_membershipBase = null;
	private MessageFormat reqLogin_role = null;
	private MessageFormat reqLogin_membershipFilter = null;

	private LdapName adminGroup = null;
	private LdapName readOnlyGroup = null;
	private Properties props = null;

	public HashMap<Path, String> updateUserProfile(UserReference userReference,
			Adaptation user) {
		// TODO Auto-generated method stub
		return null;
	}

	private DirContext connectToLDAP() {
		return connectToLDAP(null, null);
	}

	private DirContext connectToLDAP(LdapName login, final String password) {
		Hashtable<String, String> env = new Hashtable<String, String>();
		String bindDN = ldapProp(BIND_DN);

		env.put(Context.SECURITY_AUTHENTICATION, "none");
		if (login != null) {
			logger.info("Authenticating LDAP for login: "
					+ login + ".");
			// Bind as specified user
			env.put(Context.SECURITY_PRINCIPAL, login.toString());
			env.put(Context.SECURITY_CREDENTIALS, password);
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			
		} else if (bindDN != null) {
			logger.info("Connecting to LDAP as bindDN." +  bindDN);

			env.put(Context.SECURITY_PRINCIPAL, bindDN);

			if (this.credential != null) {
				env.put(Context.SECURITY_CREDENTIALS, this.credential);
				env.put(Context.SECURITY_AUTHENTICATION, "simple");
			}
		}
		env.put(Context.PROVIDER_URL, this.path);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.REFERRAL, "follow");

		try {
			DirContext ctx = new InitialDirContext(env);
			logger.info("Returning dircontext " + ctx.getEnvironment());

			return ctx;
		} catch (Exception e) {
			if (login == null) {
				logger.severe("Exception connecting to LDAP with baseDN.\n" + "LDAP Error: " + e);
			}

			e.printStackTrace();

			// User not found, or connection exception
			// In case there has been an update reload configuration
			updateDirProperties();
			return null;
		}
	}

	private LdapName getLoginForEbxUser(final String login) {
		final DirContext ctx = connectToLDAP();
		LdapName res = getLoginForEbxUser(login, ctx);
		try {
			ctx.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private LdapName getLoginForEbxUser(final String login, final DirContext ctx) {
		if (this.userSearch == null)
			return null;

		String filter = this.userSearch.format(new Object[] { login });

		String baseDNStr = ldapProp(BASE_DN);
		ArrayList<String> res = searchLdapForUser(ctx, baseDNStr, filter.replace("\"", ""));

		if (res == null || res.isEmpty()) {
			logger.info("User " + login + " not found.");
			return null;
		}
		if(null == this.baseDN){
			this.baseDN = ldapNameProp(BASE_DN);
		}
		logger.info("User " + login + " found.");

		LdapName user = null;
		try {
			if (this.baseDN != null && !res.get(0).contains(this.baseDN.toString())) {
				user = new LdapName(res.get(0) + "," + this.baseDN);
			} else {
				user = new LdapName(res.get(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("User " + user + " found.");

		return user;
	}
	private static String ldapFilterEscape(String input){
		String output = input.replaceAll("\\*", "\\\\2A");

		return output;
	}
	private ArrayList<String> searchLdap(final DirContext extCtx,
			final LdapName base, String filter) {
		filter = ldapFilterEscape(filter).replace("\"", "");

		ArrayList<String> res = new ArrayList<String>();

		SearchControls ctrl = new SearchControls();
		ctrl.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String returnedAtts[]={"sn","givenName", "samAccountName"};
		ctrl.setReturningAttributes(returnedAtts);
		try {
			DirContext ctx = extCtx;
			
			logger.info("Searching directory for <'" + base + "', '" + filter
					+ "'>");

			NamingEnumeration<SearchResult> results = ctx.search(base, filter,ctrl);
			//NamingEnumeration<SearchResult> results = ctx.search(baseDN, filter,ctrl);

			if (extCtx == null)
				ctx.close();
			// No entries found
			if (results == null)
				return res;
			while (results.hasMore())
				res.add(results.next().getName());
		} catch (Exception e) {
			logger.info("Search Exception:" + e);
			return null;
		}
		return res;
	}

	private ArrayList<String> searchLdapForUser(final DirContext extCtx,
			final String baseDN, String filter) {	
		// Create the search controls         
		SearchControls searchCtls = new SearchControls();
		ArrayList<String> res = new ArrayList<String>();

		//Specify the attributes to return
		String returnedAtts[]={"sn","DistinguishedName", "samAccountName"};
		searchCtls.setReturningAttributes(returnedAtts);

		//Specify the search scope
		searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		try {
			// Search for objects using the filter
			NamingEnumeration<SearchResult> answer = extCtx.search(baseDN, filter, searchCtls);

			//Loop through the search results
			while (answer.hasMoreElements())
			{
				SearchResult sr = (SearchResult)answer.next();
				logger.info(">>>" + sr.getName());
				Attributes attrs = sr.getAttributes();
				javax.naming.directory.Attribute attr = attrs.get("DistinguishedName");
				res.add(attr.get().toString());

			}

			extCtx.close();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return res;
	}
	public boolean authenticateLogin(String user, String password) throws Exception {
		logger.info("LdapActiveDirectory authenticateLogin for user: "+ user + " and password: " + password );

			LdapName login = getLoginForEbxUser(user);
			if (this.userLogin != null){ 
				login = new LdapName(this.userLogin.format(new String[] { user,
						login.toString() }));
			}

			if(login == null){
				logger.info("LDAP Login not found for " + user);
				return false;
			}

			logger.info("Authenticating " + login + ".");
			DirContext ctx = connectToLDAP(login, password);
			if(!isUserInRole_RequiredForLogin(ctx, UserReference.forUser(user))){
				throw new Exception(String.format("Not authorized. user[%s] is not a member of required default EBX group. ", user));
			}
			ctx.close();
		
		
		return true;
	}

	private String ldapProp(final String key) {
		String val = this.props.getProperty(PROPERTY_HEADER + key);
		if (null != val){
			final String value = val.replace("\"", "");
			return value;
		}
		return null;

	}

	public Boolean isUserInRole_RequiredForLogin(DirContext ctx, final UserReference user) {

		if (this.reqLogin_membershipBase == null) {
			logger.info("Required for login. No LDAP membership base defined. assuming it is disabled");
			return true;
		}

		final String login = user.getUserId();
		try {
			/*String ldapUserDN = "";

			// If we are using the ldapUser fetch it
			if (ldapProp(REQ_TOLOGIN_MEMBERSHIP_FILTER).contains("{1}")) {
				LdapName ldapUser = getLoginForEbxUser(login, ctx);
				if (ldapUser != null){ // If found convert from string and double
					// escape \ characters
					ldapUserDN = ldapUser.toString().replaceAll("\\\\",
							"\\\\\\\\");
				}
			}

			final String groupFilter;

			groupFilter = reqLogin_role.format(new Object[] { login, ldapUserDN });
			logger.info("reqLogin_role: "+ reqLogin_role);
			logger.info("groupFilter: "+ groupFilter);
			logger.info("ldapUserDN: "+ ldapUserDN);

			final ArrayList<String> fetchRole = searchLdap(ctx, this.reqLogin_membershipBase,
					groupFilter);

			if (fetchRole == null || fetchRole.size() == 0) {
				logger.info("Required for login. LDAP group " + groupFilter
						+ " not found");
				//return null;
			}*/
			final String filter;

			filter = this.reqLogin_membershipFilter.format(new Object[] { login });

			//final ArrayList<String> fetchUserInRole = searchLdap(ctx, this.reqLogin_membershipBase, filter);
			final ArrayList<String> fetchUserInRole = searchLdapForUser(ctx, this.reqLogin_membershipBase.toString(), filter);

			if (fetchUserInRole != null && fetchUserInRole.size() > 0) {
				logger.info("Required for login. Results found searching for " + login + " using "
						+ String.format("base[%s], filter[%s]", this.reqLogin_membershipBase, filter) + ".");
				return true;
			}

			logger.info(String.format("Required for login. No results found searching for %s using base[%s], filter[%s]", 
					login, this.reqLogin_membershipBase, filter));
			return false;
		} finally {
			try {
				if (ctx != null){
					ctx.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public Boolean isUserInRole(UserReference user, String roleId,
			String roleLabel) {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateDirProperties(Properties p) {

		this.props = p;
		updateDirProperties();

	}

	public String getUserAuthenticationURI(String fmt, Session sess) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<SimpleEntry<String, String>> getUserInfo(String user) {
		// TODO Auto-generated method stub
		return null;
	}

	public void interact() {
		// TODO Auto-generated method stub

	}
	private LdapName ldapNameProp(final String key) {
		String val = this.props.getProperty(PROPERTY_HEADER + key);
		if(null != val){
			final String value = val.replace("\"", "");

			LdapName name = null;
			try {
				name = new LdapName(value);

				return name;
			} catch (Exception e) {
				logger.severe("Invalid name for LDAP name  " + key
						+ ".  Be sure to double any \\ characters.");
			}

		}
		return null;

		
	}
	private MessageFormat ldapFormat(final String key) {
		String val = this.props.getProperty(PROPERTY_HEADER + key);
		if(null != val){
			final String value = val.replace("\"", "");
			return new MessageFormat(value);
		}
		return null;

	}

	public void updateDirProperties() {
		logger.info("Reloading directory properties.");
		path = ldapProp(PATH);
		baseDN = ldapNameProp(BASE_DN);
		bindDN = ldapNameProp(BIND_DN);
		credential = ldapProp(CREDENTIAL);
		membershipRole = ldapFormat(MEMBERSHIP_ROLE);
		membershipFilter = ldapFormat(MEMBERSHIP_FILTER);

		reqLogin_role = ldapFormat(REQ_TOLOGIN_ROLE);
		reqLogin_membershipBase = ldapNameProp(REQ_TOLOGIN_MEMBERSHIP_BASE);
		reqLogin_membershipFilter = ldapFormat(REQ_TOLOGIN_MEMBERSHIP_FILTER);

		adminGroup = ldapNameProp(ADMIN_GROUP);
		readOnlyGroup = ldapNameProp(READONLY_GROUP);
		userSearch = ldapFormat(USER_SEARCH);
		userLogin = ldapFormat(USER_LOGIN);
		membershipBase = ldapNameProp(MEMBERSHIP_BASE);
		if (membershipBase == null)
			membershipBase = baseDN;

		String attrs = ldapProp(LDAP_ATTRIBS);
		String paths = ldapProp(USER_PATHS);
		if (attrs == null)
			return;
		// Lazy - won't support LDAP properties with m
		ldapAttrib = attrs.split(",", 0);
		String[] pathStr = paths.split(",", ldapAttrib.length);
		ebxToLdapAtts = pathStr.length;
		ebxUserPaths = new Path[ebxToLdapAtts];
		for (int i = 0; i < ebxToLdapAtts; i++)
			ebxUserPaths[i] = Path.parse(pathStr[i]);
	}

}
