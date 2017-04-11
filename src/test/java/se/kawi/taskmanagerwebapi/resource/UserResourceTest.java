package se.kawi.taskmanagerwebapi.resource;

import static org.junit.Assert.*;

import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;

import se.kawi.taskmanagerwebapi.AbstractResourceTest;
import se.kawi.taskmanagerwebapi.model.UserDTO;

public class UserResourceTest extends AbstractResourceTest{

	private JSONObject newUser = new JSONObject();
	private JSONObject updateUser = new JSONObject();

	@Before
	public void prepare() throws JSONException {
		newUser.put("firstname", "Karl")
			   .put("lastname", "Wirfelt")
			   .put("username", "kawi01");
		super.prepare();
	}
	
	@After
	public void tearDown() {
		newUser = new JSONObject();
		updateUser = new JSONObject();
		super.tearDown();
	}
	
	@Test
	public void canCreateAndGetUser() {
		Response postResponse = client.target(userResourceTarget)
									  .request(MediaType.APPLICATION_JSON_TYPE)
									  .post(Entity.json(newUser.toString()));
		
		String location = postResponse.getHeaderString("location");

		Response getResponse = client.target(location).request().get();
		UserDTO createdUserDTO = getResponse.readEntity(UserDTO.class);
		
		assertEquals("Karl", createdUserDTO.getFirstname());
		assertEquals("Wirfelt", createdUserDTO.getLastname());
		assertEquals("kawi01", createdUserDTO.getUsername());
	}
	
	@Test
	public void canCreateAndGetUsers() throws JSONException {
		client.target(userResourceTarget)
			  .request(MediaType.APPLICATION_JSON_TYPE)
			  .post(Entity.json(newUser.toString()));
		
		newUser.put("username", "kawi02");
		
		client.target(userResourceTarget)
			  .request(MediaType.APPLICATION_JSON_TYPE)
			  .post(Entity.json(newUser.toString()));
		

		Response getResponse = client.target(userResourceTarget).request().get();
		List<UserDTO> userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		
		assertEquals(2, userDTOs.size());
	}
	
	@Test
	public void canCreateAndCountUsers() throws JSONException {
		client.target(userResourceTarget)
			  .request(MediaType.APPLICATION_JSON_TYPE)
			  .post(Entity.json(newUser.toString()));
		
		newUser.put("username", "kawi02");
		
		client.target(userResourceTarget)
			  .request(MediaType.APPLICATION_JSON_TYPE)
			  .post(Entity.json(newUser.toString()));
		
		Response countResponse = client.target(userResourceTarget).path("count").request().get();
		int quantity = countResponse.readEntity(Integer.class);
		assertEquals(2, quantity);
	}
	
	@Test
	public void canCreateAndGetUsersWithQueries() throws JSONException {
		client.target(userResourceTarget)
			  .request(MediaType.APPLICATION_JSON_TYPE)
			  .post(Entity.json(newUser.toString()));
		
		newUser.put("username", "kaka01");
		newUser.put("lastname", "Karlsson");
		
		client.target(userResourceTarget)
			  .request(MediaType.APPLICATION_JSON_TYPE)
			  .post(Entity.json(newUser.toString()));
		
		Response getResponse = client.target(userResourceTarget).queryParam("firstname", "Karl").request().get();
		List<UserDTO> userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(2, userDTOs.size());

		getResponse = client.target(userResourceTarget).queryParam("lastname", "Wirfelt").request().get();
		userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(1, userDTOs.size());

		getResponse = client.target(userResourceTarget).queryParam("username", "kawi01").request().get();
		userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(1, userDTOs.size());

		getResponse = client.target(userResourceTarget).queryParam("active", "true").request().get();
		userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(2, userDTOs.size());

		getResponse = client.target(userResourceTarget).queryParam("active", "false").request().get();
		userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(0, userDTOs.size());
		
		getResponse = client.target(userResourceTarget).queryParam("firstname", "Karl").queryParam("active", "true").request().get();
		userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(2, userDTOs.size());
		
		getResponse = client.target(userResourceTarget).queryParam("firstname", "Karl").queryParam("active", "false").request().get();
		userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(0, userDTOs.size());
		
		getResponse = client.target(userResourceTarget).queryParam("firstname", "Karl").queryParam("lastname", "Wirfelt").queryParam("active", "true").request().get();
		userDTOs = getResponse.readEntity(new GenericType<List<UserDTO>>(){});
		assertEquals(1, userDTOs.size());

	}
	
	@Test
	public void canCreateAndUpdateUser() throws JSONException {
		Response postResponse = client.target(userResourceTarget)
									  .request(MediaType.APPLICATION_JSON_TYPE)
									  .post(Entity.json(newUser.toString()));
		
		String location = postResponse.getHeaderString("location");

		Response getResponse = client.target(location).request().get();
		UserDTO createdUserDTO = getResponse.readEntity(UserDTO.class);
		
		updateUser.put("itemKey", createdUserDTO.getItemKey())
				  .put("firstname", "Tom")
				  .put("lastname", "Scott")
				  .put("username", createdUserDTO.getUsername())
				  .put("active", false);
		
		client.target(location)
			  .request(MediaType.APPLICATION_JSON_TYPE)
			  .put(Entity.json(updateUser.toString()));
		
		Response nextGetResponse = client.target(location).request().get();
		UserDTO updatedUserDTO = nextGetResponse.readEntity(UserDTO.class);
		
		assertEquals("Tom", updatedUserDTO.getFirstname());
		assertEquals("Scott", updatedUserDTO.getLastname());
		assertEquals(false, updatedUserDTO.isActive());
	}
	
	@Test
	public void canCreateAndDeleteUser() throws JSONException {
		Response postResponse = client.target(userResourceTarget)
									  .request(MediaType.APPLICATION_JSON_TYPE)
									  .post(Entity.json(newUser.toString()));
		
		String location = postResponse.getHeaderString("location");

		Response deleteResponse = client.target(location)
			  .request(MediaType.APPLICATION_JSON_TYPE)
			  .delete();
		
		assertEquals(204, deleteResponse.getStatus());
	}
	
}
