package com.example.api.dto;


import org.springframework.validation.annotation.Validated;


import lombok.Data;

@Data
@Validated
public class EmployeeDTOForSearchAPI {

	public String id;

	public String firstName;
	
	public String lastName;
	
	public String phoneNo;
	
	public String startDate;

	public String endDate;

	public String address;
	
	public String emailId;
	
	public String pageNo;
	
	public String pageSize;

	public EmployeeDTOForSearchAPI() {
		super();
	}
	
}
