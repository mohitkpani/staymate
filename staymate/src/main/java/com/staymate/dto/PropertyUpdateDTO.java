package com.staymate.dto;

import com.staymate.entity.PropertyType;

public record PropertyUpdateDTO(
		String title,
		String description,
		String location,
		String city,
		Double latitude,
		Double longitude,
		Double rent,
		Double securityDeposit,
		PropertyType propertyType,
		Integer availableRooms,
		String amenities
		) {

}
