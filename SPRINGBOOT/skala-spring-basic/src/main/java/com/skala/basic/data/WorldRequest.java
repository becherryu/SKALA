package com.skala.basic.data;

import lombok.Data;

@Data
public class WorldRequest {
    // 이름(name), 설명(description), 유형(type)을 포함하는 필드
    private String name;
    private String description;
    private String type;
}
