package com.staymate.service.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import com.staymate.dto.PaymentOrderResponseDTO;
import com.staymate.dto.PaymentVerificationRequestDTO;
import com.staymate.entity.Booking;
import com.staymate.entity.BookingStatus;
import com.staymate.entity.PaymentStatus;
import com.staymate.exception.BookingNotFoundException;
import com.staymate.repository.BookingRepository;
import com.staymate.service.EmailService;
import com.staymate.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final RazorpayClient razorpayClient;
    private final BookingRepository bookingRepository;
    private final EmailService emailService;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    public PaymentServiceImpl(
            RazorpayClient razorpayClient,
            BookingRepository bookingRepository,
            EmailService emailService) {

        this.razorpayClient = razorpayClient;
        this.bookingRepository = bookingRepository;
        this.emailService = emailService;
    }

    @Override
    public PaymentOrderResponseDTO createOrder(
            Long bookingId,
            String userEmail) throws Exception {

        // Find booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        // Check booking ownership
        if (!booking.getAppUser().getEmail().equals(userEmail)) {
            throw new RuntimeException(
                    "You are not authorized to pay for this booking");
        }

        // Prevent payment for an already-paid booking
        if (booking.getPaymentStatus() == PaymentStatus.SUCCESS) {
            throw new RuntimeException("Booking is already paid");
        }

        // Get booking amount
        double amount = booking.getTotalAmount();

        // Convert INR to paise
        int amountInPaise = (int) (amount * 100);

        // Create Razorpay order request
        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "booking_" + bookingId);

        // Create new Razorpay order
        Order order = razorpayClient.orders.create(orderRequest);

        // Get Razorpay Order ID
        String razorpayOrderId = order.get("id");

        // Save Razorpay Order ID
        booking.setRazorpayOrderId(razorpayOrderId);

        bookingRepository.save(booking);

        // Return payment details
        return new PaymentOrderResponseDTO(
                bookingId,
                razorpayOrderId,
                amountInPaise,
                "INR",
                razorpayKeyId
        );
    }

    @Override
    public String verifyPayment(
            PaymentVerificationRequestDTO request) throws Exception {

        // Find booking using Razorpay Order ID
        Booking booking = bookingRepository
                .findByRazorpayOrderId(request.razorpayOrderId())
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking not found"));

        // Create signature verification data
        String verificationData =
                request.razorpayOrderId()
                + "|"
                + request.razorpayPaymentId();

        // Verify Razorpay signature
        boolean isValid = Utils.verifySignature(
                verificationData,
                request.razorpaySignature(),
                razorpayKeySecret
        );

        // Payment verification failed
        if (!isValid) {

            booking.setPaymentStatus(PaymentStatus.FAILED);

            bookingRepository.save(booking);

            throw new RuntimeException(
                    "Invalid payment signature");
        }

        // Save Razorpay payment details
        booking.setRazorpayPaymentId(
                request.razorpayPaymentId());

        booking.setRazorpaySignature(
                request.razorpaySignature());

        // Update booking status
        booking.setPaymentStatus(PaymentStatus.SUCCESS);
        booking.setBookingStatus(BookingStatus.CONFIRMED);

        // Decrease available rooms
        int bookedRooms = booking.getNumberOfRooms();

        int availableRooms =
                booking.getProperty().getAvailableRooms();

        // Check room availability
        if (bookedRooms > availableRooms) {
            throw new RuntimeException(
                    "Not enough rooms available");
        }

        // Decrease available rooms
        booking.getProperty().setAvailableRooms(
                availableRooms - bookedRooms
        );

        // Save booking and updated property
        bookingRepository.save(booking);

        // Send payment confirmation email
        emailService.sendPaymentConfirmationEmail(
                booking.getAppUser().getEmail(),
                booking.getAppUser().getName(),
                booking.getId(),
                booking.getTotalAmount()
        );

        return "Payment verified successfully";
    }
}