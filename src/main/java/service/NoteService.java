package main.java.service;

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

import main.java.model.Note;
import main.java.model.User;
import main.java.model.rest.RestSearchNote;
import main.java.validator.EmailValidator;
import sun.misc.BASE64Decoder;


@Path("/note")
public class NoteService {
	@Inject
	private User user;
	@Inject
	private EmailValidator emailValidator;
	
	@GET
	@Path("/getNotes")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getNotes(@QueryParam("userId")Long userId, @HeaderParam("authorization") String authString) throws ApplicationException{
		
		try{
			if(!isUserAuthenticated(authString) && !user.getId().equals(userId)){
				return Response.status(401).entity("User is not authenticated").build();
			}else{
				return Response.ok().entity(user.getNotes()).build();
			}
		}catch(Exception e){
			throw new ApplicationException(e.getMessage(),null);
		}
	}
	
	
	@GET
	@Path("/getNote")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getNote(@QueryParam("noteId")Long noteId, @HeaderParam("authorization") String authString) throws ApplicationException{
		
		try{
			if(!isUserAuthenticated(authString)){
				return Response.status(401).entity("User is not authenticated").build();
			}
			Note note = null;
			for(Note curNote:user.getNotes()){
				if(curNote.getId().equals(noteId)){
					note = curNote;
					break;
				}
			}
			
			if(note == null){
				return Response.status(401).entity("User is not authenticated").build();
			}else{
				return Response.ok().entity(note).build();
			}
		}catch(Exception e){
			throw new ApplicationException(e.getMessage(),null);
		}
	}
	
	@GET
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(RestSearchNote searchNote, @HeaderParam("authorization") String authString) throws ApplicationException{
		
		try{
			if(!isUserAuthenticated(authString)){
				return Response.status(401).entity("User is not authenticated").build();
			}
		}catch(Exception e){
			throw new ApplicationException(e.getMessage(),null);
		}
		return Response.ok().build();
	}
	
	@GET
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(RestSearchNote searchNote, @HeaderParam("authorization") String authString) throws ApplicationException{
		
		try{
			if(!isUserAuthenticated(authString)){
				return Response.status(401).entity("User is not authenticated").build();
			}
		}catch(Exception e){
			throw new ApplicationException(e.getMessage(),null);		
		}
		return Response.ok().build();
	}
	
	@GET
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(RestSearchNote searchNote, @HeaderParam("authorization") String authString) throws ApplicationException{
		
		try{
			if(!isUserAuthenticated(authString)){
				return Response.status(401).entity("User is not authenticated").build();
			}
		}catch(Exception e){
			throw new ApplicationException(e.getMessage(),null);
		}
		return Response.ok().build();
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
