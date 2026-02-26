package com.skala.basic.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class WorldRequest {
    // 이름(name), 설명(description), 유형(type)을 포함하는 필드

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String type;

    @Positive // 양수
    private int since;
}
