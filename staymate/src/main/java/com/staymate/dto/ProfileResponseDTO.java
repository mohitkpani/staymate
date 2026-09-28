package com.staymate.dto;

import com.staymate.entity.Role;

public record ProfileResponseDTO(
		Long id,
		String name,
		String email,
		String phone,
		Role role,
		String profileImageUrl
		) {

}
