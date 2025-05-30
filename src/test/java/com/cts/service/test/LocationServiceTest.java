package com.cts.service.test;

import com.cts.dto.LocationDto;
import com.cts.entities.Location;
import com.cts.exceptions.LocationNotFoundException;
import com.cts.repository.LocationRepository;
import com.cts.service.impl.LocationServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationServiceImpl locationServiceImpl;

    private Location location1;
    private Location location2;
    private LocationDto locationDto;

    @BeforeEach
    void setUp() {
        location1 = new Location();
        location1.setId(1L);
        location1.setName("Paris");

        location2 = new Location();
        location2.setId(2L);
        location2.setName("London");

        locationDto = new LocationDto();
        locationDto.setName("New York City");
    }

    @Test
    void getAll_shouldReturnListOfLocations() {
        // Given
        List<Location> locations = Arrays.asList(location1, location2);
        when(locationRepository.findAll()).thenReturn(locations);

        // When
        List<Location> result = locationServiceImpl.getAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Paris", result.get(0).getName());
        assertEquals("London", result.get(1).getName());
        verify(locationRepository, times(1)).findAll();
    }

    @Test
    void getById_shouldReturnLocationWhenFound() {
        Long id = 1L;
        when(locationRepository.findById(id)).thenReturn(Optional.of(location1));

        // When
        Location result = locationServiceImpl.getById(id);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Paris", result.getName());
        verify(locationRepository, times(1)).findById(id);
    }

    @Test
    void getById_shouldThrowLocationNotFoundExceptionWhenNotFound() {
        // Given
        Long id = 99L;
        when(locationRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        LocationNotFoundException thrown = assertThrows(LocationNotFoundException.class, () -> {
        	locationServiceImpl.getById(id);
        });

        assertEquals("Location not found", thrown.getMessage());
        verify(locationRepository, times(1)).findById(id);
    }

    @Test
    void update_shouldUpdateExistingLocation() {
        // Given
        Long id = 1L;
        LocationDto updateDto = new LocationDto();
        updateDto.setName("Updated Paris Name");

        Location existingLocation = new Location();
        existingLocation.setId(id);
        existingLocation.setName("Paris");

        Location updatedLocation = new Location();
        updatedLocation.setId(id);
        updatedLocation.setName("Updated Paris Name");

        when(locationRepository.findById(id)).thenReturn(Optional.of(existingLocation));
        when(locationRepository.save(any(Location.class))).thenReturn(updatedLocation);

        // When
        Location result = locationServiceImpl.update(id, updateDto);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Updated Paris Name", result.getName());
        verify(locationRepository, times(1)).findById(id);
        verify(locationRepository, times(1)).save(existingLocation); // Verify save was called with the modified existingLocation
        assertEquals("Updated Paris Name", existingLocation.getName()); // Ensure the existingLocation object was modified
    }

    @Test
    void update_shouldThrowLocationNotFoundExceptionWhenLocationNotFound() {
        // Given
        Long id = 99L;
        LocationDto updateDto = new LocationDto();
        updateDto.setName("NonExistent Location");

        when(locationRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        LocationNotFoundException thrown = assertThrows(LocationNotFoundException.class, () -> {
        	locationServiceImpl.update(id, updateDto);
        });

        assertEquals("Location with id:" + id + " not found for update", thrown.getMessage());
        verify(locationRepository, times(1)).findById(id);
        verify(locationRepository, never()).save(any(Location.class)); // Ensure save is not called
    }

    @Test
    void update_shouldKeepExistingNameIfDtoNameIsNull() {
        // Given
        Long id = 1L;
        LocationDto updateDto = new LocationDto(); // Name will be null

        Location existingLocation = new Location();
        existingLocation.setId(id);
        existingLocation.setName("Original Paris Name");

        when(locationRepository.findById(id)).thenReturn(Optional.of(existingLocation));
        when(locationRepository.save(any(Location.class))).thenReturn(existingLocation); // Simulating saving the same object

        // When
        Location result = locationServiceImpl.update(id, updateDto);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Original Paris Name", result.getName()); // Name should remain unchanged
        verify(locationRepository, times(1)).findById(id);
        verify(locationRepository, times(1)).save(existingLocation);
    }

    @Test
    void update_shouldKeepExistingNameIfDtoNameIsEmpty() {
        // Given
        Long id = 1L;
        LocationDto updateDto = new LocationDto();
        updateDto.setName("   "); // Empty string after trim

        Location existingLocation = new Location();
        existingLocation.setId(id);
        existingLocation.setName("Original Paris Name");

        when(locationRepository.findById(id)).thenReturn(Optional.of(existingLocation));
        when(locationRepository.save(any(Location.class))).thenReturn(existingLocation);

        // When
        Location result = locationServiceImpl.update(id, updateDto);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Original Paris Name", result.getName()); // Name should remain unchanged
        verify(locationRepository, times(1)).findById(id);
        verify(locationRepository, times(1)).save(existingLocation);
    }


    @Test
    void delete_shouldDeleteLocationWhenExists() {
        // Given
        Long id = 1L;
        when(locationRepository.existsById(id)).thenReturn(true);
        doNothing().when(locationRepository).deleteById(id);

        // When
        assertDoesNotThrow(() -> locationServiceImpl.delete(id));

        // Then
        verify(locationRepository, times(1)).existsById(id);
        verify(locationRepository, times(1)).deleteById(id);
    }

    @Test
    void delete_shouldThrowLocationNotFoundExceptionWhenLocationNotFound() {
        // Given
        Long id = 99L;
        when(locationRepository.existsById(id)).thenReturn(false);

        // When & Then
        LocationNotFoundException thrown = assertThrows(LocationNotFoundException.class, () -> {
        	locationServiceImpl.delete(id);
        });

        assertEquals("Location with id:" + id + " not found", thrown.getMessage());
        verify(locationRepository, times(1)).existsById(id);
        verify(locationRepository, never()).deleteById(anyLong()); // Ensure deleteById is not called
    }

    @Test
    void create_shouldCreateNewLocationSuccessfully() {
        // Given
        LocationDto newLocationDto = new LocationDto();
        newLocationDto.setName("Berlin");

        Location savedLocation = new Location();
        savedLocation.setId(3L);
        savedLocation.setName("Berlin");

        when(locationRepository.save(any(Location.class))).thenReturn(savedLocation);

        // When
        Location result = locationServiceImpl.create(newLocationDto);

        // Then
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Berlin", result.getName());
        verify(locationRepository, times(1)).save(any(Location.class)); // Verify save was called with a new Location object
        assertNull(newLocationDto.getId()); // Ensure the DTO's ID was set to null if it had one
    }

    @Test
    void create_shouldSetDtoIdToNullIfProvided() {
        // Given
        LocationDto newLocationDto = new LocationDto();
        newLocationDto.setId(5L); // Simulate a DTO coming with an ID
        newLocationDto.setName("Tokyo");

        Location savedLocation = new Location();
        savedLocation.setId(10L); // The ID generated by the repository
        savedLocation.setName("Tokyo");

        when(locationRepository.save(any(Location.class))).thenReturn(savedLocation);

        // When
        Location result = locationServiceImpl.create(newLocationDto);

        // Then
        assertNotNull(result);
        assertEquals(10L, result.getId()); // Expect the ID from the saved entity
        assertEquals("Tokyo", result.getName());
        verify(locationRepository, times(1)).save(any(Location.class));
        assertNull(newLocationDto.getId()); // Verify DTO's ID was set to null
    }
}