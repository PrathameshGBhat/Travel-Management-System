package com.cts.controller.test;

import com.cts.controller.LocationController;
import com.cts.dto.LocationDto;
import com.cts.entities.Location;
import com.cts.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LocationControllerTest {

    @Mock
    private LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    private MockMvc mockMvc; // Although not strictly necessary for unit testing the controller directly, good for integration

    private Location location1;
    private Location location2;
    private LocationDto locationDto;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc (useful for integration tests, but here for completeness)
        mockMvc = MockMvcBuilders.standaloneSetup(locationController).build();

        location1 = new Location();
        location1.setId(1L);
        location1.setName("Paris");

        location2 = new Location();
        location2.setId(2L);
        location2.setName("London");

        locationDto = new LocationDto();
        locationDto.setName("New York");
    }

    @Test
    void getAll_shouldReturnListOfLocations() {
        // Given
        List<Location> locations = Arrays.asList(location1, location2);
        when(locationService.getAll()).thenReturn(locations);

        // When
        ResponseEntity<List<Location>> response = locationController.getAll();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Paris", response.getBody().get(0).getName());
        verify(locationService, times(1)).getAll();
    }

    @Test
    void getById_shouldReturnLocationWhenFound() {
        // Given
        Long locationId = 1L;
        when(locationService.getById(locationId)).thenReturn(location1);

        // When
        ResponseEntity<Location> response = locationController.getById(locationId);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(locationId, response.getBody().getId());
        assertEquals("Paris", response.getBody().getName());
        verify(locationService, times(1)).getById(locationId);
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Simulate an authenticated user with ADMIN role
    void delete_shouldReturnAcceptedStatus() {
        // Given
        Long locationId = 1L;
        doNothing().when(locationService).delete(locationId);

        // When
        ResponseEntity<String> response = locationController.delete(locationId);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(locationService, times(1)).delete(locationId);
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Simulate an authenticated user with ADMIN role
    void create_shouldReturnCreatedLocation() {
        // Given
        Location newLocation = new Location();
        newLocation.setId(3L);
        newLocation.setName(locationDto.getName());

        when(locationService.create(locationDto)).thenReturn(newLocation);

        // When
        ResponseEntity<Location> response = locationController.create(locationDto);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(3L, response.getBody().getId());
        assertEquals("New York", response.getBody().getName());
        verify(locationService, times(1)).create(locationDto);
    }

    @Test
    @WithMockUser(roles = "ADMIN") // Simulate an authenticated user with ADMIN role
    void update_shouldReturnUpdatedLocation() {
        // Given
        Long locationId = 1L;
        Location updatedLocation = new Location();
        updatedLocation.setId(locationId);
        updatedLocation.setName(locationDto.getName()); // Update name

        when(locationService.update(locationId, locationDto)).thenReturn(updatedLocation);

        // When
        ResponseEntity<Location> response = locationController.update(locationId, locationDto);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(locationId, response.getBody().getId());
        assertEquals("New York", response.getBody().getName());
        verify(locationService, times(1)).update(locationId, locationDto);
    }
}