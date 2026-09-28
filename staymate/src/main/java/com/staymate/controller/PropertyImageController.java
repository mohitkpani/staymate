package com.staymate.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.staymate.dto.PropertyImageResponseDTO;
import com.staymate.service.PropertyImageService;

@RestController
@RequestMapping("/api/properties")
public class PropertyImageController {

    private final PropertyImageService propertyImageService;

    public PropertyImageController(PropertyImageService propertyImageService) {
        super();
        this.propertyImageService = propertyImageService;
    }

    @PostMapping("/images/{propertyId}")
    public ResponseEntity<PropertyImageResponseDTO> uploadImage(@PathVariable Long propertyId, @RequestParam("file") MultipartFile file, Authentication authentication) {

        String ownerEmail = authentication.getName();

        PropertyImageResponseDTO response =
                propertyImageService.uploadImage(
                        propertyId,
                        file,
                        ownerEmail
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }
    
    @GetMapping("/images/{propertyId}")
    public ResponseEntity<List<PropertyImageResponseDTO>> getImagesByPropertyId(@PathVariable Long propertyId){
    	
    	 List<PropertyImageResponseDTO> response = propertyImageService.getImagesByPropertyId(propertyId);
    	 
    	 return new ResponseEntity<>(response, HttpStatus.OK);
    	
    }
    
    @DeleteMapping("/image/{imageId}")
    public ResponseEntity<String> deleteImage(@PathVariable Long imageId, Authentication authentication) {
    	
    	String ownerEmail = authentication.getName();
    	String response = propertyImageService.deleteImage(imageId, ownerEmail);
    	
    	return new ResponseEntity<>(response, HttpStatus.OK);
    }
}