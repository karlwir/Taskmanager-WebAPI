package se.kawi.taskmanagerwebapi.resource;

import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import se.kawi.taskmanagerwebapi.model.UserDTO;
import se.kawi.taskmanagerwebapi.model.WorkItemDTO;

public class UserResourceTest extends AbstractResourceTest{

	private JSONObject newUser = new JSONObject();
	private JSONObject newWorkItem = new JSONObject();
	private JSONObject updateUser = new JSONObject();

	@Before
	public void prepare() throws JSONException {
		newUser.put("firstname", "Karl").put("lastname", "Wirfelt").put("username", "kawi01");
		newWorkItem.put("title", "New work item title").put("priority", 1024).put("description", "New work item decription");
		super.prepare();
	}
	
	@After
	public void tearDown() {
		newUser = new JSONObject();
		updateUser = new JSONObject();
		newWorkItem = new JSONObject();
		super.tearDown();
	}
	
	@Test
	public void canCreateAndGetUser() {
		// Create user
		Response postResponse = client.target(USER_RESOURCE_URI).request().post(Entity.json(newUser.toString()));
		String location = postResponse.getHeaderString("location");

		// Get user and tests
		Response getResponse = client.target(location).request().get();
		UserDTO createdUserDTO = getResponse.readEntity(UserDTO.class);
		assertEquals("Karl", createdUserDTO.getFirstname());
		assertEquals("Wirfelt", createdUserDTO.getLastname());
		assertEquals("kawi01", createdUserDTO.getUsername());
		assertTrue(createdUserDTO.isActive());
		assertNotNull(createdUserDTO.getItemKey());
		assertNotNull(createdUserDTO.getOrigin());
	}
	
	@Test
	public void canCreateAndGetUsers() throws JSONException {
		// Create first user
		client.target(USER_RESOURCE_URI).request().post(Entity.json(newUser.toString()));
		
		// Create second user
		newUser.put("username", "kawi02");
		client.target(USER_RESOURCE_URI).request().post(Entity.json(newUser.toString()));
		
		// Get all users and test
		Response getResponse = client.target(USER_RESOURCE_URI).request().get();
		List<UserDTO> userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(2, userDTOs.size());
	}
	
	@Test
	public void canCreateAndCountUsers() throws JSONException {
		// Create first user
		client.target(USER_RESOURCE_URI).request().post(Entity.json(newUser.toString()));

		// Create second user
		newUser.put("username", "kawi02");
		client.target(USER_RESOURCE_URI).request().post(Entity.json(newUser.toString()));

		// Count all users
		Response countResponse = client.target(USER_RESOURCE_URI).path("count").request().get();
		int quantity = countResponse.readEntity(Integer.class);
		assertEquals(2, quantity);

		// Count users with firstname Karl
		countResponse = client.target(USER_RESOURCE_URI).path("count").queryParam("firstname", "Karl").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(2, quantity);

		// Count users with lastname Wirfelt
		countResponse = client.target(USER_RESOURCE_URI).path("count").queryParam("lastname", "Wirfelt").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(2, quantity);
		
		// Count users with username kawi01
		countResponse = client.target(USER_RESOURCE_URI).path("count").queryParam("username", "kawi01").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(1, quantity);

		// Count active users
		countResponse = client.target(USER_RESOURCE_URI).path("count").queryParam("active", "true").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(2, quantity);

		// Count inactive users
		countResponse = client.target(USER_RESOURCE_URI).path("count").queryParam("active", "false").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(0, quantity);
		
		// Count active users with firstname Karl
		countResponse = client.target(USER_RESOURCE_URI).path("count").queryParam("active", "true").queryParam("firstname", "Karl").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(2, quantity);
		
		// Count inactive users with lastname Wirfelt
		countResponse = client.target(USER_RESOURCE_URI).path("count").queryParam("active", "false").queryParam("lastname", "Wirfelt").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(0, quantity);
	}
	
	@Test
	public void canCreateAndGetUsersWithQueries() throws JSONException {
		// Create first user
		client.target(USER_RESOURCE_URI).request().post(Entity.json(newUser.toString()));

		// Create second user		
		newUser.put("lastname", "Karlsson");
		newUser.put("username", "kaka01");
		client.target(USER_RESOURCE_URI).request().post(Entity.json(newUser.toString()));
		
		// Firstname
		Response getResponse = client.target(USER_RESOURCE_URI).queryParam("firstname", "Karl").request().get();
		List<UserDTO> userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(2, userDTOs.size());

		// Lastname
		getResponse = client.target(USER_RESOURCE_URI).queryParam("lastname", "Wirfelt").request().get();
		userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(1, userDTOs.size());

		// Username
		getResponse = client.target(USER_RESOURCE_URI).queryParam("username", "kawi01").request().get();
		userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(1, userDTOs.size());

		// Active user true
		getResponse = client.target(USER_RESOURCE_URI).queryParam("active", "true").request().get();
		userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(2, userDTOs.size());

		// Active user false
		getResponse = client.target(USER_RESOURCE_URI).queryParam("active", "false").request().get();
		userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(0, userDTOs.size());

		// Two matching params
		getResponse = client.target(USER_RESOURCE_URI).queryParam("firstname", "Karl").queryParam("active", "true").request().get();
		userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(2, userDTOs.size());

		// Two non matching params
		getResponse = client.target(USER_RESOURCE_URI).queryParam("lastname", "Wirfelt").queryParam("username", "kaka01").request().get();
		userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(0, userDTOs.size());

		// Three matching params
		getResponse = client.target(USER_RESOURCE_URI).queryParam("firstname", "Karl").queryParam("lastname", "Wirfelt").queryParam("active", "true").request().get();
		userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(1, userDTOs.size());
		
		// Three non matching params
		getResponse = client.target(USER_RESOURCE_URI).queryParam("firstname", "Karl").queryParam("lastname", "Karlsson").queryParam("active", "false").request().get();
		userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(0, userDTOs.size());

		// Four matching params
		getResponse = client.target(USER_RESOURCE_URI).queryParam("firstname", "Karl").queryParam("lastname", "Wirfelt").queryParam("username", "kawi01").queryParam("active", "true").request().get();
		userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(1, userDTOs.size());
		
		// Four non matching params
		getResponse = client.target(USER_RESOURCE_URI).queryParam("firstname", "Karl").queryParam("lastname", "Karlsson").queryParam("username", "kaka01").queryParam("active", "false").request().get();
		userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(0, userDTOs.size());

	}
	
	@Test
	public void canCreateAndUpdateUser() throws JSONException {
		// Create the user
		Response postResponse = client.target(USER_RESOURCE_URI).request().post(Entity.json(newUser.toString()));
		String location = postResponse.getHeaderString("location");
		Response getResponse = client.target(location).request().get();
		UserDTO createdUserDTO = getResponse.readEntity(UserDTO.class);
		
		// Prepare updated JSON and update
		updateUser.put("itemKey", createdUserDTO.getItemKey()).put("firstname", "Tom").put("lastname", "Scott").put("username", "tosc01").put("active", false);
		client.target(location).request().put(Entity.json(updateUser.toString()));
		
		// Get updated user and tests
		Response secondGetResponse = client.target(location).request().get();
		UserDTO updatedUserDTO = secondGetResponse.readEntity(UserDTO.class);
		assertEquals("Tom", updatedUserDTO.getFirstname());
		assertEquals("Scott", updatedUserDTO.getLastname());
		assertEquals("tosc01", updatedUserDTO.getUsername());
		assertEquals(false, updatedUserDTO.isActive());
	}
	
	@Test
	public void canCreateAndDeleteUser() {
		// Create the user
		Response postResponse = client.target(USER_RESOURCE_URI) .request().post(Entity.json(newUser.toString()));
		String location = postResponse.getHeaderString("location");

		// Delete the user and test
		Response deleteResponse = client.target(location).request().delete();
		assertEquals(204, deleteResponse.getStatus());
	}
	
	@Test
	public void canAssignAndWithdrawWorkItemFromUser() {
		// Create the user
		Response userPostResponse = client.target(USER_RESOURCE_URI).request().post(Entity.json(newUser.toString()));
		String userLocation = userPostResponse.getHeaderString("location");
		
		// Create and get the work item
		Response workItemPostResponse = client.target(WORKITEM_RESOURCE_URI).request().post(Entity.json(newWorkItem.toString()));
		String workItemLocation = workItemPostResponse.getHeaderString("location");
		Response workItemGetResponse = client.target(workItemLocation).request().get();
		WorkItemDTO createdWorkItemDTO = workItemGetResponse.readEntity(WorkItemDTO.class);
		
		// Assign the work item and test
		client.target(userLocation).path("workitems").request().put(Entity.json(createdWorkItemDTO.toJSONString()));
		Response userWorkItemsGetResponse = client.target(userLocation).path("workitems").request().get();		
		List<WorkItemDTO> workItemDTOs = userWorkItemsGetResponse.readEntity(new GenericType<List<WorkItemDTO>>(){});
		assertEquals(1, workItemDTOs.size());
		
		//Create and assign work item in one request and test
		Response postAssignWorkItemResponse = client.target(userLocation).path("workitems").request().post(Entity.json(newWorkItem.toString()));
		String secondworkItemLocation = postAssignWorkItemResponse.getHeaderString("location");
		workItemGetResponse = client.target(secondworkItemLocation).request().get();
		WorkItemDTO secondCreatedWorkItemDTO = workItemGetResponse.readEntity(WorkItemDTO.class);
		userWorkItemsGetResponse = client.target(userLocation).path("workitems").request().get();
		workItemDTOs = userWorkItemsGetResponse.readEntity(new GenericType<List<WorkItemDTO>>(){});
		assertEquals(2, workItemDTOs.size());
		
		// Withdraw the work item and test
		client.target(userLocation).path("workitems").path(createdWorkItemDTO.getItemKey()).request().delete();
		client.target(userLocation).path("workitems").path(secondCreatedWorkItemDTO.getItemKey()).request().delete();
		userWorkItemsGetResponse = client.target(userLocation).path("workitems").request().get();		
		workItemDTOs = userWorkItemsGetResponse.readEntity(new GenericType<List<WorkItemDTO>>(){});
		assertEquals(0, workItemDTOs.size());
	}
	
}
