package com.expleo.users.data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;

public class UserData {
	
/*User parameters */
	@ApiModelProperty (required=true, hidden = false)
	private String firstName;

	private String lastName;
	@ApiModelProperty (required=true, hidden = false)
	private String username;
	
	@ApiModelProperty (required=true, hidden = false)
	@NotEmpty(message = "Please enter a email address")
    @Email(message = "Please enter a valid email address")
	private String email;
	
	@ApiModelProperty (required=true, hidden = false)
	@NotBlank(message = "Please enter a PAN card number")
	private String panNumber;
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	
	
}
