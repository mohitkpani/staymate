package com.staymate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.staymate.entity.AppUser;

public interface AppUserRepository 
	   extends JpaRepository<AppUser, Long>{
	
	boolean existsByEmail(String emnail);
	
	boolean existsByPhone(String phone);

	Optional<AppUser> findByEmail(String email);
}
