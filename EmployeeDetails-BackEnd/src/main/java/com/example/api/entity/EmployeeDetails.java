package com.example.api.entity;

import java.time.LocalDate;

import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.api.dto.EmployeeDetailsDTO;

import lombok.Data;

@Data
@Document
public class EmployeeDetails {
	
	private String id;
	private String firstName;
	private String lastName;
	private String phoneNo;
	private LocalDate startDate;
	private Binary image;
	private LocalDate endDate;
	private String status = "pending";
	private String email;
	private String address;
	private String password;
	public EmployeeDetails() {}
	
	public EmployeeDetails(EmployeeDetailsDTO empDTO) {
		this.id = empDTO.empId;
		this.firstName = empDTO.firstName;
		this.lastName = empDTO.lastName;
		this.phoneNo = empDTO.phoneNo;
		this.startDate = empDTO.startDate;
		this.endDate = empDTO.endDate;
		this.email = empDTO.emailId;
		this.address = empDTO.address;
	}
}
