package com.example.demonstration.repository;

import com.example.demonstration.entity.Device;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DeviceRepo extends MongoRepository <Device,String> {
    //custom query to return a user by user ID from the devices collection
    List<Device> findByUserId(String userId);
}
