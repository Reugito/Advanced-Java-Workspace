package com.example.api.util;

import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptionAndDecryption {

	public String getEncryptedString(String password) {
		
		return Base64.getEncoder().encodeToString(password.getBytes());
	}
	
	public String getDecryptedString(String password) {
		return new String (Base64.getMimeDecoder().decode(password));
	}
}
