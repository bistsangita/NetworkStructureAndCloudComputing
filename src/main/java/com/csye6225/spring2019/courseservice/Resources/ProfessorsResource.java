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



import com.csye6225.spring2019.courseservice.DataModel.Professor;
import com.csye6225.spring2019.courseservice.Service.ProfessorsService;


@Path("professors")
public class ProfessorsResource {

	ProfessorsService profService = new ProfessorsService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Professor> getProfessors() {
		return profService.getAllProfessors();
	}

	// ... webapi/professors/1
	@GET
	@Path("/{professorId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Professor getProfessor(@PathParam("professorId") long professorId) {
		return profService.getProfessor(professorId);
	}

	@DELETE
	@Path("/{professorId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Professor deleteProfessor(@PathParam("professorId") long professorId) {
		return profService.deleteProfessor(professorId);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Professor addProfessor(Professor prof) {
		return profService.addProfessor(prof);
	}

	@PUT
	@Path("/{professorId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Professor updateProfessor(@PathParam("professorId") long professorId, Professor prof) {
		return profService.updateProfessorInformation(professorId, prof);
	}

}