package com.example.demonstration.service;

import com.example.demonstration.dto.DeviceDTO;
import com.example.demonstration.entity.Device;
import com.example.demonstration.exception.DeviceNotFoundException;
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
        if (deviceOptional.isEmpty()) throw new DeviceNotFoundException();
        return deviceOptional.map(device -> modelMapper.map(device, DeviceDTO.class)).orElse(null);
    }

    public DeviceDTO createDevice (DeviceDTO deviceDTO){
        deviceRepo.save(modelMapper.map(deviceDTO, Device.class));
        return deviceDTO;
    }

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
            DeviceDTO deletedDeviceDTO = new DeviceDTO();
            deletedDeviceDTO.setDeviceName(existingDeviceOptional.get().getDeviceName());
            deletedDeviceDTO.setDeviceType(existingDeviceOptional.get().getDeviceType());
            deletedDeviceDTO.setStatus(existingDeviceOptional.get().getStatus());
            return Optional.of(deletedDeviceDTO);
        } else {
            throw new DeviceNotFoundException();
        }
    }
}
