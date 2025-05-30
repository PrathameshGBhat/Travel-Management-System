package com.cts.service;

import java.util.List;

import com.cts.dto.LocationDto;
import com.cts.entities.Location;

public interface LocationService {
	List<Location> getAll();
	Location getById(Long id);
	Location update(Long id, LocationDto locationDto);
	void delete(Long id);
	Location create(LocationDto locationDto);
}
