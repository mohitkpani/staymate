package com.staymate.dto;

import com.staymate.entity.Role;

public record AdminResponseDTO(
		
		Long id,
		
		String name,
		
		String email,
		
		String phone,
		
		Role role
		) {

}
