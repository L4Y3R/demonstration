package com.example.demonstration.controller;

import com.example.demonstration.dto.DeviceDTO;
import com.example.demonstration.entity.Device;
import com.example.demonstration.exception.UserNotFoundException;
import com.example.demonstration.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    /*
    @PostMapping("/new")
    public DeviceDTO createDevice(@RequestBody DeviceDTO deviceDTO){
        return deviceService.createDevice(deviceDTO);
    }
     */

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

    @PostMapping("/add")
    public ResponseEntity<Device> addDevice(@RequestBody Device device, @RequestParam String groupName, @RequestParam String userName) {
        Device savedDevice = deviceService.addDeviceToGroupWithUser(device, groupName, userName);
        if (savedDevice != null) {
            return ResponseEntity.ok(savedDevice);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Add device to user
    @PostMapping("/new")
    public ResponseEntity<Device> addDeviceToUser(@RequestBody Device device, @RequestParam String userName) {
        try {
            Device savedDevice = deviceService.addDeviceToUser(device, userName);
            return ResponseEntity.ok(savedDevice);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
