package com.cts.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor // This automatically creates a constructor that takes all the fields.
@NoArgsConstructor // This automatically creates a constructor with no fields.
@Data // This automatically makes common methods like 'get' (to read) and 'set' (to change) data.

public class LocationDto {
    private Long id;
    @NotBlank(message = "Location name cannot be blank") 
    private String name; // <--- Recommended: Make this mandatory
}