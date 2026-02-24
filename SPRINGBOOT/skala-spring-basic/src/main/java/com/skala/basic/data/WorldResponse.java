package com.skala.basic.data;

import java.util.ArrayList;

import lombok.Data;

@Data
public class WorldResponse {
    // API 응답 성공 여부(result 0성공 / 1실패), 메시지(message), WorldEntity 목록을 포함하는 필드(worlds)
    private int result;
    private String message;
    private ArrayList<WorldEntity> worlds;
}
