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

@Service
@Transactional
public class DeviceService {
    @Autowired
    private DeviceRepo deviceRepo;

    @Autowired
    private DeviceGroupRepo deviceGroupRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    //return the list of devices to the controller
    public List<DeviceDTO> getAllDevices(){
        List<Device> devices = deviceRepo.findAll();
        return modelMapper.map(devices, new TypeToken<List<DeviceDTO>>(){}.getType());
    }

    //return a single device to the controller
    public DeviceDTO getDevice(String id){
        Optional<Device> deviceOptional = deviceRepo.findById(id);
        if (deviceOptional.isEmpty()) throw new DeviceNotFoundException();
        return deviceOptional.map(device -> modelMapper.map(device, DeviceDTO.class)).orElse(null);
    }

    //update a document
    public DeviceDTO updateDevice(String id, DeviceDTO deviceDTO) {
        //looks for the device with the id to update
        Optional<Device> existingDeviceOptional = deviceRepo.findById(id);
        if (existingDeviceOptional.isPresent()) {
            Device existingDevice = existingDeviceOptional.get(); //retrieves the device from the optional
            existingDevice.setDeviceName(deviceDTO.getDeviceName()); //sets name
            existingDevice.setDeviceType(deviceDTO.getDeviceType()); //sets type
            existingDevice.setStatus(deviceDTO.getStatus()); //set status
            deviceRepo.save(existingDevice);
            return modelMapper.map(existingDevice, DeviceDTO.class); //maps and returns the saved document
        } else {
            //no such device
            throw new DeviceNotFoundException();
        }
    }

    //delete a document
    public Optional<DeviceDTO> deleteDevice(String id) {
        //looks for the device with the id to delete
        Optional<Device> existingDeviceOptional = deviceRepo.findById(id);
        if (existingDeviceOptional.isPresent()) {
            //delete the device
            deviceRepo.deleteById(id);
            DeviceDTO deletedDeviceDTO = modelMapper.map(existingDeviceOptional.get(), DeviceDTO.class); //retrieves the deleted document from device optional
            //retrieves all the device groups
            List<DeviceGroup> groupsToUpdate = deviceGroupRepo.findAll();
            //update each device group by deleting the device from those groups
            for (DeviceGroup group : groupsToUpdate) {
                if (group.getDevices().contains(id)) {
                    group.getDevices().remove(id);
                    deviceGroupRepo.save(group);
                }
            }
            return Optional.of(deletedDeviceDTO);
        } else {
            //no such device
            throw new DeviceNotFoundException();
        }
    }

    //add a device to a group
    public Device addDeviceToGroupWithUser(Device device, String groupName, String userName) {
        //find the group with the given name
        Optional<DeviceGroup> optionalGroup = deviceGroupRepo.findByGroupName(groupName);
        //find the user with the given username
        Optional<User> optionalUser = userRepo.findByUserName(userName);

        if (optionalGroup.isPresent() && optionalUser.isPresent()) {
            User user = optionalUser.get(); //retrieve the user from optional
            device.setUserId(user.getUserId()); //set device's user id to the id of the given user
            deviceRepo.save(device); //save device
            optionalGroup.get().getDevices().add(device.getDeviceId()); //add the new device to the device list of the group entered by the user
            deviceGroupRepo.save(optionalGroup.get());//save the group
            return device;
        } else {
            //if the group or user does not exist
            if(optionalUser.isEmpty()){
                throw new UserNotFoundException();
            } else {
                throw new GroupNotFoundException();
            }
        }
    }

    //add a device
    public Device addDeviceToUser(Device device, String userName) {
        //find the user with the given username
        Optional<User> optionalUser = userRepo.findByUserName(userName);
        if (optionalUser.isPresent()) {
            device.setUserId(optionalUser.get().getUserId()); //set user id of the device collection to the id of the entered user
            return deviceRepo.save(device); //save the device
        } else {
            //no such user
            throw new UserNotFoundException();
        }
    }
}
