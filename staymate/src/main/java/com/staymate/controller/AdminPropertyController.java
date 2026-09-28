package com.staymate.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staymate.dto.PropertyResponseDTO;
import com.staymate.service.PropertyService;

@RestController
@RequestMapping("/api/admin/properties")
public class AdminPropertyController {
	
	private final PropertyService propertyService;

	public AdminPropertyController(PropertyService propertyService) {
		super();
		this.propertyService = propertyService;
	}
	
	
	@GetMapping()
	public ResponseEntity<List<PropertyResponseDTO>> getAllProperties(){
		List<PropertyResponseDTO> response = propertyService.getAllProperties();
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/{propertyId}")
	public ResponseEntity<PropertyResponseDTO> getPropertyById(@PathVariable Long propertyId) {
		PropertyResponseDTO response = propertyService.getPropertyById(propertyId);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/{propertyId}")
	public ResponseEntity<String> deleteProperty(@PathVariable Long propertyId){
		String response = propertyService.deleteProperty(propertyId);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	

}
