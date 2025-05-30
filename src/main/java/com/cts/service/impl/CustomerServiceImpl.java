package com.cts.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;

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
import com.cts.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	private final CustomerRepository customerRepository;
	private final LocationRepository locationRepository;
	private final AddressRepository addressRepository;

	// A constructor getting all the database things ready.
	public CustomerServiceImpl(CustomerRepository customerRepository, LocationRepository locationRepository,
			AddressRepository addressRepository) {
		this.customerRepository = customerRepository;
		this.locationRepository = locationRepository;
		this.addressRepository = addressRepository;
	}

	// Get all customers
	@Override
	public List<Customer> getAll() {
		logger.info("Fetching all customers.");
		return customerRepository.findAll();
	}

	// Find one customer by their ID. Throws error if not found.
	@Override
	public Customer getById(Long id) {
		logger.info("Fetching customer with id: {}", id);
		return customerRepository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));
	}

	// Update an existing customer. This can handle just changing a few things!
	@Override
	public Customer update(Long id, UpdateCustomerDto updateCustomerDto) {
		logger.info("Attempting to partially update customer with id: {}", id);

		// First, find the customer. If they are not found, throw an error!
		Customer existingCustomer = customerRepository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException("Customer with id: " + id + " not found"));
		UpdateCustomerDto checkDto = new UpdateCustomerDto();
		if(checkDto.equals(updateCustomerDto)) {
			throw new CustomerCreationFailedException("Enter an input to update");
		}else {

		// If customer details are provided, update only the fields that are not null.
		// This is for partial updates.
		if (updateCustomerDto.getCustomerDetails() != null) {
			if (updateCustomerDto.getCustomerDetails().getFirstName() != null) {
				existingCustomer.setFirstName(updateCustomerDto.getCustomerDetails().getFirstName());
			} 
			if (updateCustomerDto.getCustomerDetails().getLastName() != null) {
				existingCustomer.setLastName(updateCustomerDto.getCustomerDetails().getLastName());
			}
			if (updateCustomerDto.getCustomerDetails().getStartLocation() != null) {
				existingCustomer.setStartLocation(updateCustomerDto.getCustomerDetails().getStartLocation());
			}
			if (updateCustomerDto.getCustomerDetails().getDestination() != null) {
				existingCustomer.setDestination(updateCustomerDto.getCustomerDetails().getDestination());
			}
			if (updateCustomerDto.getCustomerDetails().getPackageName() != null) {
				existingCustomer.setPackageName(updateCustomerDto.getCustomerDetails().getPackageName());
			}
			if (updateCustomerDto.getCustomerDetails().getCost() != null) {
				existingCustomer.setCost(updateCustomerDto.getCustomerDetails().getCost());
			}
			if (updateCustomerDto.getCustomerDetails().getPhone() != null) {
				existingCustomer.setPhone(updateCustomerDto.getCustomerDetails().getPhone());
			}
			if (updateCustomerDto.getCustomerDetails().getNotes() != null) {
				existingCustomer.setNotes(updateCustomerDto.getCustomerDetails().getNotes());
			}
		}

		// Handle permanent address. If DTO has address data, we update or create.
		if (updateCustomerDto.getPermanentAddress() != null) {
			Address newOrUpdatedPermanentAddress = createAddressFromDto(updateCustomerDto.getPermanentAddress());
			// If there's an existing address and a new one in DTO, update the existing one.
			if (existingCustomer.getPermanentAddress() != null && newOrUpdatedPermanentAddress != null) {
				existingCustomer.getPermanentAddress().setHouseNo(newOrUpdatedPermanentAddress.getHouseNo());
				existingCustomer.getPermanentAddress().setStreet(newOrUpdatedPermanentAddress.getStreet());
				existingCustomer.getPermanentAddress().setLandmark(newOrUpdatedPermanentAddress.getLandmark());
				existingCustomer.getPermanentAddress().setCity(newOrUpdatedPermanentAddress.getCity());
				existingCustomer.getPermanentAddress().setState(newOrUpdatedPermanentAddress.getState());
				existingCustomer.getPermanentAddress().setPin(newOrUpdatedPermanentAddress.getPin());
			} else if (newOrUpdatedPermanentAddress != null) {
				// If no existing address but DTO has data, set a new one.
				existingCustomer.setPermanentAddress(newOrUpdatedPermanentAddress);
			}
			// If permanentAddress DTO was provided but all fields were null, we leave
			// existing as is.
			// If permanentAddress DTO was null, we don't change anything for existing.
		}

		// Handle communication address. If DTO has address data, we update or create.
		if (updateCustomerDto.getCommunicationAddress() != null) {
			Address newOrUpdatedCommunicationAddress = createAddressFromDto(
					updateCustomerDto.getCommunicationAddress());
			if (existingCustomer.getCommunicationAddress() != null && newOrUpdatedCommunicationAddress != null) {
				existingCustomer.getCommunicationAddress().setHouseNo(newOrUpdatedCommunicationAddress.getHouseNo());
				existingCustomer.getCommunicationAddress().setStreet(newOrUpdatedCommunicationAddress.getStreet());
				existingCustomer.getCommunicationAddress().setLandmark(newOrUpdatedCommunicationAddress.getLandmark());
				existingCustomer.getCommunicationAddress().setCity(newOrUpdatedCommunicationAddress.getCity());
				existingCustomer.getCommunicationAddress().setState(newOrUpdatedCommunicationAddress.getState());
				existingCustomer.getCommunicationAddress().setPin(newOrUpdatedCommunicationAddress.getPin());
			} else if (newOrUpdatedCommunicationAddress != null) {
				existingCustomer.setCommunicationAddress(newOrUpdatedCommunicationAddress);
			}
		}

		// Handle locations. If the DTO has locations, we replace the old ones.
		// If DTO is null, we leave the existing locations alone.
		// If DTO is empty list, we clear all locations.
		if (updateCustomerDto.getLocations() != null) {
			List<Location> updatedLocations = processLocationsFromDto(updateCustomerDto.getLocations());
			existingCustomer.getLocations().clear(); // Clear existing
			existingCustomer.getLocations().addAll(updatedLocations); // Add new/reused ones
		}

		try {
			// Save the updated customer.
			Customer updatedCustomer = customerRepository.save(existingCustomer);
			logger.info("Customer with id {} updated successfully.", updatedCustomer.getId());
			return updatedCustomer;
		} catch (DataIntegrityViolationException ex) {
			logger.error("DB error during customer update for id {}: {}", id, ex.getMostSpecificCause().getMessage());
			throw new CustomerCreationFailedException("Failed to update customer because of some weird data problem.");
		} catch (Exception ex) {
			logger.error("An unexpected error occurred during customer update for id {}: {}", id, ex.getMessage(), ex);
			throw new RuntimeException("Something totally unexpected went wrong updating customer.");
		}}
	}

	// Create a brand new customer
	@Override
	public Customer create(CreateCustomerDto createCustomerDto) {
		logger.info("Attempting to create customer with DTO: {}", createCustomerDto);

		// Map basic customer details from DTO.
		Customer customer = mapCreateDtoToCustomer(createCustomerDto);

		// Create permanent address and set it.
		Address permanentAddress = createAddressFromDto(createCustomerDto.getPermanentAddress());
		customer.setPermanentAddress(permanentAddress);

		// Create communication address and set it.
		Address communicationAddress = createAddressFromDto(createCustomerDto.getCommunicationAddress());
		customer.setCommunicationAddress(communicationAddress);

		// Process locations (reuse or make new ones).
		List<Location> locations = processLocationsFromDto(createCustomerDto.getLocations());
		customer.setLocations(locations);

		try {
			// Save the new customer!
			Customer savedCustomer = customerRepository.save(customer);
			logger.info("Customer created successfully with id: {}", savedCustomer.getId());
			return savedCustomer;
		} catch (DataIntegrityViolationException ex) {
			logger.error("Data integrity violation during customer creation: {}",
					ex.getMostSpecificCause().getMessage());
			throw new CustomerCreationFailedException("Failed to create customer due to data integrity issue.");
		} catch (Exception ex) {
			logger.error("An unexpected error occurred during customer creation: {}", ex.getMessage(), ex);
			throw new CustomerCreationFailedException("Failed to create customer due to an internal error.");
		}
	}

	// Helper to take CustomerDto stuff and put it into a Customer entity.
	private Customer mapCreateDtoToCustomer(CreateCustomerDto createCustomerDto) {
		Customer customer = new Customer();
		if (createCustomerDto.getCustomerDetails() != null) {
			customer.setFirstName(createCustomerDto.getCustomerDetails().getFirstName());
			customer.setLastName(createCustomerDto.getCustomerDetails().getLastName());
			customer.setStartLocation(createCustomerDto.getCustomerDetails().getStartLocation());
			customer.setDestination(createCustomerDto.getCustomerDetails().getDestination());
			customer.setPackageName(createCustomerDto.getCustomerDetails().getPackageName());
			customer.setCost(createCustomerDto.getCustomerDetails().getCost());
			customer.setPhone(createCustomerDto.getCustomerDetails().getPhone());
			customer.setNotes(createCustomerDto.getCustomerDetails().getNotes());
		}
		return customer;
	}

	// Makes a new Address entity from an AddressDto.
	private Address createAddressFromDto(AddressDto addressDtoParameter) {
		if (addressDtoParameter == null) {
			return null;
		}
		Address address = new Address();
		address.setHouseNo(addressDtoParameter.getHouseNo());
		address.setStreet(addressDtoParameter.getStreet());
		address.setLandmark(addressDtoParameter.getLandmark());
		address.setCity(addressDtoParameter.getCity());
		address.setState(addressDtoParameter.getState());
		address.setPin(addressDtoParameter.getPin());
		return address; // Not saving here, Spring handles it!
	}

	// This checks for existing locations by name and reuses them, or makes new ones.
	// Tries to handle if two people try to make the same location at once!
	private List<Location> processLocationsFromDto(List<Location> locationDtoList) {
		List<Location> managedLocations = new ArrayList<>();
		if (locationDtoList != null) {
			for (Location locationDto : locationDtoList) {
				if (locationDto != null && locationDto.getName() != null) {
					String trimmedName = locationDto.getName().trim();
					if (!trimmedName.isEmpty()) {
						Location existingLocation = locationRepository.findByNameIgnoreCase(trimmedName);
						if (existingLocation != null) {
							if (!managedLocations.contains(existingLocation)) {
								managedLocations.add(existingLocation);
								logger.debug("Reusing existing location: {}", existingLocation.getName());
							}
						} else {
							Location newLocation = new Location();
							newLocation.setName(trimmedName);
							try {
								Location savedLocation = locationRepository.save(newLocation);
								managedLocations.add(savedLocation);
								logger.info("Created and saved new location: {}", savedLocation.getName());
							} catch (DataIntegrityViolationException ex) {
								logger.warn("Concurrent creation of location '{}'. Trying to re-fetch.", trimmedName);
								existingLocation = locationRepository.findByNameIgnoreCase(trimmedName);
								if (existingLocation != null && !managedLocations.contains(existingLocation)) {
									managedLocations.add(existingLocation);
								} else {
									logger.error("Failed to handle concurrent location creation for '{}': {}",
											trimmedName, ex.getMostSpecificCause().getMessage(), ex);
									throw new CustomerCreationFailedException("Failed to process location '"
											+ trimmedName + "' because of a data conflict.");
								}
							}
						}
					}
				}
			}
		}
		return managedLocations;
	}

	// Delete a customer by their ID. 
	@Override
	public void delete(Long id) {
		logger.info("Attempting to delete customer with id: {}", id);
		if (!customerRepository.existsById(id)) {
			throw new CustomerNotFoundException("Customer with id " + id + " not found");
		}
		customerRepository.deleteById(id);
		logger.info("Customer with id {} deleted successfully.", id);
	}
}