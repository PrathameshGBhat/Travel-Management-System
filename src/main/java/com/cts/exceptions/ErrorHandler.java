package com.cts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

// This class helps us deal with **errors** that happen in our application.
// Think of it as a central error manager for all our web pages (controllers).
@ControllerAdvice
public class ErrorHandler {

	// This method runs when a **CustomerNotFoundException** happens.
	// It's like saying, "If someone tries to find a customer who isn't there, do this!"
	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleCustomerNotFound(CustomerNotFoundException ex) {
		// We create a new error message object.
		ErrorResponse response = new ErrorResponse();
		// We put the error message from the exception into our response object.
		response.setMessage(ex.getMessage());

		// We send back an error message to the user, saying "404 Not Found."
		return new ResponseEntity<ErrorResponse>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(LocationNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleLocationNotFound(LocationNotFoundException ex){
		
		ErrorResponse response = new ErrorResponse();
		
		response.setMessage(ex.getMessage());
		
		return new ResponseEntity<ErrorResponse>(response,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex){
		ErrorResponse response = new ErrorResponse();
		response.setMessage(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(response,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMediaTypeNotSupported(MethodArgumentTypeMismatchException ex){
		ErrorResponse response = new ErrorResponse();
		response.setMessage(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(response,HttpStatus.NOT_FOUND);
	}
	
	
	// This method runs when there are **validation errors**.
	// This happens if data doesn't follow our rules (like a phone number not being 10 digits).
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
		// We're building a nice, easy-to-read error message here.
		StringBuilder sb = new StringBuilder();
		// We look at each specific rule that was broken.
		for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
			// We add the field that had the error and why it was wrong to our message.
			sb.append(fieldError.getField() + " : " + fieldError.getDefaultMessage() + " ");
		}

		// We create an error response using the message we just built.
		ErrorResponse errorResponse = new ErrorResponse(sb.toString());
		// We send back an error message to the user, saying "400 Bad Request" (meaning they sent bad data).
		return ResponseEntity.badRequest().body(errorResponse);
	}
	
	// This method runs if we try to **create a customer but it fails**.
	@ExceptionHandler(CustomerCreationFailedException.class)
	public ResponseEntity<ErrorResponse> handleCustomerCreationFailed(CustomerCreationFailedException ex) {
		// We create a new error message object.
		ErrorResponse response = new ErrorResponse();
		// We put the error message from the exception into our response object.
		response.setMessage(ex.getMessage());

		// We send back an error message to the user, saying "400 Bad Request."
		return new ResponseEntity<ErrorResponse>(response, HttpStatus.BAD_REQUEST);
	}
	
	// This method runs if the computer **can't understand the information sent to it**.
	// This usually happens if the data (like JSON) is badly typed or formatted.
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
		ErrorResponse response = new ErrorResponse();	
		response.setMessage(ex.getMessage()); // Get the error message from the problem.

		// We send back an error message to the user, saying "400 Bad Request."
		return new ResponseEntity<ErrorResponse>(response, HttpStatus.BAD_REQUEST);
	}
	

	
}