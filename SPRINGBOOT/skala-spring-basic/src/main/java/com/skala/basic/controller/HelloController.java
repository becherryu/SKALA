package com.skala.basic.controller;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.skala.basic.data.HelloRequest;

@RestController
public class HelloController {

    ArrayList<HelloRequest> helloRequests = new ArrayList<>();

    // POST, PUT, DELETE를 통해 HelloRequest 객체를 받아서 ArrayList에 저장하는 기능 구현
    @GetMapping("/hello")
    public ArrayList<HelloRequest> hello() {
        return helloRequests;
    }

    @PostMapping("/hello")
    public ArrayList<HelloRequest> postMethodName(@RequestBody HelloRequest request) {
        helloRequests.add(request);

        return helloRequests;
    }

    @DeleteMapping("/hello")
    public ArrayList<HelloRequest> deleteMethodName(@RequestBody HelloRequest request) {
        // helloRequests에서 hello 필드가 requestdml hello와 일치하는 객체를 찾아서 삭제
        helloRequests.removeIf(helloRequest -> helloRequest.getHello().equals(request.getHello()));

        return helloRequests;
    }

    // "/hello/list" GET 요청 시 저장된 HelloRequest 객체들의 리스트를 응답
}
