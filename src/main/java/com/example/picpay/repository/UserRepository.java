package com.example.picpay.repository;

import com.example.picpay.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByDocument(String document);
    Optional<User> findUserById(Long id);

    boolean existsByEmail(String email);

    boolean existsByDocument(String document);
}
