package com.product;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
public class otpservice {

    private static final int OTP_LENGTH = 6; // Length of the OTP
    private static final SecureRandom random = new SecureRandom();
    

    public otpservice() {
        // Initialize Twilio with the credentials
        
    }
    
    // In-memory store for OTPs
    private Map<String, String> otpStore = new HashMap<>();

    // Generate OTP
    public String generateOtp() {
        int otp = random.nextInt((int) Math.pow(10, OTP_LENGTH)); // Generate a random OTP
        return String.format("%06d", otp); // Format to ensure it's 6 digits
    }

    // Store OTP
    public void storeOtp(String phoneNumber, String otp) {
        otpStore.put(phoneNumber, otp);
    }

    // Verify OTP
    public boolean verifyOtp(String phoneNumber, String otp) {
        String storedOtp = otpStore.get(phoneNumber);
        return storedOtp != null && storedOtp.equals(otp);
    }

    // Optional: Method to clear OTPs after verification or expiration
    public void clearOtp(String phoneNumber) {
        otpStore.remove(phoneNumber);
    }
    public void sendOtpToUser(String phoneNumber, String otp) {
        // Specify your Twilio phone number here
        String twilioPhoneNumber = "+1234567890"; // Replace with your Twilio number

        // Create the message
        Message message = Message.creator(
                new PhoneNumber(phoneNumber), // Recipient's phone number
                new PhoneNumber(twilioPhoneNumber), // Your Twilio phone number
                "Your OTP is: " + otp // The message body
        ).create();

        // Log the message SID (optional)
        System.out.println("Sent message with SID: " + message.getSid());
        System.out.println("Sending OTP " + otp + " to phone number: " + phoneNumber);
    }


}
