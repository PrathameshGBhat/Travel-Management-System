package com.cts.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.entities.Address;

// This helps us manage 'Address' data in the database.
// It gives us ready-to-use functions like save, find, and delete.
public interface AddressRepository extends JpaRepository<Address, Long> {
	 Optional<Address> findByHouseNoAndStreetAndLandmarkAndCityAndStateAndPin(
		        String houseNo, String street, String landmark, String city, String state, String pin);
}