package com.example.demonstration.service;

import com.example.demonstration.dto.DeviceDTO;
import com.example.demonstration.entity.Device;
import com.example.demonstration.repository.DeviceRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private ModelMapper modelMapper;

    public List<DeviceDTO> getAllDevices(){
        List<Device> devices = deviceRepo.findAll();
        return modelMapper.map(devices, new TypeToken<List<DeviceDTO>>(){}.getType());
    }

    public DeviceDTO getDevice(String id){
        Optional<Device> deviceOptional = deviceRepo.findById(id);
        if (deviceOptional.isPresent()) {
            return modelMapper.map(deviceOptional.get(), DeviceDTO.class);
        } else {
            return null;
        }
    }

    public DeviceDTO createDevice (DeviceDTO deviceDTO){
        deviceRepo.save(modelMapper.map(deviceDTO, Device.class));
        return deviceDTO;
    }

    public DeviceDTO updateDevice(String id, DeviceDTO deviceDTO) {
        Optional<Device> existingDeviceOptional = deviceRepo.findById(id);
        if (existingDeviceOptional.isPresent()) {
            Device device = existingDeviceOptional.get();
            modelMapper.map(deviceDTO, device);
            device.setDeviceId(id);
            deviceRepo.save(device);
            return deviceDTO;
        } else {
            return null;
        }
    }

    public Optional<DeviceDTO> deleteDevice(String id) {
        Optional<Device> existingDeviceOptional = deviceRepo.findById(id);
        if (existingDeviceOptional.isPresent()) {
            deviceRepo.deleteById(id);
            DeviceDTO deletedDeviceDTO = new DeviceDTO();
            deletedDeviceDTO.setDeviceName(existingDeviceOptional.get().getDeviceName());
            deletedDeviceDTO.setDeviceType(existingDeviceOptional.get().getDeviceType());
            deletedDeviceDTO.setStatus(existingDeviceOptional.get().getStatus());
            return Optional.of(deletedDeviceDTO);
        } else {
            return Optional.empty();
        }
    }
}
