package com.staymate.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BookingRequestDTO(
		
		@NotNull(message = "Property id is required")
		Long propertyId,
		
		@NotNull(message = "Number of rooms is required")
		@Min(value = 1, message = "At least 1 room must be booked")
		Integer numberOfRooms
		) {

}
