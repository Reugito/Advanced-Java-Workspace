package com.shoternurl.api.dto;

import lombok.Data;

@Data
public class ResponseDTO {
	
	private String status;
	private int responseCode;
	private Object response;
	
	public ResponseDTO(String status, int responseCode, Object response) {
		super();
		this.status = status;
		this.responseCode = responseCode;
		this.response = response;
	}
}
