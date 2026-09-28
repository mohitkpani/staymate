package com.staymate.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.staymate.dto.AdminResponseDTO;
import com.staymate.dto.LoginRequestDTO;
import com.staymate.dto.LoginResponseDTO;
import com.staymate.dto.ProfileResponseDTO;
import com.staymate.dto.ProfileUpdateDTO;
import com.staymate.dto.RegisterRequestDTO;
import com.staymate.dto.UpdatedRoleRequestDTO;
import com.staymate.entity.AppUser;

import jakarta.servlet.http.HttpSession;

public interface AppUserService {
	
	public abstract AppUser registerUser(RegisterRequestDTO registerRequestDTO);
	
	public abstract LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO);
	
	public abstract ProfileResponseDTO getProfile(String email);
	
	public abstract ProfileResponseDTO uploadProfileImage(String email, MultipartFile file);
	
	public abstract ProfileResponseDTO updateProfile(String email, ProfileUpdateDTO profileUpdateDTO);
	
	public abstract List<AdminResponseDTO> getAllUsers();
	
	public abstract AdminResponseDTO getUserById(Long userId);
	
	public abstract AdminResponseDTO updateUserRole(Long userId, UpdatedRoleRequestDTO roleRequestDTO);
	
	public abstract String deleteUser(Long userId);
	
	public abstract LoginResponseDTO completeGoogleRegistration(String email, String name, String role);

}
