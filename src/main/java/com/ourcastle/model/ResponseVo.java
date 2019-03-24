package com.ourcastle.model;

public class ResponseVo {
	
	public ResponseVo(String status, String response){
		this.status=status;
		this.response=response;
	}
	public String status="";	//Ok / Fail
	public String response="";
}
