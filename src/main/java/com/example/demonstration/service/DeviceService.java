package com.example.demonstration.service;

import com.example.demonstration.dto.DeviceDTO;
import com.example.demonstration.entity.Device;
import com.example.demonstration.entity.DeviceGroup;
import com.example.demonstration.entity.User;
import com.example.demonstration.exception.DeviceNotFoundException;
import com.example.demonstration.exception.GroupNotFoundException;
import com.example.demonstration.exception.UserNotFoundException;
import com.example.demonstration.repository.DeviceGroupRepo;
import com.example.demonstration.repository.DeviceRepo;
import com.example.demonstration.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class DeviceService {
    @Autowired
    private DeviceRepo deviceRepo;

    @Autowired
    private DeviceGroupRepo deviceGroupRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);


    //return the list of devices to the controller
    public List<DeviceDTO> getAllDevices(){
        try{
            List<Device> devices = deviceRepo.findAll();
            logger.info("All Devices Received");
            return modelMapper.map(devices, new TypeToken<List<DeviceDTO>>(){}.getType());
        }catch(Exception e){
            logger.error("Devices Could Not Be Received", e);
            throw new DeviceNotFoundException();
        }
    }

    //return a single device to the controller
    public DeviceDTO getDevice(String id){
        try{
            Optional<Device> deviceOptional = deviceRepo.findById(id);
            if (deviceOptional.isEmpty()){
                logger.error("Device with ID {} Does Not Exist", id);
                throw new DeviceNotFoundException();
            }else{
                logger.info("Device with Requested ID{} Received", id);
                return deviceOptional.map(device -> modelMapper.map(device, DeviceDTO.class)).orElse(null);
            }
        }catch(Exception e){
            logger.error("Device Could Not Be Received", e);
            throw new DeviceNotFoundException();
        }
    }

    //return a single device to the commands
    public DeviceDTO getDeviceForCommand(String deviceId, String userId){
        logger.info("Command Request Received for Confirmation");
        Optional<Device> deviceOptional = deviceRepo.findById(deviceId);
        if (deviceOptional.isPresent()) {
            Device device = deviceOptional.get();
            logger.info("Device Existence Confirmed");
            if (device.getUserId().equals(userId)) {
                logger.info("Device Existence with the User Confirmed");
                return modelMapper.map(device, DeviceDTO.class);
            } else {
                logger.warn("User Does Not Belong to the Device");
                throw new UserNotFoundException();
            }
        } else {
            logger.error("Invalid Details in Command Request");
            throw new DeviceNotFoundException();
        }
    }

    //update a document
    @Transactional
    public DeviceDTO updateDevice(String id, DeviceDTO deviceDTO) {
        //looks for the device with the id to update
        Optional<Device> existingDeviceOptional = deviceRepo.findById(id);
        if (existingDeviceOptional.isPresent()) {
            logger.info("Device Found with the Given ID");
            Device existingDevice = existingDeviceOptional.get(); //retrieves the device from the optional
            existingDevice.setDeviceName(deviceDTO.getDeviceName()); //sets name
            existingDevice.setDeviceType(deviceDTO.getDeviceType()); //sets type
            existingDevice.setStatus(deviceDTO.getStatus()); //set status
            deviceRepo.save(existingDevice);
            logger.info("Saved Updated Device");
            return modelMapper.map(existingDevice, DeviceDTO.class); //maps and returns the saved document
        } else {
            //no such device
            logger.error("Unable to Update the Device");
            throw new DeviceNotFoundException();
        }
    }

    //delete a document
    @Transactional
    public Optional<DeviceDTO> deleteDevice(String id) {
        //looks for the device with the id to delete
        Optional<Device> existingDeviceOptional = deviceRepo.findById(id);
        if (existingDeviceOptional.isPresent()) {
            logger.info("Device Found with the Given ID");
            //delete the device
            deviceRepo.deleteById(id);
            logger.info("Device Deleted with ID {}", id);
            DeviceDTO deletedDeviceDTO = modelMapper.map(existingDeviceOptional.get(), DeviceDTO.class); //retrieves the deleted document from device optional
            //retrieves all the device groups
            List<DeviceGroup> groupsToUpdate = deviceGroupRepo.findAll();
            //update each device group by deleting the device from those groups
            for (DeviceGroup group : groupsToUpdate) {
                if (group.getDevices().contains(id)) {
                    group.getDevices().remove(id);
                    deviceGroupRepo.save(group);
                }
                logger.info("Respective Devices in Device Group Deleted");
            }
            return Optional.of(deletedDeviceDTO);
        } else {
            //no such device
            logger.error("Unable to Delete the Device");
            throw new DeviceNotFoundException();
        }
    }

    //add a device to a group
    @Transactional
    public Device addDeviceToGroupWithUser(Device device, String groupName, String userName) {
        //find the group with the given name
        Optional<DeviceGroup> optionalGroup = deviceGroupRepo.findByGroupName(groupName);
        //find the user with the given username
        Optional<User> optionalUser = userRepo.findByUserName(userName);

        if (optionalGroup.isPresent() && optionalUser.isPresent()) {
            logger.info("Device Group and Device User Exists");
            User user = optionalUser.get(); //retrieve the user from optional
            device.setUserId(user.getUserId()); //set device's user id to the id of the given user
            deviceRepo.save(device); //save device
            logger.info("Saved Device to Group");
            optionalGroup.get().getDevices().add(device.getDeviceId()); //add the new device to the device list of the group entered by the user
            deviceGroupRepo.save(optionalGroup.get());//save the group
            logger.info("Saved the Group {}", groupName);
            return device;
        } else {
            //if the group or user does not exist
            if(optionalUser.isEmpty()){
                logger.error("User {} Does not Exist",userName);
                throw new UserNotFoundException();
            } else {
                logger.error("Group {} Does not Exist",groupName);
                throw new GroupNotFoundException();
            }
        }
    }

    //add a device
    @Transactional
    public Device addDeviceToUser(Device device, String userName) {
        //find the user with the given username
        Optional<User> optionalUser = userRepo.findByUserName(userName);
        if (optionalUser.isPresent()) {
            device.setUserId(optionalUser.get().getUserId()); //set user id of the device collection to the id of the entered user
            logger.error("Added Device to User{}", userName);
            return deviceRepo.save(device); //save the device
        } else {
            //no such user
            logger.error("User{} Does not Exist",userName);
            throw new UserNotFoundException();
        }
    }
}
