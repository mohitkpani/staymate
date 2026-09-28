package com.staymate.service.impl;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.staymate.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService{
		
	private JavaMailSender javaMailSender;

	public EmailServiceImpl(JavaMailSender javaMailSender) {
		super();
		this.javaMailSender = javaMailSender;
	}
	
	@Override
	public void sendWelcomeEmail(String to, String username) {
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(to);
		message.setSubject("Welcome to StayMate!");
		message.setText(
				"Hello " + username + ",\n\n"
		                + "Welcome to StayMate!\n\n"
		                + "Your account has been successfully created.\n"
		                + "You can now search for properties, book rooms and manage your bookings.\n\n"
		                + "Thank you for choosing StayMate.\n\n"
		                + "Regards,\n"
		                + "StayMate Team"
				);
		
		javaMailSender.send(message);
	}
	
	@Override
	public void sendBookingConfirmationEmail(String to, String username, Long bookingId, String propertyName, Double amount) {
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(to);
		message.setSubject("StayMate - Booking Confirmation");
		message.setText(
				"Hello " + username + ",\n\n"
		                + "Your booking has been successfully created.\n\n"
		                + "Booking ID: " + bookingId + "\n"
		                + "Property: " + propertyName + "\n"
		                + "Amount: ₹" + amount + "\n\n"
		                + "Your booking is currently pending payment.\n\n"
		                + "Regards,\n"
		                + "StayMate Team"
		        );
		javaMailSender.send(message);
				
	}
	
	@Override
	public void sendPaymentConfirmationEmail(String to, String username, Long bookingId, Double amount) {


	    SimpleMailMessage message = new SimpleMailMessage();

	    message.setTo(to);
	    message.setSubject("StayMate - Payment Successful");

	    message.setText(
	            "Hello " + username + ",\n\n"
	            + "Your payment has been successfully completed.\n\n"
	            + "Booking ID: " + bookingId + "\n"
	            + "Amount Paid: ₹" + amount + "\n\n"
	            + "Your booking is now confirmed.\n\n"
	            + "Thank you for choosing StayMate.\n\n"
	            + "Regards,\n"
	            + "StayMate Team"
	    );

	    System.out.println("Sending email now...");

	    javaMailSender.send(message);

	    System.out.println("EMAIL SENT SUCCESSFULLY");
	}
	
	@Override
	public void sendBookingCancellationEmail(String to, String username, Long bookingId, String propertyName) {
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(to);
		message.setSubject("StayMate - Booking Cancelled");
		message.setText(
				"Hello " + username + ",\n\n"
		                + "Your booking has been cancelled successfully.\n\n"
		                + "Booking ID: " + bookingId + "\n"
		                + "Property: " + propertyName + "\n\n"
		                + "If you did not request this cancellation, please contact StayMate support.\n\n"
		                + "Regards,\n"
		                + "StayMate Team"
				);
		javaMailSender.send(message);
	}

}
