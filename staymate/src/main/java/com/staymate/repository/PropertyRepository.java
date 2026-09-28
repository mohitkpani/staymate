package com.staymate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.staymate.entity.Property;
import com.staymate.entity.PropertyType;

public interface PropertyRepository extends JpaRepository<Property, Long>{
	
	
	@Query("""
	        SELECT p FROM Property p
	        WHERE (:city IS NULL OR LOWER(p.city) = LOWER(:city))
	        AND (:propertyType IS NULL OR p.propertyType = :propertyType)
	        AND (:minRent IS NULL OR p.rent >= :minRent)
	        AND (:maxRent IS NULL OR p.rent <= :maxRent)
	        """)
	List<Property> searchProperties(
	        @Param("city") String city,
	        @Param("propertyType") PropertyType propertyType,
	        @Param("minRent") Double minRent,
	        @Param("maxRent") Double maxRent
	);
	
	List<Property> findByAvailableRoomsGreaterThan(Integer rooms);
	
	List<Property> findByOwnerEmail(String email);
}
