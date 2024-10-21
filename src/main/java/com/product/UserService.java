package com.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	@Autowired
    private UserRepository userRepository;

    private final List<OtpRecord> otpRecords = new ArrayList<>(); // Temporary storage for OTPs

    public void addUser(User user) {
        userRepository.save(user);
        String otp = generateOtp(); // Implement this method to generate OTP
        otpRecords.add(new OtpRecord(user.getId(), otp)); // Store the OTP
        sendOtp(user.getPhoneNumber(), otp); // Implement this method to send OTP
    }

    public User updateUser(int id, UserDTO userDTO) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + id));
        
        // Update user fields
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());  // Consider hashing this password
        user.setConfirmPassword(userDTO.getConfirmPassword()); // You may not need to store this

        return userRepository.save(user);
    }


    public boolean verifyOtp(int userId, String otp) {
        return otpRecords.removeIf(record -> record.getUserId() == userId && record.getOtp().equals(otp));
    }

  
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }


    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

   
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    private String generateOtp() {
        // Your logic to generate an OTP (e.g., random number)
        return String.valueOf((int) (Math.random() * 900000) + 100000); // Example 6-digit OTP
    }

    private void sendOtp(String phoneNumber, String otp) {
        // Your logic to send OTP via SMS/email
        System.out.println("Sending OTP " + otp + " to " + phoneNumber);
    }
    
    	 public void deleteUserById(Integer id) {
    	        if (userRepository.existsById(id)) {
    	            userRepository.deleteById(id);
    	        } else {
    	            throw new RuntimeException("User not found with id: " + id);
    	        }
    }
    private static class OtpRecord {
        private final int userId;
        private final String otp;

        public OtpRecord(int userId, String otp) {
            this.userId = userId;
            this.otp = otp;
        }

        public int getUserId() {
            return userId;
        }

        public String getOtp() {
            return otp;
        }
    }

}
