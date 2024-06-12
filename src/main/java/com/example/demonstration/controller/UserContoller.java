package com.example.demonstration.controller;

import com.example.demonstration.dto.UserDTO;
import com.example.demonstration.service.UserService;
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

    //get all users
    @GetMapping
    public List<UserDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    //get one user
    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable("id") String id) {
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
        return userService.createUser(userDTO);
    }

    //update a user
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
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
        Optional<UserDTO> deletedUser = userService.deleteUser(id);
        return deletedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
