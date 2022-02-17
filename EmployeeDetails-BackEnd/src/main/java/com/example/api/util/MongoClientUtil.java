package com.example.api.util;

import org.springframework.stereotype.Component;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MongoClientUtil{
	
	
	public MongoClient createConnection() {
		log.info("/MongoClientUtil.createConnection()");
		MongoClient mongoClient =  MongoClients.create("mongodb://localhost:27017/testMongodb");
		return mongoClient;
	}
//	
//	public void closeConn(MongoClient mongoClient) throws CloneNotSupportedException {
//		mongoClient.close();;
//		
//	}

}
