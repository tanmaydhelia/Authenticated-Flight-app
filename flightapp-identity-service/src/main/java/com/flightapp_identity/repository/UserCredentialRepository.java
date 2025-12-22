package com.flightapp_identity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightapp_identity.model.UserCredential;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Integer>{
	Optional<UserCredential> findByName(String name);
	
	//find by gmail
	Optional<UserCredential> findByEmail(String email);
}
