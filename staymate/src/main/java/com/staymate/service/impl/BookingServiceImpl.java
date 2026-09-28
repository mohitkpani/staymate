package com.staymate.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import com.staymate.dto.BookingRequestDTO;
import com.staymate.dto.BookingResponseDTO;
import com.staymate.entity.AppUser;
import com.staymate.entity.Booking;
import com.staymate.entity.BookingStatus;
import com.staymate.entity.PaymentStatus;
import com.staymate.entity.Property;
import com.staymate.exception.BookingAlreadyCancelledException;
import com.staymate.exception.BookingNotFoundException;
import com.staymate.exception.InsufficientRoomsException;
import com.staymate.exception.PropertyNotFoundException;
import com.staymate.exception.UnauthorizedBookingException;
import com.staymate.exception.UserNotFoundException;
import com.staymate.repository.AppUserRepository;
import com.staymate.repository.BookingRepository;
import com.staymate.repository.PropertyRepository;
import com.staymate.service.BookingService;
import com.staymate.service.EmailService;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final AppUserRepository appUserRepository;
    private final PropertyRepository propertyRepository;
    private final EmailService emailService;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            AppUserRepository appUserRepository,
            PropertyRepository propertyRepository,
            EmailService emailService) {

        this.bookingRepository = bookingRepository;
        this.appUserRepository = appUserRepository;
        this.propertyRepository = propertyRepository;
        this.emailService = emailService;
    }

    // ============================================================
    // CREATE BOOKING
    // ============================================================

    @Transactional
    @Override
    public BookingResponseDTO createBooking(
            BookingRequestDTO bookingRequestDTO,
            String userEmail) {

        // 1. Find logged-in user
        AppUser user = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        // 2. Find property
        Property property = propertyRepository
                .findById(bookingRequestDTO.propertyId())
                .orElseThrow(() ->
                        new PropertyNotFoundException("Property not found"));

        // 3. Get requested rooms
        Integer requestedRooms = bookingRequestDTO.numberOfRooms();

        // 4. Check available rooms
        if (requestedRooms > property.getAvailableRooms()) {
            throw new InsufficientRoomsException(
                    "Only " + property.getAvailableRooms()
                            + " rooms are available");
        }

        // 5. Calculate total amount
        Double totalAmount =
                property.getRent() * requestedRooms;

        // 6. Reduce available rooms
        property.setAvailableRooms(
                property.getAvailableRooms() - requestedRooms
        );

        propertyRepository.save(property);

        // 7. Create booking
        Booking booking = new Booking();

        booking.setAppUser(user);
        booking.setProperty(property);
        booking.setNumberOfRooms(requestedRooms);
        booking.setBookingdate(LocalDateTime.now());
        booking.setTotalAmount(totalAmount);

        // Payment has not happened yet
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.PENDING);

        // 8. Save booking
        Booking savedBooking =
                bookingRepository.save(booking);

        // 9. Send booking email
        emailService.sendBookingConfirmationEmail(
                user.getEmail(),
                user.getName(),
                savedBooking.getId(),
                property.getTitle(),
                savedBooking.getTotalAmount()
        );

        // 10. Return response
        return mapToResponseDTO(savedBooking);
    }

    // ============================================================
    // GET BOOKING BY ID - USER
    // ============================================================

    @Override
    public BookingResponseDTO getBookingById(
            Long bookingId,
            String userEmail) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new BookingNotFoundException(
                                "Booking not found"));

        // Check ownership
        if (!booking.getAppUser()
                .getEmail()
                .equals(userEmail)) {

            throw new UnauthorizedBookingException(
                    "You are not authorized to view this booking");
        }

        return mapToResponseDTO(booking);
    }

    // ============================================================
    // GET MY BOOKINGS
    // ============================================================

    @Override
    public List<BookingResponseDTO> getMyBookings(
            String userEmail) {

        AppUser user = appUserRepository
                .findByEmail(userEmail)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"));

        return bookingRepository
                .findByAppUser(user)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // ============================================================
    // CANCEL BOOKING - USER
    // ============================================================

    @Transactional
    @Override
    public String cancelBooking(
            Long bookingId,
            String userEmail) {

        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() ->
                        new BookingNotFoundException(
                                "Booking not found"));

        // Check ownership
        if (!booking.getAppUser()
                .getEmail()
                .equals(userEmail)) {

            throw new UnauthorizedBookingException(
                    "You are not authorized to cancel this booking");
        }

        // Check booking status
        if (booking.getBookingStatus()
                == BookingStatus.CANCELLED) {

            throw new BookingAlreadyCancelledException(
                    "Booking is already cancelled");
        }

        // Get property
        Property property = booking.getProperty();

        // Restore rooms
        property.setAvailableRooms(
                property.getAvailableRooms()
                        + booking.getNumberOfRooms()
        );

        propertyRepository.save(property);

        // Cancel booking
        booking.setBookingStatus(
                BookingStatus.CANCELLED
        );

        bookingRepository.save(booking);

        // Send cancellation email
        emailService.sendBookingCancellationEmail(
                booking.getAppUser().getEmail(),
                booking.getAppUser().getName(),
                booking.getId(),
                property.getTitle()
        );

        return "Booking cancelled successfully";
    }

    // ============================================================
    // GET OWNER BOOKINGS
    // ============================================================

    @Override
    public List<BookingResponseDTO> getOwnerBookings(
            String ownerEmail) {

        AppUser owner = appUserRepository
                .findByEmail(ownerEmail)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Owner not found"));

        return bookingRepository
                .findByPropertyOwner(owner)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // ============================================================
    // GET OWNER BOOKING BY ID
    // ============================================================

    @Override
    public BookingResponseDTO getOwnerBookingById(
            Long bookingId,
            String ownerEmail) {

        AppUser owner = appUserRepository
                .findByEmail(ownerEmail)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Owner not found"));

        Booking booking = bookingRepository
                .findByIdAndPropertyOwner(bookingId, owner)
                .orElseThrow(() ->
                        new BookingNotFoundException(
                                "Booking not found"));

        return mapToResponseDTO(booking);
    }

    // ============================================================
    // CANCEL BOOKING - OWNER
    // ============================================================

    @Transactional
    @Override
    public String cancelOwnerBooking(
            Long bookingId,
            String ownerEmail) {

        // 1. Find owner
        AppUser owner = appUserRepository
                .findByEmail(ownerEmail)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Owner not found"));

        // 2. Find booking belonging to owner's property
        Booking booking = bookingRepository
                .findByIdAndPropertyOwner(bookingId, owner)
                .orElseThrow(() ->
                        new BookingNotFoundException(
                                "Booking not found"));

        // 3. Check booking status
        if (booking.getBookingStatus()
                == BookingStatus.CANCELLED) {

            throw new BookingAlreadyCancelledException(
                    "Booking already cancelled");
        }

        // 4. Get property
        Property property = booking.getProperty();

        // 5. Restore rooms
        property.setAvailableRooms(
                property.getAvailableRooms()
                        + booking.getNumberOfRooms()
        );

        propertyRepository.save(property);

        // 6. Cancel booking
        booking.setBookingStatus(
                BookingStatus.CANCELLED
        );

        bookingRepository.save(booking);

        // 7. Send cancellation email
        emailService.sendBookingCancellationEmail(
                booking.getAppUser().getEmail(),
                booking.getAppUser().getName(),
                booking.getId(),
                property.getTitle()
        );

        return "Booking cancelled successfully";
    }

    // ============================================================
    // MAP ENTITY TO RESPONSE DTO
    // ============================================================

    private BookingResponseDTO mapToResponseDTO(
            Booking booking) {

        return new BookingResponseDTO(
                booking.getId(),
                booking.getProperty().getId(),
                booking.getProperty().getTitle(),
                booking.getAppUser().getId(),
                booking.getAppUser().getName(),
                booking.getNumberOfRooms(),
                booking.getBookingdate(),
                booking.getTotalAmount(),
                booking.getBookingStatus(),
                booking.getPaymentStatus()
        );
    }
}