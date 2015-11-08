package main.java.service;

import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.omg.CORBA.portable.ApplicationException;

import main.java.dao.interfaces.NoteDao;
import main.java.model.Note;
import main.java.model.User;
import main.java.model.rest.RestAddNote;
import main.java.model.rest.RestUpdateNote;
import main.java.validator.EmailValidator;
import sun.misc.BASE64Decoder;


@Path("/note")
public class NoteService {
	@Inject
	private User user;
	@Inject
	private EmailValidator emailValidator;
	@Inject
	private NoteDao noteDao;
	
	@GET
	@Path("/getNotes")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getNotes(@QueryParam("userId")Long userId, @HeaderParam("authorization") String authString){
		
		try{
			if(!isUserAuthenticated(authString) && !user.getId().equals(userId)){
				return Response.status(401).entity("User authentication unsuccessful.").build();
			}else{
				return Response.ok().entity(user.getNotes()).build();
			}
		}catch(Exception e){
			return Response.status(401).entity("No notes with userId "+userId).build();
		}
	}
	
	
	@GET
	@Path("/getNote")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getNote(@QueryParam("noteId")Long noteId, @HeaderParam("authorization") String authString){
		
		try{
			if(!isUserAuthenticated(authString)){
				return Response.status(401).entity("User authentication unsuccessful.").build();
			}
			Note note = null;
			for(Note curNote:user.getNotes()){
				if(curNote.getId().equals(noteId)){
					note = curNote;
					break;
				}
			}
			
			if(note == null){
				return Response.status(401).entity("User authentication unsuccessful.").build();
			}else{
				return Response.ok().entity(note).build();
			}
		}catch(Exception e){
			return Response.status(401).entity("No notes with userId "+noteId).build();
		}
	}
	
	@GET
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(RestAddNote addNote, @HeaderParam("authorization") String authString){
		
		try{
			if(!isUserAuthenticated(authString)){
				return Response.status(401).entity("User authentication unsuccessful.").build();
			}
			Note note = new Note();
			if(null != addNote.getNote() && addNote.getNote().isEmpty()){
				note.setNote(addNote.getNote());
			}
			if(null != addNote.getTitle() && addNote.getTitle().isEmpty()){
				note.setTitle(addNote.getTitle());
			}
			note.setCreatedTime(new Date());
			note = noteDao.insert(note);
			return Response.ok().entity(note).build();
		}catch(Exception e){
			return Response.status(401).entity("Insert not successful.").build();
		}
	}
	
	@GET
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(RestUpdateNote updateNote, @HeaderParam("authorization") String authString){
		
		try{
			if(!isUserAuthenticated(authString)){
				return Response.status(401).entity("User authentication unsuccessful.").build();
			}
			
			Note note = null;
			for(Note curNote:user.getNotes()){
				if(curNote.getId().equals(updateNote.getId())){
					note = curNote;
					break;
				}
			}
			
			if(note == null){
				return Response.status(401).entity("User authentication unsuccessful.").build();
			}else{
				if(null != updateNote.getNote() && updateNote.getNote().isEmpty()){
					note.setNote(updateNote.getNote());
				}
				if(null != updateNote.getTitle() && updateNote.getTitle().isEmpty()){
					note.setTitle(updateNote.getTitle());
				}
				note.setLastUpdatedTime(new Date());
				noteDao.update(note);
				return Response.ok().entity(note).build();
			}
		}catch(Exception e){
			return Response.status(401).entity("Update not successful.").build();		
		}
	}
	
	@GET
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(@QueryParam("noteId")Long noteId, @HeaderParam("authorization") String authString){
		
		try{
			if(!isUserAuthenticated(authString)){
				return Response.status(401).entity("User authentication unsuccessful.").build();
			}
			Note note = null;
			for(Note curNote:user.getNotes()){
				if(curNote.getId().equals(noteId)){
					note = curNote;
					break;
				}
			}
			
			if(note == null){
				return Response.status(401).entity("User authentication unsuccessful.").build();
			}else{
				noteDao.delete(note);
				return Response.ok().entity(note).build();
			}
		}catch(Exception e){
			return Response.status(401).entity("Delete not successful.").build();
		}
	}
	
	@SuppressWarnings("restriction")
	private boolean isUserAuthenticated(String authString){
		String decodeAuth = null;
		String[] authParts = authString.split("\\s+");
		if(authParts.length>1){
			String authInfo = authParts[1];
			try{
				decodeAuth = new String(new BASE64Decoder().decodeBuffer(authInfo));
				if(decodeAuth.contains(":")){
					authParts = decodeAuth.split(":");
					if(authParts.length>1){
						user = emailValidator.findUser(authParts[0],authParts[1]);
						if(null!=user){
							return true;
						}					
					}
					
				}
			}catch(Exception e){
				return false;
			}
		}
		return false;		
	}

}
