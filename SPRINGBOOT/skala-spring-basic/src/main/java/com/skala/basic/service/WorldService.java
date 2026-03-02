package com.skala.basic.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.skala.basic.data.WorldRequest;
import com.skala.basic.data.WorldResponse;
import com.skala.basic.repository.WorldRepository;
import com.skala.basic.table.WorldEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorldService {
    private final WorldRepository worldRepository;

    public WorldResponse createWorld(WorldRequest request) {
        WorldResponse response = new WorldResponse();

        Optional<WorldEntity> optional = worldRepository.findByName(request.getName());
        if (optional.isPresent()) {
            response.setResult(1);
            response.setMessage("이미 존재하는 세계입니다.");

            return response;
        }

        WorldEntity entity = new WorldEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setType(request.getType());
        entity.setSince(request.getSince());
        entity.setCreatedAt(new Date());

        worldRepository.save(entity);

        return response;
    }

    public WorldResponse updateWorld(Long id, WorldRequest request) {
        WorldResponse response = new WorldResponse();
        return response;
    }

    public WorldResponse deleteWorld(Long id) {
        WorldResponse response = new WorldResponse();
        return response;

    }

    // - 목록 조회 메서드는 이름 유사 매핑을 통해 검색된 목록 응답: getWorlds()
    public WorldResponse getWorlds(Integer year) {
        WorldResponse response = new WorldResponse();

        if (year != null) {
            List<WorldEntity> worldList = worldRepository.findBySinceGreaterThanEqual(year);
            response.setWorlds(worldList);
        } else {
            List<WorldEntity> worldList = worldRepository.findAll();
            response.setWorlds(worldList);
        }

        return response;
    }
}
