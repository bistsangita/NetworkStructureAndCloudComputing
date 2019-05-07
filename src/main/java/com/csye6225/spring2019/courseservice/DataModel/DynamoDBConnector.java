package com.csye6225.spring2019.courseservice.DataModel;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

public class DynamoDBConnector {
	static AmazonDynamoDB dynamoDB;

	public static void init() {
		if (dynamoDB == null) {
			InstanceProfileCredentialsProvider credentialsProvider = new InstanceProfileCredentialsProvider(false);
			credentialsProvider.getCredentials();
		
			dynamoDB = AmazonDynamoDBClientBuilder.standard().withCredentials(credentialsProvider)
					.withRegion("us-west-2").build();
			
			System.out.println("\nClient Created\n");
		}
	}

	public AmazonDynamoDB getClient() {
		return dynamoDB;
	}

}