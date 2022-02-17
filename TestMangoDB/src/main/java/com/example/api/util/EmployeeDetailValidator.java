package com.example.api.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.extern.slf4j.Slf4j;

@Component
@Validated
@Slf4j
public class EmployeeDetailValidator  {
	
	public static String invalidImage = "Image size should be less than 3 MB";
	public static String invalidStatus = "Invalid Status";
	
	public boolean isEmpIdValid(@Size(min = 1, max = 8, message = "empId size should be 0 to 8 digits")
	@Pattern(regexp = "[0-9]{1,}", message = "empId should be numeric")
	String id) {
		log.info("/EmployeeDetailValidator.isEmpIdValid()");
		return true;
	}
	
	public boolean isFirstNameValid(@NotNull(message = "firstName should't be null")
	@Pattern(regexp = "^[A-Z]{1}[a-z]{2,}$", message = "firstName not valid")
	String firstName) {
		log.info("/EmployeeDetailValidator.isFirstNameValid()");
		return true;
	}
	
	public boolean isLastNameValid(@NotNull(message = "lastName should't be null")
	@Pattern(regexp = "^[A-Z]{1}[a-z]{2,}$", message = "lastName not valid")
			String lastName) {
		log.info("/EmployeeDetailValidator.isLastNameValid()");
		return true;
	}
	
	public boolean isPhoneNoValid(@NotNull(message = "phoneNo should't be null")
	@Pattern(regexp = "^[7-9]{1}[0-9]{9}", message = "Invalid phone number")
			String phoneNo) {
		log.info("/EmployeeDetailValidator.isPhoneNoValid()");
		return true;
	}
	
	public boolean isStartDateValid(@NotNull(message = "add the start date")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@JsonFormat(pattern = "yyyy-MM-dd") LocalDate startDate) {
		log.info("/EmployeeDetailValidator.isStartDateValid()");
		return true;
	}
	
	public boolean isEndDateValid(@NotNull(message = "add the end date")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@JsonFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
		log.info("/EmployeeDetailValidator.isEndDateValid()");
		return true;
	}
	
	public boolean isProfileImageValid(MultipartFile image) {
		log.info("/EmployeeDetailValidator.isProfileImageValid()");
		if(image.getSize() <= 3000000)
			return true;
		else
			return false;
	}
	
	public boolean isPasswordValid(@NotNull(message = "add the password")
	@Pattern(regexp = "[A-Z]{1,}[a-z]{1,}[@$%&]{1}[0-9]{1,}", 
	message = "Invalid Passwprd password should be like Rao@123")
    String password) {
		log.info("/EmployeeDetailValidator.isPasswordValid()");
		return true;
	}
	
	public boolean isEmailIdValid(@NotNull(message = "add email")
	@Pattern(regexp = "[a-zA-Z0-9_.]*[-]*[+]*[a-zA-Z0-9]*@[a-zA-Z0-9]+([.][a-zA-Z]+)",
	message = "InvalidEmail") String emailId) {
		log.info("/EmployeeDetailValidator.isEmailIdValid()");
		return true;
	}
	
	public boolean isStatusValid(@NotNull(message = "add status") @NotEmpty(message = "add status") String status) {
		if(status.equals("approved") || status.equals("rejected"))
			return true;
		return false;
	}
	
	public boolean isPageNoValid(@Pattern(regexp = "[0-9]{1,10}", message = "pageNo should be Integer") String pageNo) {
		return true;
	}
	
	public LocalDate getDate(String date) {
		 DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    LocalDate ld = LocalDate.parse(date, DATEFORMATTER);
		    System.out.println(ld);
		    return ld;
	}
}
