package com.inventory.librarymanagementsystem.Service;

import com.inventory.librarymanagementsystem.Repository.UserRepository;
import com.inventory.librarymanagementsystem.entities.User;
import jakarta.ejb.Local;
import jakarta.inject.Inject;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

@Local
public interface UserService {
    User registerUser(User user);
    List<User> getAllUsers();
    User Login(String email ,String password);

}
