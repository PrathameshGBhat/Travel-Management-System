package com.cts.repository;

import com.cts.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


//This helps us manage 'User' data in the database.
//It gives us ready-to-use functions like save, find, and delete.
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsernameOrEmail(String username, String email);
}
