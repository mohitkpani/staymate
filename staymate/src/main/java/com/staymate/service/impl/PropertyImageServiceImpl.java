package com.staymate.service.impl;

import java.io.IOException;
import java.util.Map;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.staymate.dto.PropertyImageResponseDTO;
import com.staymate.entity.Property;
import com.staymate.entity.PropertyImage;
import com.staymate.exception.InvalidCredentialsException;
import com.staymate.exception.PropertyImageNotFoundException;
import com.staymate.exception.PropertyNotFoundException;
import com.staymate.exception.UnauthorizedPropertyException;
import com.staymate.repository.PropertyImageRepository;
import com.staymate.repository.PropertyRepository;
import com.staymate.service.PropertyImageService;

@Service
public class PropertyImageServiceImpl implements PropertyImageService {

    private final PropertyImageRepository propertyImageRepository;
    private final PropertyRepository propertyRepository;
    private final Cloudinary cloudinary;

    public PropertyImageServiceImpl(
            PropertyImageRepository propertyImageRepository,
            PropertyRepository propertyRepository,
            Cloudinary cloudinary) {

        this.propertyImageRepository = propertyImageRepository;
        this.propertyRepository = propertyRepository;
        this.cloudinary = cloudinary;
    }

    @Override
    public PropertyImageResponseDTO uploadImage(Long propertyId, MultipartFile file, String ownerEmail) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() ->
                        new PropertyNotFoundException("Property not found"));

        if (!property.getOwner().getEmail().equals(ownerEmail)) {
            throw new UnauthorizedPropertyException(
                    "You are not the owner of this property");
        }

        try {

            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "staymate/properties"
                    )
            );

            String imageUrl = uploadResult
                    .get("secure_url")
                    .toString();

            PropertyImage propertyImage = new PropertyImage();

            propertyImage.setImageUrl(imageUrl);
            propertyImage.setProperty(property);

            PropertyImage savedImage =
                    propertyImageRepository.save(propertyImage);

            return new PropertyImageResponseDTO(
                    savedImage.getId(),
                    property.getId(),
                    savedImage.getImageUrl()
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to upload image to Cloudinary", e);
        }
    }
    
    @Override
    public List<PropertyImageResponseDTO> getImagesByPropertyId(Long propertyId){
    	
    		List<PropertyImage> images = propertyImageRepository.findByPropertyId(propertyId);
    									 
    		return images.stream().map(image -> new PropertyImageResponseDTO(
    						image.getId(),
    						image.getProperty().getId(),
    						image.getImageUrl()
    				)).toList();
    								
    }
    
    @Override
    public String deleteImage(Long imageId, String ownerEmail) {

        PropertyImage image = propertyImageRepository.findById(imageId)
                .orElseThrow(() ->
                        new PropertyImageNotFoundException("Image not found"));

        Property property = image.getProperty();

        if (!property.getOwner().getEmail().equals(ownerEmail)) {
            throw new InvalidCredentialsException(
                    "You are not the owner of this property");
        }

        try {
            String imageUrl = image.getImageUrl();

            // Extract Cloudinary public ID from the URL
            String publicId = imageUrl
                    .substring(
                            imageUrl.indexOf("/staymate/properties/") 
                            + "/staymate/properties/".length()
                    )
                    .replaceFirst("\\.[^.]+$", "");

            publicId = "staymate/properties/" + publicId;

            cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.emptyMap()
            );

            propertyImageRepository.delete(image);

            return "Image deleted successfully";

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to delete image", e);
        }
    }
}