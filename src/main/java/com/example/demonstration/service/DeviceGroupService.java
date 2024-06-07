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
@Transactional
public class DeviceGroupService {
    @Autowired
    private DeviceGroupRepo deviceGroupRepo;
    @Autowired
    private ModelMapper modelMapper;

    public List<DeviceGroupDTO> getAllDeviceGroups(){
        List<DeviceGroup> deviceGroups = deviceGroupRepo.findAll();
        return modelMapper.map(deviceGroups, new TypeToken<List<DeviceGroupDTO>>(){}.getType());
    }

    public DeviceGroupDTO getDeviceGroup(String id){
        Optional<DeviceGroup> deviceGroupOptional = deviceGroupRepo.findById(id);
        if (deviceGroupOptional.isEmpty()) throw new GroupNotFoundException();
        return deviceGroupOptional.map(deviceGroup -> modelMapper.map(deviceGroup, DeviceGroupDTO.class)).orElse(null);
    }

    public DeviceGroupDTO createDeviceGroup (DeviceGroupDTO deviceGroupDTO){
        Optional<DeviceGroup> group = deviceGroupRepo.findByGroupName(deviceGroupDTO.getGroupName());
        if(group.isEmpty()){
            deviceGroupRepo.save(modelMapper.map(deviceGroupDTO, DeviceGroup.class));
            return deviceGroupDTO;
        }else{
            throw new GroupExistsException();
        }
    }

    public DeviceGroupDTO updateDeviceGroup(String id, DeviceGroupDTO deviceGroupDTO) {
        Optional<DeviceGroup> existingDeviceGroupOptional = deviceGroupRepo.findById(id);
        if (existingDeviceGroupOptional.isPresent()) {
            DeviceGroup existingDeviceGroup = existingDeviceGroupOptional.get();
            existingDeviceGroup.setGroupName(deviceGroupDTO.getGroupName());
            existingDeviceGroup.setAdmin(deviceGroupDTO.getAdmin());
            deviceGroupRepo.save(existingDeviceGroup);
            return modelMapper.map(existingDeviceGroup, DeviceGroupDTO.class);
        } else {
            throw new GroupNotFoundException();
        }
    }
    public Optional<DeviceGroupDTO> deleteDeviceGroup(String id) {
        Optional<DeviceGroup> existingDeviceGroupOptional = deviceGroupRepo.findById(id);
        if (existingDeviceGroupOptional.isPresent()) {
            deviceGroupRepo.deleteById(id);
            DeviceGroupDTO deletedDeviceGroupDTO = modelMapper.map(existingDeviceGroupOptional.get(), DeviceGroupDTO.class); // Map DeviceGroup to DeviceGroupDTO using ModelMapper
            return Optional.of(deletedDeviceGroupDTO);
        } else {
            throw new GroupNotFoundException();
        }
    }
}
