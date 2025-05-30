package com.cts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// This is a special class to hold information about an address.
// DTO stands for "Data Transfer Object" – it just means it helps move data around.

@AllArgsConstructor // Makes a constructor that takes all the fields (houseNo, street, etc.)
@NoArgsConstructor // Makes an empty constructor (one that takes no fields)
@Data // Automatically creates common methods like 'getters' (to read data) and 'setters' (to change data)
public class AddressDto {

    // This is the house number.
    // It can't be longer than 250 characters.
    @Size(max = 250, message = "House number cannot exceed 250 characters")
    private String houseNo;

    // This is the street name.
    // It cannot be empty or just spaces.
    // It also can't be longer than 250 characters.
    @NotBlank(message = "Street cannot be blank")
    @Size(max = 250, message = "Street cannot exceed 250 characters")
    private String street;

    // This is an optional landmark near the address.
    // It can't be longer than 250 characters.
    @Size(max = 250, message = "Landmark cannot exceed 250 characters")
    private String landmark;

    // This is the city.
    // It cannot be empty or just spaces.
    // It also can't be longer than 250 characters.
    @NotBlank(message = "City cannot be blank")
    @Size(max = 250, message = "City cannot exceed 250 characters")
    private String city;

    // This is the state.
    // It cannot be empty or just spaces.
    // It also can't be longer than 250 characters.
    @NotBlank(message = "State cannot be blank")
    @Size(max = 250, message = "State cannot exceed 250 characters")
    private String state;

    // This is the PIN code (like a zip code).
    // It cannot be empty or just spaces.
    // It *must* be exactly 6 digits long.
    @NotBlank(message = "Pin cannot be blank")
    @Pattern(regexp = "^\\d{6}$", message = "Pin must be a 6-digit number")
    private String pin;
}