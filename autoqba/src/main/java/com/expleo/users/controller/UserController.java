package com.expleo.users.controller;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.expleo.users.config.JwtTokenUtil;
import com.expleo.users.data.AuthenticationResponse;
import com.expleo.users.data.JwtRequest;
import com.expleo.users.data.JwtToken;
import com.expleo.users.data.UserData;
import com.expleo.users.data.UserResponse;
import com.expleo.users.exception.ApiException;
import com.expleo.users.exception.ApiExceptionBody;
import com.expleo.users.model.UserSchema;
import com.expleo.users.service.UserService;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@CrossOrigin
@RequestMapping("/users")
//@RestController @RequestMapping("/product")
@Api(value="onlinestore", description="User REST Controller", tags = { "Users" }) 
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

		
/*User Authentication*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/authenticate")
	@ApiOperation(value = "Users authentication")
	@ApiResponses({
		    @ApiResponse(code = 200, message = "User authenticated successfully",response = AuthenticationResponse.class), 
		    @ApiResponse(code = 401, message = "Unauthorised user"),
		    @ApiResponse(code = 403, message = "Forbidden user"),
		   // @ApiResponse(code = 404, message = "User already exist"),
		   // @ApiResponse(code = 500, message = "Internal error")
		})
	public ResponseEntity createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		try {
		UserSchema user = userService.authenticateUser(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		if(user == null) {
			 ApiExceptionBody body=new ApiExceptionBody();
			 body.setType("Error");
			 body.setMessage("Invalid credentials");
			 throw new ApiException(HttpStatus.NOT_FOUND, body );
		}else{
			AuthenticationResponse response=new AuthenticationResponse();
			Object token = jwtTokenUtil.generateToken(user);
			JwtToken jwtToken = new JwtToken();
			jwtToken.setToken(token);
			response.setMessage("User Authenticated Successfully");
			response.setBody(jwtToken);
			return ResponseEntity.ok(response);
		}
		}catch (Exception e) {
			if(e instanceof ApiException) {
				 return new ResponseEntity(((ApiException) e).body,((ApiException) e).code);
				}else {
					 e.printStackTrace();
					 return new ResponseEntity("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
				}
		}
	}
	
	
	
/*New User Registration */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("")
	@ApiOperation(value = "Create new user", authorizations = { @Authorization(value="Authorization") })
	 @ApiResponses({
		    @ApiResponse(code = 201, message = "User created successfully",response = UserResponse.class), 
		    @ApiResponse(code = 401, message = "Unauthorised user"),
		    @ApiResponse(code = 403, message = "Forbidden user"),
		    @ApiResponse(code = 404, message = "User already exist"),
		    @ApiResponse(code = 500, message = "Internal error")
		})
	public ResponseEntity registerUser(@Validated @RequestBody UserData userData, HttpHeaders headers ) {
				
		 try {
			 UserResponse data = userService.registerUser(userData);
			return new ResponseEntity<UserResponse>(data, HttpStatus.CREATED);
		 }catch(Exception e) {
			if(e instanceof ApiException) {
				 return new ResponseEntity(((ApiException) e).body,((ApiException) e).code);
				}else {
					 e.printStackTrace();
					 return new ResponseEntity("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
	}
	
	
	
	@GetMapping("/list")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "Get user list", authorizations = { @Authorization(value="Authorization") })
	@ApiResponses({//
	    @ApiResponse(code = 200, message = "Available user list",responseContainer = "List",response = UserResponse.class), //
	    @ApiResponse(code = 404, message = "User not found"),//
	    @ApiResponse(code = 401, message = "Invalid user")//
	})
	public ResponseEntity getAllUser(HttpHeaders headers ) {
		List<UserResponse> userResponse;
		try {
			userResponse = userService.getAllUser();
			return new ResponseEntity(userResponse, HttpStatus.OK);
		}catch(Exception e) {
			if(e instanceof ApiException) {
				 return new ResponseEntity(((ApiException) e).body,((ApiException) e).code);
				}else {
					 e.printStackTrace();
					 return new ResponseEntity("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
	}
	
	
/*Get User by ID*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/{id}")
	@ApiOperation(value = "Get user by ID", authorizations = { @Authorization(value="Authorization") })
	@ApiResponses({
		    @ApiResponse(code = 200, message = "User retrived successfully",response = UserResponse.class), 
		    @ApiResponse(code = 404, message = "User already exist"),
		    @ApiResponse(code = 401, message = "Unauthorised user"),
		    @ApiResponse(code = 500, message = "Internal error")
		})
	public ResponseEntity getUserById(@PathVariable String id) {
		
		try {
			UserResponse userResponse = userService.getUserById(id);
			return new ResponseEntity(userResponse, HttpStatus.OK);
		}catch(Exception e) {
			if(e instanceof ApiException) {
				 return new ResponseEntity(((ApiException) e).body,((ApiException) e).code);
				}
			else if(e instanceof NoSuchElementException ) {
				 ApiExceptionBody body=new ApiExceptionBody();
				 body.setType("Error");
				 body.setMessage("User details not found");
				 return new ResponseEntity(body, HttpStatus.NOT_FOUND);
			}
			else {
					 e.printStackTrace();
					 return new ResponseEntity("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
	}
	


/*Delete User by ID*/	
	@DeleteMapping("/{id}")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "Delete users by ID",authorizations = { @Authorization(value="Authorization") })
	@ApiResponses({
	    @ApiResponse(code = 200, message = "User deleted successfully", response=ApiExceptionBody.class), 
	    @ApiResponse(code = 404, message = "User not found"),
	    @ApiResponse(code = 401, message = "Unauthorised user"),
	    @ApiResponse(code = 500, message = "Internal error")
	})
	public ResponseEntity deleteUser(@PathVariable String id) {
		try {
			ApiExceptionBody apiResponse = userService.deleteUser(id);
			 return new ResponseEntity(apiResponse,HttpStatus.OK);
		}catch(Exception e) {
			if(e instanceof ApiException) {
				 return new ResponseEntity(((ApiException) e).body,((ApiException) e).code);
				}else {
					 e.printStackTrace();
					 return new ResponseEntity("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
	}
	
	
/*Update user details*/
	  @PutMapping("/{id}")
	  @SuppressWarnings({ "rawtypes", "unchecked" })
	  @ApiOperation(value = "Update users by ID",authorizations = { @Authorization(value="Authorization") })
	  @ApiResponses({
		    @ApiResponse(code = 200, message = "User updated successfully", response=UserResponse.class), 
		    @ApiResponse(code = 404, message = "User not found"),
		    @ApiResponse(code = 401, message = "Unauthorised user"),
		    @ApiResponse(code = 500, message = "Internal error")
		})
	  public ResponseEntity updateUser(@Validated @RequestBody UserSchema user,@PathVariable String id ) {
		 	  
	  try {
		  	UserResponse response = userService.updateUser(user,id); 
			return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
		 }catch(Exception e) {
			if(e instanceof ApiException) {
				 return new ResponseEntity(((ApiException) e).body,((ApiException) e).code);
				}else {
					 e.printStackTrace();
					 return new ResponseEntity("Internal error", HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
	  }
	 
	
	
	

/*Service call functions*/	
	
/*Get User by ID*/
	@GetMapping("/eureka/{id}")
	@ApiOperation(value = "Get user by ID", hidden=true)
	public UserSchema findByIdService(@PathVariable String id,HttpServletRequest request) {
		UserSchema userDetails = userService.findById(id);
		return userDetails;
	}
	
/*Get User by PAN number*/
	@GetMapping("/eureka/panNumber/{panNumber}")
	@ApiOperation(value = "Get user by ID", hidden=true)
	public UserSchema findByPanNumberService(@PathVariable String panNumber) {
		UserSchema userDetails = userService.findByPanNumber(panNumber);
		return userDetails;
	}
	
	@GetMapping("/getyml")
	@ApiOperation(value = "Get yml file",hidden=true)
	public String getYml() throws JSONException, IOException
	{
		String response=userService.getYml();
		return response;
	}

		
	
}
