package se.kawi.taskmanagerwebapi.resource;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import se.kawi.taskmanagerservicelib.model.User;
import se.kawi.taskmanagerservicelib.service.UserService;
import se.kawi.taskmanagerwebapi.resource.query.UserQueryBean;

@Component
@Path("/signin")
@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
@Consumes({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
public class SignInResource extends BaseResource<User, UserService> {

	protected SignInResource(UserService service) {
		super(service);
	}

	@GET
	@Path("/email/{email}")
	public Response getUser(@PathParam("email") String email, @BeanParam UserQueryBean userQuery) {
		userQuery.setEmail(email);
		return super.getOne(userQuery.buildSpecification(), userQuery.buildPageable());
	}
	
}
