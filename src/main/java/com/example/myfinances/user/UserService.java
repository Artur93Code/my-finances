package com.example.myfinances.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers()
    {
        return userRepository.findAll();
    }

    public Long getNumberOfUsers()
    {
        return userRepository.count();
    }

    public void addNewUser(User user)
    {
        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
        if(userOptional.isPresent()){
            throw new IllegalStateException("Podany adres email jest już zajęty");
        }
        userRepository.save(user);
        System.out.println(user);
    }

    public void deleteUser(Long userId)
    {
        boolean exist = userRepository.existsById(userId);
        if(!exist)
        {
            throw new IllegalStateException("Użytkownik o id: "+userId+" nie istnieje");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(Long userId, String name, String email)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with id: "+userId+" not exist"));

        if (name != null && name.length() > 0 && !Objects.equals(user.getUsername(), name))
        {
            user.setUsername(name);
        }

        if(email != null && email.length() > 0 && !Objects.equals(user.getEmail(), email))
        {
            Optional<User> userOptional = userRepository.findUserByEmail(email);
            if (userOptional.isPresent())
            {
                throw new IllegalStateException("This email address is already taken");
            }
            user.setEmail(email);
        }

    }
}
