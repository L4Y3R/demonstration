package com.example.demonstration.controller;

import com.example.demonstration.dto.DeviceDTO;
import com.example.demonstration.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


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
    public DeviceDTO getDevice(@PathVariable("id") String id) {
        return deviceService.getDevice(id);
    }

    //create a device
    @PostMapping("/new")
    public DeviceDTO createDevice(@RequestBody DeviceDTO deviceDTO){
        return deviceService.createDevice(deviceDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceDTO> updateDevice(@PathVariable String id, @RequestBody DeviceDTO deviceDTO) {
        DeviceDTO updatedDevice = deviceService.updateDevice(id, deviceDTO);
        if (updatedDevice != null) {
            return ResponseEntity.ok(updatedDevice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeviceDTO> deleteDevice(@PathVariable String id) {
        Optional<DeviceDTO> deletedDevice = deviceService.deleteDevice(id);
        return deletedDevice.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
