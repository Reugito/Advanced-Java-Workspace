package com.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.BsonBinary;
import org.bson.BsonDocument;
import org.bson.Document;
import org.springframework.stereotype.Component;

import com.mongodb.ClientEncryptionSettings;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.vault.DataKeyOptions;
import com.mongodb.client.vault.ClientEncryption;
import com.mongodb.client.vault.ClientEncryptions;

public class Util {

	public static byte[] createAndWriteMasterKey(String filePath) {

		byte[] localMasterKey = new byte[96];
		new SecureRandom().nextBytes(localMasterKey);

		try (FileOutputStream stream = new FileOutputStream(filePath)) {
			stream.write(localMasterKey);
		} catch (Exception e) {
			System.out.println(" filed to write local key in file");
		}
		return localMasterKey;
	}

	public static byte[] readMasterKeyfromFile(String filePath) {

		byte[] localMasterKey = new byte[96];

		try (FileInputStream fis = new FileInputStream(filePath)) {
			fis.readNBytes(localMasterKey, 0, 96);
		} catch (Exception e) {
			System.out.println("Failed to Read master key");
		}

		return localMasterKey;
	}

	public static byte[] getLocalMasterKey(String filename) {
		File file = new File(filename);

		if (file.exists()) {
			return readMasterKeyfromFile(filename);
		} else {
			return createAndWriteMasterKey(filename);
		}
	}

	public static String getDataEncryptionKey() {
		String connectionString = "mongodb://localhost:27017";

		String encryptionKey = findDataEncryptionKey(connectionString, "demo-key", "encryption", "__keyVault");

		if (encryptionKey == null) {
			String key = createDataEncryptionKey();
		}
		return encryptionKey;
	}

	public static String findDataEncryptionKey(String connectionString, String keyAltName, String keyDb,
			String keyColl) {
		try (MongoClient mongoClient = MongoClients.create(connectionString);) {
			Document query = new Document("keyAltNames", keyAltName);
			MongoCollection<Document> collection = mongoClient.getDatabase(keyDb).getCollection(keyColl);
			BsonDocument doc = collection.withDocumentClass(BsonDocument.class).find(query).first();

			if (doc != null) {
				return Base64.getEncoder().encodeToString(doc.getBinary("_id").getData());
			}
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}
		return null;
	}

	public static String createDataEncryptionKey() {
		String connectionString = "mongodb://localhost:27017";
		String keyVaultNamespace = "encryption.__keyVault";
		List<String> keyAltNames = new ArrayList<>();
		keyAltNames.add("demo-key");

		byte[] localMasterKey = getLocalMasterKey("master-key-doc.txt");
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put("key", localMasterKey);
		Map<String, Map<String, Object>> kmsProviders = new HashMap<String, Map<String, Object>>();
		kmsProviders.put("local", keyMap);

		ClientEncryptionSettings clientEncryptionSettings = ClientEncryptionSettings.builder()
				.keyVaultMongoClientSettings(MongoClientSettings.builder()
						.applyConnectionString(new ConnectionString(connectionString)).build())
				.keyVaultNamespace(keyVaultNamespace).kmsProviders(kmsProviders).build();

		ClientEncryption clientEncryption = ClientEncryptions.create(clientEncryptionSettings);

		BsonBinary dataKeyId = clientEncryption.createDataKey("local", new DataKeyOptions().keyAltNames(keyAltNames));

		String base64DataKeyId = Base64.getEncoder().encodeToString(dataKeyId.getData());
		System.out.println("DataKeyId [base64]: " + base64DataKeyId);

		return base64DataKeyId;
	}
}
