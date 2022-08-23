package com.example.myfinances.user;

import com.example.myfinances.role.Role;
import com.example.myfinances.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
/*import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;*/

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.Month.*;

//WebSecurityConfigurerAdapter is deprecated
@Configuration
public class UserConfig extends WebSecurityConfigurerAdapter{

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests().antMatchers("/register**")
                .permitAll() //.anyRequest().authenticated()
                .and()
                .formLogin() .loginPage("/login")
                .permitAll()
                .and()
                .authorizeRequests().antMatchers("/api/v1/user").hasAnyAuthority("ADMIN","ADMIN_TEST")
                //.hasAuthority("USER")
                .anyRequest().authenticated() //if you put this line upper the hasAuthority won't work
                .and()
                .logout().invalidateHttpSession(true)
                .clearAuthentication(true).permitAll();
    }




    private RoleRepository roleRepository;
    @Bean
    CommandLineRunner userCommandLineRunner(UserRepository userRepository){
        List<Role> listRole = new ArrayList<>();
        listRole.add(new Role("USER_TEST"));
        listRole.add(new Role("ADMIN_TEST"));
        return args -> {

            User adam = new User(
                    "Adam",
                    passwordEncoder().encode("123"),
                    "adam@wp.pl",
                    LocalDate.of(1993, MARCH, 5),
            true,
                listRole.subList(0,1)
            );

            User kasia = new User(
                    "Katarzyna",
                    passwordEncoder().encode("www"),
                    "kasia@onet.pl",
                    LocalDate.of(1999, JANUARY, 6),
                   true,
                    listRole.subList(1,2)
            );

            userRepository.saveAll(
                    List.of(adam,kasia)
            );
        };
    }
}
