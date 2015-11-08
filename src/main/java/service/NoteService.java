package main.java.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import main.java.model.User;
import main.java.model.rest.RestSearchNote;
import main.java.validator.EmailValidator;
import sun.misc.BASE64Decoder;

import com.google.inject.Inject;

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
	public Response getNotes(RestSearchNote searchNote, @HeaderParam("authorization") String authString){
		
		try{
			if(!isUserAuthenticated(authString)){
				return Response.status(401).entity("User is not authenticated").build();
			}
		}catch(Exception e){
			
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