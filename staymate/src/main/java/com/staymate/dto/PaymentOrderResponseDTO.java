package com.staymate.dto;

public record PaymentOrderResponseDTO(
		Long bookingId,
        String orderId,
        Integer amount,
        String currency,
        String keyId
		) {

}
