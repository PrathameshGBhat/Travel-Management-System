package com.cts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.entities.Customer;

//This helps us manage 'Customer' data in the database.
//It gives us ready-to-use functions like save, find, and delete.
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
