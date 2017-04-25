package com.project.app.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.project.app.util.views.UserJsonView;

@Entity
@Table(name = "user_roles")
public class UserRole implements GrantedAuthority {

	private static final long serialVersionUID = -2691788079537539989L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "role_id")
	@JsonView(UserJsonView.class)
	public int roleId;

	@Column(name = "role")
	@JsonView(UserJsonView.class)
	public String roleType;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="user_id")
	User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public void setAuthority(String authority) {
		this.roleType = authority;
	}

	@Override
	public String getAuthority() {
		return roleType;
	}

	public String toString() {
		return this.roleType;
	}
}
