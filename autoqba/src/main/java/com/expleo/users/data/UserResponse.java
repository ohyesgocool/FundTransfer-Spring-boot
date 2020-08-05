package com.expleo.users.data;

import java.util.Date;

public class UserResponse {

	private String id;
	private String firstName;
	private String lastName;
	private String username;
	private String panNumber;
	private String email;
	private Date creationData;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getCreationData() {
		return creationData;
	}
	public void setCreationData(Date creationData) {
		this.creationData = creationData;
	}
	
	
}
