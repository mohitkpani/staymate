package com.staymate.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staymate.dto.BookingResponseDTO;
import com.staymate.service.BookingService;

@RestController
@RequestMapping("/api/owner/bookings")
public class OwnerBookingController {
	
	private final BookingService bookingService;

	public OwnerBookingController(BookingService bookingService) {
		super();
		this.bookingService = bookingService;
	}
	
	@GetMapping()
	public ResponseEntity<List<BookingResponseDTO>> getOwnerBookings(Authentication authentication){
		
		String email = authentication.getName();
		List<BookingResponseDTO> response = bookingService.getOwnerBookings(email);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/{bookingId}")
	public ResponseEntity<BookingResponseDTO> getOwnerBookingById(@PathVariable Long bookingId, Authentication authentication){
		
		String email = authentication.getName();
		BookingResponseDTO response = bookingService.getOwnerBookingById(bookingId, email);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PatchMapping("/{bookingId}/cancel")
	public ResponseEntity<String> cancelOwnerBooking(@PathVariable Long bookingId, Authentication authentication){
		
		String email = authentication.getName();
		String response = bookingService.cancelOwnerBooking(bookingId, email);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	

}
