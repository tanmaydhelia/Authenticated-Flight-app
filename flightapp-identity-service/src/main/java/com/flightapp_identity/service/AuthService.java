package com.flightapp_identity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flightapp_identity.dto.ChangePasswordRequest;
import com.flightapp_identity.model.UserCredential;
import com.flightapp_identity.repository.UserCredentialRepository;

@Service
public class AuthService {
	
	@Autowired
    private UserCredentialRepository userCredRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    
    public String saveUser(UserCredential creds) {
    	creds.setPassword(passwordEncoder.encode(creds.getPassword()));
    
    	if (creds.getRole() == null || creds.getRole().isEmpty()) {
            creds.setRole("ROLE_USER");
        }
    	
    	userCredRepo.save(creds);
    	return "User Added to the system";
    }
    
    public String generateToken(String username) {
    	
    	UserCredential user = userCredRepo.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    	
    	return jwtService.generateToken(username, user.getRole(), user.getEmail());
    }
    
    public void validateToken(String token) {
    	jwtService.validateToken(token);
    }
    
    public String changePassword(ChangePasswordRequest request) {
        UserCredential user = userCredRepo.findByName(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password does not match!");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userCredRepo.save(user);

        return "Password changed successfully";
    }
}
