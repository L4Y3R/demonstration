package com.example.demonstration.controller;

import com.example.demonstration.dto.UserDTO;
import com.example.demonstration.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/users")
@CrossOrigin
public class UserContoller {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    //get all users
    @GetMapping
    public List<UserDTO> getAllUsers(){
        logger.info("Get All Users Request Received");
        return userService.getAllUsers();
    }

    //get one user
    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable("id") String id) {
        logger.info("Get User With ID {} Request Received",id);
        try{
          UserDTO user = userService.getUser(id);
          return ResponseEntity.ok(user).getBody();
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal error occurred", e);
        }
    }

    //create a user
    @PostMapping("/new")
    public UserDTO createUser(@RequestBody UserDTO userDTO){
        logger.info("Create User Request Received");
        return userService.createUser(userDTO);
    }

    //update a user
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        logger.info("Update User Request Received");
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //delete a user
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable String id) {
        logger.info("Delete User Request Received");
        Optional<UserDTO> deletedUser = userService.deleteUser(id);
        return deletedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
