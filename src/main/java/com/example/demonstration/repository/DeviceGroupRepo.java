package com.example.demonstration.repository;

import com.example.demonstration.entity.DeviceGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DeviceGroupRepo extends MongoRepository<DeviceGroup, String> {
    Optional<DeviceGroup> findByGroupName(String groupName);
}
