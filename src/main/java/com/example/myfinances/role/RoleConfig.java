package com.example.myfinances.role;

import com.example.myfinances.user.User;
import com.example.myfinances.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RoleConfig {

    @Bean
    CommandLineRunner roleCommandLineRunner(RoleRepository roleRepository){
        return args -> {

            Role userRole = new Role("USER");

/*            roleRepository.saveAll(
                    List.of(userRole));*/
            roleRepository.save(userRole);
        };
    }
}