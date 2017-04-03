package com.intuit.interview.rest.resources;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * {@link GameExceptionMapper} is the intermediate class to catch all the exceptions thrown and returns
 * a text/plain to be displayed to the end user.
 * @author gkumar9
 *
 */
@Provider
public class GameExceptionMapper implements ExceptionMapper<Exception> {

	public Response toResponse(Exception ex) {
		return Response.status(Status.INTERNAL_SERVER_ERROR)
				.entity(ex.getMessage())
				.type(MediaType.TEXT_PLAIN)
				.build();
	}

}
