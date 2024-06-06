package com.example.demonstration.repository;

import com.example.demonstration.entity.DeviceGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceGroupRepo extends MongoRepository<DeviceGroup,String> {
}
