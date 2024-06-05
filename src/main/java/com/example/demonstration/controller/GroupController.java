package com.example.demonstration.controller;

import com.example.demonstration.dto.GroupDTO;
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
    public List<GroupDTO> getAllGroups(){
        return groupService.getAllGroups();
    }

    //get one group
    @GetMapping("/{id}")
    public GroupDTO getGroup(@PathVariable("id") String id) {
        return groupService.getGroup(id);
    }

    //create a device
    @PostMapping("/new")
    public GroupDTO createGroup(@RequestBody GroupDTO groupDTO){
        return groupService.createGroup(groupDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupDTO> updateGroup(@PathVariable String id, @RequestBody GroupDTO groupDTO) {
        GroupDTO updatedGroup = groupService.updateGroup(id, groupDTO);
        if (updatedGroup != null) {
            return ResponseEntity.ok(updatedGroup);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GroupDTO> deleteGroup(@PathVariable String id) {
        Optional<GroupDTO> deletedGroup = groupService.deleteGroup(id);
        if (deletedGroup.isPresent()) {
            return ResponseEntity.ok(deletedGroup.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
