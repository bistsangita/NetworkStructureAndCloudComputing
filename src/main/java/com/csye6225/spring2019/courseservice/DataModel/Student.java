package com.csye6225.spring2019.courseservice.DataModel;
import java.util.List;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="student")
public class Student {
	private String id;
	private long studentId;
	private String firstName;
	private List<Long> associatedCourses;
	private String enrolledProgramName;
	
	
	public Student() {
		
	}
	

	public Student(String id,long studentId, String firstName, List<Long> associatedCourses, String enrolledProgramName) {
		this.id=id;
		this.studentId = studentId;
		this.firstName=firstName;
		this.associatedCourses=associatedCourses;
		this.enrolledProgramName=enrolledProgramName;
	}

	@DynamoDBIndexHashKey(attributeName = "studentId",globalSecondaryIndexName = "studentId-index")
	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	@DynamoDBAttribute(attributeName="firstName")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@DynamoDBAttribute(attributeName="associatedCourses")
	public List<Long> getAssociatedCourses() {
		return associatedCourses;
	}

	public void setAssociatedCourses(List<Long> associatedCourses) {
		this.associatedCourses = associatedCourses;
	}
	
	@DynamoDBAttribute(attributeName="enrolledProgramName")
	public String getEnrolledProgramName() {
		return enrolledProgramName;
	}

	public void setEnrolledProgramName(String enrolledProgramName) {
		this.enrolledProgramName = enrolledProgramName;
	}

	@DynamoDBHashKey(attributeName = "id")
	@DynamoDBAutoGeneratedKey
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@DynamoDBIgnore
	@Override
	public String toString() { 
		return "StudentID=" + getStudentId() + ", firstName=" + getFirstName()
				+ ", associatedCourses=" + getAssociatedCourses() + ", enrolledProgramName=" + getEnrolledProgramName();
	}
	
}