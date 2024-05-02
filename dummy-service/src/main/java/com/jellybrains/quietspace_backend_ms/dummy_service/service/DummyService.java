package com.jellybrains.quietspace_backend_ms.dummy_service.service;

import com.jellybrains.quietspace_backend_ms.dummy_service.dto.request.DummyRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DummyService {

    @SneakyThrows
    public String buildMessage(DummyRequest request){
        log.info("Wait started");
        Thread.sleep(11*1000);
        log.info("Wait ended");
        return "Hello " + request.getName();
    }
}
