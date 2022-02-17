package com.example.api.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.api.dto.EmployeeDTOForSearchAPI;
import com.example.api.dto.EmployeeDetailsDTO;
import com.example.api.dto.ResponseDTO;
import com.example.api.entity.EmployeeDetails;
import com.example.api.exceptionhandler.CustomException;
import com.example.api.repository.EmployeeDetailsRepository;
import com.example.api.util.PasswordEncryptionAndDecryption;
import com.mongodb.client.internal.MongoClientImpl;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeDetailsService implements IEmployeeDetailsService {

	@Autowired
	private EmployeeDetailsRepository empRepo;
	
	@Autowired
	MailSender mailSender;
	
	@Autowired
	MongoClientImpl mongoClient;
	
	@Autowired
	PasswordEncryptionAndDecryption pswEncodeandDecoder;
	/**
	 * @apiNote this method is for adding employee details in DB
	 * @param empDTO this is object of EmployeeDetailsDTO class which is passed from
	 *               controller this object is holding all the information which is
	 *               coming from the user end
	 * @return Response DTO containing status, response code and added employee
	 *         details
	 */
	@Override
	public ResponseDTO addEmpDetails(EmployeeDetailsDTO empDTO) {
		log.info("/add EmployeeDetailsService.addEmployeeDetails()");
		try {
			
			if (empRepo.countById(empDTO.empId) == 0 || empRepo.countByEmail(empDTO.emailId)== 0) {
				if (!empDTO.endDate.isAfter(empDTO.startDate))
					throw new CustomException("endDate should be after startDate");
				else {
					EmployeeDetails empDetails = new EmployeeDetails(empDTO);
					empDetails.setPassword(pswEncodeandDecoder.getEncryptedString(empDTO.password));
					empDetails.setImage(new Binary(empDTO.image.getBytes()));
					empRepo.save(empDetails);
					
//					sentRegistrationConfirmationEmail(empDTO.emailId, empDTO.empId, empDTO.firstName);
					return new ResponseDTO("Successfull", 200, "Employee Details added");
				}

			} else if (empRepo.countById(empDTO.empId) == 1) {
				log.info("/add endpoint EmployeeDetailsService.addEmpDetails() excecution complete");
				return new ResponseDTO("Failed", 400, "Employee id already present");
			}else if (empRepo.countByEmail(empDTO.emailId) == 1) {
				log.info("/add endpoint EmployeeDetailsService.addEmpDetails() excecution complete");
				return new ResponseDTO("Failed", 400, "Employee email already present");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("something went wrong in employeeDetailsService.addEmpDetails()");
		}
		return new ResponseDTO("Failed", 400, "Something went wrong");
	}

	/**
	 * 
	 * @apiNote This method is for updating the employee details from DB
	 * @param empDTO this is object of EmployeeDetailsDTO class which is passed from
	 *               controller this object is holding all the information which is
	 *               coming from the user end
	 * @return Response DTO containing status, response code and updated employee
	 *         details
	 * @throws CustomException
	 */
	@Override
	public ResponseDTO updateEmployeeDetails(EmployeeDetailsDTO empDTO) throws CustomException {
		try {
			log.info("/update endpoint EmployeeDetailsService.upDateEmployeeDetails() ");
			
			if (empRepo.countById(empDTO.empId) != 0 ) {
				EmployeeDetails empData = empRepo.findById(empDTO.getEmpId()).orElse(null);
				EmployeeDetails empDetails = new EmployeeDetails(empDTO);
				empDetails.setPassword(pswEncodeandDecoder.getEncryptedString(empDTO.password));
				empDetails.setImage(empData.getImage());
				empRepo.save(empDetails);

				return new ResponseDTO("Successfull", 200, "Employee Details updated with id: " + empDTO.empId);
			} else {
				throw new CustomException("Employee Details are not present with id: " + empDTO.empId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("something went wrong in EmployeeDetailsService.updateEmployee()");
		}
		return new ResponseDTO("Failed", 400, "Something went Wrong");
	}
	
	/**
	 * @apiNote this method is for finding the details which matches with employee
	 *          id
	 * @param id
	 * @return Response DTO containing status, response code and employee details
	 *         that matches with id
	 */
	@Override
	public ResponseDTO findById(String id) {
		try {
			log.info("/EmployeeDetailsService.findbyid() ");
			
		if (empRepo.countById(id) != 0) {
			Optional<EmployeeDetails> empDetails = empRepo.findById(id);
			empDetails.get().setPassword(pswEncodeandDecoder.getDecryptedString(empDetails.get().getPassword()));
			return new ResponseDTO("success", 200, empDetails);
		}
		}catch (Exception e) {
			log.error(" something went wrong in /EmployeeDetailsService.findbyid() ");
			e.printStackTrace();
		}
		return new ResponseDTO("Failed", 400, "Employee not present with empId: " + id);
	}

	/**
	 * @apiNote this method is for finding the details which matches with employee
	 *          firstName
	 * @param firstName
	 * @return Response DTO containing status, response code and employee details
	 *         that matches with firstName
	 */
	@Override
	public ResponseDTO findByFirstName(String firstName) {
		try {
			log.info("/EmployeeDetailsService.findbyfirstName() Method");
			if(empRepo.countByFirstName(firstName) != 0)
				return new ResponseDTO("success", 200, empRepo.findByFirstName(firstName));
		} catch (Exception e) {
			log.error("something went wrong in /EmployeeDetailsService.findbyfirstName() Method");
			e.printStackTrace();
		}
		return new ResponseDTO("Failed", 400, "No employees with this name");
	}

	/**
	 * @apiNote this method is for finding the details which matches with employee
	 *          lastName
	 * @param lastName
	 * @return Response DTO containing status, response code and employee details
	 *         that matches with lastName
	 */
	@Override
	public ResponseDTO findByLastName(String lastName) {
		try {
			log.info("/EmployeeDetailsService.findbylastName() ");
			if(empRepo.countByLastName(lastName) !=0)
				return new ResponseDTO("success", 200, empRepo.findByLastName(lastName));
		} catch (Exception e) {
			log.error("something went Wrong /EmployeeDetailsService.findbylastName() ");
			e.printStackTrace();
			}
		return new ResponseDTO("Failed", 400, "No employees with this Lastname");
	}

	/**
	 * @apiNote this method is for finding the details which matches with employee
	 *          phoneNo
	 * @param phoneNo
	 * @return Response DTO containing status, response code and employee details
	 *         that matches with phoneNo
	 */
	@Override
	public ResponseDTO findByPhoneNo(String phoneNo) {
		try {
			log.info("/EmployeeDetailsService.findbyphoneNo() ");
			if(empRepo.countByPhoneNo(phoneNo) !=0)
				return new ResponseDTO("success", 200, empRepo.findByPhoneNo(phoneNo));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(" Somthing went wrong in /EmployeeDetailService.findByPhoneNo()");
		}
		return new ResponseDTO("Failed", 400,"Employee with is phoneNo not available");
	}

	/**
	 * @apiNote this method is for finding the details which matches with employee
	 *          startDate
	 * @param startDate
	 * @return Response DTO containing status, response code and employee details
	 *         that matches with startDate
	 */
	@Override
	public ResponseDTO findByStartDate(LocalDate startDate) {
		try {
			log.info("/EmployeeDetailsService.findbyStartDate() ");
			if(empRepo.countByStartDate(startDate) !=0)
				return new ResponseDTO("success", 200, empRepo.findByStartDate(startDate));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(" something went wrong /EmployeeDetailsService.findbyStartDate() ");
		}
		return new ResponseDTO("Failed", 400, "Employees with this startDate are not available");
	}

	/**
	 * @apiNote this method is for finding the details which matches with employee
	 *          endDate
	 * @param endDate
	 * @return Response DTO containing status, response code and employee details
	 *         that matches with endDate
	 */
	@Override
	public ResponseDTO findByEndDate(LocalDate endDate) {
		try {
			log.info("/EmployeeDetailsService.findbyendDate()");
			if(empRepo.countByEndDate(endDate) !=0)
				return new ResponseDTO("success", 200, empRepo.findByEndDate(endDate));
		} catch (Exception e) {
			e.printStackTrace();
			log.error(" something went wrong /EmployeeDetailsService.findbyEndDate()");
		}
		return new ResponseDTO("Failed", 400, "Employees with this endDate are not available");
	}
	
	/**
	 * @apiNote This method is for deleting the employee details from DB
	 * @param id
	 * @return Response DTO containing status, response code and id of details
	 */
	@Override
	public ResponseDTO deleteEmployeeDetails(String id) {
		try {
			log.info("/delete endpoint EmployeeDetailsService.deleteEmpDetails() ");
			if (empRepo.countById(id) !=0) {
				empRepo.deleteById(id);
				return new ResponseDTO("Successfull", 200, "Employee Details Deleted with id: " + id);
			} 
		} catch (Exception e) {
			e.printStackTrace();
			log.error("something went Wrong in /delete endpoint EmployeeDetailsService.deleteEmpDetails() ");
		}
		return new ResponseDTO("Failed", 400, "Employee Details are not present with id: " + id);
	}
	
	/**
	 * @apiNote This method is for approve the employee status
	 * @param empId
	 * @return Response DTO containing status, response code and updated employee
	 *         status
	 */
	@Override
	public ResponseDTO approveEmployeeStatus(String empId) {
		try {
			log.info("This call is from /EmployeeDetailsService.approveEmpStatus() ");
			if (empRepo.countById(empId) != 0) {
				EmployeeDetails empDetails = empRepo.findById(empId).orElse(null);
				if(empDetails.getStatus().equals("pending") || empDetails.getStatus().equals("rejected")) {
					empDetails.setStatus("approved");
					empRepo.save(empDetails);
					
					return new ResponseDTO("Successfull", 200, "Employee status approved with id: " + empId);
				}else if (empRepo.countById(empId) != 0) {
					return new ResponseDTO("Failed", 400, "Employee status is already approved with id: " + empId);
					} 
			} 
		} catch (Exception e) {
			e.printStackTrace();
			log.error("The error occured in /EmployeeDetailsService.approveEmpStatus() ");
		}
		return new ResponseDTO("Failed", 400, "Employee Details are not present with id: " + empId);
	}

	/**
	 *@apiNote This method is for managing the pageNo and PageSize
	 *@param pageNo pageSize
	 *@return Response DTO containing status, response code and 
	 * employee details equal to the pageSize
	 */
	@Override
	public ResponseDTO findAllByPagination(String pageNo, String pageSize) {
		try {
			log.info("This call is from /EmployeeDetailsService.findAllByPagination() Method");
			Pageable paging = PageRequest.of(Integer.parseInt(pageNo)-1, Integer.parseInt(pageSize));
			
			return new ResponseDTO("Success", 200, empRepo.findAll(paging).getContent());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("This error occured in /EmployeeDetailsService.findAllByPagination() Method");
		}
		return new ResponseDTO("Failed", 400, "Pagination Failed");
	}

	/**
	 * @apiNote this method is for  approved employee details in DB
	 * @return Response DTO containing status, response code and approved 
	 * employee details
	 */
	@Override
	public ResponseDTO findAllByStatusDetails(String status) {
		log.info("/the conn is: "+mongoClient.getCluster().isClosed());
		log.info("/custerDes : "+mongoClient.getClusterDescription());
		try {
			log.info("This call is from /EmployeeDetailsService.findAllByStatusDetails Method");
			if(status.equals("approved") || status.equals("rejected")) {
				System.out.println(" status " +status);
				if(empRepo.countByStatus(status) != 0) {
					return new ResponseDTO("Success", 200, empRepo.findByStatus(status));
				}else {
					return new ResponseDTO("Failed", 400, "No Employees with "+status+" status");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("This error occured in /EmployeeDetailsService.findAllBetweenDates()");
		}finally {
			log.info("/custerDes2nd : "+mongoClient.getClusterDescription().getServerDescriptions());
		}
		return new ResponseDTO("Failed", 400, "There are no employees with approved status");
	}
	
	
	/**
	 *@apiNote this method is for retrieving the details between two startDates
	 *@param startDate endDate
	 *@return Response DTO containing status, response code and 
	 * employee details between two startDates
	 */
	@Override
	public ResponseDTO findAllBetweenDates(LocalDate startDate, LocalDate endDate) {
		log.info("This is call from /EmployeeDetailsService.findAllBetweenDates()");
		try {
			return new ResponseDTO("Success", 200, empRepo.findAllBetweenDates(startDate, endDate));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("This error occured in /EmployeeDetailsService.findAllBetweenDates()");
		}
		return new ResponseDTO("Failed", 400,"There are no empDetails between"+startDate+" to "+endDate);
	}

	/**
	 * @apiNote this method is for employee logIn
	 * @param email, password
	 * @return Response DTO containing status, response code and logIn message
	 */
	@Override
	public ResponseDTO logIn(String email, String password) {
		log.info("This empLogIn call from /EmployeeDetailsService.logIn()");
		try {
			if(empRepo.countByEmail(email) !=0) {
				EmployeeDetails empDetails = empRepo.findByEmail(email);
				if(password.equals(pswEncodeandDecoder.getDecryptedString(empDetails.getPassword()))) {
					return new ResponseDTO("Success", 200,"Successfully loged in") ;
				}
			}else if(empRepo.countByEmail(email) ==0 ) {
				return new ResponseDTO("Failed", 400, "Employee not present with this email");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error occured in /EmployeeDetailsService.logIn()");
		}
		return new ResponseDTO("Failed", 400,"Login Failed") ;
	}

	/**
	 * @apiNote this method is for finding the details which matches with employee
	 *          ID
	 * @param empId
	 * @return Response DTO containing status, response code and employee details
	 *         that matches with empID
	 */
	@Override
	public ResponseDTO findByEmpId(String id) {
		log.info("This findByEmpId call from /EmployeeDetailService.findByEmpId()");
		try {
			if(empRepo.countById(id) != 0) {
				List<EmployeeDetails> empDetails = empRepo.findByEmpId(id);
				empDetails.get(0).setPassword(pswEncodeandDecoder.getDecryptedString(empDetails.get(0).getPassword()));
				return new ResponseDTO("Success", 200, empDetails) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error occured in /EmployeeDetailService.findByEmpId()");
		}
		return new ResponseDTO("Failed", 400, "employees not found with: "+id) ;
	}

	/**
	 * @apiNote this method is for getting the total employee count from DB
	 * 
	 * @return Response DTO containing status, response code and employee count
	 */
	@Override
	public ResponseDTO getEmployeeCount() {
		log.info("/EmployeeDetailsService.getEmployeeCount()");
		try {
			return new ResponseDTO("Success", 200, empRepo.findAll().size());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(" error occured /EmployeeDetailsService.getEmployeeCount()");
			}
		return new ResponseDTO("Failed", 400, "failed to employee count");
	}

	/**
	 * @apiNote this method is for uploading employee image with specific employee ID
	 * @param file, empId
	 * @return Response DTO containing status, response code and response message
	 */
	@Override
	public ResponseDTO uploadImage(MultipartFile file, String empId) {
		log.info("/EmployeeDetailsService.uploadImage()");
		try {
			if(empRepo.countById(empId) !=0 ) {
				EmployeeDetails empData = empRepo.findById(empId).orElse(null);
				empData.setImage(new Binary(file.getBytes()));
				empRepo.save(empData);
				return new ResponseDTO("Success", 200, "employee Image Updated");
			}
			
		} catch (IOException e) {
			log.error("error occured ing /EmployeeDetailsService.uploadImage()");
			e.printStackTrace();
		}
		return new ResponseDTO("Failed", 400, "failed to Updated employee Image ");
	}
	/**
	 * @apiNote This method is for reject the employee status
	 * @param empId
	 * @return Response DTO containing status, response code and updated employee
	 *         status
	 */
	@Override
	public ResponseDTO rejectEmployeeStatus(String empId) {
	try {
		log.info("This call is from /EmployeeDetailsService.rejectEmployeeStatus() ");
		if (empRepo.countById(empId) != 0) {
			EmployeeDetails empDetails = empRepo.findById(empId).orElse(null);
			if(empDetails.getStatus().equals("pending") || empDetails.getStatus().equals("approved")) {
				empDetails.setStatus("rejected");
				empRepo.save(empDetails);
				return new ResponseDTO("Successfull", 200, "Employee status rejected with id: " + empId);
			}else if (empDetails.getStatus().equals("rejected")) {
				return new ResponseDTO("Failed", 400, "Employee status is already rejected with id: " + empId);
			}
		}else if(empRepo.countById(empId) == 0) {
			return new ResponseDTO("Failed", 400, "Employee Details are not present with id: " + empId);
		}
	} catch (Exception e) {
		e.printStackTrace();
		log.error("The error occured in /EmployeeDetailsService.rejectEmployeeStatus()");
	}
	return new ResponseDTO("Failed", 400, "Something went wrong : ");

	}

	@Override
	public ResponseDTO test(@Valid EmployeeDTOForSearchAPI dto) {
		return new ResponseDTO("",200,dto);
	}
	
	/**
	 * @apiNote This method is to send registration confirmation Email to 
	 * 			Registered Employee
	 * @param to - receiver email Id
	 * @param empId
	 * @param firstName
	 */
	@SuppressWarnings("unused")
	private void sentRegistrationConfirmationEmail(String to, String empId, String firstName) {
		 log.info("/EmployeeDetailsService.sentRegistrationConfirmationEmail()");
		try {
			 SimpleMailMessage simpleMailMessage = new SimpleMailMessage(); 
			 simpleMailMessage.setFrom("raodhotre.rao@gmail.com");
			 simpleMailMessage.setTo(to); 
			 simpleMailMessage.setSubject("Successfull Employee Registration"); 

			  // message contains HTML markups
		        String message = "Greetings "+firstName
		        		+"\n\n"
		        		+"\t\t Congratulations "+firstName +" you have successfully Registered with "
		        		+""+empId +" Employee Id\n\n"
		        		+"Regards Rao\n"
		        		+"raodhotre.rao@gmail.com";       
				simpleMailMessage.setText(message);
				mailSender.send(simpleMailMessage);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error occured in /EmployeeDetailsService.sentRegistrationConfirmationEmail()");
		}
	}	
}
