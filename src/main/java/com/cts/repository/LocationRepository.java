package com.cts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.entities.Location;


//This helps us manage 'Location' data in the database.
//It gives us ready-to-use functions like save, find, and delete.
public interface LocationRepository extends JpaRepository<Location, Long>{
	Location findByNameIgnoreCase(String name);
}
