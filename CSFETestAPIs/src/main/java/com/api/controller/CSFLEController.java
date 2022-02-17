package com.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.util.EncryptedClientCreator;
import com.api.util.Util;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

@RestController
@RequestMapping("csfle")
public class CSFLEController {

	@Autowired
	EncryptedClientCreator clientCreator;

	@Value("${db}")
	String db ;
	
	@Value("${dbcollection}")
	String dbcoll ;
	
	@Value("${filename}")
	String filename = "master-key-doc.txt";
	
	String dataKey = Util.getDataEncryptionKey();
	
	byte[] localKey = Util.readMasterKeyfromFile(filename);
	
	 private Semaphore mutex = new Semaphore(1);
	
	@GetMapping("/")
	public ResponseEntity<Object> getAll() {
		MongoClient client = clientCreator.createEncryptedClient(localKey, dataKey);
		List<Document> details = new ArrayList<>();
		try {
			details = getDetails(client, details);
			return new ResponseEntity<>(details, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println("Failed to read collection");
		} finally {
			client.close();
		}
		return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
	}
	
	private List<Document> getDetails(MongoClient client, List<Document> details) {
		try {
			MongoCollection<Document> collection = client.getDatabase(db).getCollection(dbcoll);
			
			mutex.acquire();
			FindIterable<Document> all = collection.find();
			for (Document d : all) {
				details.add(d);
			}
			mutex.release();
		} catch (Exception e) {
			System.out.println("Error in getDetails()..");
		}
		return details;
	}

	@PostMapping("/add")
	public ResponseEntity<Object> add(@RequestBody Document body) {
		System.out.println("Received data : " + body);

		MongoClient client = clientCreator.createEncryptedClient(localKey, dataKey);

		try {
			MongoCollection<Document> collection = client.getDatabase(db).getCollection(dbcoll);
			collection.insertOne(body);
			return new ResponseEntity<>("Successfully Added Data", HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(" Error in Add Method");
		} finally {
			client.close();
			System.out.println("Colsed connection");
		}
		return new ResponseEntity<>("Failed Add Data", HttpStatus.BAD_REQUEST);
	}
	
	

}
