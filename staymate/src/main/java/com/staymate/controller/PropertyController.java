package com.staymate.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.staymate.dto.PropertyRequestDTO;
import com.staymate.dto.PropertyResponseDTO;
import com.staymate.dto.PropertyUpdateDTO;
import com.staymate.entity.PropertyType;
import com.staymate.service.PropertyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {
		
	private final PropertyService propertyService;

	public PropertyController(PropertyService propertyService) {
		super();
		this.propertyService = propertyService;
	}
	
	
	@PostMapping()
	public ResponseEntity<PropertyResponseDTO> createProperty(@RequestBody @Valid 
			PropertyRequestDTO propertyRequestDTO, Authentication authentication){
		
		 String ownerEmail = authentication.getName();
		PropertyResponseDTO response = propertyService.createProperty(propertyRequestDTO, ownerEmail);
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
		 
	}
	
	@GetMapping()
	public ResponseEntity<List<PropertyResponseDTO>> getAllProperties(){
			
			 List<PropertyResponseDTO> response = propertyService.getAllProperties();
			 
			 return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	@GetMapping("/{propertyId}")
	public ResponseEntity<PropertyResponseDTO> getPropertyById(@PathVariable Long propertyId){
			
			PropertyResponseDTO response = propertyService.getPropertyById(propertyId);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	
	@GetMapping("/search/filter")
	public ResponseEntity<List<PropertyResponseDTO>> searchProperties(
									@RequestParam(required = false)	String city, 
									@RequestParam(required = false)	PropertyType propertyType, 
									@RequestParam(required = false) Double minRent, 
									@RequestParam(required = false) Double maxRent){
		
		 List<PropertyResponseDTO> response = propertyService.searchProperties(city, propertyType, minRent, maxRent);
		 
		 return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/available")
	public ResponseEntity<List<PropertyResponseDTO>> findByAvailableRoomsGreaterThan(@RequestParam Integer rooms){
			 List<PropertyResponseDTO> response = propertyService.findByAvailableRoomsGreaterThan(rooms);
			 
			 return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PutMapping("/{propertyId}")
	public ResponseEntity<PropertyResponseDTO> updateProperty(
								@PathVariable Long propertyId, 
								@RequestBody @Valid PropertyRequestDTO propertyRequestDTO, 
								Authentication authentication) {
		
			String ownerEmail = authentication.getName();
			
			PropertyResponseDTO response = propertyService.updateProperty(propertyId, propertyRequestDTO, ownerEmail);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/{propertyId}")
	public ResponseEntity<String> deleteProperty(@PathVariable Long propertyId, Authentication authentication){
		
		String ownerEmail = authentication.getName();
		
		String response = propertyService.deleteProperty(propertyId, ownerEmail);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PatchMapping("/{propertyId}")
	public ResponseEntity<PropertyResponseDTO> updatePropertyPartially(
								@PathVariable Long propertyId, 
								@RequestBody PropertyUpdateDTO propertyUpdateDTO, 
								Authentication authentication){
			
		String email = authentication.getName();
		PropertyResponseDTO response = propertyService.updatePropertyPartially(propertyId, propertyUpdateDTO, email);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/my-properties")
    public ResponseEntity<List<PropertyResponseDTO>> getMyProperties(
            Authentication authentication) {

        String ownerEmail = getAuthenticatedEmail(authentication);

        List<PropertyResponseDTO> response =
                propertyService.getPropertiesByOwner(ownerEmail);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private String getAuthenticatedEmail(Authentication authentication) {

        if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {

            return oauth2User.getAttribute("email");

        }

        return authentication.getName();
    }
	
}
