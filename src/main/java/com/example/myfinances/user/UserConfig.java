package com.example.myfinances.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
/*import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;*/

import java.time.LocalDate;
import java.util.List;

import static java.time.Month.*;

@Configuration
public class UserConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests().antMatchers("/register**")
                .permitAll() .anyRequest().authenticated()
                .and()
                .formLogin() .loginPage("/login")
                .permitAll()
                .and()
                .logout() .invalidateHttpSession(true)
                .clearAuthentication(true) .permitAll();
    }

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
