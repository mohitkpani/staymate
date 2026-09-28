package com.staymate.service;

public interface EmailService {
	
	void sendWelcomeEmail(String to, String username);
	
	void sendBookingConfirmationEmail(String to, String username, Long bookingId, String propertyName, Double amount);
	
	void sendPaymentConfirmationEmail(String to, String username, Long bookingId, Double amount);
	
	void sendBookingCancellationEmail(String to, String username, Long bookingId, String propertyName);
}
