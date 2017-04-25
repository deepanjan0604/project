package com.project.app.auth.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.app.entities.User;
import com.project.app.entities.UserRole;
import com.project.app.entities.VerificationToken;
import com.project.app.enums.UserEnable;
import com.project.app.repositories.TokenVerificationRepository;
import com.project.app.repositories.UserRepository;
import com.project.app.repositories.UserRoleRepository;
import com.project.app.tokenverifcation.services.TokenVerificationServices;
import com.project.app.util.AuthUtilService;
import com.project.app.util.DefaultRestException;
import com.project.app.util.MailUtilities;
import com.project.app.util.TokenVerificationUtiities;
import com.project.app.util.Utilities;

@Service
public class AuthService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Autowired
	AuthUtilService authUtilService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	TokenVerificationRepository tokenVerificationRepository;


	@Autowired
	MailUtilities mailUtilities;

	@Value("${spring.application-path}")
	private String hostName;

	@Autowired
	TokenVerificationServices tokenVerificationServices;

	public Logger logger = LoggerFactory.getLogger(AuthService.class);

	public User getLogin() {

		User user = new User();
		if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)
				&& SecurityContextHolder.getContext().getAuthentication()
						.isAuthenticated() == true) {

			user = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();

		}
		return user;
	}

	/**
	 * User status has to set to PENDING while adding a new user
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> addUser(User user) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		try{
		UserRole userRole = new UserRole();
		userRole.setUser(user);
		userRole.setRoleType("user");
		user.setStatus(UserEnable.PENDING);
		String tenantDomainName = authUtilService.getLoggedInUsersTenantName();
		User username = userRepository.findByUsernameAndTenant(
				user.getUsername(), authUtilService.getLoggedInUser());
		if (username == null) {
			Tenant tenant = tenantRepository.findByDomain(tenantDomainName);
			user.setTenant(tenant);
			user = userRepository.save(user);
			userRole = userRoleRepository.save(userRole);
			setVerificationTokenAndSendMail(new VerificationToken(), user);
			map.put("status", "Mail Sent succesfully");
		} else {
			logger.info("Username already exists");
			map.put("error", "Username already exists");
			throw new DefaultRestException( map, "Username already exists");
		}
		}catch(Exception e){
			throw new DefaultRestException(map, "Email Id already taken");
		}
		return map;
	}

	/**
	 * user property has to be fetched and set manually along with user roles as
	 * there is no cascade property
	 * 
	 * @param user
	 */
	public void updateUser(User user) {
		try {
			User userEdit = userRepository.findById(user.getId());
			userEdit.setStatus(userEdit.getStatus());
			userEdit.setName(user.getName());
			userEdit.setEmail(user.getEmail());
			UserRole userRole = userRoleRepository.findByUserId(user.getId());

			if (userRole != null) {
				setUserRole(userRole, user, (user.getRoles().get(0)).toString());
			} else {
				UserRole userNewRole = new UserRole();
				setUserRole(userNewRole, user,
						(user.getRoles().get(0)).toString());
			}
			userRepository.save(userEdit);
		} catch (Exception e) {
		}
	}

	/**
	 * Updates the user role while editing user
	 * 
	 * @param userRole
	 * @param user
	 * @param roleType
	 */
	public void setUserRole(UserRole userRole, User user, String roleType) {
		userRole.setUser(user);
		userRole.setRoleType(roleType);
		userRoleRepository.save(userRole);

	}

	public List<UserRole> getRoles() {
		return userRoleRepository.findAll();
	}

	public List<User> getUsers(String name) {
		List<User> usersList = new ArrayList<User>();

		if (name == null || name.isEmpty()) {
			usersList = userRepository.findByTenant(authUtilService
					.getLoggedInUser());
		} else {
			usersList = userRepository
					.findByUsernameIgnoreCaseContainingAndTenant(name,
							authUtilService.getLoggedInUser());
		}
		List<User> userList = new ArrayList<User>();
		for (User user : usersList) {
			if (!user.getUsername().equals("super")) {
				userList.add(user);
			}
		}
		return userList;
	}

	/**
	 * @param email
	 */
	public void requestForgetPassword(HashMap<Object, Object> userid) {

		try {

			String[] userData = userid.get("username").toString().split("@");

			User user = userRepository.findByUsernameAndTenantDomain(
					userData[0], userData[1]);
			VerificationToken verificationToken = tokenVerificationRepository
					.findByUser(user);
			
			if (verificationToken != null) {
				setVerificationTokenAndSendMail(verificationToken, user);

			} else {

				setVerificationTokenAndSendMail(new VerificationToken(), user);
				
			}

		} catch (Exception e) {

		}
	}

	
	

	/**
	 * 
	 * Add a new user on new tenant registration and user joning a tenant
	 * 
	 * @param userObj
	 * @param tenant
	 * @return
	 */
	public User addNewUser(User userObj, Tenant tenant) {
		User newuser = new User();
		newuser.setName(userObj.getName());
		newuser.setUsername(userObj.getUsername());
		newuser.setPassword(passwordEncoder.encode(userObj.getPassword()));
		newuser.setEmail(userObj.getEmail());
		newuser.setTenant(tenant);
		newuser.setEnabled(true);
		newuser.setStatus(UserEnable.ACTIVE);
		userRepository.save(newuser);
		return newuser;
	}

	

	/**
	 * 
	 * @param userObj
	 * @param domain
	 * @return
	 * @throws DefaultRestException 
	 */
	public Map<String, Object> registerUser(User userObj, String domain) throws DefaultRestException {

		User newUser = new User();
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			Tenant tenant = tenantRepository.findByDomain(domain);
			newUser = addNewUser(userObj, tenant);
			UserRole userrole = new UserRole();
			setUserRole(userrole, newUser, "user");
		} catch (Exception e) {	
			throw new DefaultRestException(map, "Email Id/Username already taken");
		}
		return map;
	}

	
	/**
	 * @param userid
	 */
	public void deactivateUser(String userid) {
		User user = new User();
		user = userRepository.findById(userid);
		user.setEnabled(false);
		user.setStatus(UserEnable.INACTIVE);
		userRepository.save(user);
	}

	/**
	 * @param userid
	 */
	public void activateUser(String userid) {
		User user = userRepository.findById(userid);
		user.setEnabled(true);
		user.setStatus(UserEnable.ACTIVE);
		userRepository.save(user);
	}

	

	/**
	 * @param id
	 * @return
	 */
	public User getEditUser(String id) {
		return userRepository.findById(id);
	}

	/**
	 * @param tenant
	 * @param mail
	 */
	public Map<String, Object> checkForEmail(User useremail) throws Exception {

		HashMap<String, Object> map = new HashMap<String, Object>();
		String email = useremail.getEmail().toString();
		User user = userRepository.findByEmail(email);
		if (user != null) {
			map.put("message", "Email id already taken");
			map.put("status", "false");
			throw new DefaultRestException(map,"Email id already taken");
		} else {
			map.put("message", "Email id accepted");
			map.put("status", "true");
		}
		return map;
	}

	/**
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> resetPassword(String user) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		User userObj = userRepository.findById(user);
		if (userObj.getStatus() != UserEnable.INACTIVE) {
			VerificationToken verificationToken = tokenVerificationRepository
					.findByUser(userObj);

			if (verificationToken != null) {
				setVerificationTokenAndSendMail(verificationToken, userObj);

			} else {
				setVerificationTokenAndSendMail(new VerificationToken(),
						userObj);
			}
			map.put("message", "Reset Password link sent successfully");
			map.put("status", "success");
		} else {
			map.put("message", "User inactive");
			map.put("status", "danger");
			throw new DefaultRestException(map, "User inactive");
		}
		return map;
	}

	public VerificationToken setVerificationTokenAndSendMail(
			VerificationToken verificationToken, User userObj) throws Exception {
		String token = TokenVerificationUtiities.generateToken();
		String link = TokenVerificationUtiities.generateLink(hostName, token,
				userObj.getId());
		verificationToken.setToken(token);
		verificationToken.setVerified(false);
		verificationToken.setUser(userObj);
		verificationToken.setExpiryDate(Utilities.getCurrentDateAndTime());
		tokenVerificationRepository.save(verificationToken);
		new Thread() {
			public void run() {
				try {
					tokenVerificationServices.sendEmail(userObj.getEmail(),
							link, token);
				} catch (Exception v) {
					v.printStackTrace();
				}
			}
		}.start();
		return verificationToken;
	}

	/**
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 * 
	 */
	@ResponseBody
	public Map<String, Object> resendActivation(String user) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		User userObj = userRepository.findById(user);
		if (userObj.getStatus() == UserEnable.PENDING) {
			VerificationToken verificationToken = tokenVerificationRepository
					.findByUser(userObj);

			if (verificationToken != null) {
				setVerificationTokenAndSendMail(verificationToken, userObj);
				map.put("message", "Activation link sent successfully!!");
				map.put("status", "success");

			} else {
				setVerificationTokenAndSendMail(new VerificationToken(),
						userObj);
				map.put("message", "Activation link sent successfully!!");
				map.put("status", "success");
			}
		} else {
			map.put("status", "danger");
			map.put("message", "User already set his password!!");
			throw new DefaultRestException(map, "User already set his password!!");
		}
		
		return map;
	}

	
	
	@ExceptionHandler(DefaultRestException.class)
	@ResponseBody
	public Map<String,Object> errorResponse(DefaultRestException ex){
		return ex.getMap();
	}


}
