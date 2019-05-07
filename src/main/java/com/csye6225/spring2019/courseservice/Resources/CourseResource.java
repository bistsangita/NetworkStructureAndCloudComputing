package com.csye6225.spring2019.courseservice.Resources;



import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import com.csye6225.spring2019.courseservice.DataModel.Course;
import com.csye6225.spring2019.courseservice.Service.CoursesService;

//.. /webapi/myresource
@Path("courses")

public class CourseResource {
	CourseService courseServiceObject = new CourseService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Course> getAllCourses() {
		return courseServiceObject.getAllCourses();
	}

	// ... webapi/courses/1
	@GET
	@Path("/courses/{courseId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Course getCourse(@PathParam("courseId") long courseId) {
		return courseServiceObject.getCourse(courseId);
	}

	@DELETE
	@Path("/{courseId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Course deleteCourse(@PathParam("courseId") long courseId) {
		return courseServiceObject.deleteCourse(courseId);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Course addCourse(Course course) {
		return courseServiceObject.addCourse(course);
	}

	@PUT
	@Path("/{courseId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Course updateCourse(@PathParam("courseId") long courseId, Course course) {
		return courseServiceObject.updateCourse(courseId, course);
	}

}
