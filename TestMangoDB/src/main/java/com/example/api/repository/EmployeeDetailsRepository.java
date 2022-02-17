package com.example.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.api.entity.EmployeeDetails;

@Repository("empRepo")
public interface EmployeeDetailsRepository extends MongoRepository<EmployeeDetails, String> {

	/**
	 * @apiNote This generic method  for finding the employee details that 
	 *          matches the attributes of entity 
	 * @param key, value 
	 * @return This list of Employee Details 
	 */
	@Query("{?0:/?1/}")
	List<EmployeeDetails> findByEachEntity(String key, String value);
	
	/**
	 * @apiNote This generic method for finding the employee details that 
	 *          matches with date
	 * @param key, date
	 * @return This list of Employee Details 
	 */
	@Query("{?0: ?1}")
	List<EmployeeDetails> findAllByDate(String key,LocalDate date);
	
	/**
	 * @apiNote This method is for finding the employee details that 
	 *          are between date1 and date2
	 * @param key, date1, date2
	 * @return This list of Employee Details 
	 */
	@Query( "{?0:{$gte:?1,$lt:?2}}")
	List<EmployeeDetails> findAllBetweenDates(String key, LocalDate start, LocalDate end);

	/**
	 * @apiNote This method is for finding the employee details that 
	 *          matches the firstName 
	 * @param firstName
	 * @return This list of Employee Details 
	 */
	@Query( "{firstName: /?0/}")
	List<EmployeeDetails> findByFirstName(String firstName);
	
	/**
	 * @apiNote This method is for finding the employee count that 
	 *          matches the firstName 
	 * @param firstName
	 * @return employee count 
	 */
    long countByFirstName(String firstNAme);
	
	/**
	 * @apiNote This method is for finding the employee details that 
	 *          matches the lastName
	 * @param lastName
	 * @return This list of Employee Details 
	 */
	@Query( "{lastName: /?0/}")
	List<EmployeeDetails> findByLastName(String lastName);
	
	/**
	 * @apiNote This method is for finding the employee count that 
	 *          matches the lastName 
	 * @param lastName
	 * @return employee count 
	 */
	long countByLastName(String lastName);
	/**
	 * @apiNote This method is for finding the employee details that 
	 *          matches the phoneNo
	 * @param phoneNo
	 * @return This list of Employee Details 
	 */
	@Query( "{phoneNo: /?0/}")
	List<EmployeeDetails> findByPhoneNo(String phoneNo);
	
	/**
	 * @apiNote This method is for finding the employee count that 
	 *          matches the phoneNo 
	 * @param phoneNo 
	 * @return employee count 
	 */
	long countByPhoneNo(String phoneNo);
	
	/**
	 * @apiNote This method is for finding the employee details that 
	 *          matches the startDate 
	 * @param startDate
	 * @return This list of Employee Details 
	 */
	List<EmployeeDetails> findByStartDate(LocalDate startDate);
	
	/**
	 * @apiNote This method is for finding the employee count that 
	 *          matches the startDate 
	 * @param startdate 
	 * @return employee count 
	 */
	long countByStartDate(LocalDate startDate);
//	long countByStartDate(LocalDate startDate);
	/**
	 * @apiNote This method is for finding the employee details that 
	 *          matches the endDate 
	 * @param endDate
	 * @return This list of Employee Details 
	 */
	List<EmployeeDetails> findByEndDate(LocalDate endDate);
	
	/**
	 * @apiNote This method is for finding the employee count that 
	 *          matches the endDate
	 * @param endDate
	 * @return employee count 
	 */
	long countByEndDate(LocalDate endDate);
	
	
	@Query( "{startDate:{$gte:?0,$lt:?1}}")
	List<EmployeeDetails> findAllBetweenDates(LocalDate start, LocalDate end);
	
	List<EmployeeDetails> findByStatus(String status);
	
	/**
	 * @apiNote This method is for finding the employee count that 
	 *          matches the status
	 * @param status
	 * @return employee count 
	 */
	long countByStatus(String status);
	
	
	@Query( "{id: ?0}")
	List<EmployeeDetails> findByEmpId(String id);
	
	/**
	 * @apiNote This method is for finding the employee count that 
	 *          matches the EmployeeId 
	 * @param id
	 * @return employee count 
	 */
	@Query(value="{id:?0}", count=true)
	long countById(String id);
	
	EmployeeDetails findByEmail(String email);
	
	/**
	 * @apiNote This method is for finding the employee count that 
	 *          matches the EmailId 
	 * @param EmailId
	 * @return employee count 
	 */
	long countByEmail(String email);
}
