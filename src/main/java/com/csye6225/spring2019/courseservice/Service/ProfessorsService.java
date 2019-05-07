package com.csye6225.spring2019.courseservice.Service;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;


import com.csye6225.spring2019.courseservice.DataModel.DynamoDBConnector;
import com.csye6225.spring2019.courseservice.DataModel.Professor;

public class ProfessorsService {
	static DynamoDBConnector dynamoDbConnector;
	DynamoDBMapper mapper;
	Table dynamoDBTable;
	DynamoDB dynamoDB;

	public ProfessorsService() {
		dynamoDbConnector = new DynamoDBConnector();
		dynamoDbConnector.init();
		dynamoDB = new DynamoDB(dynamoDbConnector.getClient());
		dynamoDBTable = dynamoDB.getTable("professor");
		mapper = new DynamoDBMapper(dynamoDbConnector.getClient());
	}

	// Getting a list of all professor
	// GET "..webapi/professors"
	public List<Professor> getAllProfessors() {
		// Getting the list
		ArrayList<Professor> list = new ArrayList<>();
		ScanRequest scanRequest = new ScanRequest().withTableName("professor");
		ScanResult result = dynamoDbConnector.getClient().scan(scanRequest);
		for (Map<String, AttributeValue> item : result.getItems()) {
			Professor professorObject = new Professor(item.get("id").getS(),
					Long.parseLong(item.get("professorId").getN()), item.get("firstName").getS(),
					item.get("department").getS(), item.get("joiningDate").getS());
			list.add(professorObject);
		}
		return list;
	}

	public Professor addProfessor(Professor prof) {
		Random rand = new Random();
		long randomNumber = Math.abs(rand.nextLong());
		prof.setProfessorId(randomNumber);
		mapper.save(prof);
		return prof;
	}

	// Getting One Professor
	public Professor getProfessor(long professorId) {
		Professor professorObject = queryingProfessor(professorId);
		return professorObject;
	}

	// Deleting a professor
	public Professor deleteProfessor(long professorId) {
		Professor deletedProfObject = queryingProfessor(professorId);
		mapper.delete(deletedProfObject);
		return deletedProfObject;
	}

	// Updating Professor Info
	public Professor updateProfessorInformation(long professorId, Professor prof) {
		Professor oldProfObject = queryingProfessor(professorId);
		oldProfObject.setFirstName(prof.getFirstName());
		oldProfObject.setDepartment(prof.getDepartment());
		oldProfObject.setJoiningDate(prof.getJoiningDate());
		mapper.save(oldProfObject);

		return oldProfObject;
	}

	private Professor queryingProfessor(long professorId) {
		Professor queriedProfessorObject;
		Map<String, String> expressionAttributeNames = new HashMap<>();
		expressionAttributeNames.put("#professorId", "professorId");
		Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
		expressionAttributeValues.put(":professorId", new AttributeValue().withN(String.valueOf(professorId)));

		DynamoDBQueryExpression<Professor> queryExpression = new DynamoDBQueryExpression<Professor>()
				.withIndexName("professorId-index").withKeyConditionExpression("#professorId=:professorId")
				.withExpressionAttributeNames(expressionAttributeNames)
				.withExpressionAttributeValues(expressionAttributeValues).withConsistentRead(false);

		List<Professor> professorObject = mapper.query(Professor.class, queryExpression);
		if (professorObject.size() > 0) {
			queriedProfessorObject = professorObject.get(0);
		} else {
			queriedProfessorObject = new Professor();
		}
		return queriedProfessorObject;
	}

}