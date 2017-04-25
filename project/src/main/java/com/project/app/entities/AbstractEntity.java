package com.project.app.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonView;
import com.project.app.util.AbstractEntityListener;
import com.project.app.util.views.UserJsonView;

@MappedSuperclass
@EntityListeners({ AbstractEntityListener.class, AuditingEntityListener.class })
public abstract class AbstractEntity implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@JsonView({ UserJsonView.class })
	String id;

	@Column(name = "created_date")
	@CreatedDate
	@JsonView({})
	public Date createdDate = new Date();

	@Column(name = "last_modified_date")
	@LastModifiedDate
	@JsonView({})
	public Date lastModifiedDate;

	@CreatedBy
	@ManyToOne
	@JoinColumn(name = "created_by_id")
	@JsonView({})
	public User createdBy;

	@LastModifiedBy
	@ManyToOne
	@JoinColumn(name = "last_modified_by_id")
	@JsonView({})
	public User lastModifiedBy;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

	protected AbstractEntity() {

	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

}
