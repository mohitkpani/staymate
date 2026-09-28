package com.staymate.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.staymate.entity.AppUser;
import com.staymate.repository.AppUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	private final AppUserRepository appUserRepository;

	public CustomUserDetailsService(AppUserRepository appUserRepository) {
		super();
		this.appUserRepository = appUserRepository;
	}
	
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
			AppUser appUser = appUserRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
			
			return org.springframework.security.core.userdetails.User
					.withUsername(appUser.getEmail())
					.password(appUser.getPassword())
					.roles(appUser.getRole().name())
					.build();
	}
}
