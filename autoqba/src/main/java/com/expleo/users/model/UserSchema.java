package com.expleo.users.model;

import java.util.Date;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty ;

@ApiModel(description="User Schema")

@Document(collection = "User")
public class UserSchema {
	
	@Id 
	@ApiModelProperty (required=false, hidden = true)
	private String id;
	@ApiModelProperty (required=true, hidden = false)
	@NotEmpty(message = "Please enter a first name")
	private String firstName;
	@ApiModelProperty (required=true, hidden = false)
	private String lastName;
	@ApiModelProperty (required=true, hidden = false)
	private String username;
	@ApiModelProperty (required=true, hidden = false)
	@NotEmpty(message = "Please enter a email address")
    @Email(message = "Please enter a valid email address")
	private String email;
	private String password;
	@ApiModelProperty (required=false, hidden = true)
	private Date creationDate;
	@ApiModelProperty (required=true, hidden = false)
	@NotBlank(message = "Please enter a PAN card number")
	private String panNumber;
	
	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserSchema() {
		this.creationDate = new Date();
	}
	
	public Date getCreationDate() {
		return creationDate;
	}

	//public void setCreationDate(Date creationDate) {
	//	this.creationDate = creationDate;
	//}
	public String getId() {
		return id;
	}

	//public void setId(String id) {
	//	this.id = id;
	//}
	
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	

}
