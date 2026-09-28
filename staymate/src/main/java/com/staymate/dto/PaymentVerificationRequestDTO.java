package com.staymate.dto;

public record PaymentVerificationRequestDTO(
		String razorpayOrderId,
        String razorpayPaymentId,
        String razorpaySignature
		) {

}
