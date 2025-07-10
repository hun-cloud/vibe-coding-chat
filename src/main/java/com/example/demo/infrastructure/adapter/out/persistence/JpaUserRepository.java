package com.example.demo.infrastructure.adapter.out.persistence;

import com.example.demo.application.port.out.UserRepository;
import com.example.demo.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {

    @Override
    Optional<User> findByUsername(String username);

    @Override
    Optional<User> findByEmail(String email);

    @Override
    boolean existsByUsername(String username);

    @Override
    boolean existsByEmail(String email);
} 