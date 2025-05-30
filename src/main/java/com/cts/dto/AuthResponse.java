package com.cts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// This class is used to send back authorization details after a user logs in.
// DTO stands for "Data Transfer Object" – it helps move specific data.

@AllArgsConstructor // Automatically creates a constructor that takes all fields (like 'token' and 'type').
@NoArgsConstructor // Automatically creates an empty constructor (one that takes no fields).
@Data // Automatically creates common methods like 'getters' (to read data) and 'setters' (to change data).
public class AuthResponse {

    // This is the actual authorization token (a special string that proves who you are).
    private String token;

    // This tells you the type of token. "Bearer" is a common type.
    // It's set to "Bearer" by default.
    private String type = "Bearer";
}