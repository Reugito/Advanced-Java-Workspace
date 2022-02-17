package com.shoternurl.api.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoternurl.api.dto.ResponseDTO;
import com.shoternurl.api.dto.URLDTO;
import com.shoternurl.api.service.IURLService;

import lombok.extern.slf4j.Slf4j;

@RestController()
@RequestMapping("/url")
@Slf4j
public class URLController {
	
	@Autowired
	IURLService urlservice;
	
	/**
	 * @apiNote this method will retrieve the actual URL based on shorten URL
	 * @param shortenURL 
	 * @return Redirect to actualURL page or ResponseDTO containing status responseCode and response
	 *
	 */
	@GetMapping(value = {"/{shortenURL}"})
	public ResponseDTO redirect(@PathVariable String shortenURL, HttpServletResponse response) throws IOException {
		log.info("/URLController.redirect()");
		ResponseDTO urlPath = urlservice.getActualURL(shortenURL);
		if(urlPath.getResponseCode() == 200)
			response.sendRedirect((String) urlPath.getResponse());
		return urlPath;
	}
	
	/**
	 * @apiNote this method will save the shorten URL and actual URL in mongoDB
	 * @param dto - DTO contains the actual URL;
	 * @return ResponseDTO containing status responseCode and response
	 *
	 */
	@PostMapping("/add")
	public ResponseDTO saveUrl(@RequestBody @Valid URLDTO dto) {
		log.info("/URLController.add()");
		System.out.println("---->"+" ".isBlank());
		return urlservice.saveShortenURL(dto);
	}
}
