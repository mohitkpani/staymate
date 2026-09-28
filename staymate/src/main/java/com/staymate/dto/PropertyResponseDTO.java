package com.staymate.dto;

import com.staymate.entity.PropertyType;

public record PropertyResponseDTO(
		Long id,
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
		String amenities,
		Long ownerid,
		String ownerName
		) {

}
