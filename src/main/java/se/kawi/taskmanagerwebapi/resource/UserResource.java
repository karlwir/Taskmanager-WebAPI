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

import java.net.URI;
import java.util.List;

import org.springframework.stereotype.Component;

import se.kawi.taskmanagerservicelib.model.User;
import se.kawi.taskmanagerservicelib.model.WorkItem;
import se.kawi.taskmanagerwebapi.exception.BadRequestException;
import se.kawi.taskmanagerwebapi.model.UserDTO;
import se.kawi.taskmanagerwebapi.model.WorkItemDTO;
import se.kawi.taskmanagerservicelib.service.UserService;
import se.kawi.taskmanagerwebapi.resource.query.UserQueryBean;
import se.kawi.taskmanagerwebapi.resource.query.WorkItemQueryBean;
import se.kawi.taskmanagerwebapi.resource.validation.ValidUser;
import se.kawi.taskmanagerwebapi.resource.validation.ValidUserNew;
import se.kawi.taskmanagerwebapi.resource.validation.ValidWorkItem;
import se.kawi.taskmanagerwebapi.resource.validation.ValidWorkItemNew;

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
	@Path("/{itemKey}")
	public Response updateUser(@PathParam("itemKey") String itemKey, @ValidUser UserDTO userDTO) {
		if (itemKey.equals(userDTO.getItemKey())) {
			return serviceRequest(() -> {
				User user = service.getByItemKey(userDTO.getItemKey());
				service.save(userDTO.reflectDTO(user));
				return Response.noContent().build();
			});
		}
		else {
			throw new BadRequestException("Item key in JSON dont match item key in url");
		}
	}

	@DELETE
	@Path("/{itemKey}")
	public Response deleteUser(@PathParam("itemKey") String itemKey) {
		return super.delete(itemKey);
	}

	@GET
	@Path("/{itemKey}/workitems")
	public Response getuserWorkItems(@BeanParam WorkItemQueryBean workItemQuery, @PathParam("itemKey") String itemKey) {
		return serviceRequest(() -> {
			User user = service.getByItemKey(itemKey);
			workItemQuery.setUser(user);
			List<WorkItem> userWorkItems = service.getUserWorkItems(workItemQuery.buildSpecification(),	workItemQuery.buildPageable());
			return Response.ok().entity(dtoFactory.buildWorkItemDTOs(userWorkItems, true, uriInfo)).build();
		});
	}

	@POST
	@Path("/{itemKey}/workitems")
	public Response assignNewWorkItem(@ValidWorkItemNew WorkItemDTO workItemDTO, @PathParam("itemKey") String itemKey) {
		return serviceRequest(() -> {
			User user = service.getByItemKey(itemKey);
			WorkItem workItem = service.assignNewWorkItem(workItemDTO.buildWorkItem(), user);
			URI location = uriInfo.getBaseUriBuilder().path(WorkItemResource.class).path(workItem.getItemKey()).build();
			return Response.created(location).build();
		});
	}

	@PUT
	@Path("/{itemKey}/workitems")
	public Response assignWorkItem(@ValidWorkItem WorkItemDTO workItemDTO, @PathParam("itemKey") String itemKey) {
		return serviceRequest(() -> {
			User user = service.getByItemKey(itemKey);
			service.assignWorkItem(workItemDTO.getItemKey(), user);
			return Response.noContent().build();
		});
	}

	@DELETE
	@Path("/{itemKey}/workitems")
	public Response withdrawWorkItem(@ValidWorkItem WorkItemDTO workItemDTO, @PathParam("itemKey") String itemKey) {
		return serviceRequest(() -> {
			User user = service.getByItemKey(itemKey);
			service.withdrawWorkItem(workItemDTO.getItemKey(), user);
			return Response.noContent().build();
		});
	}

}