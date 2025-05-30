package com.cts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor // This automatically creates a constructor that takes all the fields.
@NoArgsConstructor // This automatically creates a constructor with no fields.
@Data // This automatically makes common methods like 'get' (to read) and 'set' (to change) data.

// Used for login for access
public class LoginDto {
	
    private String usernameOrEmail; // Can be login using Username or Email 
    private String password;
}
