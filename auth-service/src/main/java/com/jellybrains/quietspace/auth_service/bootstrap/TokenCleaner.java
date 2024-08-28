package com.jellybrains.quietspace.auth_service.bootstrap;

import com.jellybrains.quietspace.auth_service.entity.Token;
import com.jellybrains.quietspace.auth_service.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenCleaner {
    private final TokenRepository tokenRepository;

    @Scheduled(fixedRate = 86400000L)
    void tokenCleanup() {
        log.info("running token cleanup...");
        for (Token currentToken : tokenRepository.findAll()) {
            boolean isTokenExpired = currentToken.getCreateDate().isBefore(OffsetDateTime.now().minusDays(1));
            if (isTokenExpired) tokenRepository.delete(currentToken);
        }
    }
}
