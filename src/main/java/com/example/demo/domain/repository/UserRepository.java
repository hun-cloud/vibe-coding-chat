package com.example.demo.domain.repository;

import com.example.demo.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    
    User save(User user);
    
    Optional<User> findById(Long id);
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    List<User> findAll();
    
    List<User> findByRole(User.UserRole role);
    
    void deleteById(Long id);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
} 