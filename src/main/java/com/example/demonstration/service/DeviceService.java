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

@Service
@Transactional
public class DeviceService {
    @Autowired
    private DeviceRepo deviceRepo;
    @Autowired
    private ModelMapper modelMapper;

    public DeviceDTO createDevice (DeviceDTO deviceDTO){
        deviceRepo.save(modelMapper.map(deviceDTO, Device.class));
        return deviceDTO;
    }

    public List<DeviceDTO> getAllDevices(){
        List<Device> devices = deviceRepo.findAll();
        return modelMapper.map(devices, new TypeToken<List<DeviceDTO>>(){}.getType());
    }
}
