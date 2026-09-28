package com.staymate.dto;

import com.staymate.entity.PropertyType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record PropertyRequestDTO(

        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @NotBlank(message = "Location is required")
        String location,

        @NotBlank(message = "City is required")
        String city,

        Double latitude,

        Double longitude, 

        @NotNull(message = "Rent is required")
        @Positive(message = "Rent must be greater than 0")
        Double rent,

        @NotNull(message = "Security deposit is required")
        @PositiveOrZero(message = "Security deposit can't be negative")
        Double securityDeposit,

        @NotNull(message = "Property type is required")
        PropertyType propertyType,

        @NotNull(message = "Available rooms is required")
        @Min(value = 0, message = "Available rooms can't be negative")
        Integer availableRooms,

        @NotBlank(message = "Amenities is required")
        String amenities

) {
}