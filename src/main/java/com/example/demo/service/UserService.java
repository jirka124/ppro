package com.example.demo.service;

import com.example.demo.model.Car;
import com.example.demo.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {

    User findByUsername(String username);
    List<User> getAllUsers();
    User getUserById(long id);
    void deleteUserById(long id);
    User save(User user);
}
