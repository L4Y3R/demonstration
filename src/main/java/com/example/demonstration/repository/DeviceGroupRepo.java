package com.example.demonstration.repository;

import com.example.demonstration.entity.DeviceGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DeviceGroupRepo extends MongoRepository<DeviceGroup, String> {
    //custom query to return a device group by group name
    Optional<DeviceGroup> findByGroupName(String groupName);
}
