package com.example.myfinances.authentication;

import com.example.myfinances.user.User;
import com.example.myfinances.authentication.SecurityUserDetailsService;
import com.example.myfinances.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

        @Component public class AuthProvider implements AuthenticationProvider {
            private static final int ATTEMPTS_LIMIT = 3;
            @Autowired private SecurityUserDetailsService userDetailsService;
            @Autowired private PasswordEncoder passwordEncoder;
            @Autowired private AttemptsRepository attemptsRepository;
            @Autowired private UserRepository userRepository;
            @Override
            public Authentication authenticate(Authentication authentication)
                    throws AuthenticationException {
                String username = authentication.getName();
                String password = authentication.getCredentials().toString();

                /*SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ANOTHER");
                List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
                updatedAuthorities.add(authority);*/

                Optional<Attempts>
                        userAttempts = attemptsRepository.findAttemptsByUsername(username);

                if (userAttempts.isPresent()) {
                    Attempts attempts = userAttempts.get();
                    attempts.setAttempts(0); attemptsRepository.save(attempts);
                }
                if(userRepository.findUserByUsername(username).isPresent()/*set loadByUsername form SecurityUserDet...*/) {

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            authentication.getPrincipal(),
                            authentication.getCredentials(),
                            /*new ArrayList<GrantedAuthority>()*/null);
                    return authToken;
                }
                return null;
            }
            private void processFailedAttempts(String username, User user) {
                Optional<Attempts>
                        userAttempts = attemptsRepository.findAttemptsByUsername(username);
                if (userAttempts.isEmpty()) {
                    Attempts attempts = new Attempts();
                    attempts.setUsername(username);
                    attempts.setAttempts(1);
                    attemptsRepository.save(attempts);
                } else {
                    Attempts attempts = userAttempts.get();
                    attempts.setAttempts(attempts.getAttempts() + 1);
                    attemptsRepository.save(attempts);

                    if (attempts.getAttempts() + 1 >
                            ATTEMPTS_LIMIT) {
                        user.setAccountNonLocked(false);
                        userRepository.save(user);
                        throw new LockedException("Too many invalid attempts. Account is locked!!");
                    }
                }
            }
            @Override public boolean supports(Class<?> authentication) {
                return true;
            }
        }
