package com.example.api.services;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import com.example.api.dto.EmployeeDTOForSearchAPI;
import com.example.api.dto.EmployeeDetailsDTO;
import com.example.api.dto.ResponseDTO;
import com.example.api.exceptionhandler.CustomException;

@Validated
public interface IEmployeeDetailsService {
	
	public ResponseDTO test(@Valid EmployeeDTOForSearchAPI dto);
	
	public ResponseDTO addEmpDetails(EmployeeDetailsDTO empDTO);
	
	public ResponseDTO findById(String id);
	
	public ResponseDTO findByFirstName(String firstName);
	
	public ResponseDTO findByLastName(String lastName);
	
	public ResponseDTO findByPhoneNo(String phoneNo);
	
	public ResponseDTO findByStartDate(LocalDate startDate);
	
	public ResponseDTO findByEndDate(LocalDate endDate);
	
	public ResponseDTO findAllBetweenDates(LocalDate startDate, LocalDate endDate);
	
	public ResponseDTO deleteEmployeeDetails(String id);
	
	public ResponseDTO updateEmployeeDetails(@Valid EmployeeDetailsDTO empDTO) throws CustomException;
	
	public ResponseDTO approveEmployeeStatus(String empId);
	
	public ResponseDTO findAllByPagination(String pageNo, String pageSize);
	
	public ResponseDTO findAllByStatusDetails(String status);
	
	
	public ResponseDTO logIn(String email, String password);
	
	public ResponseDTO findByEmpId(String id);
	
	public ResponseDTO getEmployeeCount();
	
	public ResponseDTO uploadImage(MultipartFile file, String empId);
	
	public ResponseDTO rejectEmployeeStatus(String empId);
	
}
