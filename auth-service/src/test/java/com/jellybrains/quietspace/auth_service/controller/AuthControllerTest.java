package com.jellybrains.quietspace.auth_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jellybrains.quietspace.auth_service.entity.User;
import com.jellybrains.quietspace.auth_service.model.RegistrationRequest;
import com.jellybrains.quietspace.auth_service.security.JwtUtil;
import com.jellybrains.quietspace.auth_service.service.impls.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "user", roles = "USER", authorities = "USER, ADMIN")
class AuthControllerTest {

    private MockMvc mockMvc;

    @Spy
    ObjectMapper objectMapper;
    @Mock
    AuthService authService;
    @Mock
    JwtUtil jwtUtil;
    @InjectMocks
    AuthController userController;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;
    @Captor
    ArgumentCaptor<RegistrationRequest> registerArgumentCaptor;

    private String userId;
    private RegistrationRequest request;
    private User user;


    @BeforeEach
    void setUp() {

        this.userId = String.valueOf(UUID.randomUUID());

        this.request = RegistrationRequest.builder()
                .email("guest@email.com")
                .firstname("firstname")
                .lastname("lastname")
                .password("passWord")
                .username("guest")
                .build();

        this.user = User.builder()
                .id(userId)
                .username("user")
                .email("user@email.com")
                .password("pAsSword")
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Rollback
    @Transactional
    void register() throws Exception {
        when(authService.register(any(RegistrationRequest.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post(AuthController.AUTH_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());

        verify(authService.register(registerArgumentCaptor.capture()));
        assertThat(request.getUsername()).isEqualTo(registerArgumentCaptor.getValue().getUsername());
        assertThat(request.getPassword()).isEqualTo(registerArgumentCaptor.getValue().getPassword());
    }

    @Test
    void authenticate() {

    }

    @Test
    void confirm() {
    }

    @Test
    void signout() {
    }

    @Test
    void refreshToken() {
    }

    @Test
    void resendActivationEmail() {
    }

    @Test
    void getCurrentUser() {
    }
}