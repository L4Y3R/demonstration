package com.example.demonstration.service;

import com.example.demonstration.dto.UserDTO;
import com.example.demonstration.entity.Device;
import com.example.demonstration.entity.User;
import com.example.demonstration.exception.UserExistsException;
import com.example.demonstration.exception.UserHasDevicesException;
import com.example.demonstration.exception.UserNotFoundException;
import com.example.demonstration.repository.DeviceRepo;
import com.example.demonstration.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DeviceRepo deviceRepo;

    @Autowired
    private ModelMapper modelMapper;

    //return the list of users to the controller
    public List<UserDTO> getAllUsers(){
        List<User> users = userRepo.findAll();
        return modelMapper.map(users, new TypeToken<List<UserDTO>>(){}.getType());
    }

    //return a single user to the controller
    public UserDTO getUser(String id){
        Optional<User> userOptional = userRepo.findById(id);
        if(userOptional.isEmpty()) throw new UserNotFoundException();
        return userOptional.map(user -> modelMapper.map(user, UserDTO.class)).orElse(null);
    }

    //create a user
    public UserDTO createUser(UserDTO userDTO){
        //retrieve any user with the same username
        Optional<User> user = userRepo.findByUserName(userDTO.getUserName());
        if(user.isEmpty()){
            userRepo.save(modelMapper.map(userDTO, User.class)); //save the user
            return userDTO;
        } else {
            //user already exists
            throw new UserExistsException();
        }
    }

    //update the user
    public UserDTO updateUser(String id, UserDTO userDTO) {
        //get the user document that needs updating
        Optional<User> existingUserOptional = userRepo.findById(id);
        if (existingUserOptional.isPresent()) {
            User user = existingUserOptional.get(); //retrieve the user from the optional
            modelMapper.map(userDTO, user);
            userRepo.save(user);
            return userDTO;
        } else {
            //no such user
            throw new UserNotFoundException();
        }
    }

    //delete a user
    public Optional<UserDTO> deleteUser(String id) {
        //get the user document to be deleted
        Optional<User> existingUserOptional = userRepo.findById(id);

        if (existingUserOptional.isPresent()) {
            List<Device> userDevices = deviceRepo.findByUserId(id); //get the list of devices associated with the user
            if (!userDevices.isEmpty()) {
                throw new UserHasDevicesException(); //user has devices and cannot be deleted
            }
            //if there are no associated devices
            userRepo.deleteById(id);
            UserDTO deletedUserDTO = modelMapper.map(existingUserOptional.get(), UserDTO.class);
            return Optional.of(deletedUserDTO);
        } else {
            //no such user
            throw new UserNotFoundException();
        }
    }
}
