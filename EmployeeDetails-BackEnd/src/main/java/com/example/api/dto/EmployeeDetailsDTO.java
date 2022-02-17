package com.example.api.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;


@Data
public class EmployeeDetailsDTO {

	@NotNull(message = "empId is null")
	@Size(min = 1, max = 8, message = "empId size should be 0 to 8 digits")
	@Pattern(regexp = "[0-9]{1,}", message = "empId should be numeric")
	public String empId;

	@NotNull(message = "firstName should't be null")
	@Pattern(regexp = "^[A-Z]{1}[a-z]{2,}$", message = "firstName not valid")
	public String firstName;
	
	@NotNull(message = "lastName should't be null")
	@Pattern(regexp = "^[A-Z]{1}[a-z]{2,}$",  message = "lastName not valid")
	public String lastName;
	
	@NotNull(message = "phoneNo should't be null")
	@Pattern(regexp = "^[7-9]{1}[0-9]{9}", message = "Invalid phone number")
	public String phoneNo;

	@NotNull(message = "add the start date")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@JsonFormat(pattern = "yyyy-MM-dd")
	public LocalDate startDate;

	@NotNull(message = "add the end date")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@JsonFormat(pattern = "yyyy-MM-dd")
	public LocalDate endDate;

	public String address;

	@NotNull(message = "add the password")
	@Pattern(regexp = "[A-Z]{1,}[a-z]{1,}[@$%&]{1}[0-9]{1,}", 
				message = "Invalid Passwprd password should be like Rao@123")
	public String password;
	
	@NotNull(message = "add email")
	@Pattern(regexp = "[a-zA-Z0-9_.]*[-]*[+]*[a-zA-Z0-9]*@[a-zA-Z0-9]+([.][a-zA-Z]+)",
			message = "InvalidEmail")
	public String emailId;
	
	public MultipartFile image;
}
