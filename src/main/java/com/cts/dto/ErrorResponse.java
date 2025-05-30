package com.cts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
@AllArgsConstructor // This automatically creates a constructor that takes all the fields.
@NoArgsConstructor // This automatically creates a constructor with no fields.
@Data // This automatically makes common methods like 'get' (to read) and 'set' (to change) data.

//This is a RESPONSE for each operation which have a "message" and a "status code"
public class ErrorResponse { 
    private String message;
    private HttpStatus status;
}
