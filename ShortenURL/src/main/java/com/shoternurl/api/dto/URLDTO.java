package com.shoternurl.api.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class URLDTO {
	
	@NotNull(message = "actual URL must not null")
	@Pattern(regexp = "((http|https)://)(www.)?[a-zA-Z0-9@:%._\\+~#?&//=]{2,}"
			+ ".[a-z]{2,6}\\b([-a-zA-Z0-9@:%._\\+~#?&//=]*)", message = "Invalid URL")
	public String actualURL;
}
