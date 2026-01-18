package com.inventory.librarymanagementsystem.Service;

import com.inventory.librarymanagementsystem.Repository.UserRepository;
import com.inventory.librarymanagementsystem.entities.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static com.inventory.librarymanagementsystem.utils.PasswordUtils.*;

@Stateless
public class UserServiceImpl implements UserService {
    @Inject
    UserRepository userRepository;
    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());

    @Override
    public User registerUser(User user) {
        LOGGER.info("register");
     if(userRepository.findByEmail(user.getEmail()).isPresent()){
         throw new IllegalArgumentException("Email already in use");
     }
     String password=user.getPassword();
     user.setPassword(hashPassword(password));
     return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User Login(String email, String password) {
        User user=userRepository.findByEmail(email).orElse(null);
        if (user != null && checkPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
