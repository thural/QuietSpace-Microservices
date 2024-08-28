package com.jellybrains.quietspace.auth_service.bootstrap;

import com.jellybrains.quietspace.auth_service.entity.Role;
import com.jellybrains.quietspace.auth_service.enums.RoleType;
import com.jellybrains.quietspace.auth_service.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleLoader implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public RoleLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadUserRole();
    }

    private void loadUserRole() {
        if (roleRepository.findByName(RoleType.USER.toString()).isEmpty()) {
            roleRepository.save(Role.builder()
                    .name(RoleType.USER.toString())
                    .build());
        }
    }
}
