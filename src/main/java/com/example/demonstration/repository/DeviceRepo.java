package com.example.demonstration.repository;

import com.example.demonstration.entity.Device;
import com.example.demonstration.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepo extends MongoRepository <Device,String> {
    List<Device> findByUserId(String userId);
}
