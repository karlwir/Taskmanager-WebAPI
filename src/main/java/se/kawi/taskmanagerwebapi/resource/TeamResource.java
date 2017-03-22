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
	public Response createTeam(@ValidTeamNew Team entity) {
		return super.create(entity);
	}

	@GET
	@Path("/{id}")
	public Response getTeam(@PathParam("id") Long id) {
		return super.byId(id);
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
	public Response updateTeam(@ValidTeam Team entity) {
		return super.update(entity);
	}

	@DELETE
	public Response deleteTeam(@ValidTeam Team entity) {
		return super.delete(entity);
	}

	@GET
	@Path("/{id}/users")
	public Response getTeamMembers(@BeanParam UserQueryBean userQuery, @PathParam("id") Long id) {
		return serviceRequest(() -> {
			Team team = service.getById(id);
			if (team != null) {
				userQuery.setTeam(team);
				List<User> teamMembers = service.getTeamMembers(userQuery.buildSpecification(), userQuery.buildPageable());
				return Response.ok().entity(teamMembers).build();
			} else {
				return Response.status(404).build();
			}
		});
	}

	@PUT
	@Path("/{id}/users")
	public Response addTeamMember(@ValidUser User user, @PathParam("id") Long id) {
		return serviceRequest(() -> {
			Team team = service.getById(id);
			if (team != null) {
				service.addTeamMember(user, team);
				return Response.noContent().build();
			} else {
				return Response.status(404).build();
			}
		});
	}

	@DELETE
	@Path("/{id}/users")
	public Response removeTeamMember(@ValidUser User user, @PathParam("id") Long id) {
		return serviceRequest(() -> {
			Team team = service.getById(id);
			if (team != null) {
				service.removeTeamMember(user, team);
				return Response.noContent().build();
			} else {
				return Response.status(404).build();
			}
		});
	}
	
	@GET
	@Path("/{id}/workitems")
	public Response getTeamWorkItems(@BeanParam WorkItemQueryBean workItemQuery, @PathParam("id") Long id) {
		return serviceRequest(() -> {
			Team team = service.getById(id);
			if (team != null) {
				workItemQuery.setUsers(team.getUsers());
				List<WorkItem> teamWorkItems = service.getTeamWorkItems(workItemQuery.buildSpecification(), workItemQuery.buildPageable());
				return Response.ok().entity(teamWorkItems).build();
			}
			return Response.status(404).build();
		});
	}

}