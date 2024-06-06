package com.example.demonstration.dto;

import com.example.demonstration.entity.Device;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class DeviceGroupDTO {
    private String groupName;
    private String ownerName;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "groupId", referencedColumnName = "groupId")
    private List<Device> devices;
}
