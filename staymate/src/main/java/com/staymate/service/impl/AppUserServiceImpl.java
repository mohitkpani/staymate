package com.staymate.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.staymate.dto.AdminResponseDTO;
import com.staymate.dto.LoginRequestDTO;
import com.staymate.dto.LoginResponseDTO;
import com.staymate.dto.ProfileResponseDTO;
import com.staymate.dto.ProfileUpdateDTO;
import com.staymate.dto.RegisterRequestDTO;
import com.staymate.dto.UpdatedRoleRequestDTO;
import com.staymate.entity.AppUser;
import com.staymate.entity.Role;
import com.staymate.exception.EmailAlreadyExistsException;
import com.staymate.exception.InvalidCredentialsException;
import com.staymate.exception.PhoneAlreadyExistsException;
import com.staymate.exception.ProfileImageException;
import com.staymate.exception.UserNotFoundException;
import com.staymate.repository.AppUserRepository;
import com.staymate.security.JwtService;
import com.staymate.service.AppUserService;
import com.staymate.service.EmailService;

@Service
public class AppUserServiceImpl implements AppUserService {
	
	private final AppUserRepository appUserRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final EmailService emailService;
	private final Cloudinary cloudinary;
	

	

	public AppUserServiceImpl(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder,
			JwtService jwtService, EmailService emailService, Cloudinary cloudinary) {
		super();
		this.appUserRepository = appUserRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.emailService = emailService;
		this.cloudinary = cloudinary;
	}

	@Override
	public AppUser registerUser(RegisterRequestDTO requestDTO) {

	    if (appUserRepository.existsByEmail(requestDTO.email())) {
	        throw new EmailAlreadyExistsException("Email already registered.");
	    }

	    if (appUserRepository.existsByPhone(requestDTO.phone())) {
	        throw new PhoneAlreadyExistsException("Phone number already registered.");
	    }

	    AppUser appUser = new AppUser();

	    appUser.setName(requestDTO.name());
	    appUser.setEmail(requestDTO.email());
	    appUser.setPhone(requestDTO.phone());
	    appUser.setPassword(passwordEncoder.encode(requestDTO.password()));
	    appUser.setAuthProvider("LOCAL");
	    Role selectedRole = requestDTO.role();

	    if (selectedRole == null || selectedRole == Role.ADMIN) {
	        throw new IllegalArgumentException(
	            "Please select either TENANT or OWNER."
	        );
	    }

	    appUser.setRole(selectedRole);

	    AppUser savedUser =
	        appUserRepository.save(appUser);

	    emailService.sendWelcomeEmail(
	        savedUser.getEmail(),
	        savedUser.getName()
	    );

	    return savedUser;
	}
	
	@Override
	public LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {
	    AppUser user = appUserRepository.findByEmail(loginRequestDTO.email())
	            .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

	    if (user.getPassword() == null) {
	        throw new InvalidCredentialsException("This account uses Google login. Please login with Google");
	    }

	    if (!passwordEncoder.matches(loginRequestDTO.password(), user.getPassword())) {
	        throw new InvalidCredentialsException("Invalid email or password");
	    }

	    String token = jwtService.generateToken(user.getEmail());

	    return new LoginResponseDTO(
	            "User logged in successfully",
	            token,
	            user.getRole().name()
	    );
	}
	
	@Override
	public ProfileResponseDTO getProfile(String email) {
		AppUser user = appUserRepository.findByEmail(email)
		.orElseThrow(() -> new InvalidCredentialsException("user not found"));
		
		return new ProfileResponseDTO(
	            user.getId(),
	            user.getName(),
	            user.getEmail(),
	            user.getPhone(),
	            user.getRole(),
	            user.getProfileImageUrl()
	    );
	}
	
	@Override
	public ProfileResponseDTO uploadProfileImage(
	        String email,
	        MultipartFile file) {

	    try {

	        AppUser user = appUserRepository.findByEmail(email)
	                .orElseThrow(() ->
	                        new UserNotFoundException("User not found"));

	        if (file == null || file.isEmpty()) {
	            throw new ProfileImageException("Profile image is required");
	        }

	        Map<String, Object> uploadResult = cloudinary.uploader()
	                .upload(
	                        file.getBytes(),
	                        ObjectUtils.asMap(
	                                "folder", "staymate/profile-images"
	                        )
	                );

	        String imageUrl = (String) uploadResult.get("secure_url");

	        user.setProfileImageUrl(imageUrl);

	        AppUser updatedUser = appUserRepository.save(user);

	        return new ProfileResponseDTO(
	                updatedUser.getId(),
	                updatedUser.getName(),
	                updatedUser.getEmail(),
	                updatedUser.getPhone(),
	                updatedUser.getRole(),
	                updatedUser.getProfileImageUrl()
	        );

	    } catch (Exception e) {
	        throw new RuntimeException(
	                "Failed to upload profile image: " + e.getMessage());
	    }
	}
	
	@Override
	 public ProfileResponseDTO updateProfile(String email, ProfileUpdateDTO profileUpdateDTO) {
		AppUser user = appUserRepository.findByEmail(email)
		.orElseThrow(() -> new UserNotFoundException("User not found"));
		
		user.setName(profileUpdateDTO.name());
		user.setPhone(profileUpdateDTO.phone());
		
		AppUser updatedUser = appUserRepository.save(user);
		
		return new ProfileResponseDTO(
				updatedUser.getId(),
				updatedUser.getName(),
				updatedUser.getEmail(),
				updatedUser.getPhone(),
				updatedUser.getRole(),
				updatedUser.getProfileImageUrl()
				);
	}
	
	@Override
	public List<AdminResponseDTO> getAllUsers(){
		List<AppUser> users = appUserRepository.findAll();
		
		return users.stream()
					.map(user -> new AdminResponseDTO(
							user.getId(),
							user.getName(),
							user.getEmail(),
							user.getPhone(),
							user.getRole()
							)).toList();
	}
	
	@Override
	public AdminResponseDTO getUserById(Long id){
		AppUser user = appUserRepository.findById(id)
		.orElseThrow(() -> new UserNotFoundException("User not found"));
		
		return new AdminResponseDTO(
				user.getId(),
				user.getName(),
				user.getEmail(),
				user.getPhone(),
				user.getRole()
				);
	}
	
	@Override
	public AdminResponseDTO updateUserRole(Long userId, UpdatedRoleRequestDTO roleRequestDTO) {
		AppUser user = appUserRepository.findById(userId).
		orElseThrow(() -> new UserNotFoundException("User not found"));
		
		user.setRole(roleRequestDTO.role());
		
		AppUser updatedUser = appUserRepository.save(user);
		
		return new AdminResponseDTO(
				updatedUser.getId(),
				updatedUser.getName(),
				updatedUser.getEmail(),
				updatedUser.getPhone(),
				updatedUser.getRole()
				);
		
	}
	
	@Override
	public String deleteUser(Long userId) {
		 AppUser user = appUserRepository.findById(userId)
		.orElseThrow(() -> new UserNotFoundException("User not found"));
		 
		 appUserRepository.delete(user);
		 return "User deleted successfully";
	}
	
	
	@Override
	public LoginResponseDTO completeGoogleRegistration(
	        String email,
	        String name,
	        String role) {

	    AppUser user = new AppUser();

	    user.setName(name);
	    user.setEmail(email);
	    user.setPhone(null);
	    user.setPassword(null);
	    user.setAuthProvider("GOOGLE");

	    Role selectedRole;

	    try {
	        selectedRole = Role.valueOf(role.toUpperCase());
	    } catch (Exception e) {
	        throw new IllegalArgumentException("Invalid role selected");
	    }

	    if (selectedRole != Role.TENANT && selectedRole != Role.OWNER) {
	        throw new IllegalArgumentException(
	                "Please select either TENANT or OWNER."
	        );
	    }

	    user.setRole(selectedRole);

	    AppUser savedUser = appUserRepository.save(user);

	    // Send welcome email after successful Google registration
	    emailService.sendWelcomeEmail(
	            savedUser.getEmail(),
	            savedUser.getName()
	    );

	    String token = jwtService.generateToken(savedUser.getEmail());

	    return new LoginResponseDTO(
	            "Google registration successful",
	            token,
	            savedUser.getRole().name()
	    );
	}
	

}
