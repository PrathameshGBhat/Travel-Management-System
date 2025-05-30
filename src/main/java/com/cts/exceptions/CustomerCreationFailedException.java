package com.cts.exceptions;

// This is a special error (called an "exception") that happens when we try to create a customer, but it fails.
// It's a specific type of error for *just* customer creation problems.

public class CustomerCreationFailedException extends RuntimeException {

	// This is a basic way to create this error without a specific message.
	public CustomerCreationFailedException() {
		super(); // Calls the constructor of the parent class (RuntimeException).
		// The "TODO Auto-generated constructor stub" is just a note that can be removed.
	}

	// This is a way to create this error with a specific message about why it failed.
	public CustomerCreationFailedException(String message) {
		super(message); // Calls the parent constructor and passes along the error message.
		// The "TODO Auto-generated constructor stub" is just a note that can be removed.
	} 

}