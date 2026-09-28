package com.staymate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "properties")
public class Property {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, length = 1000)
	private String description;

	@Column(nullable = false)
	private String location;

	@Column(nullable = false)
	private String city;
	
	private Double latitude;
	private Double longitude;

	@Column(nullable = false)
	private Double rent;

	@Column(nullable = false)
	private Double securityDeposit;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PropertyType propertyType;

	@Column(nullable = false)
	private Integer availableRooms;

	@Column(nullable = false)
	private String amenities;

	@ManyToOne
	@JoinColumn(name = "owner_id", nullable = false)
	private AppUser owner;

	public Property() {
		super();
	}

	public Property(Long id, String title, String description, String location, String city, Double rent,
			Double securityDeposit, PropertyType propertyType, Integer availableRooms, String amenities,
			AppUser owner) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.location = location;
		this.city = city;
		this.rent = rent;
		this.securityDeposit = securityDeposit;
		this.propertyType = propertyType;
		this.availableRooms = availableRooms;
		this.amenities = amenities;
		this.owner = owner;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Double getRent() {
		return rent;
	}

	public void setRent(Double rent) {
		this.rent = rent;
	}

	public Double getSecurityDeposit() {
		return securityDeposit;
	}

	public void setSecurityDeposit(Double securityDeposit) {
		this.securityDeposit = securityDeposit;
	}

	public PropertyType getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(PropertyType propertyType) {
		this.propertyType = propertyType;
	}

	public Integer getAvailableRooms() {
		return availableRooms;
	}

	public void setAvailableRooms(Integer availableRooms) {
		this.availableRooms = availableRooms;
	}

	public String getAmenities() {
		return amenities;
	}

	public void setAmenities(String amenities) {
		this.amenities = amenities;
	}

	public AppUser getOwner() {
		return owner;
	}

	public void setOwner(AppUser owner) {
		this.owner = owner;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "Property [id=" + id + ", title=" + title + ", description=" + description + ", location=" + location
				+ ", city=" + city + ", rent=" + rent + ", securityDeposit=" + securityDeposit + ", propertyType="
				+ propertyType + ", availableRooms=" + availableRooms + ", amenities=" + amenities + ", owner=" + owner
				+ "]";
	}
	
	
	
}
