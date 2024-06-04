package com.example.demonstration.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="device")
public class Device {

    @javax.persistence.Id
    private String deviceId;
    @Setter
    private String deviceName;
    @Setter
    private String deviceType;
    @Setter
    private Boolean status;
}
