package se.kawi.taskmanagerwebapi.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import se.kawi.taskmanagerwebapi.model.IssueDTO;

public class IssueResourceTest extends AbstractResourceTest {
	
	private JSONObject newIssue = new JSONObject();
	private JSONObject updateIssue = new JSONObject();

	@Before
	public void prepare() throws JSONException {
		newIssue.put("title", "New issue title").put("description", "New issue description");
		super.prepare();
	}
	
	@After
	public void tearDown() {
		updateIssue = new JSONObject();
		newIssue = new JSONObject();
		super.tearDown();
	}
	
	@Test
	public void canCreateAndGetIssue() {
		// Create issue
		Response postResponse = client.target(ISSUE_RESOURCE_URI).request().post(Entity.json(newIssue.toString()));
		String location = postResponse.getHeaderString("location");
		// Get issue and tests
		Response getResponse = client.target(location).request().get();
		IssueDTO createdIsssueDTO = getResponse.readEntity(IssueDTO.class);
		assertEquals("New issue title", createdIsssueDTO.getTitle());
		assertEquals("New issue description", createdIsssueDTO.getDescription());
		assertTrue(createdIsssueDTO.isOpen());
		assertNotNull(createdIsssueDTO.getItemKey());
		assertNotNull(createdIsssueDTO.getOrigin());
	}
	
	@Test
	public void canCreateAndGetIssues() throws JSONException {
		// Create first issue
		client.target(ISSUE_RESOURCE_URI).request().post(Entity.json(newIssue.toString()));

		// Create second issue
		client.target(ISSUE_RESOURCE_URI).request().post(Entity.json(newIssue.toString()));
		
		// Get all issues and test
		Response getResponse = client.target(ISSUE_RESOURCE_URI).request().get();
		List<IssueDTO> issueDTOs = getResponse.readEntity(new GenericType<List<IssueDTO>>(){});
		assertEquals(2, issueDTOs.size());
	}

	@Test
	public void canCreateAndCountIssues() throws JSONException {
		// Create first issue
		client.target(ISSUE_RESOURCE_URI).request().post(Entity.json(newIssue.toString()));

		// Create second issue
		newIssue.put("title", "Second issue title");
		newIssue.put("description", "Second issue description");
		client.target(ISSUE_RESOURCE_URI).request().post(Entity.json(newIssue.toString()));

		// Count all issues
		Response countResponse = client.target(ISSUE_RESOURCE_URI).path("count").request().get();
		int quantity = countResponse.readEntity(Integer.class);
		assertEquals(2, quantity);

		// Count issues with title including "...title..."
		countResponse = client.target(ISSUE_RESOURCE_URI).path("count").queryParam("title", "title").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(2, quantity);

		// Count issues with description including "...Second..."
		countResponse = client.target(ISSUE_RESOURCE_URI).path("count").queryParam("description", "Second").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(1, quantity);
		
		// Count open issues
		countResponse = client.target(ISSUE_RESOURCE_URI).path("count").queryParam("open", "true").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(2, quantity);

		// Count closed issues
		countResponse = client.target(ISSUE_RESOURCE_URI).path("count").queryParam("open", "false").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(0, quantity);
		
		// Count open issues with title ...title...
		countResponse = client.target(ISSUE_RESOURCE_URI).path("count").queryParam("open", "true").queryParam("title", "title").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(2, quantity);
		
		// Count closed issues with title "Second..."
		countResponse = client.target(ISSUE_RESOURCE_URI).path("count").queryParam("open", "false").queryParam("title", "Second").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(0, quantity);
	}
	
	@Test
	public void canCreateAndGetIssuesWithQueries() throws JSONException {
		// Create first issue
		client.target(ISSUE_RESOURCE_URI).request().post(Entity.json(newIssue.toString()));

		// Create second issue
		newIssue.put("title", "Second issue title");
		newIssue.put("description", "Second issue description");
		client.target(ISSUE_RESOURCE_URI).request().post(Entity.json(newIssue.toString()));
		
		// Title
		Response getResponse = client.target(ISSUE_RESOURCE_URI).queryParam("title", "title").request().get();
		List<IssueDTO> issueDTOs = getResponse.readEntity(new GenericType<List<IssueDTO>>(){});
		assertEquals(2, issueDTOs.size());

		// Description
		getResponse = client.target(ISSUE_RESOURCE_URI).queryParam("description", "Second issue").request().get();
		issueDTOs = getResponse.readEntity(new GenericType<List<IssueDTO>>(){});
		assertEquals(1, issueDTOs.size());

		// Open
		getResponse = client.target(ISSUE_RESOURCE_URI).queryParam("open", "true").request().get();
		issueDTOs = getResponse.readEntity(new GenericType<List<IssueDTO>>(){});
		assertEquals(2, issueDTOs.size());

		// Closed
		getResponse = client.target(ISSUE_RESOURCE_URI).queryParam("open", "false").request().get();
		issueDTOs = getResponse.readEntity(new GenericType<List<IssueDTO>>(){});
		assertEquals(0, issueDTOs.size());

		// Two matching params
		getResponse = client.target(ISSUE_RESOURCE_URI).queryParam("open", "true").queryParam("title", "title").request().get();
		issueDTOs = getResponse.readEntity(new GenericType<List<IssueDTO>>(){});
		assertEquals(2, issueDTOs.size());

		// Two non matching params
		getResponse = client.target(ISSUE_RESOURCE_URI).queryParam("title", "title").queryParam("description", "cake").request().get();
		issueDTOs = getResponse.readEntity(new GenericType<List<IssueDTO>>(){});
		assertEquals(0, issueDTOs.size());

		// Three matching params
		getResponse = client.target(ISSUE_RESOURCE_URI).queryParam("title", "title").queryParam("description", "description").queryParam("open", "true").request().get();
		issueDTOs = getResponse.readEntity(new GenericType<List<IssueDTO>>(){});
		assertEquals(2, issueDTOs.size());
		
		// Three non matching params
		getResponse = client.target(ISSUE_RESOURCE_URI).queryParam("title", "title").queryParam("description", "description").queryParam("open", "false").request().get();
		issueDTOs = getResponse.readEntity(new GenericType<List<IssueDTO>>(){});
		assertEquals(0, issueDTOs.size());

	}
	
	@Test
	public void canCreateAndUpdateIssue() throws JSONException {
		// Create and get issue
		Response postResponse = client.target(ISSUE_RESOURCE_URI).request().post(Entity.json(newIssue.toString()));
		String location = postResponse.getHeaderString("location");
		Response getResponse = client.target(location).request().get();
		IssueDTO createdIssueDTO = getResponse.readEntity(IssueDTO.class);
		
		// Prepare updated JSON and update
		updateIssue.put("itemKey", createdIssueDTO.getItemKey()).put("title", "Updated title").put("description", "Updated description").put("open", "false");
		client.target(location).request().put(Entity.json(updateIssue.toString()));
		
		// Get updated issue and tests
		Response secondGetResponse = client.target(location).request().get();
		IssueDTO updatedIssueDTO = secondGetResponse.readEntity(IssueDTO.class);
		assertEquals("Updated title", updatedIssueDTO.getTitle());
		assertEquals("Updated description", updatedIssueDTO.getDescription());
		assertFalse(updatedIssueDTO.isOpen());
	}
	
	@Test
	public void canCreateAndDeleteIssue() {
		// Create the issue
		Response postResponse = client.target(ISSUE_RESOURCE_URI).request().post(Entity.json(newIssue.toString()));
		String location = postResponse.getHeaderString("location");

		// Delete the issue and test
		Response deleteResponse = client.target(location).request().delete();
		assertEquals(204, deleteResponse.getStatus());
	}
	
}
