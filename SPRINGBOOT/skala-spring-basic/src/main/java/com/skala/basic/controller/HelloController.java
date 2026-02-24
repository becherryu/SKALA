package com.skala.basic.controller;

import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    // GET /hello : HashMap을 통해 { "message": "SKALA에 오신 것을 환영합니다." } 응답
    @GetMapping("/hello")
    public HashMap<String, String> hello() {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "SKALA에 오신 것을 환영합니다.");
        return response;
    }

    // POST, PUT, DELETE를 통해 HelloRequest 객체를 받아서 ArrayList에 저장하는 기능 구현
    // "/hello/list" GET 요청 시 저장된 HelloRequest 객체들의 리스트를 응답
}
