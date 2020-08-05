package com.expleo.users.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.expleo.users.model.UserSchema;


@Repository
public interface UserRepository extends MongoRepository<UserSchema, String> {

	public UserSchema findByUsername(String username);
	public UserSchema findByPanNumber(String panNumber);
	public UserSchema findByUsernameAndPanNumber(String userName, String panNumber);
	
}
