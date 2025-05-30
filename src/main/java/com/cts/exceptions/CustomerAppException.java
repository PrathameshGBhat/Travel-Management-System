package com.cts.exceptions;

import org.springframework.http.HttpStatus;

// This is a special type of error (called an "exception") that our customer application can throw.
// It's used when something goes wrong that's specific to our customer operations.

public class CustomerAppException extends RuntimeException {

    // This holds the HTTP status code (like 404 for Not Found, 400 for Bad Request, etc.).
    private HttpStatus status;
    // This holds a message explaining what went wrong.
    private String message;

    // This is the constructor that's used to create a new CustomerAppException.
    // When you create one, you need to give it a status code and a message.
    public CustomerAppException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

}