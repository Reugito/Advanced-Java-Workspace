package com.shoternurl.api.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "shortenurl")
@Data
public class URLEntity {

	private String shortenURL;
	
	private String actualURL;

	public URLEntity( String shortenURL, String actualURL) {
		super();
		this.shortenURL = shortenURL;
		this.actualURL = actualURL;
	}
}
