package com.cts.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.cts.dto.LocationDto;
import com.cts.entities.Location;
import com.cts.exceptions.LocationNotFoundException;
import com.cts.repository.LocationRepository;
import com.cts.service.LocationService;

import jakarta.transaction.Transactional;

@Service
public class LocationServiceImpl implements LocationService {

	private final LocationRepository locationRepository;

	public LocationServiceImpl(LocationRepository locationRepository) {
		this.locationRepository = locationRepository;
	}

	private static final Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);

	@Override
	public List<Location> getAll() {
		logger.info("Fetching all locations.");
		return locationRepository.findAll();
	}

	@Override
	public Location getById(Long id) {
		logger.info("Fetching location by id");
		return locationRepository.findById(id)
				.orElseThrow(() -> new LocationNotFoundException("Location with id:"+id+" not found"));
	}

	@Override
	public Location update(Long id, LocationDto locationDto) {
		logger.info("Updating location with id: {}", id);

		// 1. Find the existing location. If not found, throw an exception.
		Location existingLocation = locationRepository.findById(id)
				.orElseThrow(() -> new LocationNotFoundException("Location with id:" + id + " not found for update"));

		// 2. Update the 'name' property.
		// You might add checks here, e.g., if locationDto.getName() is null,
		// you might choose not to update the name, allowing partial updates.
		if (locationDto.getName() != null && !locationDto.getName().trim().isEmpty()) {
			existingLocation.setName(locationDto.getName());
		} else {
            // Option 1: Throw an error if name is mandatory
            // throw new IllegalArgumentException("Location name cannot be null or empty for update.");
            // Option 2: Log a warning and proceed without updating the name
            logger.warn("Location name not provided in DTO for update, keeping existing name for ID: {}", id);
        }

		// 3. Save the updated location.
		// Spring Data JPA's save() method will perform an UPDATE if the entity
		// already has an ID, and an INSERT if it's a new entity.
		Location updatedLocation = locationRepository.save(existingLocation);
		logger.info("Location with id {} updated successfully.", updatedLocation.getId());
		return updatedLocation;
	}

	@Override
	public void delete(Long id) {
		logger.info("Deleting location");
		if (!locationRepository.existsById(id)) {
			throw new LocationNotFoundException("Location with id:" + id + " not found");
		}
		locationRepository.deleteById(id);
		logger.info("Location with id {} deleted successfully.", id);
	}

	@Override
	public Location create(LocationDto locationDto) {
		if (locationDto.getId() != null) {
			logger.warn("Location id : ({}) already present", locationDto.getId());
			locationDto.setId(null);
		}
		Location location = new Location();
		location.setName(locationDto.getName());
		
		Location savedLoc = locationRepository.save(location);
		logger.info("Location created successfully with ID: {}", savedLoc.getId());
		return savedLoc;
	}

}
