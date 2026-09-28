package com.staymate.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staymate.dto.BookingRequestDTO;
import com.staymate.dto.BookingResponseDTO;
import com.staymate.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
	
	private final BookingService bookingService;

	public BookingController(BookingService bookingService) {
		super();
		this.bookingService = bookingService;
	}
	
	@PostMapping()
	public ResponseEntity<BookingResponseDTO> createBooking(
					@RequestBody @Valid BookingRequestDTO bookingRequestDTO,
					Authentication authentication
			) {
		
		String email = authentication.getName();
		BookingResponseDTO response = bookingService.createBooking(bookingRequestDTO, email);
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/{bookingId}")
	public ResponseEntity<BookingResponseDTO>  getbookingById(@PathVariable Long bookingId, Authentication authentication){
		 
		String email = authentication.getName();
		BookingResponseDTO response = bookingService.getBookingById(bookingId, email);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping()
	public ResponseEntity<List<BookingResponseDTO>> getMyBookings(Authentication authentication){
		
		String email = authentication.getName();
		 List<BookingResponseDTO> response = bookingService.getMyBookings(email);
		 
		 return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/{bookingId}")
	public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId, Authentication authentication){
		
		String email = authentication.getName();
		String response = bookingService.cancelBooking(bookingId, email);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
