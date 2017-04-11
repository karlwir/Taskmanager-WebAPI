package se.kawi.taskmanagerwebapi.resource;

import static org.junit.Assert.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.Before;

import se.kawi.taskmanagerwebapi.AbstractTest;
import se.kawi.taskmanagerwebapi.model.UserDTO;

public class UserResourceTest extends AbstractTest{
	
	private String userResourceTarget = "http://localhost:8080/users";
	private String newUserJSON = "{\"firstname\": \"Karl\", \"lastname\": \"Wirfelt\", \"username\": \"kawi01\"}";

	@Before
	public void prepare() {
		// later...
	}

	@Test
	public void canPostAndGetUser() {
		Client client = ClientBuilder.newClient();
		
		Response postResponse = client.target(userResourceTarget).request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(newUserJSON));
		String location = postResponse.getHeaderString("location");

		Response getResponse = client.target(location).request().get();
		UserDTO createdUser = getResponse.readEntity(UserDTO.class);
		
		assertEquals("Karl", createdUser.getFirstname());
		assertEquals("Wirfelt", createdUser.getLastname());
		assertEquals("kawi01", createdUser.getUsername());
	}
	
}
