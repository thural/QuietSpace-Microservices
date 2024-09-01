package com.jellybrains.quietspace.user_service.bootstrap;

import com.jellybrains.quietspace.user_service.entity.Profile;
import com.jellybrains.quietspace.user_service.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileLoader implements CommandLineRunner {
    // TODO: remove after kafka implementation of user creation
    private final ProfileRepository profileRepository;


    @Override
    public void run(String... args) throws Exception {
        loadUserProfile();
    }

    private void loadUserProfile() {
        String signedUserId = "ed05122c-aced-40ec-9133-1dfe8ce42e83";
        String signedUserName = "sigma";

        if (profileRepository.findByUserId(signedUserId).isEmpty()) {
            profileRepository.save(
                    Profile.builder()
                            .userId(signedUserId)
                            .username(signedUserName)
                            .email(signedUserName + "@gmail.com")
                            .build()
                    );
        }
    }
}
