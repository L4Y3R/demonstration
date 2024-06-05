package com.example.demonstration.controller;

import com.example.demonstration.dto.UserDTO;
import com.example.demonstration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/vi/users")
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
        return userService.getUser(id);
    }

    //create a device
    @PostMapping("/new")
    public UserDTO createUser(@RequestBody UserDTO userDTO){
        return userService.createUser(userDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable String id) {
        Optional<UserDTO> deletedUser = userService.deleteUser(id);
        if (deletedUser.isPresent()) {
            return ResponseEntity.ok(deletedUser.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
