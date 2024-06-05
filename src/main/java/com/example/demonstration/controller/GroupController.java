package com.example.demonstration.controller;

import com.example.demonstration.dto.DeviceGroupDTO;
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
    private GroupService groupService;

    //get all groups
    @GetMapping
    public List<DeviceGroupDTO> getAllGroups(){
        return groupService.getAllGroups();
    }

    //get one group
    @GetMapping("/{id}")
    public DeviceGroupDTO getGroup(@PathVariable("id") String id) {
        return groupService.getGroup(id);
    }

    //create a device
    @PostMapping("/new")
    public DeviceGroupDTO createGroup(@RequestBody DeviceGroupDTO groupDTO){
        return groupService.createGroup(groupDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceGroupDTO> updateGroup(@PathVariable String id, @RequestBody DeviceGroupDTO groupDTO) {
        DeviceGroupDTO updatedGroup = groupService.updateGroup(id, groupDTO);
        if (updatedGroup != null) {
            return ResponseEntity.ok(updatedGroup);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeviceGroupDTO> deleteGroup(@PathVariable String id) {
        Optional<DeviceGroupDTO> deletedGroup = groupService.deleteGroup(id);
        if (deletedGroup.isPresent()) {
            return ResponseEntity.ok(deletedGroup.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
