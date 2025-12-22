package com.flightapp_identity.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flightapp_identity.model.UserCredential;
import com.flightapp_identity.repository.UserCredentialRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

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
    	userCredRepo.save(creds);
    	return "User Added to the system";
    }
    
    public String generateToken(String username) {
    	return jwtService.generateToken(username);
    }
    
    public void validateToken(String token) {
    	jwtService.validateToken(token);
    }
    
    private static final String GOOGLE_CLIENT_ID = "620654976994-4qr1fai0i79ip40ijs38st3r4h80l2a4.apps.googleusercontent.com";
    
    public String loginWithGoogle(String googleToken) {
    	try {
            // 1. Verify the token with Google
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(googleToken);
            if (idToken == null) {
                throw new RuntimeException("Invalid Google Token");
            }
            
         // 2. Get User Info from Token
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            // 3. Check if user exists
            return userCredRepo.findByEmail(email)
                    .map(user -> generateToken(user.getName())) // User exists? Generate JWT.
                    .orElseGet(() -> {
                        // User doesn't exist? Create one automatically.
                        UserCredential newUser = new UserCredential();
                        newUser.setName(name); // Use Google name
                        newUser.setEmail(email);
                        // Set dummy password or handle null in entity (Social users don't have passwords)
                        newUser.setPassword(""); 
                        userCredRepo.save(newUser);
                        return generateToken(name);
                    });
    	}
    	catch (Exception e) {
            throw new RuntimeException("Google Login Failed: " + e.getMessage());
        }
    }
}
