package com.example.demonstration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDTO {
    private String deviceName;
    private String deviceType;
    private Boolean status;
}
