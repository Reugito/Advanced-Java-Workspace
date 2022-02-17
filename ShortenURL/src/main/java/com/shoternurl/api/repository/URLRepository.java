package com.shoternurl.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shoternurl.api.entity.URLEntity;

public interface URLRepository extends MongoRepository<URLEntity, String> {

	List<URLEntity> findByShortenURL(String shortenURL);
	
	long countByShortenURL(String shortenURL);
	
	List<URLEntity> findByActualURL(String actualURL);
	
	long countByActualURL(String actualURL);
}
