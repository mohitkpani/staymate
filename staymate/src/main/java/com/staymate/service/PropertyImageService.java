package com.staymate.service;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.staymate.dto.PropertyImageResponseDTO;

public interface PropertyImageService {
	
	 PropertyImageResponseDTO uploadImage(Long propertyId, MultipartFile file, String ownerEmail);
	
	 List<PropertyImageResponseDTO> getImagesByPropertyId(Long propertyId);
	 
	 String deleteImage(Long imageId, String ownerEmail);

}
