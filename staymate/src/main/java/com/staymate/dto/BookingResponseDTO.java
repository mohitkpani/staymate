package com.staymate.dto;

import java.time.LocalDateTime;

import com.staymate.entity.BookingStatus;
import com.staymate.entity.PaymentStatus;

public record BookingResponseDTO(
		
		Long bookingId,
		Long propertyid,
		String propertyTitle,
		Long userId,
		String username,
		Integer numberOfRooms,
		LocalDateTime bookingDate,
		Double totalAmount,
		BookingStatus bookingStatus,
		PaymentStatus paymentStatus
		) {

}
