package com.example.myfinances.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

import static java.time.Month.*;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository){
        return args -> {

            User adam = new User(
                    "Adam",
                    "haslo123",
                    "adam@wp.pl",
                    LocalDate.of(1993, MARCH, 5)
            );

            User kasia = new User(
                    "Katarzyna",
                    "123",
                    "kasia@onet.pl",
                    LocalDate.of(1999, JANUARY, 6)
            );

            userRepository.saveAll(
                    List.of(adam,kasia)
            );
        };
    }
}
