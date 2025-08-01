package com.gentlemonster.GentleMonsterBE.Initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gentlemonster.GentleMonsterBE.Entities.User;
import com.gentlemonster.GentleMonsterBE.Repositories.IRoleRepository;
import com.gentlemonster.GentleMonsterBE.Repositories.IUserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class UserInitializer {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;


    @Bean
    public CommandLineRunner initDatabase(){
        return args -> {
            if (userRepository.count() == 0) {
                User user = User.builder()
                        .build();
                
            }
        };
    }
}
