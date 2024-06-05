package com.example.demonstration.controller;

import com.example.demonstration.dto.DeviceGroupDTO;
import com.example.demonstration.service.DeviceGroupService;
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

    //get all groups
    @GetMapping
    public List<DeviceGroupDTO> getAllGroups(){
        return deviceGroupService.getAllDeviceGroups();
    }

    //get one group
    @GetMapping("/{id}")
    public DeviceGroupDTO getGroup(@PathVariable("id") String id) {
        return deviceGroupService. getDeviceGroup(id);
    }

    //create a device
    @PostMapping("/new")
    public DeviceGroupDTO createGroup(@RequestBody DeviceGroupDTO groupDTO){
        return deviceGroupService.createDeviceGroup(groupDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceGroupDTO> updateGroup(@PathVariable String id, @RequestBody DeviceGroupDTO groupDTO) {
        DeviceGroupDTO updatedGroup = deviceGroupService.updateDeviceGroup(id, groupDTO);
        if (updatedGroup != null) {
            return ResponseEntity.ok(updatedGroup);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeviceGroupDTO> deleteGroup(@PathVariable String id) {
        Optional<DeviceGroupDTO> deletedGroup = deviceGroupService.deleteDeviceGroup(id);
        if (deletedGroup.isPresent()) {
            return ResponseEntity.ok(deletedGroup.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
