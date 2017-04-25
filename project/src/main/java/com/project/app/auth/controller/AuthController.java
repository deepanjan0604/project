package com.project.app.auth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.app.auth.services.AuthService;
import com.project.app.entities.User;
import com.project.app.entities.UserRole;
import com.project.app.repositories.UserRepository;
import com.project.app.repositories.UserRoleRepository;
import com.project.app.util.AuthUtilService;
import com.project.app.util.DefaultRestException;
import com.project.app.util.views.UserJsonView;

@RestController
public class AuthController {

	@Autowired
	AuthService authService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserRoleRepository userRoleRepository;


	@Autowired
	AuthUtilService authUtilService;



	@RequestMapping("/public/login")
	@JsonView(UserJsonView.class)
	public User getLogin() {
		return authService.getLogin();
	}

	@RequestMapping("/admin/adduser")
	public Map<String, Object> addUser(@RequestBody User user) throws Exception {
		return authService.addUser(user);
	}

	@RequestMapping("/admin/updateUser")
	@JsonView(UserJsonView.class)
	public void updateUser(@RequestBody User user) {
		authService.updateUser(user);
	}

	@RequestMapping("/admin/getuseredit")
	@JsonView(UserJsonView.class)
	public User getUser(@RequestParam String id) {
		return authService.getEditUser(id);
	}

	@RequestMapping("/admin/getRoles")
	public List<UserRole> getRoles() {
		return authService.getRoles();
	}

	@RequestMapping("/all/getUsers")
	@JsonView(UserJsonView.class)
	public List<User> getUsers(@RequestParam("value") String name) {
		return authService.getUsers(name);
	}

	

	@RequestMapping("/public/request/forgetpassword")
	public void requestForgetPassword(@RequestBody HashMap<Object, Object> email) {
		authService.requestForgetPassword(email);
	}


	
	

	@RequestMapping("/public/userregistration")
	public Map<String, Object> registerUser(
			@RequestBody HashMap<Object, Object> user,
			@RequestParam String domain) throws DefaultRestException {
		ObjectMapper objMap = new ObjectMapper();
		User userObj = objMap.convertValue(user, User.class);
		return authService.registerUser(userObj, domain);
	}

	@RequestMapping("/admin/deactivateuser")
	@JsonView(UserJsonView.class)
	public void deactivateUser(@RequestBody String userid) {
		authService.deactivateUser(userid);
	}

	@RequestMapping("/admin/activateuser")
	@JsonView(UserJsonView.class)
	public void activateUser(@RequestBody String userid) {
		authService.activateUser(userid);
	}

	@RequestMapping("/public/checkforemail")
	@JsonView(UserJsonView.class)
	public Map<String, Object> checkForEmail(@RequestBody User usermail)
			throws Exception {
		return authService.checkForEmail(usermail);
	}

	@RequestMapping("/admin/reset/pass")
	@JsonView(UserJsonView.class)
	public Map<String, Object> resetPassword(@RequestParam String user)
			throws Exception {
		return authService.resetPassword(user);
	}

	@RequestMapping("/admin/resend/act")
	@JsonView(UserJsonView.class)
	public Map<String, Object> resendActivation(@RequestParam String user)
			throws Exception {
		return authService.resendActivation(user);
	}

	@RequestMapping("/all/getUserDetail")
	@JsonView(UserJsonView.class)
	public User getUserDetail(@RequestParam String username) throws Exception {
		return authService.getUserDetail(username);
	}
}