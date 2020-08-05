package com.expleo.users.data;

import java.io.Serializable;

public class JwtResponse implements Serializable{

	private static final long serialVersionUID = -8091879091924046844L;
	 
	
	private String message;
	private int status;
	private Object body;
	private  Object token;
	
	public JwtResponse() {
		this.token=null;
	}
	
	public JwtResponse(String token) {
		this.token = token;
	}
	
	
	public void settoken(String token) {
		this.token = token;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	
	public void setToken(String token) {
		this.token = token;
	}

	public Object getToken() {
		return token;
	}

	public void setToken(Object token) {
		this.token = token;
	}

	

}
