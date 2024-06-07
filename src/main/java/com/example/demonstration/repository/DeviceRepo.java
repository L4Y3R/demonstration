package com.example.demonstration.repository;

import com.example.demonstration.entity.Device;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DeviceRepo extends MongoRepository <Device,String> {
    List<Device> findByUserId(String userId);
}
