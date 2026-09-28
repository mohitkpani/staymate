package com.staymate.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
		
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex){
		Map<String, String> errors = new HashMap<>();
		
		ex.getBindingResult().getFieldErrors().forEach(error ->
        errors.put(error.getField(), error.getDefaultMessage()));
		
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<Map<String, String>> handleEmailAlreadyExists(EmailAlreadyExistsException ex){
		Map<String, String> error = new HashMap<>();
		
		error.put("message", ex.getMessage());
		
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(PhoneAlreadyExistsException.class)
	public ResponseEntity<Map<String, String>> handlePhoneAlreadyExists(PhoneAlreadyExistsException ex){
		Map<String, String> error = new HashMap<>();
		
		error.put("message", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<Map<String, String>> handleInvalidCredentials(InvalidCredentialsException ex){
		Map<String, String> error = new HashMap<>();
		
		error.put("message", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(BookingNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleBookingNotFound(BookingNotFoundException ex){
		Map<String, String> error = new HashMap<>();
		
		error.put("message", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UnauthorizedBookingException.class)
	public ResponseEntity<Map<String, String>> handleUnauthorizedBooking(UnauthorizedBookingException ex){
		Map<String, String> error = new HashMap<>();
		
		error.put("message", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(BookingAlreadyCancelledException.class)
	public ResponseEntity<Map<String, String>> handleBookingAlreadyCancelled(BookingAlreadyCancelledException ex){
		Map<String, String> error = new HashMap<>();
		
		error.put("message", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(InsufficientRoomsException.class)
	public ResponseEntity<Map<String, String>> handleInsufficientRooms(InsufficientRoomsException ex){
		Map<String, String> error = new HashMap<>();
		
		error.put("message", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex){
		Map<String, String> error = new HashMap<>();
		
		error.put("message", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(PropertyNotFoundException.class)
	public ResponseEntity<Map<String, String>> handlePropertyNotFound(PropertyNotFoundException ex){
		Map<String, String> error = new HashMap<>();
		
		error.put("message", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);	
	}
	
	@ExceptionHandler(UnauthorizedPropertyException.class)
	public ResponseEntity<Map<String, String>> handleUnauthorizedProperty(UnauthorizedPropertyException ex){
		Map<String, String> error = new HashMap<>();
		
		error.put("message", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);	
	}
	
	@ExceptionHandler(PropertyImageNotFoundException.class)
	public ResponseEntity<Map<String, String>> handlePropertyImageNotFound(PropertyImageNotFoundException ex){
		Map<String, String> error = new HashMap<>();
		
		error.put("message", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);	
	}
	
	@ExceptionHandler(CloudinaryOperationException.class)
	public ResponseEntity<Map<String, String>> handleCloudinaryOperation(CloudinaryOperationException ex) {

	    Map<String, String> error = new HashMap<>();
	    error.put("message", ex.getMessage());

	    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ProfileImageException.class)
	public ResponseEntity<Map<String, String>> handleProfileImageException(ProfileImageException ex){
		Map<String, String> error = new HashMap<>();
		
		error.put("message", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<Map<String, String>> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex) {

	    Map<String, String> error = new HashMap<>();
	    error.put("message", "Please upload a profile image less than 1 MB.");

	    return new ResponseEntity<>(error, HttpStatus.PAYLOAD_TOO_LARGE);
	}
}
