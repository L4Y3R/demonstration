package com.example.demonstration.controller;

import com.example.demonstration.dto.DeviceGroupDTO;
import com.example.demonstration.service.DeviceGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/groups")
@CrossOrigin
public class GroupController {
    @Autowired
    private DeviceGroupService deviceGroupService;

    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    //get all groups
    @GetMapping
    public List<DeviceGroupDTO> getAllGroups(){
        logger.info("Get All Groups Request Received");
        return deviceGroupService.getAllDeviceGroups();
    }

    //get one group
    @GetMapping("/{id}")
    public DeviceGroupDTO getGroup(@PathVariable("id") String id) {
        logger.info("Get Group With ID {} Request Received",id);
        return deviceGroupService. getDeviceGroup(id);
    }

    //create a group
    @PostMapping("/new")
    public DeviceGroupDTO createGroup(@RequestBody DeviceGroupDTO groupDTO){
        logger.info("Create Group Request Received");
        return deviceGroupService.createDeviceGroup(groupDTO);
    }

    //update a group
    @PutMapping("/{id}")
    public ResponseEntity<DeviceGroupDTO> updateGroup(@PathVariable String id, @RequestBody DeviceGroupDTO groupDTO) {
        logger.info("Update Group Request Received");
        DeviceGroupDTO updatedGroup = deviceGroupService.updateDeviceGroup(id, groupDTO);
        if (updatedGroup != null) {
            return ResponseEntity.ok(updatedGroup);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //delete a group
    @DeleteMapping("/{id}")
    public ResponseEntity<DeviceGroupDTO> deleteGroup(@PathVariable String id) {
        logger.info("Delete Group Request Received");
        Optional<DeviceGroupDTO> deletedGroup = deviceGroupService.deleteDeviceGroup(id);
        return deletedGroup.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
