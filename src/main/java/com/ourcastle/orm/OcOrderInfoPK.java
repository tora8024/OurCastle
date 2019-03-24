package com.ourcastle.orm;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the oc_order_info database table.
 * 
 */
@Embeddable
public class OcOrderInfoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="order_id")
	private String orderId;

	@Temporal(TemporalType.DATE)
	@Column(name="booking_date")
	private java.util.Date bookingDate;

	@Column(name="period_id")
	private String periodId;

	@Column(name="phone_num")
	private String phoneNum;

	public OcOrderInfoPK() {
	}
	public String getOrderId() {
		return this.orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public java.util.Date getBookingDate() {
		return this.bookingDate;
	}
	public void setBookingDate(java.util.Date bookingDate) {
		this.bookingDate = bookingDate;
	}
	public String getPeriodId() {
		return this.periodId;
	}
	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}
	public String getPhoneNum() {
		return this.phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof OcOrderInfoPK)) {
			return false;
		}
		OcOrderInfoPK castOther = (OcOrderInfoPK)other;
		return 
			this.orderId.equals(castOther.orderId)
			&& this.bookingDate.equals(castOther.bookingDate)
			&& this.periodId.equals(castOther.periodId)
			&& this.phoneNum.equals(castOther.phoneNum);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.orderId.hashCode();
		hash = hash * prime + this.bookingDate.hashCode();
		hash = hash * prime + this.periodId.hashCode();
		hash = hash * prime + this.phoneNum.hashCode();
		
		return hash;
	}
}