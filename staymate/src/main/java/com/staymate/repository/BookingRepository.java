package com.staymate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.staymate.entity.AppUser;
import com.staymate.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByAppUser(AppUser appUser);

    Optional<Booking> findByRazorpayOrderId(String razorpayOrderId);
    
    boolean existsBypropertyId(Long propertyId);
    
    List<Booking> findByPropertyOwner(AppUser appUser);
    
    Optional<Booking> findByIdAndPropertyOwner(Long bookingId, AppUser owner);
}