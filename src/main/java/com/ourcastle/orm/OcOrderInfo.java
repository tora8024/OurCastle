package com.ourcastle.orm;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the oc_order_info database table.
 * 
 */
@Entity
@Table(name="oc_order_info")
//@NamedQuery(name="OcOrderInfo.findAll", query="SELECT o FROM OcOrderInfo o")
public class OcOrderInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private OcOrderInfoPK id;

	@Column(name="booking_status")
	private String bookingStatus;

	@Column(name="children_cnt")
	private int childrenCnt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;

	@Column(name="parent_name")
	private String parentName;

	@Column(name="mail")
	private String mail;

	private String remark;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_time")
	private Date updateTime;

	
	
	public OcOrderInfo() {
	}

	public OcOrderInfoPK getId() {
		return this.id;
	}

	public void setId(OcOrderInfoPK id) {
		this.id = id;
	}

	public String getBookingStatus() {
		return this.bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public int getChildrenCnt() {
		return this.childrenCnt;
	}

	public void setChildrenCnt(int childrenCnt) {
		this.childrenCnt = childrenCnt;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getParentName() {
		return this.parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}