package com.expleo.users.service;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.springframework.stereotype.Service;
import com.expleo.users.data.UserData;
import com.expleo.users.data.UserResponse;
import com.expleo.users.exception.ApiExceptionBody;
import com.expleo.users.model.UserSchema;

//import com.expleo.aib.data.UserRegisterationResponse;



public interface UserService {
	
	
	public UserSchema authenticateUser(String username, String password);
	//public ServiceResponse registerUser(UserData userData);
	//public ServiceResponse getAllUser();
	//public ServiceResponse getUserById(String id);
	public UserSchema findById(String id);
	public UserSchema findByUsername(String username);
	public UserSchema findByPanNumber(String panNumber);
	
	//public  ServiceResponse deleteUser(String id);
	//public ServiceResponse updateUser(UserSchema user, String id);
	

	public UserResponse registerUser(UserData userData) throws Exception;
	public List<UserResponse>  getAllUser() throws Exception;
	public UserResponse getUserById(String id) throws Exception;
	public  ApiExceptionBody deleteUser(String id) throws Exception;
	public UserResponse updateUser(UserSchema user, String id)throws Exception;
	public String getYml() throws JSONException, IOException;
}
