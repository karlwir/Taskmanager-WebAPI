package se.kawi.taskmanagerwebapi.resource;

import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.BeanParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

import org.springframework.stereotype.Component;

import se.kawi.taskmanagerservicelib.model.User;
import se.kawi.taskmanagerservicelib.model.WorkItem;
import se.kawi.taskmanagerservicelib.service.UserService;
import se.kawi.taskmanagerwebapi.model.UserDTO;
import se.kawi.taskmanagerwebapi.model.WorkItemDTO;
import se.kawi.taskmanagerwebapi.resource.query.UserQueryBean;
import se.kawi.taskmanagerwebapi.resource.query.WorkItemQueryBean;
import se.kawi.taskmanagerwebapi.resource.validation.ValidUser;
import se.kawi.taskmanagerwebapi.resource.validation.ValidUserNew;
import se.kawi.taskmanagerwebapi.resource.validation.ValidWorkItem;

@Component
@Path("/users")
@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
@Consumes({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
public class UserResource extends BaseResource<User, UserService> {

	protected UserResource(UserService service) {
		super(service);
	}

	@POST
	public Response createUser(@ValidUserNew UserDTO userDTO) {
		return super.create(userDTO.buildUser());
	}

	@GET
	@Path("/{itemKey}")
	public Response getUser(@PathParam("itemKey") String itemKey) {
		return super.byItemKey(itemKey);
	}

	@GET
	public Response getUsers(@BeanParam UserQueryBean userQuery) {
		return super.get(userQuery.buildSpecification(), userQuery.buildPageable());
	}
	
	@GET
	@Path("/count")
	public Response countUsers(@BeanParam UserQueryBean userQuery) {
		return super.count(userQuery.buildSpecification());
	}

	@PUT
	public Response updateUser(@ValidUser UserDTO userDTO) {
		return serviceRequest(() -> {
			User user = service.getByItemKey(userDTO.getItemKey());
			if (user != null ) {
				service.save(userDTO.reflectDTO(user));
				return Response.noContent().build();
			} else {
				return Response.status(404).build();
			}
		});
	}

	@DELETE
	public Response deleteUser(@ValidUser UserDTO userDTO) {
		return super.delete(userDTO);
	}
	
	@GET
	@Path("/{itemKey}/workitems")
	public Response getuserWorkItems(@BeanParam WorkItemQueryBean workItemQuery, @PathParam("itemKey") String itemKey) {
		return serviceRequest(() -> {
			User user = service.getByItemKey(itemKey);
			if (user != null) {
				workItemQuery.setUser(user);
				List<WorkItem> userWorkItems = service.getUserWorkItems(workItemQuery.buildSpecification(), workItemQuery.buildPageable());
				return Response.ok().entity(dtoFactory.buildWorkItemDTOs(userWorkItems, true)).build();
			} else {
				return Response.status(404).build();
			}
		});	
	}
	
	@PUT
	@Path("/{itemKey}/workitems")
	public Response assignWorkItem(@ValidWorkItem WorkItemDTO workItemDTO, @PathParam("itemKey") String itemKey) {
		return serviceRequest(() -> {
			User user = service.getByItemKey(itemKey);
			if (user != null) {
				service.assignWorkItem(workItemDTO.getItemKey(), user);
				return Response.noContent().build();
			} else {
				return Response.status(404).build();
			}
		});
	}
	
	@DELETE
	@Path("/{itemKey}/workitems")
	public Response withdrawWorkItem(@ValidWorkItem WorkItemDTO workItemDTO, @PathParam("itemKey") String itemKey) {
		return serviceRequest(() -> {
			User user = service.getByItemKey(itemKey);
			if (user != null) {
				service.withdrawWorkItem(workItemDTO.getItemKey(), user);
				return Response.noContent().build();
			} else {
				return Response.status(404).build();
			}
		});		
	}

}