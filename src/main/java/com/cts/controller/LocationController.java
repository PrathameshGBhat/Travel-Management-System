package com.cts.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.dto.LocationDto;
import com.cts.entities.Location;
import com.cts.service.LocationService;
import com.cts.service.impl.LocationServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/TravelAgency/Location")
public class LocationController {

	private static final Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);

	private final LocationService locationService;

	public LocationController(LocationService locationService) {
		this.locationService = locationService;
	}

	@GetMapping("/locations")
	public ResponseEntity<List<Location>> getAll() {
		return ResponseEntity.ok(locationService.getAll());
	}
	

	@GetMapping("/{id}")
	public ResponseEntity<Location> getById(@PathVariable Long id) {
		return ResponseEntity.ok(locationService.getById(id));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		locationService.delete(id);
		logger.info("Deleted");
		return new ResponseEntity<String>("Deleted",HttpStatus.ACCEPTED);
	}

	@PostMapping("/save")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Location> create(@Valid @RequestBody LocationDto locationDto) {
		Location createdloc = locationService.create(locationDto);
		return new ResponseEntity<>(createdloc, HttpStatus.CREATED);
	}


	@PutMapping("/{id}") 
	@PreAuthorize("hasRole('ADMIN')") 
	public ResponseEntity<Location> update(@PathVariable Long id, @RequestBody LocationDto locationDto) {
		logger.info("Received request to update location with id: {}", id);
		Location updatedLoc = locationService.update(id, locationDto);
		return new ResponseEntity<>(updatedLoc, HttpStatus.CREATED); 
	}

}
