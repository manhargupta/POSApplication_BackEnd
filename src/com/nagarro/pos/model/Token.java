package com.nagarro.pos.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.nagarro.pos.constant.Constant;
import com.nagarro.pos.constant.MyDoc;

/**
 * User Entity.
 */
@MyDoc(author = Constant.AUTHOR, date = Constant.CREATION_DATE, currentRevision = 1)
@Entity
public class Token implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 531645384534093989L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String token;
	private int empId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date loginDateTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiryDateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public Date getLoginDateTime() {
		return loginDateTime;
	}

	public void setLoginDateTime(Date loginDateTime) {
		this.loginDateTime = loginDateTime;
	}

	public Date getExpiryDateTime() {
		return expiryDateTime;
	}

	public void setExpiryDateTime(Date expiryDateTime) {
		this.expiryDateTime = expiryDateTime;
	}

	public Token(String token, int empId, Date loginDateTime, Date expiryDateTime) {
		super();
		this.token = token;
		this.empId = empId;
		this.loginDateTime = loginDateTime;
		this.expiryDateTime = expiryDateTime;
	}

	public Token() {
	}

}
