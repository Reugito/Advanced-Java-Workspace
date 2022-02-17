package com.shoternurl.api.service;

import javax.servlet.http.HttpServletResponse;

import com.shoternurl.api.dto.ResponseDTO;
import com.shoternurl.api.dto.URLDTO;
import com.shoternurl.api.entity.URLEntity;

public interface IURLService {
	
	/**
	 * @apiNote this method will save the shorten URL and actual URL in mongoDB
	 * @param dto - DTO contains the actual URL;
	 * @return ResponseDTO containing status responseCode and response
	 *
	 */
	public ResponseDTO saveShortenURL(URLDTO dto);
	
	/**
	 * @apiNote this method will retrieve the actual URL based on shorten URL
	 * @param shortenURL 
	 * @return ResponseDTO containing status responseCode and response
	 *
	 */
	public ResponseDTO getActualURL(String shortenURL);
	
}
