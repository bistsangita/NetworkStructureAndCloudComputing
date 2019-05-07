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


import com.csye6225.spring2019.courseservice.DataModel.Course;
import com.csye6225.spring2019.courseservice.DataModel.DynamoDBConnector;

public class CourseService {
	static DynamoDBConnector dynamoDbConnector;
	DynamoDBMapper mapper;
	Table dynamoDBTable;
	DynamoDB dynamoDB;

	public CourseService() {
		dynamoDbConnector = new DynamoDBConnector();
		dynamoDbConnector.init();
		dynamoDB = new DynamoDB(dynamoDbConnector.getClient());
		dynamoDBTable = dynamoDB.getTable("course");
		mapper = new DynamoDBMapper(dynamoDbConnector.getClient());
	}


	public List<Course> getAllCourses() {
		// Getting the list
		ArrayList<Course> allCoursesList = new ArrayList<>();
		ScanRequest scanRequest = new ScanRequest().withTableName("course");
		ScanResult result = dynamoDbConnector.getClient().scan(scanRequest);
		for (Map<String, AttributeValue> item : result.getItems()) {
			List<AttributeValue> tempLectureList = new ArrayList<>(item.get("associatedLectureId").getL());
			List<AttributeValue> tempStudentsList = new ArrayList<>(item.get("associatedStudentsId").getL());

			List<Long> associatedLectures = new ArrayList<>();
			List<Long> associatedStudents = new ArrayList<>();
			for (AttributeValue eachLecture : tempLectureList) {
				associatedLectures.add(Long.parseLong(eachLecture.getN()));
			}
			for (AttributeValue eachStudent : tempStudentsList) {
				associatedStudents.add(Long.parseLong(eachStudent.getN()));
			}

			Course courseObject = new Course(item.get("id").getS(), Long.parseLong(item.get("courseId").getN()),
					item.get("courseName").getS(), associatedLectures, associatedStudents,
					Long.parseLong(item.get("associatedProfessorId").getN()),
					Long.parseLong(item.get("boardId").getN()), Long.parseLong(item.get("associatedTAId").getN()));

			allCoursesList.add(courseObject);
		}
		return allCoursesList;
	}

	// Adding a course
	public Course addCourse(Course course) {
		Random rand = new Random();
		long randomNumber =Math.abs(rand.nextLong());
		course.setCourseId(randomNumber);
		mapper.save(course);
		return course;
	}

	// Getting One Course
	public Course getCourse(long courseId) {
		Course courseObject = queryingCourse(courseId);
		return courseObject;
	}

	// Deleting a course
	public Course deleteCourse(long courseId) {
		Course deletedCourse = queryingCourse(courseId);
		mapper.delete(deletedCourse);
		return deletedCourse;
	}

	// Updating Course Info
	public Course updateCourse(long courseId, Course course) {
		Course oldCourseObject = queryingCourse(courseId);
		oldCourseObject.setCourseName(course.getCourseName());
		oldCourseObject.setAssociatedLectureId(course.getAssociatedLectureId());
		oldCourseObject.setAssociatedStudentsId(course.getAssociatedStudentsId());
		oldCourseObject.setAssociatedProfessorId(course.getAssociatedProfessorId());
		oldCourseObject.setAssociatedTAId(course.getAssociatedTAId());
		oldCourseObject.setBoardId(course.getBoardId());

		mapper.save(oldCourseObject);

		return oldCourseObject;
	}

	private Course queryingCourse(long courseId) {
		Course queriedCourseObject;
		Map<String, String> expressionAttributeNames = new HashMap<>();
		expressionAttributeNames.put("#courseId", "courseId");
		Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
		expressionAttributeValues.put(":courseId", new AttributeValue().withN(String.valueOf(courseId)));

		DynamoDBQueryExpression<Course> queryExpression = new DynamoDBQueryExpression<Course>()
				.withIndexName("courseId-index").withKeyConditionExpression("#courseId=:courseId")
				.withExpressionAttributeNames(expressionAttributeNames)
				.withExpressionAttributeValues(expressionAttributeValues).withConsistentRead(false);

		List<Course> courseObject = mapper.query(Course.class, queryExpression);
		if (courseObject.size() > 0) {
			queriedCourseObject = courseObject.get(0);
		} else {
			queriedCourseObject = new Course();
		}
		return queriedCourseObject;
	}

}