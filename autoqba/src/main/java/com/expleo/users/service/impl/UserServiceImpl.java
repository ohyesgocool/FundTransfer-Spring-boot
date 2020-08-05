package com.expleo.users.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.message.Message;
import org.apache.tomcat.util.http.Parameters.FailReason;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.expleo.users.data.UserData;
import com.expleo.users.data.UserResponse;
import com.expleo.users.exception.ApiException;
import com.expleo.users.exception.ApiExceptionBody;
import com.expleo.users.model.UserSchema;
import com.expleo.users.repository.UserRepository;
import com.expleo.users.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.mongodb.client.model.FindOneAndReplaceOptions;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;
	

	
/* User Authentication */
	@Override
	public UserSchema authenticateUser(String username, String password) {
		UserSchema user = null;
		try {
			user = repository.findByUsername(username);
			if (user != null && user.getPassword().equals(password)) {
				return user;
			} else {
				return null;
			}

		} catch (Exception e) {
			return null;
		}
	}



/* Create new Users */
	@Override
	public UserResponse registerUser(UserData userData) throws Exception  {
		
		String panNumber = userData.getPanNumber();
		try {
			UserSchema userPanNumber = repository.findByPanNumber(panNumber);
			UserSchema userName=repository.findByUsername(userData.getUsername());
			
			if(userPanNumber==null) {
				if(userName==null) {
					UserSchema userSchema = new UserSchema();
					userSchema.setFirstName(userData.getFirstName());
					userSchema.setLastName(userData.getLastName());
					userSchema.setUsername(userData.getUsername());
					userSchema.setEmail(userData.getEmail());
					userSchema.setPanNumber(userData.getPanNumber());
					userSchema.setPassword("root");
					UserSchema userdetails = repository.save(userSchema);
					
					UserResponse responseObject = new UserResponse();
					responseObject.setId(userdetails.getId());
					responseObject.setFirstName(userdetails.getFirstName());
					responseObject.setLastName(userdetails.getLastName());
					responseObject.setUsername(userdetails.getUsername());
					responseObject.setPanNumber(userdetails.getPanNumber());
					responseObject.setEmail(userdetails.getEmail());
					responseObject.setCreationData(userdetails.getCreationDate());
					return responseObject;	
				}else {
					ApiExceptionBody body=new ApiExceptionBody();
					 body.setType("Error");
					 body.setMessage("Username already exist");
					 throw new ApiException(HttpStatus.NOT_FOUND, body );
				}
			}
			else {
				 ApiExceptionBody body=new ApiExceptionBody();
				 body.setType("Error");
				 body.setMessage("User details already exist");
				 throw new ApiException(HttpStatus.NOT_FOUND, body );
			}
			}catch(Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

	
	
	
/* Get all Users */
	@Override
	public ArrayList<UserResponse>  getAllUser() throws Exception {
		
		try {
		 List<UserSchema> responseObject = repository.findAll();
		 
		 ArrayList<UserResponse> responseList=new ArrayList<UserResponse>();
		if(responseObject!=null) {
			Iterator<UserSchema> iter =responseObject.iterator();
			while(iter.hasNext()) 
			{
				UserResponse userResponse = new UserResponse();
				UserSchema userDetails=(UserSchema) iter.next();
				userResponse.setId(userDetails.getId());
				userResponse.setFirstName(userDetails.getFirstName());
				userResponse.setLastName(userDetails.getLastName());
				userResponse.setUsername(userDetails.getUsername());
				userResponse.setEmail(userDetails.getEmail());
				userResponse.setPanNumber(userDetails.getPanNumber());
				userResponse.setCreationData(userDetails.getCreationDate());
				responseList.add(userResponse);
			}
			return responseList;
		}else {
			 ApiExceptionBody body=new ApiExceptionBody();
			 body.setType("Error");
			 body.setMessage("User details not found");
			 throw new ApiException(HttpStatus.NOT_FOUND, body );
		}
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
			}
		}

	
	
	
/* Get user Details by ID */
	@Override
	public UserResponse getUserById(String id) throws Exception {
	
		try {
			UserSchema userdetails = repository.findById(id).get();
			if(userdetails!=null) {
				UserResponse responseObject = new UserResponse();
				responseObject.setId(userdetails.getId());
				responseObject.setFirstName(userdetails.getFirstName());
				responseObject.setLastName(userdetails.getLastName());
				responseObject.setUsername(userdetails.getUsername());
				responseObject.setEmail(userdetails.getEmail());
				responseObject.setPanNumber(userdetails.getPanNumber());
				responseObject.setCreationData(userdetails.getCreationDate());
				
				return responseObject;
			}else {
				 ApiExceptionBody body=new ApiExceptionBody();
				 body.setType("Error");
				 body.setMessage("User details not found");
				 throw new ApiException(HttpStatus.NOT_FOUND, body );
			}
			}catch(Exception e) {
				e.printStackTrace();
				throw e;
			}
		
		
		}
	
	
	
	

/* Delete User */
	@Override
	public ApiExceptionBody deleteUser(String id) throws Exception {
		try {
			boolean result = repository.findById(id).isPresent();
			if(result==true) {
				repository.deleteById(id);
				ApiExceptionBody body=new ApiExceptionBody();
				body.setType("Success");
				body.setMessage("User deleted successfully");
				return body;
				}
			else {
				ApiExceptionBody body=new ApiExceptionBody();
				body.setType("Error");
				body.setMessage("User not found");
				throw new ApiException(HttpStatus.NOT_FOUND, body );
			}
		}catch(Exception e) {
				e.printStackTrace();
				throw e;
			}
	}
	
	
	
/*update user*/	
	@Override
	public UserResponse updateUser(UserSchema user, String id) throws Exception {
		try {
		Optional<UserSchema> existingUser = repository.findById(id);
		
		if(existingUser.isPresent()) {
			if(existingUser.get().getPanNumber().equals(user.getPanNumber()))
			{
				if(existingUser.get().getUsername().equalsIgnoreCase(user.getUsername())) {
					user.setId(id);
					UserSchema userdetails = repository.save(user);
					UserResponse responseObject = new UserResponse();
					responseObject.setId(userdetails.getId());
					responseObject.setFirstName(userdetails.getFirstName());
					responseObject.setLastName(userdetails.getLastName());
					responseObject.setUsername(userdetails.getUsername());
					responseObject.setPanNumber(userdetails.getPanNumber());
					responseObject.setEmail(userdetails.getEmail());
					responseObject.setCreationData(userdetails.getCreationDate());
					return responseObject;	
				}else {
					ApiExceptionBody body=new ApiExceptionBody();
					 body.setType("Error");
					 body.setMessage("Username already exist");
					 throw new ApiException(HttpStatus.NOT_FOUND, body );
				}
			}
			else {
				ApiExceptionBody body=new ApiExceptionBody();
				 body.setType("Error");
				 body.setMessage("PAN number does not match");
				 throw new ApiException(HttpStatus.NOT_FOUND, body );
			}
		}else {
			 ApiExceptionBody body=new ApiExceptionBody();
			 body.setType("Error");
			 body.setMessage("No user found");
			 throw new ApiException(HttpStatus.NOT_FOUND, body );
		}
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
			}
	}


	
	
/*Internal Function*/
	@Override
	public UserSchema findByUsername(String username) {

		UserSchema user = null;
		try {
			user = repository.findByUsername(username);
			if (user != null) {
				return user;
			} else {
				return null;
			}

		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}


/*Service Call functions*/
	@Override
	public UserSchema findById(String id) {
		Optional<UserSchema> response = repository.findById(id);
		UserSchema result = response.get();
		
		return result;
	}



	@Override
	public UserSchema findByPanNumber(String panNumber) {
		UserSchema user = repository.findByPanNumber(panNumber);
		return user;
	}
	
	
/* converting Json to Yaml */
	public String getYml() throws JSONException, IOException {
		JSONObject json = readJsonFromUrl("http://localhost:8080/v2/api-docs");

		UserServiceImpl obj=new UserServiceImpl();
	
    String yaml1=json.toString();
	String yamlobj=obj.asYaml(yaml1);
	//System.out.println(yamlobj);
	return yamlobj;
		
	}
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }
	  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		    InputStream is = new URL(url).openStream();
		    try {
		      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		      String jsonText = readAll(rd);
		      JSONObject json = new JSONObject(jsonText);
		      return json;
		    } finally {
		      is.close();
		    }
	  }
	  public  String asYaml(String jsonString) throws JsonProcessingException, IOException {
	      
	        JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonString);
	    
	        String jsonAsYaml = new YAMLMapper().writeValueAsString(jsonNodeTree);
	        return jsonAsYaml;
	    }





	

}
