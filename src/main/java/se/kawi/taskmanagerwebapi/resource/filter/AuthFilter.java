package se.kawi.taskmanagerwebapi.resource.filter;

import java.io.IOException;

//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Response.Status;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;

@PreMatching
public class AuthFilter implements ContainerRequestFilter {

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
//		if (requestContext.getHeaders().get("api-key") == null || !requestContext.getHeaders().get("api-key").contains("secretkey")) {
//			requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity("Api-key must be defined.").build());
//		}
	}

}
