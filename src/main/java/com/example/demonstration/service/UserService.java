package com.example.demonstration.service;


import com.example.demonstration.dto.UserDTO;
import com.example.demonstration.entity.User;
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
    private ModelMapper modelMapper;

    public List<UserDTO> getAllUsers(){
        List<User> users = userRepo.findAll();
        return modelMapper.map(users, new TypeToken<List<UserDTO>>(){}.getType());
    }

    public UserDTO getUser(String id){
        Optional<User> userOptional = userRepo.findById(id);
        if(userOptional.isPresent()){
            return modelMapper.map(userOptional.get(), UserDTO.class);
        }else {
            return null;
        }
    }

    public UserDTO createUser(UserDTO userDTO){
        userRepo.save(modelMapper.map(userDTO, User.class));
        return userDTO;
    }

    public UserDTO updateUser(String id, UserDTO userDTO){
        Optional<User> existingUserOptional = userRepo.findById(id);
        if(existingUserOptional.isPresent()){
            User user = existingUserOptional.get();
            modelMapper.map(userDTO, user);
            user.setUserId(id);
            userRepo.save(user);
            return userDTO;
        }else {
            return null;
        }
    }

    public Optional<UserDTO> deleteUser(String id) {
        Optional<User> existingUserOptional = userRepo.findById(id);
        if (existingUserOptional.isPresent()) {
            userRepo.deleteById(id);
            UserDTO deletedUserDTO = new UserDTO();
            deletedUserDTO.setUserName(existingUserOptional.get().getUserName());
            return Optional.of(deletedUserDTO);
        } else {
            return Optional.empty();
        }
    }


}
