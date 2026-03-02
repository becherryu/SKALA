package com.skala.basic.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.skala.basic.data.YachtEntity;

@RestController
public class YachtController {

    @PostMapping("/yacht")
    public String postYacht(@RequestBody YachtEntity yacht) {
        return "Yacht created successfully";
    }
}
