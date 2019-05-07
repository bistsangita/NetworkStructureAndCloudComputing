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

public class BoardService {
	static DynamoDBConnector dynamoDbConnector;
	DynamoDBMapper mapper;
	Table dynamoDBTable;
	DynamoDB dynamoDB;

	public BoardService() {
		dynamoDbConnector = new DynamoDBConnector();
		dynamoDbConnector.init();
		dynamoDB = new DynamoDB(dynamoDbConnector.getClient());
		dynamoDBTable = dynamoDB.getTable("board");
		mapper = new DynamoDBMapper(dynamoDbConnector.getClient());
	}

	// Getting a list of all boards
	// GET "..webapi/boards"
	public List<Board> getAllBoards() {
		ArrayList<Board> allBoardsList = new ArrayList<>();
		ScanRequest scanRequest = new ScanRequest().withTableName("board");
		ScanResult result = dynamoDbConnector.getClient().scan(scanRequest);
		for (Map<String, AttributeValue> item : result.getItems()) {

			Board boardObject = new Board(item.get("id").getS(), Long.parseLong(item.get("boardId").getN()),
					Long.parseLong(item.get("courseId").getN()));
			allBoardsList.add(boardObject);
		}
		return allBoardsList;
	}

	public Board addBoard(Board board) {
		Random rand = new Random();
		long randomNumber = Math.abs(rand.nextLong());
		board.setBoardId(randomNumber);
		mapper.save(board);
		return board;
	}

	// Getting One Board
	public Board getBoard(long boardId) {
		Board boardObject = queryingBoard(boardId);
		return boardObject;
	}

	// Deleting a board
	public Board deleteBoard(long boardId){
		Board deletedBoard = queryingBoard(boardId);
		mapper.delete(deletedBoard);
		return deletedBoard;
	}

	// Updating Board Info
	public Board updateBoardDetails(long boardId, Board board) {
		Board oldBoardObject = queryingBoard(boardId);
		oldBoardObject.setCourseId(board.getCourseId());
		mapper.save(oldBoardObject);
		return oldBoardObject;
	}
	
	private Board queryingBoard(long boardId) {
		Board queriedBoardObject;
		Map<String, String> expressionAttributeNames = new HashMap<>();
		expressionAttributeNames.put("#boardId", "boardId");
		Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
		expressionAttributeValues.put(":boardId", new AttributeValue().withN(String.valueOf(boardId)));
		
		DynamoDBQueryExpression<Board> queryExpression = new DynamoDBQueryExpression<Board>()
				.withIndexName("boardId-index").withKeyConditionExpression("#boardId=:boardId")
				.withExpressionAttributeNames(expressionAttributeNames)
				.withExpressionAttributeValues(expressionAttributeValues).withConsistentRead(false);

		List<Board> boardObject = mapper.query(Board.class, queryExpression);
		if(boardObject.size()>0) {
			queriedBoardObject = boardObject.get(0);
		}
		else {
			queriedBoardObject=new Board();
		}
		return queriedBoardObject;
	}

}