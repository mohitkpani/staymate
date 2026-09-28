package com.staymate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staymate.dto.PaymentOrderResponseDTO;
import com.staymate.dto.PaymentVerificationRequestDTO;
import com.staymate.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        super();
        this.paymentService = paymentService;
    }

    @PostMapping("/create-order/{bookingId}")
    public ResponseEntity<PaymentOrderResponseDTO> createOrder( @PathVariable Long bookingId, Authentication authentication) {

        try {

            String userEmail = authentication.getName();

            PaymentOrderResponseDTO response =
                    paymentService.createOrder(bookingId, userEmail);

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody PaymentVerificationRequestDTO request) {

        try {

            String response = paymentService.verifyPayment(request);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {

            return new ResponseEntity<>(
                    "Payment verification failed: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}