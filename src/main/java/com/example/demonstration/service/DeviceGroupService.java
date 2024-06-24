package com.example.demonstration.service;

import com.example.demonstration.dto.DeviceGroupDTO;
import com.example.demonstration.entity.DeviceGroup;
import com.example.demonstration.exception.GroupExistsException;
import com.example.demonstration.exception.GroupNotFoundException;
import com.example.demonstration.repository.DeviceGroupRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(DeviceGroupService.class);

    //return the list of device groups to the controller
    public List<DeviceGroupDTO> getAllDeviceGroups(){
        try{
            List<DeviceGroup> deviceGroups = deviceGroupRepo.findAll();
            logger.info("Device Groups Received");
            return modelMapper.map(deviceGroups, new TypeToken<List<DeviceGroupDTO>>(){}.getType());
        }catch (Exception e){
            logger.error("Device Groups Not Received");
            throw new GroupNotFoundException();
        }
    }

    //return a single device group to the controller
    public DeviceGroupDTO getDeviceGroup(String id){
        Optional<DeviceGroup> deviceGroupOptional = deviceGroupRepo.findById(id);
        if (deviceGroupOptional.isEmpty()) throw new GroupNotFoundException();
        logger.info("Device Group with ID {} Received",id);
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
            logger.info("Device Groups Successfully Created");
            return deviceGroupDTO;
        }else{
            //group already exists
            logger.error("Device Group Already Exists");
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
            deviceGroupRepo.save(existingDeviceGroup);//save
            logger.info("Device Groups Updated");
            return modelMapper.map(existingDeviceGroup, DeviceGroupDTO.class); //maps and returns the saved document
        } else {
            //no such group exists
            logger.error("Device Groups Does Not Exist");
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
            logger.info("Device Group with ID {} Successfully Deleted",id);
            DeviceGroupDTO deletedDeviceGroupDTO = modelMapper.map(existingDeviceGroupOptional.get(), DeviceGroupDTO.class); // Map DeviceGroup to DeviceGroupDTO
            return Optional.of(deletedDeviceGroupDTO);
        } else {
            //no such group exists
            logger.error("Device Group with ID {} Does Not Exist",id);
            throw new GroupNotFoundException();
        }
    }
}
