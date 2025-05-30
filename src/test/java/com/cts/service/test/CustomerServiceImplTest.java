package com.cts.service.test;

import com.cts.dto.AddressDto;
import com.cts.dto.CreateCustomerDto;
import com.cts.dto.CustomerDto;
import com.cts.dto.UpdateCustomerDto;
import com.cts.entities.Address;
import com.cts.entities.Customer;
import com.cts.entities.Location;
import com.cts.exceptions.CustomerCreationFailedException;
import com.cts.exceptions.CustomerNotFoundException;
import com.cts.repository.AddressRepository;
import com.cts.repository.CustomerRepository;
import com.cts.repository.LocationRepository;
import com.cts.service.impl.CustomerServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Customer Service Impl Basic Tests")
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private AddressRepository addressRepository; // Keep mocked, as it might be used by JPA internally if not directly

    @InjectMocks
    private CustomerServiceImpl customerService;

    // Common test data
    private Customer customer1; // A simple existing customer entity
    private CreateCustomerDto createCustomerDto; // DTO for new customer creation
    private Location existingLocationChennai;

    @BeforeEach
    void setUp() {
        // Initialize an existing Location entity
        existingLocationChennai = new Location(100L, "Chennai");

        // Initialize a simple existing Customer entity for fetching and updating
        customer1 = new Customer();
        customer1.setId(1L);
        customer1.setFirstName("John");
        customer1.setLastName("Doe");
        customer1.setPhone("1234567890");
        customer1.setPermanentAddress(new Address(10L, "123 Main", "St", "Near Park", "City", "State", "123456"));
        customer1.setLocations(new ArrayList<>(Arrays.asList(existingLocationChennai)));
        // Ensure lists are initialized to avoid NPEs if not set later
        if (customer1.getLocations() == null) {
            customer1.setLocations(new ArrayList<>());
        }


        // Initialize a CreateCustomerDto for testing the create method
        CustomerDto createDetailsDto = new CustomerDto(
                "New", "Customer", "Mumbai", "Delhi", "Basic", 5000.0, "9876543210", "Notes"
        );
        AddressDto createPermAddressDto = new AddressDto(
                "Flat 1A", "Street A", "Area X", "City Y", "State Z", "100001"
        );
        AddressDto createCommAddressDto = new AddressDto(
                "Flat 1B", "Street B", "Area W", "City V", "State U", "100002"
        );
        // Locations for creation: one new, one existing
        List<Location> createLocationsList = Arrays.asList(
                new Location(null, "Hyderabad"), // Represents a new location to be created
                new Location(null, "Chennai")    // Represents an existing location to be found
        );
        createCustomerDto = new CreateCustomerDto(createDetailsDto, createPermAddressDto, createCommAddressDto, createLocationsList);
    }

    // --- Test cases for getAll() ---
    @Test
    @DisplayName("getAll - Should return a list of all customers")
    void testGetAll_ReturnsListOfCustomers() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1));

        List<Customer> customers = customerService.getAll();

        assertNotNull(customers);
        assertEquals(1, customers.size());
        assertEquals("John", customers.get(0).getFirstName());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getAll - Should return an empty list if no customers exist")
    void testGetAll_ReturnsEmptyList() {
        when(customerRepository.findAll()).thenReturn(new ArrayList<>());

        List<Customer> customers = customerService.getAll();

        assertNotNull(customers);
        assertTrue(customers.isEmpty());
        verify(customerRepository, times(1)).findAll();
    }

    // --- Test cases for getById() ---
    @Test
    @DisplayName("getById - Should return customer when ID exists")
    void testGetById_Success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));

        Customer foundCustomer = customerService.getById(1L);

        assertNotNull(foundCustomer);
        assertEquals(1L, foundCustomer.getId());
        assertEquals("John", foundCustomer.getFirstName());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getById - Should throw CustomerNotFoundException when ID does not exist")
    void testGetById_NotFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        CustomerNotFoundException thrown = assertThrows(CustomerNotFoundException.class, () -> {
            customerService.getById(99L);
        });

        assertEquals("Customer with id 99 not found", thrown.getMessage());
        verify(customerRepository, times(1)).findById(99L);
    }

    // --- Test cases for create() ---
    @Test
    @DisplayName("create - Should successfully create a new customer")
    void testCreate_Success() {
        // No need to mock addressRepository.save() as it's not directly called by the service
        // when(addressRepository.save(any(Address.class))).thenAnswer(...) // REMOVED

        // Mock LocationRepository behavior
        when(locationRepository.findByNameIgnoreCase("Hyderabad")).thenReturn(null); // New location
        when(locationRepository.findByNameIgnoreCase("Chennai")).thenReturn(existingLocationChennai); // Existing location

        // Mock saving new location (only for new ones found in DTO)
        when(locationRepository.save(argThat(loc -> "Hyderabad".equals(loc.getName()))))
            .thenAnswer(invocation -> {
                Location newLoc = invocation.getArgument(0);
                newLoc.setId(200L); // Simulate ID for new location
                return newLoc;
            });

        // Mock CustomerRepository save
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer savedCustomer = invocation.getArgument(0);
            savedCustomer.setId(50L); // Simulate ID for new customer
            // Crucially, when customerRepository.save() is called, it should also handle cascading
            // If the Address entity is not having an ID, ensure it gets one for realistic testing
            if (savedCustomer.getPermanentAddress() != null && savedCustomer.getPermanentAddress().getId() == null) {
                savedCustomer.getPermanentAddress().setId(1000L);
            }
            if (savedCustomer.getCommunicationAddress() != null && savedCustomer.getCommunicationAddress().getId() == null) {
                savedCustomer.getCommunicationAddress().setId(1001L);
            }
            return savedCustomer;
        });

        Customer createdCustomer = customerService.create(createCustomerDto);

        assertNotNull(createdCustomer);
        assertNotNull(createdCustomer.getId());
        assertEquals("New", createdCustomer.getFirstName());
        assertEquals(2, createdCustomer.getLocations().size()); // One new, one existing
        assertNotNull(createdCustomer.getPermanentAddress().getId()); // Ensure addresses have IDs (from cascade handling)
        assertNotNull(createdCustomer.getCommunicationAddress().getId());

        verify(customerRepository, times(1)).save(any(Customer.class));
        // verify(addressRepository, times(2)).save(any(Address.class)); // REMOVED - not directly invoked
        verify(locationRepository, times(1)).findByNameIgnoreCase("Hyderabad");
        verify(locationRepository, times(1)).findByNameIgnoreCase("Chennai");
        verify(locationRepository, times(1)).save(argThat(loc -> "Hyderabad".equals(loc.getName()))); // Only new one saved
    }

    @Test
    @DisplayName("create - Should throw CustomerCreationFailedException on DataIntegrityViolation")
    void testCreate_DataIntegrityViolation() {
        // No need to mock addressRepository.save() directly
        // when(addressRepository.save(any(Address.class))).thenAnswer(...) // REMOVED

        // MOCK LocationRepository behavior to allow processing of locations.
        when(locationRepository.findByNameIgnoreCase("Hyderabad")).thenReturn(null); // New location
        when(locationRepository.findByNameIgnoreCase("Chennai")).thenReturn(existingLocationChennai); // Existing location
        when(locationRepository.save(argThat(loc -> "Hyderabad".equals(loc.getName()))))
            .thenAnswer(invocation -> {
                Location newLoc = invocation.getArgument(0);
                newLoc.setId(200L);
                return newLoc;
            });

        // The main stub for this test: make customerRepository.save() throw an exception
        when(customerRepository.save(any(Customer.class))).thenThrow(new DataIntegrityViolationException("Duplicate entry"));


        CustomerCreationFailedException thrown = assertThrows(CustomerCreationFailedException.class, () -> {
            customerService.create(createCustomerDto);
        });

        assertEquals("Failed to create customer due to data integrity issue.", thrown.getMessage());
        // Verify that the necessary interactions happened *before* the exception was thrown
        // verify(addressRepository, times(2)).save(any(Address.class)); // REMOVED
        verify(locationRepository, times(1)).findByNameIgnoreCase("Hyderabad");
        verify(locationRepository, times(1)).findByNameIgnoreCase("Chennai");
        verify(locationRepository, times(1)).save(argThat(loc -> "Hyderabad".equals(loc.getName())));
        verify(customerRepository, times(1)).save(any(Customer.class));
    }


    // --- Test cases for update() ---
    @Test
    @DisplayName("update - Should successfully update customer first name")
    void testUpdate_SuccessFirstName() {
        Long customerId = 1L;
        UpdateCustomerDto updateDto = new UpdateCustomerDto();
        CustomerDto detailsDto = new CustomerDto();
        detailsDto.setFirstName("UpdatedJohn");
        updateDto.setCustomerDetails(detailsDto);

        // Mock existing customer retrieval
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer1));
        // Mock save to return the updated customer
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer updated = invocation.getArgument(0);
            // Verify that the firstName was updated correctly in the object passed to save
            assertEquals("UpdatedJohn", updated.getFirstName());
            return updated;
        });

        Customer result = customerService.update(customerId, updateDto);

        assertNotNull(result);
        assertEquals("UpdatedJohn", result.getFirstName()); // Verify the returned object has the updated name
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("update - Should throw CustomerNotFoundException if customer to update does not exist")
    void testUpdate_NotFound() {
        Long customerId = 99L;
        UpdateCustomerDto updateDto = new UpdateCustomerDto();
        // Ensure this DTO is not empty, so the service's empty DTO check is bypassed
        updateDto.setCustomerDetails(new CustomerDto("Any", null, null, null, null, null, null, null));

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        CustomerNotFoundException thrown = assertThrows(CustomerNotFoundException.class, () -> {
            customerService.update(customerId, updateDto);
        });

        assertEquals("Customer with id: " + customerId + " not found", thrown.getMessage());
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, never()).save(any(Customer.class));
    }

//    @Test
//    @DisplayName("update - Should throw CustomerCreationFailedException if no input to update")
//    void testUpdate_NoInput() {
//        Long customerId = 1L;
//        UpdateCustomerDto emptyUpdateDto = new UpdateCustomerDto(); // All fields are null
//
//        CustomerCreationFailedException thrown = assertThrows(CustomerCreationFailedException.class, () -> {
//            customerService.update(customerId, emptyUpdateDto);
//        });
//
//        assertEquals("Enter an input to update", thrown.getMessage());
//        verify(customerRepository, never()).findById(anyLong());
//        verify(customerRepository, never()).save(any(Customer.class));
//    }

    // --- Test cases for delete() ---
    @Test
    @DisplayName("delete - Should successfully delete an existing customer")
    void testDelete_Success() {
        Long customerId = 1L;
        when(customerRepository.existsById(customerId)).thenReturn(true);
        doNothing().when(customerRepository).deleteById(customerId); // Mock void method

        assertDoesNotThrow(() -> customerService.delete(customerId));

        verify(customerRepository, times(1)).existsById(customerId);
        verify(customerRepository, times(1)).deleteById(customerId);
    }

    @Test
    @DisplayName("delete - Should throw CustomerNotFoundException if customer to delete does not exist")
    void testDelete_NotFound() {
        Long customerId = 99L;
        when(customerRepository.existsById(customerId)).thenReturn(false);

        CustomerNotFoundException thrown = assertThrows(CustomerNotFoundException.class, () -> {
            customerService.delete(customerId);
        });

        assertEquals("Customer with id " + customerId + " not found", thrown.getMessage());
        verify(customerRepository, times(1)).existsById(customerId);
        verify(customerRepository, never()).deleteById(anyLong());
    }
}