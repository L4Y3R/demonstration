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
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="device")
public class Device {

    @Id
    private String deviceId;
    private String deviceName;
    private String deviceType;
    private Boolean status;
    @javax.persistence.Id
    private Long id;
}
