package com.example.demonstration.service;

import com.example.demonstration.dto.DeviceGroupDTO;
import com.example.demonstration.entity.DeviceGroup;
import com.example.demonstration.exception.GroupExistsException;
import com.example.demonstration.exception.GroupNotFoundException;
import com.example.demonstration.repository.DeviceGroupRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceGroupService {
    @Autowired
    private DeviceGroupRepo deviceGroupRepo;
    @Autowired
    private ModelMapper modelMapper;

    //return the list of device groups to the controller
    public List<DeviceGroupDTO> getAllDeviceGroups(){
        List<DeviceGroup> deviceGroups = deviceGroupRepo.findAll();
        return modelMapper.map(deviceGroups, new TypeToken<List<DeviceGroupDTO>>(){}.getType());
    }

    //return a single device group to the controller
    public DeviceGroupDTO getDeviceGroup(String id){
        Optional<DeviceGroup> deviceGroupOptional = deviceGroupRepo.findById(id);
        if (deviceGroupOptional.isEmpty()) throw new GroupNotFoundException();
        return deviceGroupOptional.map(deviceGroup -> modelMapper.map(deviceGroup, DeviceGroupDTO.class)).orElse(null);
    }

    //create a new document
    @Transactional
    public DeviceGroupDTO createDeviceGroup (DeviceGroupDTO deviceGroupDTO){
        //looks for the groups that has the same name in the database
        Optional<DeviceGroup> group = deviceGroupRepo.findByGroupName(deviceGroupDTO.getGroupName());
        if(group.isEmpty()){
            //if there are no groups with the same name, save the document
            deviceGroupRepo.save(modelMapper.map(deviceGroupDTO, DeviceGroup.class));
            return deviceGroupDTO;
        }else{
            //group already exists
            throw new GroupExistsException();
        }
    }

    //update the document
    @Transactional
    public DeviceGroupDTO updateDeviceGroup(String id, DeviceGroupDTO deviceGroupDTO) {
        //finds the device group from the database with the given id
        Optional<DeviceGroup> existingDeviceGroupOptional = deviceGroupRepo.findById(id);
        if (existingDeviceGroupOptional.isPresent()) {
            DeviceGroup existingDeviceGroup = existingDeviceGroupOptional.get(); //retrieves device group from the optional
            existingDeviceGroup.setGroupName(deviceGroupDTO.getGroupName()); //set group name
            existingDeviceGroup.setAdmin(deviceGroupDTO.getAdmin()); //set admin
            deviceGroupRepo.save(existingDeviceGroup); //save
            return modelMapper.map(existingDeviceGroup, DeviceGroupDTO.class); //maps and returns the saved document
        } else {
            //no such group exists
            throw new GroupNotFoundException();
        }
    }

    //delete a device group
    @Transactional
    public Optional<DeviceGroupDTO> deleteDeviceGroup(String id) {
        //finds the device group from the database with the given id
        Optional<DeviceGroup> existingDeviceGroupOptional = deviceGroupRepo.findById(id);
        if (existingDeviceGroupOptional.isPresent()) {
            deviceGroupRepo.deleteById(id);
            DeviceGroupDTO deletedDeviceGroupDTO = modelMapper.map(existingDeviceGroupOptional.get(), DeviceGroupDTO.class); // Map DeviceGroup to DeviceGroupDTO
            return Optional.of(deletedDeviceGroupDTO);
        } else {
            //no such group exists
            throw new GroupNotFoundException();
        }
    }
}
