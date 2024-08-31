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
        String signedUserId = "a400e8b6-52d4-4568-89fd-494388506c7d";
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
