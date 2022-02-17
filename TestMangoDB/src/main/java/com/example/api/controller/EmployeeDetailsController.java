package com.example.api.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.api.dto.EmployeeDTOForSearchAPI;
import com.example.api.dto.EmployeeDetailsDTO;
import com.example.api.dto.ResponseDTO;
import com.example.api.exceptionhandler.CustomException;
import com.example.api.services.IEmployeeDetailsService;
import com.example.api.util.EmployeeDetailValidator;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

/**
 * @author raosa
 *
 */
@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/emp")
@Slf4j
public class EmployeeDetailsController {

	@Autowired
	private IEmployeeDetailsService empServices;

	@Autowired
	private EmployeeDetailValidator validator;

	/**
	 * @apiNote this method finds the matching details with received parameter
	 * @param id firstName lastName phoneNo pageNo pageSize startDate endDate
	 * @return Response DTO containing status, response code and list of employee
	 *         details that matches with received parameter
	 *
	 * @throws CustomException
	 */
	@GetMapping(value = "/getall")
	public ResponseDTO getEmployeeDetailsWithParam(@RequestParam String searchDTO) {
		try {
			EmployeeDTOForSearchAPI dto = new Gson().fromJson(searchDTO, EmployeeDTOForSearchAPI.class);
			System.out.println("dto = " + dto);
			log.info("/getall endPoint EmployeeDetailsController.getEmployeeDetailsWithParam()");
			if (dto.id != null && dto.firstName == null && dto.lastName == null && dto.startDate == null
					&& dto.endDate == null) {
				if (validator.isEmpIdValid(dto.id))
					return empServices.findByEmpId(dto.id);
			} else if (dto.firstName != null && dto.id == null && dto.lastName == null && dto.startDate == null
					&& dto.endDate == null) {
				if (validator.isFirstNameValid(dto.firstName))
					return empServices.findByFirstName(dto.firstName);
			} else if (dto.lastName != null && dto.id == null && dto.firstName == null && dto.startDate == null
					&& dto.endDate == null) {
				if (validator.isLastNameValid(dto.lastName))
					return empServices.findByLastName(dto.lastName);
			} else if (dto.phoneNo != null && dto.id == null && dto.firstName == null && dto.lastName == null
					&& dto.startDate == null && dto.endDate == null) {
				if (validator.isPhoneNoValid(dto.phoneNo))
					return empServices.findByPhoneNo(dto.phoneNo);
			} else if (dto.startDate != null && dto.endDate == null && dto.id == null && dto.firstName == null
					&& dto.lastName == null) {
				if (validator.isStartDateValid(validator.getDate(dto.startDate)))
					return empServices.findByStartDate(validator.getDate(dto.startDate));

			} else if (dto.endDate != null && dto.startDate == null && dto.id == null && dto.firstName == null
					&& dto.lastName == null) {
				if (validator.isEndDateValid(validator.getDate(dto.endDate)))
					return empServices.findByEndDate(validator.getDate(dto.endDate));
			} else if (dto.startDate != null && dto.endDate != null && dto.id == null && dto.firstName == null
					&& dto.lastName == null) {
				if (validator.isStartDateValid(validator.getDate(dto.startDate))
						&& validator.isEndDateValid(validator.getDate(dto.endDate))) {
					log.info("getall between Two startDates");
					if (validator.getDate(dto.startDate).isBefore(validator.getDate(dto.endDate)))
						return empServices.findAllBetweenDates(validator.getDate(dto.startDate),
								validator.getDate(dto.endDate));
					else
						return new ResponseDTO("success", 200, "endDate should be greater than startDate");
				}

			} else if (dto.pageNo != null) {
				if (validator.isPageNoValid(dto.pageNo))
					return empServices.findAllByPagination(dto.pageNo, dto.pageSize);
			}
		} catch (Exception e) {
			log.error("/This error in EmployeeServiceController.getEmployeeDetailsWithParam()");
			throw e;
		}
		return new ResponseDTO("Failed", 400, "Incorrect selection");
	}

	/**
	 * @apiNote this method is for approved employee details in DB
	 * @return Response DTO containing status, response code and approved employee
	 *         details
	 */
	@GetMapping("/getall/status")
	public ResponseDTO getDetailsbyStatus(@RequestParam String status) {
		try {
			log.info("/getall status endPoint EmployeeDetailsController.getApprovedDetails()");
			if (validator.isStatusValid(status))
				return empServices.findAllByStatusDetails(status);
			else if (!validator.isStatusValid(status))
				return new ResponseDTO("Failed", 400, "Invalid Status");
		} catch (Exception e) {
			log.error("Error Occured in EmployeeDetailsController.getApprovedDetails()");
			throw e;
		}
		return new ResponseDTO("Failed", 400, "Something went Wrong");
	}

	/**
	 * @apiNote this method is for adding employee details in DB
	 * @param empDTO this is object of EmployeeDetailsDTO class which is passed from
	 *               controller this object is holding all the information which is
	 *               coming from the user end
	 * @return Response DTO containing status, response code and added employee
	 *         details
	 */
	@PostMapping(value = "/add")
	public ResponseDTO addEmpDetails(@ModelAttribute @Valid EmployeeDetailsDTO empDTO) throws CustomException {
		try {
			log.info("/add endPoint EmployeeDetailsController.addEmployeeDetails()");
			System.out.println("bytes: " + empDTO.image.getSize());
			if (empDTO.image.getSize() <= 3000000) { // size = 3 MB
				return empServices.addEmpDetails(empDTO);
			} else if (empDTO.image.getSize() > 3000000) {
				return new ResponseDTO("Failed", 400, "Image size should be less than 3 MB");
			}
		} catch (Exception e) {
			log.error("error occured in EmployeeDetailsController.addEmployeeDetails()");
			throw e;
		}
		return new ResponseDTO("Failed", 400, "Something went wrong");
	}

	/**
	 * @apiNote This method is for deleting the employee details from DB
	 * @param id
	 * @return Response DTO containing status, response code and id of details
	 */
	@DeleteMapping(value = "/delete/{id}")
	public ResponseDTO deleteEmployeeDetails(@PathVariable String id) {
		try {
			log.info("/add endPoint EmployeeDetailsController.deleteEmployeeDetails()");
			if (validator.isEmpIdValid(id))
				return empServices.deleteEmployeeDetails(id);
		} catch (Exception e) {
			log.error("error occured in EmployeeDetailsController.deleteEmployeeDetails()");
			throw e;
		}
		return new ResponseDTO("Failed", 400, "Something went wrong");
	}

	/**
	 * 
	 * @apiNote This method is for updating the employee details from DB
	 * @param empDTO
	 * @return Response DTO containing status, response code and updated employee
	 *         details
	 * @throws CustomException
	 */
	@PutMapping(value = "/update")
	public ResponseDTO updateEmployeeDetails(@RequestBody @Valid EmployeeDetailsDTO empDTO) throws CustomException {
		try {
			log.info("/update endPoint EmployeeDetailsController.updateEmployeeDetails()");
			if (empDTO.startDate.isBefore(empDTO.endDate))
				return empServices.updateEmployeeDetails(empDTO);
			else
				return new ResponseDTO("Failed", 400, "EndDate should be greater Than startDate");
		} catch (Exception e) {
			log.error("error occured in EmployeeDetailsController.updateEmployeeDetails()");
		}
		return new ResponseDTO("Failed", 400, "Something went wrong");
	}

	/**
	 * @apiNote This method is for approve the employee status
	 * @param empId
	 * @return Response DTO containing status, response code and updated employee
	 *         status
	 * @throws CustomException
	 */
	@PutMapping("/approve/{empId}")
	public ResponseDTO approveEmployeeStatus(@PathVariable String empId) throws CustomException {
		try {
			log.info("/approve status endPoint EmployeeDetailsController.approveEmployeeStatus()");
			if (validator.isEmpIdValid(empId))
				return empServices.approveEmployeeStatus(empId);
		} catch (Exception e) {
			log.error("error occured in EmployeeDetailsController.approveEmployeeStatus");
			throw e;
		}
		return new ResponseDTO("Failed", 400, "Something went wrong");
	}

	/**
	 * @apiNote This method is for reject the employee status
	 * @param empId
	 * @return Response DTO containing status, response code and updated employee
	 *         status
	 * @throws CustomException
	 */
	@PutMapping("/reject/{empId}")
	public ResponseDTO rejectEmployeeStatus(@PathVariable String empId) {
		try {
			log.info("/Reject status endPoint EmployeeDetailsController.approveEmployeeStatus()");
			if (validator.isEmpIdValid(empId))
				return empServices.rejectEmployeeStatus(empId);
		} catch (Exception e) {
			log.error("error occured in EmployeeDetailsController.rejectEmployeeStatus");
			throw e;
		}
		return new ResponseDTO("Failed", 400, "Something went wrong");
	}

	/**
	 * @apiNote this method is for employee logIn
	 * @param email, password
	 * @return Response DTO containing status, response code and logIn message
	 */
	@PostMapping("/login")
	public ResponseDTO employeeLogin(@RequestParam String email, @RequestParam String password) {
		try {
			log.info("login endPoint EmployeeDetailsController.employeeLogin()");
			if (validator.isEmailIdValid(email) && validator.isPasswordValid(password))
				return empServices.logIn(email, password);
		} catch (Exception e) {
			log.error("error occured in EmployeeDetailsController.employeeLogIn()");
			throw e;
		}
		return new ResponseDTO("Failed", 400, "Something went wrong");
	}

	/**
	 * @apiNote this method is for getting the total employee count from DB
	 * 
	 * @return Response DTO containing status, response code and employee count
	 */
	@GetMapping("/empcount")
	public ResponseDTO getemployeeCount() {
		try {
			log.info("/empcount endPoint EmployeeDetailsController.getemployeeCount()");
			return empServices.getEmployeeCount();
		} catch (Exception e) {
			log.error("error occured in EmployeeDetailsController.getEmployeeCount()");
		}
		return new ResponseDTO("Failed", 400, "Something went wrong");
	}

	/**
	 * @apiNote this method is for uploading employee image with specific employee
	 *          ID
	 * @param file, empId
	 * @return Response DTO containing status, response code and response message
	 */
	@PostMapping("/upload")
	public ResponseDTO uploadImage(@RequestParam String empID, @RequestParam MultipartFile image) throws IOException {
		try {
			log.info("/upload endPoint EmployeeDetailsController.uploadEmployeeIamge()");
			if (validator.isEmpIdValid(empID) && validator.isProfileImageValid(image))
				return empServices.uploadImage(image, empID);
			else if (!validator.isProfileImageValid(image))
				return new ResponseDTO("Failed", 400, "Image size should be less than 3 MB");
		} catch (Exception e) {
			log.error("error occured in EmployeeDetailsController.uploadImage()");
			throw e;
		}
		return new ResponseDTO("Failed", 400, "Something went wrong");
	}
}
