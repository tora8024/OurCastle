package com.ourcastle.orm;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the oc_add_attr database table.
 * 
 */
@Entity
@Table(name="oc_add_attr" )
//@NamedQuery(name="OcAddAttr.findAll", query="SELECT o FROM OcAddAttr o;")
public class OcAddAttr implements Serializable {
	private static final long serialVersionUID = 1L;
	  
	@Id
	@Column(name="seq", insertable = true, updatable = true)
	private String seq;
	
	@Column(name="category", insertable = true, updatable = true)
	private String category;

	@Column(name="param_name", insertable = true, updatable = true)
	private String paramName;

	@Column(name="param_value", insertable = true, updatable = true)
	private String paramValue;

	@Column(name="param_desc", insertable = true, updatable = true)
	private String paramDesc;

	public OcAddAttr() {
		seq="";
		category="";
		paramName="";
		paramValue="";
		paramDesc="";
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getParamDesc() {
		return paramDesc;
	}

	public void setParamDesc(String paramDesc) {
		this.paramDesc = paramDesc;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}