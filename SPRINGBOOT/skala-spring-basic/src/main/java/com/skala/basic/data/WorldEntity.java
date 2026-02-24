package com.skala.basic.data;

import java.util.Date;

import lombok.Data;

@Data
public class WorldEntity {

    private Long id;
    private String name;
    private String description;
    private String type;
    private Date createdAt;
    private Date updatedAt;

}
