package com.staymate.dto;

import com.staymate.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
		
		@NotBlank(message = "Name is required.")
		@Size(min = 2, max = 50, message = "Name must be between 2 to 50 characters.")
		String name,
		@NotBlank(message = "Email is required.")
		@Email(message = "Please enter a valid email.")
		String email,
		@NotBlank(message = "Phone number is required.")
		String phone,
		@NotBlank(message = "Password is required.")
		@Size(min = 4, max = 15, message = "Password must be between 4 to 15 characters.")
		String password,
		Role role
		) {

}
