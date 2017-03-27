package se.kawi.taskmanagerwebapi.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BadRequestException extends WebApplicationException implements ExceptionMapper<BadRequestException> {

	private static final long serialVersionUID = 1L;

	public BadRequestException() {
	        super("Input data had bad format");
	    }

	public BadRequestException(String string) {
	        super(string);
	    }

	@Override
	public Response toResponse(BadRequestException exception) {
		return Response.status(400).entity(exception.getMessage()).type("text/plain").build();
	}

}
