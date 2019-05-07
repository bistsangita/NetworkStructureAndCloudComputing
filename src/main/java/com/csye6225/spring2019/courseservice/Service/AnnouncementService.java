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



import com.csye6225.spring2019.courseservice.DataModel.*;

public class AnnouncementService {
	static DynamoDBConnector dynamoDbConnector;
	DynamoDBMapper mapper;
	Table dynamoDBTable;
	DynamoDB dynamoDB;

	public AnnouncementService() {
		dynamoDbConnector = new DynamoDBConnector();
		dynamoDbConnector.init();
		dynamoDB = new DynamoDB(dynamoDbConnector.getClient());
		dynamoDBTable = dynamoDB.getTable("announcement");
		mapper = new DynamoDBMapper(dynamoDbConnector.getClient());
	}

	// Getting a list of all boards
	// GET "..webapi/boards"
	public List<Announcement> getAllAnnouncements() {
		ArrayList<Announcement> allAnnouncementsList = new ArrayList<>();
		ScanRequest scanRequest = new ScanRequest().withTableName("announcement");
		ScanResult result = dynamoDbConnector.getClient().scan(scanRequest);
		for (Map<String, AttributeValue> item : result.getItems()) {
			Announcement announcementObject = new Announcement(item.get("id").getS(),
					Long.parseLong(item.get("announcementId").getN()), item.get("announcementText").getS(),
					Long.parseLong(item.get("boardId").getN()));
			allAnnouncementsList.add(announcementObject);
		}
		return allAnnouncementsList;
	}

	public Announcement addAnnouncement(Announcement announcement) {
		String checkString = announcement.getAnnouncementText();
		char[] checkArray = checkString.toCharArray();
		if (checkArray.length > 200) {
			System.out.println("Cannot add announcement");
			return new Announcement();
		} else {
			Random rand = new Random();
			long randomNumber = Math.abs(rand.nextLong());
			announcement.setAnnouncementId(randomNumber);
			mapper.save(announcement);
			return announcement;
		}
	}

	// Getting One Announcement
	public Announcement getAnnouncement( long boardId, long announcementId) {
			Announcement announcementObject = queryingAnnouncement(boardId,announcementId);
		return announcementObject;
	}

	// Deleting a Announcement
	public Announcement deleteAnnouncement(long announcementId, long boardId) {
		Announcement deletedAnnouncement = queryingAnnouncement(announcementId,boardId);
		mapper.delete(deletedAnnouncement);
		return deletedAnnouncement;
	}

	// Updating Announcement Info
	public Announcement updateAnnouncementDetails(long announcementId, long boardId, Announcement announcement) {
		Announcement oldAnnouncementObject = queryingAnnouncement(announcementId,boardId);
		oldAnnouncementObject.setAnnouncementText(announcement.getAnnouncementText());
		mapper.save(oldAnnouncementObject);
		return oldAnnouncementObject;
	}

	private Announcement queryingAnnouncement(long boardId,long announcementId) {
		Announcement queriedAnnouncementObject;
		Map<String, String> expressionAttributeNames = new HashMap<>();
		expressionAttributeNames.put("#boardId", "boardId");
		expressionAttributeNames.put("#announcementId", "announcementId");
	
		
		Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
		expressionAttributeValues.put(":boardId", new AttributeValue().withN(String.valueOf(boardId)));
		expressionAttributeValues.put(":announcementId", new AttributeValue().withN(String.valueOf(announcementId)));
		
		DynamoDBQueryExpression<Announcement> queryExpression = new DynamoDBQueryExpression<Announcement>()
				.withIndexName("boardId-announcementId-index")
				.withKeyConditionExpression("#boardId=:boardId and #announcementId=:announcementId")
				.withExpressionAttributeNames(expressionAttributeNames)
				.withExpressionAttributeValues(expressionAttributeValues).withConsistentRead(false); 
		List<Announcement> announcementObject = mapper.query(Announcement.class, queryExpression); 
	
		if (announcementObject.size() > 0) {
			queriedAnnouncementObject = announcementObject.get(0);
		} else {
			queriedAnnouncementObject = new Announcement();
		}
		return queriedAnnouncementObject;
	}
}