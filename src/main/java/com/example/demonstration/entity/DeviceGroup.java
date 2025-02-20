package com.example.demonstration.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;

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
    private String admin;
    private List<String> devices = new ArrayList<>();
}
