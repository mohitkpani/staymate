package com.staymate.service;

import java.util.List;

import com.staymate.dto.BookingRequestDTO;
import com.staymate.dto.BookingResponseDTO;

public interface BookingService {
	
	BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO, String userEmail);
	
	BookingResponseDTO getBookingById(Long bookingId, String userEmail);
	
	List<BookingResponseDTO> getMyBookings(String userEmail);
	
	String cancelBooking(Long bookingId, String userEmail);
	
	List<BookingResponseDTO> getOwnerBookings(String ownerEmail);
	
	BookingResponseDTO getOwnerBookingById(Long bookingId, String ownerEmail);
	
	String cancelOwnerBooking(Long bookingId, String ownerEmail);

}
