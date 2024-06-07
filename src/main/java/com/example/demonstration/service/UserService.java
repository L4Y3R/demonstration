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

    public List<UserDTO> getAllUsers(){
        List<User> users = userRepo.findAll();
        return modelMapper.map(users, new TypeToken<List<UserDTO>>(){}.getType());
    }

    public UserDTO getUser(String id){
        Optional<User> userOptional = userRepo.findById(id);
        if(userOptional.isEmpty()) throw new UserNotFoundException();
        return userOptional.map(user -> modelMapper.map(user, UserDTO.class)).orElse(null);
    }

    public UserDTO createUser(UserDTO userDTO){
        Optional<User> user = userRepo.findByUserName(userDTO.getUserName());
        if(user.isEmpty()){
            userRepo.save(modelMapper.map(userDTO, User.class));
            return userDTO;
        } else {
            throw new UserExistsException();
        }
    }

    public UserDTO updateUser(String id, UserDTO userDTO) {
        Optional<User> existingUserOptional = userRepo.findById(id);
        if (existingUserOptional.isPresent()) {
            User user = existingUserOptional.get();
            modelMapper.map(userDTO, user);
            userRepo.save(user);
            return userDTO;
        } else {
            throw new UserNotFoundException();
        }
    }

    public Optional<UserDTO> deleteUser(String id) {
        Optional<User> existingUserOptional = userRepo.findById(id);

        if (existingUserOptional.isPresent()) {
            List<Device> userDevices = deviceRepo.findByUserId(id);
            if (!userDevices.isEmpty()) {
                throw new UserHasDevicesException();
            }
            userRepo.deleteById(id);
            UserDTO deletedUserDTO = modelMapper.map(existingUserOptional.get(), UserDTO.class);
            return Optional.of(deletedUserDTO);
        } else {
            throw new UserNotFoundException();
        }
    }
}
