package com.todoapp;

import static spark.Spark.*;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

public class Bootstrap {
	
	private static final String IP_ADDRESS = System.getenv("OPENSHIFT_DIY_IP") != null ? System.getenv("OPENSHIFT_DIY_IP") : "localhost";
    private static final int PORT = System.getenv("OPENSHIFT_DIY_PORT") != null ? Integer.parseInt(System.getenv("OPENSHIFT_DIY_PORT")) : 8080;
	
	public static void main(String[] args) throws Exception {
		ipAddress(IP_ADDRESS);
        port(PORT);
        staticFileLocation("/public");
        new TodoResource(new TodoService(mongo()));
	}
	
	private static DB mongo() throws Exception {
		System.out.println("OPENSHIFT_MONGODB_DB_HOST: " + System.getenv("OPENSHIFT_MONGODB_DB_HOST"));
		System.out.println("OPENSHIFT_MONGODB_DB_PORT: " + System.getenv("OPENSHIFT_MONGODB_DB_PORT"));
		System.out.println("OPENSHIFT_APP_NAME: " + System.getenv("OPENSHIFT_APP_NAME"));
		System.out.println("OPENSHIFT_MONGODB_DB_PASSWORD: " + System.getenv("OPENSHIFT_MONGODB_DB_USERNAME"));
		System.out.println("OPENSHIFT_MONGODB_DB_USERNAME: " + System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD"));
        String host = System.getenv("OPENSHIFT_MONGODB_DB_HOST: " + System.getenv("OPENSHIFT_MONGODB_DB_HOST"));
        if (host == null) {
            MongoClient mongoClient = new MongoClient("localhost");
            return mongoClient.getDB("todoapp");
        }
        int port = Integer.parseInt(System.getenv("OPENSHIFT_MONGODB_DB_PORT"));
        String dbname = System.getenv("OPENSHIFT_APP_NAME");
        String username = System.getenv("OPENSHIFT_MONGODB_DB_USERNAME");
        String password = System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD");
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().build();
        MongoClient mongoClient = new MongoClient(new ServerAddress(host, port), mongoClientOptions);
        mongoClient.setWriteConcern(WriteConcern.SAFE);
        DB db = mongoClient.getDB(dbname);
        if (db.authenticate(username, password.toCharArray())) {
        	System.out.println("Entro aca");
            return db;
        } else {
            throw new RuntimeException("Not able to authenticate with MongoDB");
        }
    }

}
