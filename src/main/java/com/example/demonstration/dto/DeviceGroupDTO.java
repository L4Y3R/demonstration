package com.example.demonstration.dto;

import com.example.demonstration.entity.Device;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class DeviceGroupDTO {
    private String groupName;
    private String ownerName;
    private List<Device> devices;
}
