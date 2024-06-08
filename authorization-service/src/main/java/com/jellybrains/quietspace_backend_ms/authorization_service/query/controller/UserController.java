package com.jellybrains.quietspace_backend_ms.authorization_service.query.controller;

import com.jellybrains.quietspace_backend_ms.authorization_service.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

//    private final KeycloakService keycloakService;
//
//    @GetMapping("/token")
//    public Mono<String> getToken(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
//                                 @AuthenticationPrincipal DefaultOidcUser user) {
//        log.info(authorizedClient.getPrincipalName());
//        log.info(user.getPreferredUsername());
//        return Mono.just(authorizedClient.getAccessToken().getTokenValue());
//    }

}


