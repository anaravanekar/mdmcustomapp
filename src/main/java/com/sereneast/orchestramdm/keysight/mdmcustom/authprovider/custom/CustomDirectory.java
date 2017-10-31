package com.sereneast.orchestramdm.keysight.mdmcustom.authprovider.custom;

import java.io.FileInputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.onwbp.adaptation.Adaptation;
import com.onwbp.adaptation.AdaptationHome;
import com.onwbp.adaptation.AdaptationName;
import com.onwbp.adaptation.AdaptationTable;
import com.onwbp.adaptation.PrimaryKey;
import com.onwbp.base.text.bean.LabelDescription;
import com.orchestranetworks.instance.Repository;
import com.orchestranetworks.schema.Path;
import com.orchestranetworks.service.Procedure;
import com.orchestranetworks.service.ProcedureContext;
import com.orchestranetworks.service.ProcedureResult;
import com.orchestranetworks.service.ProgrammaticService;
import com.orchestranetworks.service.Role;
import com.orchestranetworks.service.Session;
import com.orchestranetworks.service.UserReference;
import com.orchestranetworks.service.ValueContextForUpdate;
import com.orchestranetworks.service.directory.AuthenticationException;
import com.orchestranetworks.service.directory.Directory;
import com.orchestranetworks.service.directory.DirectoryDefault;
import com.orchestranetworks.service.directory.DirectoryDefaultFactory;
import com.orchestranetworks.service.directory.DirectoryDefaultHelper;

/**
 * @author Derek Seabury
 * @author Mickael GERMEMONT
 *
 * A DirectoryInstance supporting SSO and internal and external directories
 *  
 */
public class CustomDirectory extends DirectoryDefault {
	
	private final static Logger logger = LoggerCustomDirectory.getLogger();
	
	protected Properties props;
	private ExternalDirectory extDir = null;
	private HttpAuthenticate checkSSO = null;
	private AdaptationHome aHome = null;
	private AdaptationTable dirTable = null;
	
	// Property strings
	protected static final String _SSO_CLASSNAME = "ebx.directory.SSOClass";
	protected static final String _ADMIN_USERID = "userCreationAcct";
	protected static final String _ENABLE_UPDATE = "enableProfileUpdate";
	protected static final String _ENABLE_LOGIN = "enableLogin";
	protected static final String _ENABLE_SSO = "enableSSO";
	protected static final String _ENABLE_BECOME = "enableBecome";
	protected static final String _ENABLE_CREATION = "enableUserCreation";
	protected static final String _LOGIN_URI = "loginURI";
	protected static final String _MEMBER_CACHE = "membershipCacheMs";

	// Property variables
	// Note that the default properties are defined in the updateDirProperties() function
	// and not by these initial values
	private String ssoClassName = "com.orchestranetworks.ps.customDirectory.SSOCheck";
	private UserReference adminUser = null;
	private boolean enableUpdate = false;
	private boolean enableLogin = true;
	private boolean enableSSO = true;
	private boolean enableBecome = true;
	private boolean enableUserCreation = false;
	private String loginURI = null;
	private long membershipCacheMs = 0;
	
	// Membership cache
	private HashMap<UserReference, UserMembershipCache> membershipCache;
	private class UserMembershipCache {
		private HashMap<Role, RoleMembership> cachedMembership;
	}
	private class RoleMembership {
		public boolean isMember = false;
		public long expiration = 0;
	}

	public CustomDirectory(AdaptationHome aHome){
		super(aHome);
		
		this.aHome = aHome;
		try{
			this.dirTable = aHome.findAdaptationOrNull(AdaptationName.forName("ebx-directory")).getTable(Path.parse("/directory/user"));
		} catch (Exception e){
			this.dirTable = null;
		}
		setExternalDirectory(new LdapActiveDirectory());

		updateDirProperties();
	}
	
	public void updateDirProperties(){
		Properties newProps = new Properties();
		try {
			String propPath = System.getProperty("ebx.properties", "ebx.properties");
			newProps.load(new FileInputStream(propPath)); 

			this.props = newProps;
			this.ssoClassName = props.getProperty(_SSO_CLASSNAME, SSOCheck.class.getCanonicalName());
			this.adminUser = UserReference.forUser(dirProp(_ADMIN_USERID,"admin"));
			this.enableUpdate= isTrueDirProp(_ENABLE_UPDATE, false);
			this.enableLogin = isTrueDirProp(_ENABLE_LOGIN, true);
			this.enableBecome = isTrueDirProp(_ENABLE_BECOME, true);
			this.enableSSO = isTrueDirProp(_ENABLE_SSO, false);
			this.enableUserCreation = isTrueDirProp(_ENABLE_CREATION, false);
			this.loginURI= dirProp(_LOGIN_URI, null);
			
			// Setup membership cache 
			try {
				this.membershipCacheMs = Integer.parseInt(dirProp(_MEMBER_CACHE, "0"));
			} catch (NumberFormatException e) {
				logger.warning("Could not parse ebx.properties "+ _MEMBER_CACHE + "value " + dirProp(_MEMBER_CACHE, "0"));
				this.membershipCacheMs = 0;
			}
			if( membershipCacheMs < 0 )
				membershipCache = null;
			else if( membershipCache==null )
				membershipCache = new HashMap<UserReference, UserMembershipCache>(20);
			
			if( extDir!=null )
				extDir.updateDirProperties(props);
			if( enableSSO ){
				try{
					if( checkSSO==null || !checkSSO.getClass().getName().equals(ssoClassName) ){
						// Need new SSO class instance
						Object newSSO = Class.forName(ssoClassName).newInstance();
						if( newSSO instanceof HttpAuthenticate ){
							checkSSO = (HttpAuthenticate) newSSO;
						} else {
							checkSSO = null;
							logger.warning("Invalid value for " + _SSO_CLASSNAME + ".  SSOClass is not superclass of " + ssoClassName);
						}
					}
				} catch (Exception e) {
					logger.warning("Invalid value for " + _SSO_CLASSNAME + ".  Could not create " + ssoClassName);
					checkSSO = null;
				}
				checkSSO.updateSSOProperties(props);
			}
		} catch (final Exception ex) {
			logger.severe("Exception updating directory properties:" + ex.getMessage());
		}
	}

	protected void setExternalDirectory( ExternalDirectory ext){
		this.extDir = ext;
		updateDirProperties();
	}
	
	protected Properties getProps(){
		return props;
	}
		
	private String dirProp(final String key, final String defaultValue){
		String val = props.getProperty("ebx.directory." + key);
		if( val==null || val.equals("") )
			return defaultValue;
		return val;
	}

	private boolean isTrueDirProp(String key, boolean defaultValue){
		return "true".equalsIgnoreCase(dirProp(key, defaultValue ? "true" : "false"));
	}
	
	@Override
	public URI getUserAuthenticationURI(Session sess) {
		if( this.loginURI==null )
			return null;
		
		String uriString = null;
		if( extDir!=null )
			uriString = extDir.getUserAuthenticationURI(loginURI, sess);
		if( uriString==null )
			uriString = loginURI;
		try{
			return new URI(uriString);
		} catch (Exception e){
			logger.severe("Could not parse ebx.properties "+ _LOGIN_URI + "value.");
		}
		return null;
	}

	protected final String _INTERNAL_SESSION = "internalSession";
	@Override
	public UserReference authenticateUserFromLoginPassword(String login,
			String password) {
		UserReference user = null;
		logger.info("authenticateUserFromLoginPassword  ");

		if( login==_INTERNAL_SESSION ) {// Note this is not .equals but object identity.
			// If internal session creation call
			logger.info("Internal session ");
			return this.adminUser;
		}
			try {
				if( extDir!=null && extDir.authenticateLogin(login, password) ){
					// Check externally for user credentials
					logger.info("Authenticating against externaldirectory ");

					user = UserReference.forUser(login);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		if( user==null && !this.enableLogin ){
			// Not found externally and internal directory is disabled
			logger.info("Denying user/password login for '"+login+"'.");

			updateDirProperties();
			throw new AuthenticationException("Please request access to this system.");
		} 
		if( user==null ){
			logger.info("Authenticate by default directory"+login+"'.");
			user= super.authenticateUserFromLoginPassword(login, password);
		}
		
		if( user==null ){
			// Ensure we are up to date if we are rejecting logins
			updateDirProperties();
			return null;
		}
		
		if( !isUserDefined(user) ){
	
			if( this.enableUserCreation ){
				createUser(user);
			} else {
				// Ensure we are up to date if we are rejecting logins
				updateDirProperties();
				logger.info("User '" + login + "' not found.");
				throw new AuthenticationException("User '" + login + "' not found.\nPlease request access to this EBX system.");
			}
		}
		if( this.enableUpdate && extDir!=null ){
			updateUser(user);
		}
		
		// Clear membership cache if any
		if( this.membershipCache!=null ){
			this.membershipCache.remove(user);
		}
		
		return user;
	}

	private void updateUser(UserReference user){
		if( this.dirTable==null ){
			this.dirTable = aHome.findAdaptationOrNull(AdaptationName.forName("ebx-directory")).getTable(Path.parse("/directory/user"));
		}
		
		// Update EBX user record
		try {
			ProgrammaticService svc = ProgrammaticService.createForSession(
					aHome.getRepository().createSessionFromLoginPassword(this._INTERNAL_SESSION, ""), aHome);
			final Adaptation userRecord = 
				dirTable.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(user.getUserId()));
			HashMap <Path, String> params = extDir.updateUserProfile(user, userRecord);
			if( params.size() == 0 )
				return;

			@SuppressWarnings("unchecked")
			final HashMap <Path, String> updates = (HashMap<Path, String>)params.clone();
			Procedure proc = new Procedure()
			{
				public void execute(ProcedureContext pContext) throws Exception
				{
					ValueContextForUpdate vc = pContext.getContext(userRecord.getAdaptationName());
					pContext.setAllPrivileges(true);
					for(Path param : updates.keySet()) 
						vc.setValue(updates.get(param), param);
					pContext.doModifyContent(userRecord, vc);
					pContext.setAllPrivileges(false);
				}
			};
			ProcedureResult res = svc.execute(proc);
			res.hasFailed();
		} catch (Exception e) {
			String msg = "Error updading user profile: "+e.getMessage();
			logger.warning(msg);
			e.printStackTrace();
		}
	}
	
	@Override
	public UserReference authenticateUserFromHttpRequest(
			HttpServletRequest req) throws AuthenticationException {
		String uname = null;
		UserReference user = null;
		String password = null;

		// If in a SOAP request use secondary authentication
		if( req.getHeader("SOAPAction")!=null )
			return null;

		DirectoryDefault dir = null;
		try{
			dir = DirectoryDefault.getInstance(Repository.getDefault());
		} catch ( Exception e ){
			throw new AuthenticationException("Could not find directory instance.");
		}

		// Authentication of user

		// If SSO is enabled use credential from request
		if( enableSSO && checkSSO!=null ){
			user = checkSSO.GetUserFromHTTPRequest(req);
			if( user==null )
				logger.info("No user info found in request.");
			else
				logger.info("Found remote user " + user + ".");
		}
		if( user!=null && !dir.isUserDefined(user) ){
			// If SSO user not found check for add or new install
			if( this.enableUserCreation ){
				createUser(user);
			} else if( dir.getAllUserReferences().size()==1 ) {
				// Newly set up repository, only account is admin 
				logger.info(
						"Allowing repository setup admin for '" + uname + "'.");
				return (UserReference) (dir.getAllUserReferences().get(0));
			} else if( !enableLogin ) {	
				logger.info("Denying SSO access for user '" + uname + "'");
				throw new AuthenticationException(
						"Please request access to this system.  " +
				"Authorized users are logged in automatically.");
			}
		}

		if( user==null && this.enableLogin ){
			logger.info("Line 328: ");

			user = super.authenticateUserFromHttpRequest(req);
		}
		
		if( user==null && this.enableLogin ){
			// Try with URL parameters
			uname = req.getParameter("login");
			password = req.getParameter("password");
			if( uname!=null && password!=null )
				user = authenticateUserFromLoginPassword(uname, password);
		}

		if( user==null ) {
			// Ensure we are up to date if we are rejecting logins
			updateDirProperties();
			return null;
		}

		// User is defined and authenticated
		String become = req.getParameter("become");
		if( become!=null && this.enableBecome && isUserInRole(user, UserReference.ADMINISTRATOR) ){
			UserReference beUser = null; 
			if(become != null )
				beUser = UserReference.forUser(become);
			if(beUser != null && dir.isUserDefined(beUser) ){
				logger.info("Allowing user '" + uname + "' to become user '" + become +"'");
				user = beUser;
			}
		}

		// Update from external directory
		// By updating the 'become' user we create a mechanism to mass update users 
		if( user!=null && extDir!=null && this.enableUpdate){
			updateUser(user);
		}
		
		// Clear membership cache if any
		if( this.membershipCache!=null ){
			this.membershipCache.remove(user);
		}
		
		return user;
	}

	@Override
	public boolean isUserInRole(final UserReference user, final Role role) {
		Boolean isMember = null;
		UserMembershipCache userMemberships = null;
		RoleMembership cachedMembership = null;
		// Check cache
		if( membershipCache!=null){
			userMemberships = membershipCache.get(user);
			if( userMemberships==null ){
				userMemberships = new UserMembershipCache();
				userMemberships.cachedMembership = new HashMap<Role, RoleMembership>(5);
				membershipCache.put(user, userMemberships);
			} else {
				cachedMembership = userMemberships.cachedMembership.get(role);
				if( cachedMembership!=null && (cachedMembership.expiration==0 || cachedMembership.expiration > System.currentTimeMillis()) )
					// Use cache value
					return cachedMembership.isMember;
			}
		}
		
		if( extDir!=null )
			// Check external directory
			isMember = extDir.isUserInRole(user, role.getRoleName(), role.getLabel());

		if( isMember==null || isMember==false)
			// Not cached, not an external group, check EBX membership 
			isMember = super.isUserInRole(user, role);
		
		// Update cache
		if( membershipCache!=null ){
			cachedMembership = new RoleMembership();
			cachedMembership.isMember = isMember;
			if( this.membershipCacheMs==0 )
				cachedMembership.expiration = 0;
			else
				cachedMembership.expiration = this.membershipCacheMs + System.currentTimeMillis();
			userMemberships.cachedMembership.put(role, cachedMembership);
		}
		return isMember;
	}
	
	private void createUser(final UserReference user){
		createUser(user, "nil");
	}
	
	private void createUser(final UserReference user, final String cred){
		final UserEntity userEntity = DirectoryDefaultHelper.newUser(user, this);
		userEntity.setBuiltInAdministrator(false);
		userEntity.setReadOnly(false);
		
		DirectoryDefaultHelper.saveUser(userEntity, "", this);
		
//		if( this.dirTable==null ){
//			this.dirTable = aHome.findAdaptationOrNull(AdaptationName.forName("ebx-directory")).getTable(Path.parse("/directory/user"));
//		}

//		final Procedure addUserProc = new Procedure() {
//
//			final Path loginPath = Path.parse("./login");
//			final Path pwPath = Path.parse("./password");
////			final Path pwChangePath = Path.parse("./passwordMustChange");
//			final Path adminPath = Path.parse("./builtInRoles/readOnly");
//			final Path readOnlyPath = Path.parse("./builtInRoles/readOnly");
//
//			@Override
//			public void execute(final ProcedureContext pContext) throws Exception {
//				final BuiltInRoles builtInRole = new BuiltInRoles();
//
//				builtInRole.setAdministrator(false);
//				builtInRole.setReadOnly(false);
//				pContext.setAllPrivileges(true);
//				final ValueContextForUpdate vc = pContext.getContextForNewOccurrence(dirTable);
////				vc.setValue(user.getUserId(), loginPath);
////				vc.setValueFromXsString("", pwPath);
////				vc.setValueFromXsString("false", adminPath);
////				vc.setValueFromXsString("false", readOnlyPath);
//				pContext.doCreateOverwriting(vc, dirTable);
//				pContext.setAllPrivileges(false);
//			}
//		};
//
//		final ProgrammaticService svc = ProgrammaticService.createForSession(
//				aHome.getRepository().createSessionFromLoginPassword(this._INTERNAL_SESSION, ""), aHome);
//		svc.execute(addUserProc);
	}
	
	/* Capability extension to support need to access role descriptions.  
	 * This should be added to base Role API and then this can be deprecated. */
	public String getRoleDescription(Role role, Locale locale){
		if( role.isBuiltIn() )
			return "EBX Built-in Role " + role.getLabel();
		try{
			AdaptationTable roleTable = this.dirTable.getContainerAdaptation().getTable(Path.parse("/directory/roles"));
			Adaptation aRole = roleTable.lookupAdaptationByPrimaryKey(PrimaryKey.parseString(role.getRoleName()));
			LabelDescription doc= (LabelDescription) aRole.get(Path.parse("./documentation"));
			if( doc.getLocalizedDocumentation(locale)==null )
				return "";
			final String desc = doc.getLocalizedDocumentation(locale).getDescription();
			if( desc==null )
				return "";
			return desc;
		} catch (Exception e){
			return "";
		}		
	}

	/* Default factory for custom directory 
	 * @see com.orchestranetworks.service.directory.DirectoryDefaultFactory#createDirectory(com.onwbp.adaptation.AdaptationHome)
	 */
	public static class Factory extends DirectoryDefaultFactory {
		/* Create a custom directory without an external directory. 
		 * A simple extension of the DirectoryDefaultFactory.
		 * 
		 * @see com.orchestranetworks.service.directory.DirectoryDefaultFactory#createDirectory(com.onwbp.adaptation.AdaptationHome)
		 */
		@Override
		public Directory createDirectory(AdaptationHome aHome) throws Exception {
			// Returns a base directory with no external secondary
			Directory dir = new CustomDirectory(aHome);
			return dir;
		}
	}
	
}
