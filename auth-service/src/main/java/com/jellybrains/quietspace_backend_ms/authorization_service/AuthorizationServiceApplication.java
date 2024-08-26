package com.jellybrains.quietspace_backend_ms.authorization_service;

import com.jellybrains.quietspace.common_service.enums.RoleType;
import com.jellybrains.quietspace_backend_ms.authorization_service.entity.Role;
import com.jellybrains.quietspace_backend_ms.authorization_service.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuthorizationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName(RoleType.USER.toString()).isEmpty()) {
				roleRepository.save(Role.builder()
						.name(RoleType.USER.toString())
						.build());
			}
		};
	}
}
