package com.staymate.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import com.staymate.dto.PropertyRequestDTO;
import com.staymate.dto.PropertyResponseDTO;
import com.staymate.dto.PropertyUpdateDTO;
import com.staymate.entity.AppUser;
import com.staymate.entity.Property;
import com.staymate.entity.PropertyImage;
import com.staymate.entity.PropertyType;
import com.staymate.exception.PropertyNotFoundException;
import com.staymate.exception.UnauthorizedPropertyException;
import com.staymate.exception.UserNotFoundException;
import com.staymate.repository.AppUserRepository;
import com.staymate.repository.BookingRepository;
import com.staymate.repository.PropertyImageRepository;
import com.staymate.repository.PropertyRepository;
import com.staymate.service.PropertyService;

@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final AppUserRepository appUserRepository;
    private final PropertyImageRepository propertyImageRepository;
    private final BookingRepository bookingRepository;
  

	public PropertyServiceImpl(PropertyRepository propertyRepository, AppUserRepository appUserRepository,
			PropertyImageRepository propertyImageRepository, BookingRepository bookingRepository) {
		super();
		this.propertyRepository = propertyRepository;
		this.appUserRepository = appUserRepository;
		this.propertyImageRepository = propertyImageRepository;
		this.bookingRepository = bookingRepository;
	}

	@Override
    public PropertyResponseDTO createProperty(PropertyRequestDTO propertyRequestDTO, String ownerEmail) {

        AppUser owner = appUserRepository.findByEmail(ownerEmail)
                .orElseThrow(() ->
                        new UserNotFoundException("Owner not found"));

        Property property = new Property();

        property.setTitle(propertyRequestDTO.title());
        property.setDescription(propertyRequestDTO.description());
        property.setLocation(propertyRequestDTO.location());
        property.setCity(propertyRequestDTO.city());

        // Google Maps coordinates
        property.setLatitude(propertyRequestDTO.latitude());
        property.setLongitude(propertyRequestDTO.longitude());

        property.setRent(propertyRequestDTO.rent());
        property.setSecurityDeposit(propertyRequestDTO.securityDeposit());
        property.setPropertyType(propertyRequestDTO.propertyType());
        property.setAvailableRooms(propertyRequestDTO.availableRooms());
        property.setAmenities(propertyRequestDTO.amenities());
        property.setOwner(owner);

        Property savedProperty = propertyRepository.save(property);

        return new PropertyResponseDTO(
                savedProperty.getId(),
                savedProperty.getTitle(),
                savedProperty.getDescription(),
                savedProperty.getLocation(),
                savedProperty.getCity(),
                savedProperty.getLatitude(),
                savedProperty.getLongitude(),
                savedProperty.getRent(),
                savedProperty.getSecurityDeposit(),
                savedProperty.getPropertyType(),
                savedProperty.getAvailableRooms(),
                savedProperty.getAmenities(),
                savedProperty.getOwner().getId(),
                savedProperty.getOwner().getName()
        );
    }

    @Override
    public List<PropertyResponseDTO> getAllProperties() {

        List<Property> properties = propertyRepository.findAll();

        return properties.stream()
                .map(property -> new PropertyResponseDTO(
                        property.getId(),
                        property.getTitle(),
                        property.getDescription(),
                        property.getLocation(),
                        property.getCity(),
                        property.getLatitude(),
                        property.getLongitude(),
                        property.getRent(),
                        property.getSecurityDeposit(),
                        property.getPropertyType(),
                        property.getAvailableRooms(),
                        property.getAmenities(),
                        property.getOwner().getId(),
                        property.getOwner().getName()
                ))
                .toList();
    }

    @Override
    public PropertyResponseDTO getPropertyById(Long propertyId) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() ->
                        new PropertyNotFoundException("Property not found."));

        return new PropertyResponseDTO(
                property.getId(),
                property.getTitle(),
                property.getDescription(),
                property.getLocation(),
                property.getCity(),
                property.getLatitude(),
                property.getLongitude(),
                property.getRent(),
                property.getSecurityDeposit(),
                property.getPropertyType(),
                property.getAvailableRooms(),
                property.getAmenities(),
                property.getOwner().getId(),
                property.getOwner().getName()
        );
    }

    @Override
    public List<PropertyResponseDTO> searchProperties(String city, PropertyType propertyType, Double minRent, Double maxRent) {

        List<Property> properties =
                propertyRepository.searchProperties(
                        city,
                        propertyType,
                        minRent,
                        maxRent);

        return properties.stream()
                .map(property -> new PropertyResponseDTO(
                        property.getId(),
                        property.getTitle(),
                        property.getDescription(),
                        property.getLocation(),
                        property.getCity(),
                        property.getLatitude(),
                        property.getLongitude(),
                        property.getRent(),
                        property.getSecurityDeposit(),
                        property.getPropertyType(),
                        property.getAvailableRooms(),
                        property.getAmenities(),
                        property.getOwner().getId(),
                        property.getOwner().getName()
                ))
                .toList();
    }

    @Override
    public List<PropertyResponseDTO> findByAvailableRoomsGreaterThan(Integer rooms) {

        List<Property> properties =
                propertyRepository.findByAvailableRoomsGreaterThan(rooms);

        return properties.stream()
                .map(property -> new PropertyResponseDTO(
                        property.getId(),
                        property.getTitle(),
                        property.getDescription(),
                        property.getLocation(),
                        property.getCity(),
                        property.getLatitude(),
                        property.getLongitude(),
                        property.getRent(),
                        property.getSecurityDeposit(),
                        property.getPropertyType(),
                        property.getAvailableRooms(),
                        property.getAmenities(),
                        property.getOwner().getId(),
                        property.getOwner().getName()
                ))
                .toList();
    }

    @Override
    public PropertyResponseDTO updateProperty(Long propertyId, PropertyRequestDTO propertyRequestDTO, String ownerEmail) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() ->
                        new PropertyNotFoundException("Property not found"));

        if (!property.getOwner().getEmail().equals(ownerEmail)) {
            throw new UnauthorizedPropertyException(
                    "You are not the owner of this property");
        }

        property.setTitle(propertyRequestDTO.title());
        property.setDescription(propertyRequestDTO.description());
        property.setLocation(propertyRequestDTO.location());
        property.setCity(propertyRequestDTO.city());

        // Google Maps coordinates
        property.setLatitude(propertyRequestDTO.latitude());
        property.setLongitude(propertyRequestDTO.longitude());

        property.setRent(propertyRequestDTO.rent());
        property.setSecurityDeposit(propertyRequestDTO.securityDeposit());
        property.setPropertyType(propertyRequestDTO.propertyType());
        property.setAvailableRooms(propertyRequestDTO.availableRooms());
        property.setAmenities(propertyRequestDTO.amenities());

        Property updatedProperty = propertyRepository.save(property);

        return new PropertyResponseDTO(
                updatedProperty.getId(),
                updatedProperty.getTitle(),
                updatedProperty.getDescription(),
                updatedProperty.getLocation(),
                updatedProperty.getCity(),
                updatedProperty.getLatitude(),
                updatedProperty.getLongitude(),
                updatedProperty.getRent(),
                updatedProperty.getSecurityDeposit(),
                updatedProperty.getPropertyType(),
                updatedProperty.getAvailableRooms(),
                updatedProperty.getAmenities(),
                updatedProperty.getOwner().getId(),
                updatedProperty.getOwner().getName()
        );
    }

    @Override
    public String deleteProperty(Long propertyId, String ownerEmail) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() ->
                        new PropertyNotFoundException("Property not found"));

        if (!property.getOwner().getEmail().equals(ownerEmail)) {
            throw new UnauthorizedPropertyException(
                    "You are not the owner of this property");
        }

        propertyRepository.delete(property);

        return "Property deleted successfully";
    }

    @Override
    public PropertyResponseDTO updatePropertyPartially(long propertyId, PropertyUpdateDTO propertyUpdateDTO,  String ownerEmail) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() ->
                        new PropertyNotFoundException("Property not found"));

        if (!property.getOwner().getEmail().equals(ownerEmail)) {
            throw new RuntimeException(
                    "You are not the owner of this property");
        }

        if (propertyUpdateDTO.title() != null) {
            property.setTitle(propertyUpdateDTO.title());
        }

        if (propertyUpdateDTO.description() != null) {
            property.setDescription(propertyUpdateDTO.description());
        }

        if (propertyUpdateDTO.location() != null) {
            property.setLocation(propertyUpdateDTO.location());
        }

        if (propertyUpdateDTO.city() != null) {
            property.setCity(propertyUpdateDTO.city());
        }

        // Google Maps coordinates
        if (propertyUpdateDTO.latitude() != null) {
            property.setLatitude(propertyUpdateDTO.latitude());
        }

        if (propertyUpdateDTO.longitude() != null) {
            property.setLongitude(propertyUpdateDTO.longitude());
        }

        if (propertyUpdateDTO.rent() != null) {
            property.setRent(propertyUpdateDTO.rent());
        }

        if (propertyUpdateDTO.securityDeposit() != null) {
            property.setSecurityDeposit(
                    propertyUpdateDTO.securityDeposit());
        }

        if (propertyUpdateDTO.propertyType() != null) {
            property.setPropertyType(
                    propertyUpdateDTO.propertyType());
        }

        if (propertyUpdateDTO.availableRooms() != null) {
            property.setAvailableRooms(
                    propertyUpdateDTO.availableRooms());
        }

        if (propertyUpdateDTO.amenities() != null) {
            property.setAmenities(
                    propertyUpdateDTO.amenities());
        }

        Property updatedProperty =
                propertyRepository.save(property);

        return new PropertyResponseDTO(
                updatedProperty.getId(),
                updatedProperty.getTitle(),
                updatedProperty.getDescription(),
                updatedProperty.getLocation(),
                updatedProperty.getCity(),
                updatedProperty.getLatitude(),
                updatedProperty.getLongitude(),
                updatedProperty.getRent(),
                updatedProperty.getSecurityDeposit(),
                updatedProperty.getPropertyType(),
                updatedProperty.getAvailableRooms(),
                updatedProperty.getAmenities(),
                updatedProperty.getOwner().getId(),
                updatedProperty.getOwner().getName()
        );
    }
    
    @Override
    public String deleteProperty(Long propertyId) {
    	Property property = propertyRepository.findById(propertyId)
    	.orElseThrow(() -> new PropertyNotFoundException("Property not found"));
    	
    	if(bookingRepository.existsBypropertyId(propertyId)) {
    		return "Cannot delete property because bookings exist";
    	}
    	
    	List<PropertyImage> images = propertyImageRepository.findByPropertyId(propertyId);
    	
    	propertyImageRepository.deleteAll(images);
    	propertyRepository.delete(property);
    	
    	return "Property deleted successfully";
    }
    
    @Override
    public List<PropertyResponseDTO> getPropertiesByOwner(String ownerEmail) {

        List<Property> properties = propertyRepository.findByOwnerEmail(ownerEmail);

        return properties.stream()
                .map(property -> new PropertyResponseDTO(
                        property.getId(),
                        property.getTitle(),
                        property.getDescription(),
                        property.getLocation(),
                        property.getCity(),
                        property.getLatitude(),
                        property.getLongitude(),
                        property.getRent(),
                        property.getSecurityDeposit(),
                        property.getPropertyType(),
                        property.getAvailableRooms(),
                        property.getAmenities(),
                        property.getOwner().getId(),
                        property.getOwner().getName()
                ))
                .toList();
    }
}