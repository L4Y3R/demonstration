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

    public List<DeviceDTO> getAllDevices(){
        List<Device> devices = deviceRepo.findAll();
        return modelMapper.map(devices, new TypeToken<List<DeviceDTO>>(){}.getType());
    }

    public DeviceDTO getDevice(String id){
        Optional<Device> deviceOptional = deviceRepo.findById(id);
        if (deviceOptional.isEmpty()) throw new DeviceNotFoundException();
        return deviceOptional.map(device -> modelMapper.map(device, DeviceDTO.class)).orElse(null);
    }

    /*
    public DeviceDTO createDevice (DeviceDTO deviceDTO){
        deviceRepo.save(modelMapper.map(deviceDTO, Device.class));
        return deviceDTO;
    }
     */

    public DeviceDTO updateDevice(String id, DeviceDTO deviceDTO) {
        Optional<Device> existingDeviceOptional = deviceRepo.findById(id);
        if (existingDeviceOptional.isPresent()) {
            Device existingDevice = existingDeviceOptional.get();
            existingDevice.setDeviceName(deviceDTO.getDeviceName());
            existingDevice.setDeviceType(deviceDTO.getDeviceType());
            existingDevice.setStatus(deviceDTO.getStatus());
            deviceRepo.save(existingDevice);
            return modelMapper.map(existingDevice, DeviceDTO.class);
        } else {
            throw new DeviceNotFoundException();
        }
    }

    public Optional<DeviceDTO> deleteDevice(String id) {
        Optional<Device> existingDeviceOptional = deviceRepo.findById(id);
        if (existingDeviceOptional.isPresent()) {
            deviceRepo.deleteById(id);
            DeviceDTO deletedDeviceDTO = modelMapper.map(existingDeviceOptional.get(), DeviceDTO.class);

            List<DeviceGroup> groupsToUpdate = deviceGroupRepo.findAll();
            for (DeviceGroup group : groupsToUpdate) {
                if (group.getDevices().contains(id)) {
                    group.getDevices().remove(id);
                    deviceGroupRepo.save(group);
                }
            }
            return Optional.of(deletedDeviceDTO);
        } else {
            throw new DeviceNotFoundException();
        }
    }

    public Device addDeviceToGroupWithUser(Device device, String groupName, String userName) {
        Optional<DeviceGroup> optionalGroup = deviceGroupRepo.findByGroupName(groupName);
        Optional<User> optionalUser = userRepo.findByUserName(userName);

        if (optionalGroup.isPresent() && optionalUser.isPresent()) {
            User user = optionalUser.get();
            Device savedDevice = deviceRepo.save(device);
            savedDevice.setUserId(user.getUserId());
            deviceRepo.save(savedDevice);
            optionalGroup.get().getDevices().add(savedDevice.getDeviceId());
            deviceGroupRepo.save(optionalGroup.get());
            return savedDevice;
        } else {
            if (optionalUser.isEmpty()) throw new UserNotFoundException();
            return null;
        }
    }

    public Device addDeviceToUser(Device device, String userName) {
        Optional<User> optionalUser = userRepo.findByUserName(userName);
        if (optionalUser.isPresent()) {
            device.setUserId(optionalUser.get().getUserId());
            return deviceRepo.save(device);
        } else {
            throw new UserNotFoundException();
        }
    }
}
