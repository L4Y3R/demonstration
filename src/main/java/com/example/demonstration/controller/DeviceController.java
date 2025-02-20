package com.example.demonstration.controller;

import com.example.demonstration.dto.DeviceDTO;
import com.example.demonstration.entity.Device;
import com.example.demonstration.exception.DeviceNotFoundException;
import com.example.demonstration.exception.UserNotFoundException;
import com.example.demonstration.service.DeviceGroupService;
import com.example.demonstration.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/v1/devices")
@CrossOrigin
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    //get all devices
    @GetMapping
    public List<DeviceDTO> getAllDevices(){
        logger.info("Get All Devices Request Received");
        return deviceService.getAllDevices();
    }

    //get one device
    @GetMapping("/{id}")
    public DeviceDTO getDevice(@PathVariable("id") String id) {
        logger.info("Get Device With ID {} Request Received",id);
        try{
            DeviceDTO device =  deviceService.getDevice(id);
            return ResponseEntity.ok(device).getBody();
        }catch (Exception e){
            logger.error("Unable to Get With ID {}",id);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An internal error occurred", e);
        }
    }

    @GetMapping("conf/")
    public DeviceDTO getDeviceForCommand(@RequestParam("deviceId") String deviceId, @RequestParam("userId") String userId) {
        try {
            DeviceDTO device = deviceService.getDeviceForCommand(deviceId, userId);
            logger.info("Command Request Received");
            if (device == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found or not associated with the user");
            }
            return device;
        } catch (DeviceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found", e);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", e);
        }
    }

    //update a device
    @PutMapping("/{id}")
    public ResponseEntity<DeviceDTO> updateDevice(@PathVariable String id, @RequestBody DeviceDTO deviceDTO) {
        logger.info("Device Update Request Received");
        DeviceDTO updatedDevice = deviceService.updateDevice(id, deviceDTO);
        if (updatedDevice != null) {
            return ResponseEntity.ok(updatedDevice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //delete a device
    @DeleteMapping("/{id}")
    public ResponseEntity<DeviceDTO> deleteDevice(@PathVariable String id) {
        logger.info("Device Delete Request Received");
        Optional<DeviceDTO> deletedDevice = deviceService.deleteDevice(id);
        return deletedDevice.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //add a device to a group
    @PostMapping("/add")
    public ResponseEntity<Device> addDevice(@RequestBody Device device, @RequestParam String groupName, @RequestParam String userName) {
        logger.info("Device Add Request Received");
        Device savedDevice = deviceService.addDeviceToGroupWithUser(device, groupName, userName);
        if (savedDevice != null) {
            return ResponseEntity.ok(savedDevice);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    //add a device with a user
    @PostMapping("/new")
    public ResponseEntity<Device> addDeviceToUser(@RequestBody Device device, @RequestParam String userName) {
        logger.info("Add Device to User Request Received");
        try {
            Device savedDevice = deviceService.addDeviceToUser(device, userName);
            return ResponseEntity.ok(savedDevice);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
