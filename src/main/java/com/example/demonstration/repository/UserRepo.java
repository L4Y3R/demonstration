package com.example.demonstration.repository;

import com.example.demonstration.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<User,String> {
    //custom query to return a user by username
    Optional<User> findByUserName(String userName);
}
