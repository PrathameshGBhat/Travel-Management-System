package com.cts.controller.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cts.controller.LocationController;
import com.cts.dto.AddressDto;
import com.cts.dto.CreateCustomerDto;
// import com.cts.dto.CreateCustomerDto.CustomerDetails; // REMOVE THIS LINE
import com.cts.dto.CustomerDto; // <--- ADD THIS IMPORT
import com.cts.dto.LocationDto;
import com.cts.dto.UpdateCustomerDto;
import com.cts.entities.Address;
import com.cts.entities.Customer;
import com.cts.entities.Location;
import com.cts.service.CustomerService;
import com.cts.service.LocationService;
import com.fasterxml.jackson.databind.ObjectMapper;

//@WebMvcTest(CustomerController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private CustomerService customerService;
	private LocationService locationService;

	private Customer customer;
	private CreateCustomerDto createCustomerDto;
	private UpdateCustomerDto updateCustomerDto;
	private AddressDto permanentAddressDto;
	private AddressDto communicationAddressDto;

	@BeforeEach
	void setUp() {
		// Initialize DTOs for request bodies
		permanentAddressDto = new AddressDto();
		permanentAddressDto.setHouseNo("42A");
		permanentAddressDto.setStreet("Gandhi Nagar");
		permanentAddressDto.setLandmark("Near Apollo Hospital");
		permanentAddressDto.setCity("Mumbai");
		permanentAddressDto.setState("Maharashtra");
		permanentAddressDto.setPin("400001");

		communicationAddressDto = new AddressDto();
		communicationAddressDto.setHouseNo("10A");
		communicationAddressDto.setStreet("Nehru Street");
		communicationAddressDto.setLandmark("Opposite Railway Station");
		communicationAddressDto.setCity("Coimbatore");
		communicationAddressDto.setState("Tamil Nadu");
		communicationAddressDto.setPin("641001");

		// Prepare CustomerDto for DTOs
		// CHANGE HERE: Use the new top-level CustomerDto class
		CustomerDto customerDetailsDto = new CustomerDto(); // Changed from CustomerDetails to CustomerDto
		customerDetailsDto.setFirstName("Karthik");
		customerDetailsDto.setLastName("SK");
		customerDetailsDto.setStartLocation("Chennai");
		customerDetailsDto.setDestination("Mumbai");
		customerDetailsDto.setPackageName("Rajahamsha Spcl");
		customerDetailsDto.setCost(2500.0);
		customerDetailsDto.setPhone("9876543210");
		customerDetailsDto.setNotes("Customer requires Luggage carrier.");

		// CreateCustomerDto setup
		// CHANGE HERE: Set the new top-level CustomerDto
		createCustomerDto = new CreateCustomerDto();
		createCustomerDto.setCustomerDetails(customerDetailsDto); // Set the new CustomerDto object
		createCustomerDto.setPermanentAddress(permanentAddressDto);
		createCustomerDto.setCommunicationAddress(communicationAddressDto);
		createCustomerDto.setLocations(Arrays.asList(new Location(null, "Kovallam"), new Location(null, "Kerala")));

		// UpdateCustomerDto setup (can reuse customerDetailsDto if fields are the same)
		// CHANGE HERE: Set the new top-level CustomerDto
		updateCustomerDto = new UpdateCustomerDto();
		updateCustomerDto.setCustomerDetails(customerDetailsDto); // Set the new CustomerDto object
		updateCustomerDto.setPermanentAddress(permanentAddressDto);
		updateCustomerDto.setCommunicationAddress(communicationAddressDto);
		updateCustomerDto
				.setLocations(Arrays.asList(new Location(null, "UpdatedLocA"), new Location(null, "UpdatedLocB")));

		// Initialize a Customer entity for expected service returns
		customer = new Customer();
		customer.setId(1L);
		customer.setFirstName("Karthik");
		customer.setLastName("SK");
		customer.setStartLocation("Chennai");
		customer.setDestination("Mumbai");
		customer.setPackageName("Rajahamsha Spcl");
		customer.setCost(2500.0);
		customer.setPhone("9876543210");
		customer.setNotes("Customer requires Luggage carrier.");

		Address permanentAddress = new Address();
		permanentAddress.setId(101L);
		permanentAddress.setHouseNo("42A");
		permanentAddress.setStreet("Gandhi Nagar");
		permanentAddress.setLandmark("Near Apollo Hospital");
		permanentAddress.setCity("Mumbai");
		permanentAddress.setState("Maharashtra");
		permanentAddress.setPin("400001");
		customer.setPermanentAddress(permanentAddress);

		Address communicationAddress = new Address();
		communicationAddress.setId(102L);
		communicationAddress.setHouseNo("10A");
		communicationAddress.setStreet("Nehru Street");
		communicationAddress.setLandmark("Opposite Railway Station");
		communicationAddress.setCity("Coimbatore");
		communicationAddress.setState("Tamil Nadu");
		communicationAddress.setPin("641001");
		customer.setCommunicationAddress(communicationAddress);


		List<Location> customerLocations = Arrays.asList(new Location(1L, "Kovallam"), new Location(2L, "Kerala"),
				new Location(3L, "Mangalore"));
		customer.setLocations(customerLocations);
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void testAddCustomer_Success() throws Exception {
		// Mock the service call
		when(customerService.create(any(CreateCustomerDto.class))).thenReturn(customer);
		// Perform the POST request
		ResultActions result = mockMvc.perform(post("/api/TravelAgency/Customer/save").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createCustomerDto)));
		// Verify the response
		result.andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.firstName").value("Karthik"))
				.andExpect(jsonPath("$.destination").value("Mumbai"))
				.andExpect(jsonPath("$.permanentAddress.houseNo").value("42A"))
				.andExpect(jsonPath("$.locations[0].name").value("Kovallam"));

		// Verify that the service method was called exactly once
		verify(customerService, times(1)).create(any(CreateCustomerDto.class));

	}


	

//
//    @Test
//    void testAddCustomer_CreationFailed() throws Exception {
//        // Mock the service to throw a creation failed exception
//        when(customerService.create(any(CreateCustomerDto.class)))
//                .thenThrow(new CustomerCreationFailedException("Failed to create customer due to database error"));
//
//        // Perform the POST request
//        ResultActions result = mockMvc.perform(post("/api/TravelAgency/Customer/save")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(createCustomerDto)));
//
//        // Verify the response for a bad request status
//        result.andExpect(status().isBadRequest());
//
//        verify(customerService, times(1)).create(any(CreateCustomerDto.class));
//    }

	@Test
	@WithMockUser(roles = "ADMIN")
	void testUpdateCustomer_Success() throws Exception {
		Long customerId = 1L;
		// Mock the service call
		when(customerService.update(eq(customerId), any(UpdateCustomerDto.class))).thenReturn(customer);

		// Perform the PUT request
		ResultActions result = mockMvc.perform(put("/api/TravelAgency/Customer/{id}", customerId)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateCustomerDto)));

		// Verify the response
		result.andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.firstName").value("Karthik"));

		// Verify that the service method was called
		verify(customerService, times(1)).update(eq(customerId), any(UpdateCustomerDto.class));
	}

//    @Test
//    void testUpdateCustomer_NotFound() throws Exception {
//        Long customerId = 99L; // Non-existent ID
//        // Mock the service to throw NotFound exception
//        when(customerService.update(eq(customerId), any(UpdateCustomerDto.class)))
//                .thenThrow(new CustomerNotFoundException("Customer not found"));
//
//        // Perform the PUT request
//        ResultActions result = mockMvc.perform(put("/api/TravelAgency/Customer/{id}", customerId)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(updateCustomerDto)));
//
//        // Verify the response is NOT_FOUND
//        result.andExpect(status().isNotFound());
//
//        verify(customerService, times(1)).update(eq(customerId), any(UpdateCustomerDto.class));
//    }

	@Test
	@WithMockUser(roles = "ADMIN")
	void testDeleteCustomer_Success() throws Exception {
		Long customerId = 1L;
		// Mock the service call
		doNothing().when(customerService).delete(customerId);

		// Perform the DELETE request
		ResultActions result = mockMvc.perform(delete("/api/TravelAgency/Customer/{id}", customerId));

		// Verify the response is ACCEPTED (202)
		result.andExpect(status().isAccepted());

		// Verify that the service method was called
		verify(customerService, times(1)).delete(customerId);
	}

//    @Test
//    void testDeleteCustomer_NotFound() throws Exception {
//        Long customerId = 99L; // Non-existent ID
//        // Mock the service to throw NotFound exception
//        doThrow(new CustomerNotFoundException("Customer not found")).when(customerService).delete(customerId);
//
//        // Perform the DELETE request
//        ResultActions result = mockMvc.perform(delete("/api/TravelAgency/Customer/{id}", customerId));
//
//        // Verify the response is NOT_FOUND
//        result.andExpect(status().isNotFound());
//
//        verify(customerService, times(1)).delete(customerId);
//    }

	@Test
	@WithMockUser
	void testGetCustomerById_Success() throws Exception {
		Long customerId = 1L;
		// Mock the service call
		when(customerService.getById(customerId)).thenReturn(customer);

		// Perform the GET request
		ResultActions result = mockMvc.perform(get("/api/TravelAgency/Customer/{id}", customerId));

		// Verify the response
		result.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.firstName").value("Karthik"))
				.andExpect(jsonPath("$.permanentAddress.pin").value("400001"));

		// Verify that the service method was called
		verify(customerService, times(1)).getById(customerId);
	}

//    @Test
//    void testGetCustomerById_NotFound() throws Exception {
//        Long customerId = 99L; // Non-existent ID
//        // Mock the service to throw NotFound exception
//        when(customerService.getById(customerId)).thenThrow(new CustomerNotFoundException("Customer not found"));
//
//        // Perform the GET request
//        ResultActions result = mockMvc.perform(get("/api/TravelAgency/Customer/{id}", customerId));
//
//        // Verify the response is NOT_FOUND
//        result.andExpect(status().isNotFound());
//
//        verify(customerService, times(1)).getById(customerId);
//    }

	@Test
	@WithMockUser
	void testGetAllCustomers_Success() throws Exception {
		List<Customer> customerList = Arrays.asList(customer, new Customer()); // Create another dummy customer
		customerList.get(1).setId(2L);
		customerList.get(1).setFirstName("Jane");

		// Mock the service call
		when(customerService.getAll()).thenReturn(customerList);

		// Perform the GET request
		ResultActions result = mockMvc.perform(get("/api/TravelAgency/Customer/customers"));

		// Verify the response
		result.andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[0].firstName").value("Karthik")).andExpect(jsonPath("$[1].id").value(2L))
				.andExpect(jsonPath("$[1].firstName").value("Jane")).andExpect(jsonPath("$.length()").value(2)); // Check
																													// list
																													// size

		// Verify that the service method was called
		verify(customerService, times(1)).getAll();
	}

//    @Test
//    void testGetAllCustomers_NoContent() throws Exception {
//        // Mock the service call to return an empty list
//        when(customerService.getAll()).thenReturn(List.of());
//
//        // Perform the GET request
//        ResultActions result = mockMvc.perform(get("/api/TravelAgency/Customer/customers"));
//
//        // Verify the response is OK and an empty array
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(0));
//
//        verify(customerService, times(1)).getAll();
//    }

}