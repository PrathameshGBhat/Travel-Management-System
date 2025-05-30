package com.cts.repository;

import com.cts.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;


//This helps us manage 'Role' data in the database.
//It gives us ready-to-use functions like save, find, and delete.
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
    boolean existsByName(String name);
}
