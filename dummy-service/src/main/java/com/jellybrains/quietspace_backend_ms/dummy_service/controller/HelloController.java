package com.jellybrains.quietspace_backend_ms.dummy_service.controller;

import com.jellybrains.quietspace_backend_ms.dummy_service.dto.request.DummyRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dummy")
public class HelloController {

    @GetMapping("/hello")
    @CircuitBreaker(name = "dummy", fallbackMethod = "fallbackMethod")
    public String hello(@RequestBody DummyRequest dummyRequest) {
        return "Hello " + dummyRequest.getName();
    }

    public String fallbackMethod(DummyRequest dummyRequest, RuntimeException runtimeException) {
        return "Something went wrong, please try later";
    }
}
