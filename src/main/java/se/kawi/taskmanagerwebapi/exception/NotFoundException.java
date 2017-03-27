package se.kawi.taskmanagerwebapi.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundException extends WebApplicationException implements ExceptionMapper<NotFoundException>  {

	private static final long serialVersionUID = 1L;

	public NotFoundException() {
	        super("Could not find entity");
	    }

	public NotFoundException(String string) {
	        super(string);
	    }
	
	@Override
	public Response toResponse(NotFoundException exception) {
		return Response.status(404).entity(exception.getMessage()).type("text/plain").build();
	}

}
