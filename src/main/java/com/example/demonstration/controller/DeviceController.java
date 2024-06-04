package com.example.demonstration.controller;

import com.example.demonstration.dto.DeviceDTO;
import com.example.demonstration.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/v1/devices")
@CrossOrigin
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    //get all devices
    @GetMapping
    public List<DeviceDTO> getAllDevices(){
        return deviceService.getAllDevices();
    }

    //get one device
    @GetMapping("/{id}")
    public String getDevice() {
        return "hello";
    }

    //create a device
    @PostMapping("/new")
    public DeviceDTO createDevice(@RequestBody DeviceDTO deviceDTO){
        return deviceService.createDevice(deviceDTO);
    }

    // Update a device
    @PutMapping("/{id}")
    public String updateDevice() {
        return "hello";
    }

    // Delete a device
    @DeleteMapping("/{id}")
    public String deleteDevice() {
        return "hello";
    }
}
