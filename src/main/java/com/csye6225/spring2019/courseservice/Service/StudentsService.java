
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
import com.csye6225.spring2019.courseservice.DataModel.Student;

public class StudentsService {
	static DynamoDBConnector dynamoDbConnector;
	DynamoDBMapper mapper;
	Table dynamoDBTable;
	DynamoDB dynamoDB;

	public StudentsService() {
		dynamoDbConnector = new DynamoDBConnector();
		dynamoDbConnector.init();
		dynamoDB = new DynamoDB(dynamoDbConnector.getClient());
		dynamoDBTable = dynamoDB.getTable("student");
		mapper = new DynamoDBMapper(dynamoDbConnector.getClient());
	}

	// Getting a list of all students
	// GET "..webapi/students"
	public List<Student> getAllStudents() {
		ArrayList<Student> allStudentsList = new ArrayList<>();
		ScanRequest scanRequest = new ScanRequest().withTableName("student");
		ScanResult result = dynamoDbConnector.getClient().scan(scanRequest);
		for (Map<String, AttributeValue> item : result.getItems()) {
			List<AttributeValue> tempList = new ArrayList<>(item.get("associatedCourses").getL());
			List<Long> associatedCourseList = new ArrayList<>();

			for (AttributeValue eachCourse : tempList) {
				associatedCourseList.add(Long.parseLong(eachCourse.getN()));
			}

			Student studentObject = new Student(item.get("id").getS(), Long.parseLong(item.get("studentId").getN()),
					item.get("firstName").getS(), associatedCourseList, item.get("enrolledProgramName").getS());
			allStudentsList.add(studentObject);
		}
		return allStudentsList;
	}

	public Student addStudent(Student student) {
		Random rand = new Random();
		long randomNumber = Math.abs(rand.nextLong());
		student.setStudentId(randomNumber);
		mapper.save(student);
		return student;
	}

	// Getting One Student
	public Student getStudent(long studentId) {
		Student studentObject=queryingStudent(studentId);
		return studentObject;
		}

	// Deleting a student
	public Student deleteStudent(long studentId) {
		Student deletedStudent = queryingStudent(studentId);
		mapper.delete(deletedStudent);
		return deletedStudent;
	}

	// Updating Student Info
	public Student updateStudentDetails(long studentId, Student student) {
		Student oldStudentObject=queryingStudent(studentId);
		oldStudentObject.setFirstName(student.getFirstName());
		oldStudentObject.setAssociatedCourses(student.getAssociatedCourses());
		oldStudentObject.setEnrolledProgramName(student.getEnrolledProgramName());
		mapper.save(oldStudentObject);

		return oldStudentObject;
	}
	
	private Student queryingStudent(long studentId) {
		Student queriedStudentObject;
		Map<String, String> expressionAttributeNames = new HashMap<>();
		expressionAttributeNames.put("#studentId", "studentId");
		Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
		expressionAttributeValues.put(":studentId", new AttributeValue().withN(String.valueOf(studentId)));
		
		DynamoDBQueryExpression<Student> queryExpression = new DynamoDBQueryExpression<Student>()
				.withIndexName("studentId-index").withKeyConditionExpression("#studentId=:studentId")
				.withExpressionAttributeNames(expressionAttributeNames)
				.withExpressionAttributeValues(expressionAttributeValues).withConsistentRead(false);

		List<Student> studentObject = mapper.query(Student.class, queryExpression);
		if(studentObject.size()>0) {
		queriedStudentObject = studentObject.get(0);
		}
		else {
			queriedStudentObject=new Student();
		}
		return queriedStudentObject;
	}

}