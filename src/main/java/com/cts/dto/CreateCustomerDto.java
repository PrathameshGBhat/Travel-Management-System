// CreateCustomerDto.java
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

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCustomerDto {
	@Valid // <--- This tells Spring to validate the CustomerDto object
	@NotNull(message = "Customer details cannot be null") // <--- Recommended: Make this mandatory
	private CustomerDto customerDetails;
	@Valid // <--- This tells Spring to validate the permanentAddressDto object
	@NotNull(message = "Permanent address cannot be null") // <--- Recommended: Make this mandatory
	private AddressDto permanentAddress;
	@Valid
	private AddressDto communicationAddress;
	private List<Location> locations;
}	