package com.example.demonstration.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document (collection="deviceGroup")

public class DeviceGroup {

    @Id
    private String groupId;
    private String groupName;
    private String ownerName;
    @javax.persistence.Id
    private Long Id;
}
