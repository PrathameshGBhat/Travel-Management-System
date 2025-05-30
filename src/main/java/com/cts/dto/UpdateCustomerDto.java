package com.cts.dto;

import java.util.List;

import java.util.Set;

import com.cts.entities.Address;
import com.cts.entities.Location;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor // This automatically creates a constructor that takes all the fields.
@NoArgsConstructor // This automatically creates a constructor with no fields.
@Data // This automatically makes common methods like 'get' (to read) and 'set' (to change) data.
public class UpdateCustomerDto {
	private CustomerDto customerDetails;
    private AddressDto permanentAddress;
    private AddressDto communicationAddress; 
    private List<Location> locations;
}