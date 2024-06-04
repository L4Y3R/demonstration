package com.example.demonstration.repository;

import com.example.demonstration.entity.Device;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceRepo extends MongoRepository <Device,String> {
}
