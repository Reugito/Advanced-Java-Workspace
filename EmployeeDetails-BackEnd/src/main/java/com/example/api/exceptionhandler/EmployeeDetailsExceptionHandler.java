package com.example.api.exceptionhandler;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.api.dto.ResponseDTO;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
@Validated
public class EmployeeDetailsExceptionHandler {
	
	/**
	 * @apiNote This method is for handling the MethodArgumentNotValidException
	 *           which generates after failing the validations
	 * @param exception
	 * @return Response Entity containing ResponseDTO( status, response code 
	 * 			and error message) and HttpStatus
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseDTO> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException exception) {
		log.info("handleMethodArgumentNotValidException methos execution started");
		List<ObjectError> errorList = exception.getBindingResult().getAllErrors();
		List<String> errMsg = errorList.stream().map(objErr -> objErr.getDefaultMessage()).collect(Collectors.toList());
		ResponseDTO respDTO = new ResponseDTO("Failed3", 400, errMsg.get(0));
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.BAD_REQUEST);
	}
	
	
	
	/**
	 * @apiNote This method is for handling the InvalidFormatException
	 *           which generates after failing the validation of numeric emp id
	 * @param exception
	 * @return Response Entity containing ResponseDTO( status, response code 
	 * 			and error message) and HttpStatus
	 */
//	@ExceptionHandler(InvalidFormatException.class)
//	public ResponseEntity<ResponseDTO> handelInvalidFormatException(InvalidFormatException e){
//		log.info("handelInvalidFormatException method execution started");
//		ResponseDTO respDTO = new ResponseDTO("Failed", 400, "Id should be Integer");
//		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.BAD_REQUEST); 
//	}
	
	@ExceptionHandler(DateTimeParseException.class)
	public ResponseEntity<ResponseDTO> handelInvalidFormatException(DateTimeParseException e){
		log.info("handelInvalidFormatException method execution started");
		ResponseDTO respDTO = new ResponseDTO("Failed2", 400, " format yyyy-MM-dd ");
		e.getMessage();
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.BAD_REQUEST); 
	}
	
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ResponseDTO> customExceptionHandler(
			CustomException exception) {
		log.info("customExceptionHandler method execution started");
		ResponseDTO respDTO = new ResponseDTO("Failed12", 400, exception.getMessage());
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ResponseDTO> ConstraintViolationExceptionHandler(ConstraintViolationException e){
		ResponseDTO respDTO = new ResponseDTO("Failed Constraint", 400, e.getMessage().split(":")[1]);
		return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.BAD_REQUEST);
	}
	
//	  @ExceptionHandler(Exception.class)
//	  public final ResponseEntity<ResponseDTO> handleAllExceptions(Exception ex, WebRequest request) {
//		  if(ex.getMessage().split(":")[1].contains("Cannot deserialize value of type `java.time.LocalDate`")
//				  || ex.getMessage().split(":")[1].contains("springframework.format.annotation.DateTimeFormat java.time.LocalDate")) {
//			  ResponseDTO respDTO = new ResponseDTO("Failed13", 400, "format yyyy-MM-dd");
//			  return new ResponseEntity<ResponseDTO>(respDTO, HttpStatus.BAD_REQUEST);
//		  }
//		  List<ObjectError> errorList = ((BindException) ex).getBindingResult().getAllErrors();
//			List<String> errMsg = errorList.stream().map(objErr -> objErr.getDefaultMessage()).collect(Collectors.toList());
//		  ResponseDTO errorObj = new ResponseDTO("Failed14",400, errMsg.get(0));
//	    return new ResponseEntity<>(errorObj,  HttpStatus.BAD_REQUEST);
//	  }
}
