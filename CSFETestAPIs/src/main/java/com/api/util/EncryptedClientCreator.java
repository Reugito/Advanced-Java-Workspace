package com.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.BsonDocument;
import org.bson.Document;
import org.springframework.stereotype.Component;

import com.mongodb.AutoEncryptionSettings;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Component
public class EncryptedClientCreator {
	
	public static final String DETERMINISTIC_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic";
    public static final String RANDOM_ENCRYPTION_TYPE = "AEAD_AES_256_CBC_HMAC_SHA_512-Random";

    public static Document buildEncryptedField(String bsonType, Boolean isDeterministic) {
        return new Document().
                append("encrypt", new Document()
                        .append("bsonType", bsonType)
                        .append("algorithm",
                                (isDeterministic) ? DETERMINISTIC_ENCRYPTION_TYPE : RANDOM_ENCRYPTION_TYPE));
    }

    public static Document buildEncryptMetadata(String keyId) {
        List<Document> keyIds = new ArrayList<Document>();
        keyIds.add(new Document()
                .append("$binary", new Document()
                        .append("base64", keyId)
                        .append("subType", "04")));
        return new Document().append("keyId", keyIds);
    }
    
    public static Document createJSONSchema(String keyId) {
        return new Document().append("bsonType", "object").append("encryptMetadata", buildEncryptMetadata(keyId))
                .append("properties", new Document()
                        .append("email", buildEncryptedField("string", true))
                        .append("password", buildEncryptedField("string", false)));
    }
    
    public MongoClient createEncryptedClient(byte[] localMasterKey, String dataKey) {
    	String keyVaultNamespace = "encryption.__keyVault";
    	
    	String dbAndColl = "test.coll";
    	
    	Document jsonSchema = createJSONSchema(dataKey);
    	
    	Map<String, Object> keyMap = new HashMap<String, Object>();
    	keyMap.put("key", localMasterKey);
    	
    	Map<String, Map<String, Object>> kmsProviders = new HashMap<String, Map<String, Object>>();
    	kmsProviders.put("local", keyMap);
    	
    	HashMap<String, BsonDocument> schemaMap = new HashMap<String, BsonDocument>();
    	schemaMap.put(dbAndColl, BsonDocument.parse(jsonSchema.toJson()));
    	
    	Map<String, Object> extraOptions = new HashMap<String, Object>();
    	extraOptions.put("mongocryptdBypassSpawn", true);
    	
    	MongoClientSettings clientSettings = MongoClientSettings.builder()
    		    .applyConnectionString(new ConnectionString("mongodb://localhost:27017"))
    		    .autoEncryptionSettings(AutoEncryptionSettings.builder()
    		        .keyVaultNamespace(keyVaultNamespace)
    		        .kmsProviders(kmsProviders)
    		        .schemaMap(schemaMap)
    		        .extraOptions(extraOptions)
    		        .build())
    		    .build();
    	MongoClient mongoClient = MongoClients.create(clientSettings);
    	return mongoClient;
    }
}
