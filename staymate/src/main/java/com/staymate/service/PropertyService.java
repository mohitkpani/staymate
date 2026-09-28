package com.staymate.service;

import java.util.List;

import com.staymate.dto.PropertyRequestDTO;
import com.staymate.dto.PropertyResponseDTO;
import com.staymate.dto.PropertyUpdateDTO;
import com.staymate.entity.PropertyType;

public interface PropertyService {
	
	PropertyResponseDTO createProperty(PropertyRequestDTO propertyRequestDTO, String ownerEmail);
	
	List<PropertyResponseDTO> getAllProperties();
	
	PropertyResponseDTO getPropertyById(Long propertyId);
	
	List<PropertyResponseDTO> searchProperties(String city, PropertyType propertyType, Double minRent, Double maxRent);
	
	List<PropertyResponseDTO> findByAvailableRoomsGreaterThan(Integer rooms);
	
	PropertyResponseDTO updateProperty(Long propertyId, PropertyRequestDTO propertyRequestDTO, String ownerEmail);
	
	String deleteProperty(Long propertyId, String ownerEmail);
	
	PropertyResponseDTO updatePropertyPartially(long propertyId, PropertyUpdateDTO propertyUpdateDTO, String ownerEmail);
	
	String deleteProperty(Long propertyId);
	
	List<PropertyResponseDTO> getPropertiesByOwner(String ownerEmail);
}
