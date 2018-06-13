package com.nagarro.pos.model;

import java.util.Date;


import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nagarro.pos.constant.Constant;
import com.nagarro.pos.constant.MyDoc;

@MyDoc(author = Constant.AUTHOR, date = Constant.CREATION_DATE, currentRevision = 1)
@MappedSuperclass
public abstract class AbstractTimestampEntity {

	@JsonFormat(
		      shape = JsonFormat.Shape.STRING,
		      pattern = "MMMM dd,yyyy#hh:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", nullable = true)
	private Date created;

	@JsonFormat(
		      shape = JsonFormat.Shape.STRING,
		      pattern = "MMMM dd,yyyy#hh:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated", nullable = true)
	private Date updated;

	@PrePersist
	protected void onCreate(Object o) {

		updated = created = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		updated = new Date();
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

}
