package com.cts.dto;

import org.hibernate.validator.constraints.UniqueElements;

import jakarta.persistence.Column;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// This class helps to move customer information around in our program.
// DTO stands for "Data Transfer Object."

@AllArgsConstructor // This automatically creates a constructor that takes all the fields.
@NoArgsConstructor // This automatically creates a constructor with no fields.
@Data // This automatically makes common methods like 'get' (to read) and 'set' (to change) data.
public class CustomerDto {
	// This is the customer's first name.
	// It can only contain letters (A-Z, a-z).
	@Pattern(regexp = "[A-Za-z]+")
	@NotBlank(message = "First Name cannot be blank")
	private String firstName;

	// This is the customer's last name.
	// It can only contain letters (A-Z, a-z).
	@Pattern(regexp = "[A-Za-z]+")
	@NotBlank(message = "Last Name cannot be blank") // Added message for clarity
	private String lastName;

	// This is where the customer's trip starts.
	// It can only contain letters (A-Z, a-z).
	@Pattern(regexp = "[A-Za-z]+")
	@NotBlank(message = "Start Location cannot be blank")
	private String startLocation;

	// This is where the customer's trip ends.
	// It can only contain letters (A-Z, a-z).
	@Pattern(regexp = "[A-Za-z]+")
	@NotBlank(message = "Destination cannot be blank")
	private String destination;

	// This is the name of the travel package the customer chose.
	@NotBlank(message = "Package Name cannot be blank") // Added message for clarity
	private String packageName;

	// This is the cost of the package.
	// It must be a positive number or zero.
	@PositiveOrZero(message = "Cost must be a positive number or zero if provided.")
	@NotNull(message = "Cost cannot be null") 
	private Number cost;

	// This is the customer's phone number.
	// It must be exactly 10 digits long.
	@Pattern(regexp = "^\\d{10}$", message = "Phone number must be a 10-digit number")
	@NotBlank
	private String phone;

	// These are any extra notes about the customer or their trip.
	// They can't be longer than 250 characters.
	@Size(max = 250, message = "Notes cannot exceed 250 characters")
	private String notes;
}