package com.staymate.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.staymate.dto.LoginRequestDTO;
import com.staymate.dto.LoginResponseDTO;
import com.staymate.dto.ProfileResponseDTO;
import com.staymate.dto.ProfileUpdateDTO;
import com.staymate.dto.RegisterRequestDTO;
import com.staymate.dto.RegisterResponseDTO;
import com.staymate.service.AppUserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class AppUserController {
	
	private final AppUserService appUserService;

	public AppUserController(AppUserService appUserService) {
		super();
		this.appUserService = appUserService;
	}
	
	
	@PostMapping("/register")
	public ResponseEntity<RegisterResponseDTO> registerUser(@RequestBody @Valid RegisterRequestDTO requestDTO){
		 appUserService.registerUser(requestDTO);
		 
		 return new ResponseEntity<>(
				 new RegisterResponseDTO("User registered successfully."), HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody @Valid LoginRequestDTO loginRequestDTO){
		LoginResponseDTO response = appUserService.loginUser(loginRequestDTO);
		
		return new ResponseEntity<>(response , HttpStatus.OK);
	}
	
	@GetMapping("/profile")
	public ResponseEntity<ProfileResponseDTO> profile(Authentication authentication) {

	    String email = authentication.getName();
	    ProfileResponseDTO response = appUserService.getProfile(email);

	    return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@PutMapping("/profile")
	public ResponseEntity<ProfileResponseDTO> updateProfile(
									Authentication authentication,
									@RequestBody @Valid ProfileUpdateDTO profileUpdateDTO 
									){
		
		String email = authentication.getName();
		ProfileResponseDTO response = appUserService.updateProfile(email, profileUpdateDTO);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	@PostMapping("/profile/image")
	public ResponseEntity<ProfileResponseDTO> uploadProfileImage(
	        						Authentication authentication,
	        						@RequestParam("file") MultipartFile file) {

	    String email = authentication.getName();
	    ProfileResponseDTO response = appUserService.uploadProfileImage(email, file);

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/google/complete")
	public ResponseEntity<LoginResponseDTO> completeGoogleRegistration(
	        						@RequestBody Map<String, String> request,
	        						HttpSession session) {

	    String email = (String) session.getAttribute("googleEmail");
	    String name = (String) session.getAttribute("googleName");
	    String role = request.get("role");

	    if (email == null || name == null) {
	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	    }

	    LoginResponseDTO response = appUserService.completeGoogleRegistration(email,name,role);

	    session.removeAttribute("googleEmail");
	    session.removeAttribute("googleName");

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	

}
