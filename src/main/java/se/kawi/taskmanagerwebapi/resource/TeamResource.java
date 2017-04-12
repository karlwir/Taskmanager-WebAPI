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

import se.kawi.taskmanagerservicelib.model.Team;
import se.kawi.taskmanagerservicelib.model.User;
import se.kawi.taskmanagerservicelib.model.WorkItem;
import se.kawi.taskmanagerservicelib.service.TeamService;
import se.kawi.taskmanagerwebapi.exception.BadRequestException;
import se.kawi.taskmanagerwebapi.model.TeamDTO;
import se.kawi.taskmanagerwebapi.model.UserDTO;
import se.kawi.taskmanagerwebapi.resource.query.TeamQueryBean;
import se.kawi.taskmanagerwebapi.resource.query.UserQueryBean;
import se.kawi.taskmanagerwebapi.resource.query.WorkItemQueryBean;
import se.kawi.taskmanagerwebapi.resource.validation.ValidTeam;
import se.kawi.taskmanagerwebapi.resource.validation.ValidTeamNew;
import se.kawi.taskmanagerwebapi.resource.validation.ValidUser;

@Component
@Path("/teams")
@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
@Consumes({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
public class TeamResource extends BaseResource<Team, TeamService> {

	protected TeamResource(TeamService service) {
		super(service);
	}

	@POST
	public Response createTeam(@ValidTeamNew TeamDTO teamDTO) {
		return super.create(teamDTO.buildTeam());
	}

	@GET
	@Path("/{itemKey}")
	public Response getTeam(@PathParam("itemKey") String itemKey) {
		return super.byItemKey(itemKey);
	}

	@GET
	public Response getTeams(@BeanParam TeamQueryBean teamQuery) {
		return super.get(teamQuery.buildSpecification(), teamQuery.buildPageable());
	}

	@GET
	@Path("/count")
	public Response countTeams(@BeanParam TeamQueryBean teamQuery) {
		return super.count(teamQuery.buildSpecification());
	}

	@PUT
	@Path("/{itemKey}")
	public Response updateTeam(@PathParam("itemKey") String itemKey, @ValidTeam TeamDTO teamDTO) {
		if (itemKey.equals(teamDTO.getItemKey())) {
			return serviceRequest(() -> {
				Team team = service.getByItemKey(teamDTO.getItemKey());
				service.save(teamDTO.reflectDTO(team));
				return Response.noContent().build();
			});
		}
		else {
			throw new BadRequestException("Item key in JSON dont match item key in url");
		}
	}

	@DELETE
	@Path("/{itemKey}")
	public Response deleteTeam(@PathParam("itemKey") String itemKey) {
		return super.delete(itemKey);
	}

	@GET
	@Path("/{itemKey}/users")
	public Response getTeamMembers(@BeanParam UserQueryBean userQuery, @PathParam("itemKey") String itemKey) {
		return serviceRequest(() -> {
			Team team = service.getByItemKey(itemKey);
			userQuery.setTeam(team);
			List<User> teamMembers = service.getTeamMembers(userQuery.buildSpecification(), userQuery.buildPageable());
			return Response.ok().entity(dtoFactory.buildUserDTOs(teamMembers, true, uriInfo)).build();
		});
	}

	@PUT
	@Path("/{itemKey}/users")
	public Response addTeamMember(@ValidUser UserDTO userDTO, @PathParam("itemKey") String itemKey) {
		return serviceRequest(() -> {
			Team team = service.getByItemKey(itemKey);
			service.addTeamMember(userDTO.getItemKey(), team);
			return Response.noContent().build();
		});
	}

	@DELETE
	@Path("/{teamItemKey}/users/{userItemKey}")
	public Response removeTeamMember(@PathParam("teamItemKey") String teamItemKey, @PathParam("teamItemKey") String userItemKey) {
		return serviceRequest(() -> {
			Team team = service.getByItemKey(teamItemKey);
			service.removeTeamMember(userItemKey, team);
			return Response.noContent().build();
		});
	}
	
	@GET
	@Path("/{itemKey}/workitems")
	public Response getTeamWorkItems(@BeanParam WorkItemQueryBean workItemQuery, @PathParam("itemKey") String itemKey) {
		return serviceRequest(() -> {
			Team team = service.getByItemKey(itemKey);
			workItemQuery.setUsers(team.getUsers());
			List<WorkItem> teamWorkItems = service.getTeamWorkItems(workItemQuery.buildSpecification(), workItemQuery.buildPageable());
			return Response.ok().entity(dtoFactory.buildWorkItemDTOs(teamWorkItems, true, uriInfo)).build();
		});
	}

}