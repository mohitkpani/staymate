package com.staymate.service;

import com.staymate.dto.PaymentOrderResponseDTO;
import com.staymate.dto.PaymentVerificationRequestDTO;

public interface PaymentService {
	
	abstract PaymentOrderResponseDTO createOrder(Long bookingId, String userEmail) throws Exception;
	
	abstract String verifyPayment(PaymentVerificationRequestDTO request) throws Exception;
}
