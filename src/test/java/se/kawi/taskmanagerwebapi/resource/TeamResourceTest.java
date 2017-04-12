package se.kawi.taskmanagerwebapi.resource;

import static org.junit.Assert.*;

import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.kawi.taskmanagerwebapi.AbstractResourceTest;
import se.kawi.taskmanagerwebapi.model.TeamDTO;
import se.kawi.taskmanagerwebapi.model.UserDTO;
import se.kawi.taskmanagerwebapi.model.WorkItemDTO;

public class TeamResourceTest extends AbstractResourceTest {

	private JSONObject newTeam = new JSONObject();
	private JSONObject updateTeam = new JSONObject();
	private JSONObject newWorkItem = new JSONObject();
	private JSONObject newUser = new JSONObject();

	@Before
	public void prepare() throws JSONException {
		newTeam.put("name", "New team");
		newUser.put("firstname", "Karl").put("lastname", "Wirfelt").put("username", "kawi01");
		newWorkItem.put("title", "New work item title").put("priority", 1024).put("description", "New work item description");
		super.prepare();
	}
	
	@After
	public void tearDown() {
		newTeam = new JSONObject();
		updateTeam = new JSONObject();
		newUser = new JSONObject();
		newWorkItem = new JSONObject();
		super.tearDown();
	}
	
	@Test
	public void canCreateAndGetTeam() {
		// Create team
		Response postResponse = client.target(TEAM_RESOURCE_URI).request().post(Entity.json(newTeam.toString()));
		String location = postResponse.getHeaderString("location");

		// Get team and tests
		Response getResponse = client.target(location).request().get();
		TeamDTO createdTeamDTO = getResponse.readEntity(TeamDTO.class);
		assertEquals("New team", createdTeamDTO.getName());
		assertTrue(createdTeamDTO.isActive());
		assertNotNull(createdTeamDTO.getItemKey());
		assertNotNull(createdTeamDTO.getOrigin());
	}
	
	@Test
	public void canCreateAndGetTeams() throws JSONException {
		// Create first team
		client.target(TEAM_RESOURCE_URI).request().post(Entity.json(newTeam.toString()));

		// Create second team
		newTeam.put("name", "Second team");
		client.target(TEAM_RESOURCE_URI).request().post(Entity.json(newTeam.toString()));
		
		// Get all teams and test
		Response getResponse = client.target(TEAM_RESOURCE_URI).request().get();
		List<TeamDTO> teamDTOs = getResponse.readEntity(new GenericType<List<TeamDTO>>(){});
		assertEquals(2, teamDTOs.size());
	}

	@Test
	public void canCreateAndCountTeams() throws JSONException {
		// Create first team
		client.target(TEAM_RESOURCE_URI).request().post(Entity.json(newTeam.toString()));

		// Create second team
		newTeam.put("name", "Second team");
		client.target(TEAM_RESOURCE_URI).request().post(Entity.json(newTeam.toString()));

		// Count all teams
		Response countResponse = client.target(TEAM_RESOURCE_URI).path("count").request().get();
		int quantity = countResponse.readEntity(Integer.class);
		assertEquals(2, quantity);

		// Count teams with name containing "...team..."
		countResponse = client.target(TEAM_RESOURCE_URI).path("count").queryParam("name", "team").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(2, quantity);

		// Count active teams
		countResponse = client.target(TEAM_RESOURCE_URI).path("count").queryParam("active", "true").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(2, quantity);

		// Count inactive teams
		countResponse = client.target(TEAM_RESOURCE_URI).path("count").queryParam("active", "false").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(0, quantity);
		
		// Count active teams with name containing "Second"
		countResponse = client.target(TEAM_RESOURCE_URI).path("count").queryParam("active", "true").queryParam("name", "Second").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(1, quantity);
		
		// Count inactive teams with name containing "team"
		countResponse = client.target(TEAM_RESOURCE_URI).path("count").queryParam("active", "false").queryParam("name", "team").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(0, quantity);
	}
	
	@Test
	public void canCreateAndGetTeamsWithQueries() throws JSONException {
		// Create first team
		client.target(TEAM_RESOURCE_URI).request().post(Entity.json(newTeam.toString()));

		// Create second team
		newTeam.put("name", "Second team");
		client.target(TEAM_RESOURCE_URI).request().post(Entity.json(newTeam.toString()));
		
		// Name
		Response getResponse = client.target(TEAM_RESOURCE_URI).queryParam("name", "team").request().get();
		List<TeamDTO> teamDTOs = getResponse.readEntity(new GenericType<List<TeamDTO>>(){});
		assertEquals(2, teamDTOs.size());

		// Active
		getResponse = client.target(TEAM_RESOURCE_URI).queryParam("active", "true").request().get();
		teamDTOs = getResponse.readEntity(new GenericType<List<TeamDTO>>(){});
		assertEquals(2, teamDTOs.size());

		// Inactive
		getResponse = client.target(TEAM_RESOURCE_URI).queryParam("active", "false").request().get();
		teamDTOs = getResponse.readEntity(new GenericType<List<TeamDTO>>(){});
		assertEquals(0, teamDTOs.size());

		// Two matching params
		getResponse = client.target(TEAM_RESOURCE_URI).queryParam("open", "true").queryParam("name", "Second").request().get();
		teamDTOs = getResponse.readEntity(new GenericType<List<TeamDTO>>(){});
		assertEquals(1, teamDTOs.size());

		// Two non matching params
		getResponse = client.target(TEAM_RESOURCE_URI).queryParam("open", "true").queryParam("name", "cake").request().get();
		teamDTOs = getResponse.readEntity(new GenericType<List<TeamDTO>>(){});
		assertEquals(0, teamDTOs.size());

	}
	
	@Test
	public void canCreateAndUpdateTeam() throws JSONException {
		// Create the team
		Response postResponse = client.target(TEAM_RESOURCE_URI).request().post(Entity.json(newTeam.toString()));
		String location = postResponse.getHeaderString("location");
		Response getResponse = client.target(location).request().get();
		TeamDTO createdTeamDTO = getResponse.readEntity(TeamDTO.class);
		
		// Prepare updated JSON and update
		updateTeam.put("itemKey", createdTeamDTO.getItemKey()).put("name", "Updated name").put("active", "false");
		client.target(location).request().put(Entity.json(updateTeam.toString()));
		
		// Get updated team and tests
		Response secondGetResponse = client.target(location).request().get();
		TeamDTO updatedTeamDTO = secondGetResponse.readEntity(TeamDTO.class);
		assertEquals("Updated name", updatedTeamDTO.getName());
		assertFalse(updatedTeamDTO.isActive());
	}
	
	@Test
	public void canCreateAndDeleteTeam() {
		// Create the team
		Response postResponse = client.target(TEAM_RESOURCE_URI).request().post(Entity.json(newTeam.toString()));
		String location = postResponse.getHeaderString("location");

		// Delete the team and test
		Response deleteResponse = client.target(location).request().delete();
		assertEquals(204, deleteResponse.getStatus());
	}
	
	@Test
	public void canAddAndRemoveUserFromTeam() {
		// Create the team
		Response postResponse = client.target(TEAM_RESOURCE_URI).request().post(Entity.json(newTeam.toString()));
		String teamLocation = postResponse.getHeaderString("location");
		
		// Create and get the user
		Response userPostResponse = client.target(USER_RESOURCE_URI).request().post(Entity.json(newUser.toString()));
		String userLocation = userPostResponse.getHeaderString("location");
		Response userGetResponse = client.target(userLocation).request().get();
		UserDTO createdUserDTO = userGetResponse.readEntity(UserDTO.class);
		
		// Add the user to the team and test
		client.target(teamLocation).path("users").request().put(Entity.json(createdUserDTO.toJSONString()));
		Response teamUsersGetResponse = client.target(teamLocation).path("users").request().get();		
		List<UserDTO> userDTOs = teamUsersGetResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(1, userDTOs.size());
		
		// Remove the user from the team and test
		client.target(teamLocation).path("users").path(createdUserDTO.getItemKey()).request().delete();
		teamUsersGetResponse = client.target(teamLocation).path("users").request().get();		
		userDTOs = teamUsersGetResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(0, userDTOs.size());
	}
	
	@Test
	public void canGetWorkItemsAssignedToTeamMembers() throws JSONException {
		// Create the team
		Response postResponse = client.target(TEAM_RESOURCE_URI).request().post(Entity.json(newTeam.toString()));
		String teamLocation = postResponse.getHeaderString("location");
		
		// Create and get the user
		Response userPostResponse = client.target(USER_RESOURCE_URI).request().post(Entity.json(newUser.toString()));
		String userLocation = userPostResponse.getHeaderString("location");
		Response userGetResponse = client.target(userLocation).request().get();
		UserDTO createdUserDTO = userGetResponse.readEntity(UserDTO.class);
		
		// Create and get the work item
		Response workItemPostResponse = client.target(WORKITEM_RESOURCE_URI).request().post(Entity.json(newWorkItem.toString()));
		String workItemLocation = workItemPostResponse.getHeaderString("location");
		Response workItemGetResponse = client.target(workItemLocation).request().get();
		WorkItemDTO createdWorkItemDTO = workItemGetResponse.readEntity(WorkItemDTO.class);
		
		// Assign the work item
		client.target(userLocation).path("workitems").request().put(Entity.json(createdWorkItemDTO.toJSONString()));
		
		// Add user to team
		client.target(teamLocation).path("users").request().put(Entity.json(createdUserDTO.toJSONString()));
		
		// Get team members work items and test
		Response teamWorkItemGetResponse = client.target(teamLocation).path("workitems").request().get();		
		List<WorkItemDTO> workItemDTOs = teamWorkItemGetResponse.readEntity(new GenericType<List<WorkItemDTO>>(){});
		assertEquals(1, workItemDTOs.size());
		
	}
	
}
