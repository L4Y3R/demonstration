package com.example.demonstration.service;


import com.example.demonstration.dto.DeviceDTO;
import com.example.demonstration.entity.Device;
import com.example.demonstration.repository.DeviceRepo;
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
        if (deviceGroupOptional.isPresent()) {
            return modelMapper.map(deviceGroupOptional.get(), DeviceGroupDTO.class);
        } else {
            return null;
        }
    }

    public DeviceGroupDTO createDeviceGroup (DeviceGroupDTO deviceGroupDTO){
        deviceGroupRepo.save(modelMapper.map(deviceGroupDTO, DeviceGroup.class));
        return deviceGroupDTO;
    }

    public DeviceGroupDTO updateDeviceGroup(String id, DeviceGroupDTO deviceGroupDTO) {
        Optional<DeviceGroup> existingDeviceGroupOptional = deviceGroupRepo.findById(id);
        if (existingDeviceGroupOptional.isPresent()) {
            DeviceGroup deviceGroup = existingDeviceGroupOptional.get();
            modelMapper.map(deviceGroupDTO, deviceGroup);
            deviceGroup.setDeviceGroupId(id);
            deviceGroupRepo.save(deviceGroup);
            return deviceGroupDTO;
        } else {
            return null;
        }
    }

    public Optional<DeviceGroupDTO> deleteDeviceGroup(String id) {
        Optional<DeviceGroup> existingDeviceGroupOptional = deviceGroupRepo.findById(id);
        if (existingDeviceGroupOptional.isPresent()) {
            deviceGroupRepo.deleteById(id);
            DeviceGroupDTO deletedDeviceGroupDTO = new DeviceGroupDTO();
            deletedDeviceGroupDTO.setDeviceGroupName(existingDeviceGroupOptional.get().getDeviceGroupName());
            deletedDeviceGroupDTO.setDeviceGroupType(existingDeviceGroupOptional.get().getDeviceGroupType());
            return Optional.of(deletedDeviceGroupDTO);
        } else {
            return Optional.empty();
        }
    }
}
