package com.skala.basic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skala.basic.data.WorldRequest;
import com.skala.basic.data.WorldResponse;
import com.skala.basic.service.WorldService;

@RestController
@RequestMapping("/api")
public class WorldController {
    // @Autowired를 통해 WorldService 주입
    @Autowired
    private WorldService worldService;

    // "/world" 엔드포인트에 대한 HTTP 메서드 구현: postWorld(), putWorld(), deleteWorld(),

    @PostMapping("/world")
    public WorldResponse postWorld(@RequestBody WorldRequest request) {
        return worldService.createWorld(request);
    }

    @GetMapping("/world")
    public WorldResponse getWorld() {
        return worldService.getWorlds();
    }

    @PutMapping("/world/{id}")
    public WorldResponse putWorld(@PathVariable Long id, @RequestBody WorldRequest request) {
        return worldService.updateWorld(id, request);
    }

    @DeleteMapping("/world/{id}")
    public WorldResponse deleteWorld(@PathVariable Long id) {
        return worldService.deleteWorld(id);
    }

}
