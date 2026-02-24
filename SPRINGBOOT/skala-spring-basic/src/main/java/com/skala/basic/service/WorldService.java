package com.skala.basic.service;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.skala.basic.data.WorldEntity;
import com.skala.basic.data.WorldRequest;
import com.skala.basic.data.WorldResponse;

@Service
public class WorldService {
    // - 세계관을 관리할 수 있는 List<WorldEntity> worldList 보유
    private ArrayList<WorldEntity> worldList = new ArrayList<>();

    // - CUD 메서드는 WordRequest를 매개변수로 받아서 WorldResponse로 응답 : createWorld(),
    // updateWorld(), deleteWorld()
    public WorldResponse createWorld(WorldRequest request) {

        WorldEntity entity = new WorldEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setType(request.getType());
        entity.setCreatedAt(new Date());

        if (worldList.isEmpty()) {
            entity.setId(1L);
        } else {
            entity.setId(worldList.get(worldList.size() - 1).getId() + 1);
        }
        worldList.add(entity);

        WorldResponse response = new WorldResponse();
        response.setResult(0);
        response.setMessage("세계관이 성공적으로 생성되었습니다.");
        response.setWorlds(worldList);

        return response;
    }

    public WorldResponse updateWorld(Long id, WorldRequest request) {
        for (WorldEntity entity : worldList) {
            if (entity.getId().equals(id)) {
                entity.setName(request.getName());
                entity.setDescription(request.getDescription());
                entity.setType(request.getType());
                entity.setUpdatedAt(new Date());

                WorldResponse response = new WorldResponse();
                response.setResult(0);
                response.setMessage("세계관이 성공적으로 업데이트되었습니다.");
                response.setWorlds(worldList);
                return response;
            }
        }

        WorldResponse response = new WorldResponse();
        response.setResult(1);
        response.setMessage("세계관을 찾을 수 없습니다.");
        response.setWorlds(worldList);
        return response;
    }

    public WorldResponse deleteWorld(Long id) {
        for (WorldEntity entity : worldList) {
            if (entity.getId().equals(id)) {
                worldList.remove(entity);

                WorldResponse response = new WorldResponse();
                response.setResult(0);
                response.setMessage("세계관이 성공적으로 삭제되었습니다.");
                response.setWorlds(worldList);
                return response;
            }
        }

        WorldResponse response = new WorldResponse();
        response.setResult(1);
        response.setMessage("세계관을 찾을 수 없습니다.");
        response.setWorlds(worldList);
        return response;
    }

    // - 목록 조회 메서드는 이름 유사 매핑을 통해 검색된 목록 응답: getWorlds()
    public WorldResponse getWorlds() {
        WorldResponse response = new WorldResponse();
        response.setResult(0);
        response.setMessage("세계관 목록이 성공적으로 조회되었습니다.");
        response.setWorlds(worldList);
        return response;
    }
}