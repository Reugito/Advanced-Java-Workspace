package com.shoternurl.api.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoternurl.api.dto.ResponseDTO;
import com.shoternurl.api.dto.URLDTO;
import com.shoternurl.api.entity.URLEntity;
import com.shoternurl.api.repository.URLRepository;
import com.shoternurl.api.util.ShortenURLGenerator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class URLService implements IURLService {

	@Autowired
	ShortenURLGenerator urlGenerator;

	@Autowired
	URLRepository urlRepo;

	/**
	 * @apiNote this method will save the shorten URL and actual URL in mongoDB
	 * @param dto - DTO contains the actual URL;
	 * @return ResponseDTO containing status responseCode and response
	 *
	 */
	@Override
	public ResponseDTO saveShortenURL(URLDTO dto) {
		log.info("/URLService.saveShortenURL(URLDTO dto)");
		try {
			if (urlRepo.countByActualURL(dto.actualURL) == 0) {
				String validshortenURL = "";
				do {
					String shortenURL = urlGenerator.getShortenURL(16);
					if (urlRepo.countByShortenURL(shortenURL) == 0 && !shortenURL.isEmpty())
						validshortenURL = shortenURL;
				} while (validshortenURL.isEmpty());
				URLEntity entity = new URLEntity(validshortenURL, dto.getActualURL());
				urlRepo.save(entity);
				return new ResponseDTO("Successful", 200, "Successfully added the url");
			} else if (urlRepo.countByActualURL(dto.actualURL) != 0)
				return new ResponseDTO("Failed", 400, "this actualUrl alrady present");
		} catch (Exception e) {
			log.error("ERROR in /URLService.saveShortenURL(URLDTO dto)");
		}
		return new ResponseDTO("Failed", 400, "somthing went wrong");
	}

	/**
	 * @apiNote this method will retrieve the actual URL based on shorten URL
	 * @param shortenURL
	 * @return ResponseDTO containing status responseCode and response
	 *
	 */
	@Override
	public ResponseDTO getActualURL(String shortenURL) {
		try {
			log.info("/URLService.redirectToActualURL(String shortenURL)");
			if (urlRepo.countByShortenURL(shortenURL) != 0)
				return new ResponseDTO("successful", 200, urlRepo.findByShortenURL(shortenURL).get(0).getActualURL());
			else if (urlRepo.countByShortenURL(shortenURL) == 0)
				return new ResponseDTO("Failed", 400, "This shortern url not present");
		} catch (Exception e) {
			log.error("ERROR in /URLService.redirectToActualURL(String shortenURL)");
		}
		return new ResponseDTO("Failed", 400, urlRepo.countByShortenURL(shortenURL) == 0);
	}
}
