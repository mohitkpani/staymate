package com.staymate.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staymate.dto.AdminResponseDTO;
import com.staymate.dto.UpdatedRoleRequestDTO;
import com.staymate.service.AppUserService;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
	
	private final AppUserService appUserService;
	
	
	public AdminUserController(AppUserService appUserService) {
		super();
		this.appUserService = appUserService;
	}

	
	@GetMapping
	public  ResponseEntity<List<AdminResponseDTO>>  getAllUsers(){
		List<AdminResponseDTO> response = appUserService.getAllUsers();
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<AdminResponseDTO> getUserById(@PathVariable Long userId) {
		 AdminResponseDTO response = appUserService.getUserById(userId);
		 
		 return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PatchMapping("/{userId}/role")
	public ResponseEntity<AdminResponseDTO> updateUserRole(@PathVariable Long userId,
															@RequestBody UpdatedRoleRequestDTO  roleRequestDTO){
		AdminResponseDTO response = appUserService.updateUserRole(userId,roleRequestDTO);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Long userId){
		String response = appUserService.deleteUser(userId);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
}
