package com.shoternurl.api.util;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ShortenURLGenerator {

	public String getShortenURL(int lenthOfURL) {
		log.info("/ShortenURLGenerator.getShortenURL(int lenthOfURL)");
		try {
			String alphanum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" + "0123456789"
					+ LocalDateTime.now();
			char[] characterSet = alphanum.toCharArray();
			
			Random random = new SecureRandom();
			
			char[] shortenURL = new char[lenthOfURL];
			for (int i = 0; i < shortenURL.length; i++) {
				int randomIndex = random.nextInt(characterSet.length);
				shortenURL[i] = characterSet[randomIndex];
			}
			return new String(shortenURL);
		} catch (Exception e) {
			log.error("/error in ShortenURLGenerator.getShortenURL(int lenthOfURL)");
		}
		return "";
	}
}
