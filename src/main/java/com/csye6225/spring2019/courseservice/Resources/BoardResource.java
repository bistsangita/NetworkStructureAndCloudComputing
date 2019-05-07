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

import com.csye6225.spring2019.courseservice.DataModel.Board;

import com.csye6225.spring2019.courseservice.Service.BoardService;

//.. /webapi/myresource
@Path("board")
public class BoardResource {
	BoardService boardServiceObject = new BoardService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Board> getAllBoards() {
		return boardServiceObject.getAllBoards();
	}

	// ... webapi/board/1
	@GET
	@Path("/{boardId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Board getBoard(@PathParam("boardId") long boardId) {
		return boardServiceObject.getBoard(boardId);
	}

	@DELETE
	@Path("/{boardId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Board deleteBoard(@PathParam("boardId") long boardId) {
		return boardServiceObject.deleteBoard(boardId);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Board addBoard(Board board) {
		return boardServiceObject.addBoard(board);
	}

	@PUT
	@Path("/{boardId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Board updateBoard(@PathParam("boardId") long boardId, Board board) {
		return boardServiceObject.updateBoardDetails(boardId, board);
	}

}