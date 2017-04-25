package com.project.app.entities;

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonView;
import com.project.app.enums.UserEnable;
import com.project.app.util.views.UserJsonView;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"email", "username" }),@UniqueConstraint(columnNames = {
				"email", "email" }) })
public class User extends AbstractEntity implements UserDetails {

	private static final long serialVersionUID = 7217841877446097601L;

	@Column(name = "name")
	@JsonView({ UserJsonView.class })
	public String name;

	@Column(name = "username")
	@JsonView({ UserJsonView.class })
	public String username;

	@Column(name = "email")
	@JsonView({ UserJsonView.class })
	public String email;

	@Column(name = "password")
	public String password;

	@Column(name = "enabled")
	private boolean enabled = false;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	@JsonView({ UserJsonView.class })
	private UserEnable status;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonView(UserJsonView.class)
	public List<UserRole> roles;




	public List<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(List<UserRole> roles) {
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public UserEnable getStatus() {
		return status;
	}

	public void setStatus(UserEnable status) {
		this.status = status;
	}

	@JsonIgnore
	public boolean isSuper() {
		/*
		 * List<UserRoles> authorities = (List<UserRoles>)
		 * this.getAuthorities(); if (authorities != null) { for (UserRoles
		 * userRole : authorities) { if
		 * (userRole.getAuthority().equals("SUPER")) { return true; } } }
		 */
		return false;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public Collection<UserRole> getAuthorities() {
		return this.roles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (enabled != other.enabled)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		if (status != other.status)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}



	

}
