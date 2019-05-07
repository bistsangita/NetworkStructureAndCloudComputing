.
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

import com.csye6225.spring2019.courseservice.DataModel.Announcement;

import com.csye6225.spring2019.courseservice.service.AnnouncementService;

//.. /webapi/myresource
@Path("announcements")
public class AnnouncementResource {
	AnnouncementService announcementServiceObject = new AnnouncementService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Announcement> getAllAnnouncements() {
		return announcementServiceObject.getAllAnnouncements();
	}

	// ... webapi/announcement/1/2
	@GET
	@Path("/{boardId}/{announcementId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Announcement getAnnouncement(@PathParam("boardId") long boardId,@PathParam("announcementId")long announcementId) {
		return announcementServiceObject.getAnnouncement(boardId,announcementId);
	}

	@DELETE
	@Path("/{boardId}/{announcementId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Announcement deleteAnnouncement(@PathParam("boardId")long boardId, @PathParam("announcementId") long announcementId) {
		return announcementServiceObject.deleteAnnouncement(boardId,announcementId);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Announcement addAnnouncement(Announcement announcement) {
		return announcementServiceObject.addAnnouncement(announcement);
	}

	@PUT
	@Path("/{boardId}/{announcementId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Announcement updateAnnouncement(@PathParam("boardId")long boardId, @PathParam("announcementId") long announcementId, Announcement announcement) {
		return announcementServiceObject.updateAnnouncementDetails(boardId, announcementId, announcement);
	}

}