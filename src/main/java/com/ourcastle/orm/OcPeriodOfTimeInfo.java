package com.ourcastle.orm;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the oc_period_of_time_info database table.
 * 
 */
@Entity
@Table(name="oc_period_of_time_info" )
//@NamedQuery(name="OcPeriodOfTimeInfo.findAll", query="SELECT o FROM OcPeriodOfTimeInfo o;")
public class OcPeriodOfTimeInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="period_id", insertable = true, updatable = true)
	private String periodId;
	
	@Column(name="category", insertable = true, updatable = true)
	private String category;

	@Column(name="title", insertable = true, updatable = true)
	private String title;

	public OcPeriodOfTimeInfo() {
		periodId="";
		category="";
		title="";
	}

	public String getPeriodId() {
		return this.periodId;
	}

	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}