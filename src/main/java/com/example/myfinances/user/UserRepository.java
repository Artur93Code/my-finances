package com.example.myfinances.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//    SELECT * FROM user WHERE email = ?
//    @Query("SELECT u FROM User u WHERE u.email=? ")
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByUsername(String username);
}
