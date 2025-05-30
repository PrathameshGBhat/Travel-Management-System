package com.cts.exceptions;

// This is a special error (called an "exception") that happens when we try to find a customer, but they don't exist.
// It's a specific type of error for *just* when a customer isn't found.

public class CustomerNotFoundException extends RuntimeException {

	// This is a basic way to create this error without a specific message.
	public CustomerNotFoundException() {
		super(); // Calls the constructor of the parent class (RuntimeException).
		// The "TODO Auto-generated constructor stub" is just a note from the IDE that can be removed.
	}

	// This is a way to create this error with a specific message about which customer wasn't found.
	public CustomerNotFoundException(String message) {
		super(message); // Calls the parent constructor and passes along the error message.
		// The "TODO Auto-generated constructor stub" is just a note from the IDE that can be removed.
	}

	
}