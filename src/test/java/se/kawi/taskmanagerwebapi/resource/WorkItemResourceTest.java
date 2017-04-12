package se.kawi.taskmanagerwebapi.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.kawi.taskmanagerwebapi.model.IssueDTO;
import se.kawi.taskmanagerwebapi.model.WorkItemDTO;
import se.kawi.taskmanagerservicelib.model.WorkItem;
import se.kawi.taskmanagerwebapi.AbstractResourceTest;

public class WorkItemResourceTest extends AbstractResourceTest {

	private JSONObject newWorkItem = new JSONObject();
	private JSONObject updateWorkItem = new JSONObject();
	private JSONObject newIssue = new JSONObject();

	@Before
	public void prepare() throws JSONException {
		newWorkItem.put("title", "New work item title").put("priority", 1024).put("description", "New work item description");
		newIssue.put("title", "New issue title").put("description", "New iusse description");
		super.prepare();
	}
	
	@After
	public void tearDown() {
		newWorkItem = new JSONObject();
		updateWorkItem = new JSONObject();
		newIssue = new JSONObject();
		super.tearDown();
	}
	
	@Test
	public void canCreateAndGetWorkItem() {
		// Create work item
		Response postResponse = client.target(WORKITEM_RESOURCE_URI).request().post(Entity.json(newWorkItem.toString()));
		String location = postResponse.getHeaderString("location");

		// Get work item and tests
		Response getResponse = client.target(location).request().get();
		WorkItemDTO createdWorkItemDTO = getResponse.readEntity(WorkItemDTO.class);
		assertEquals("New work item title", createdWorkItemDTO.getTitle());
		assertEquals("New work item description", createdWorkItemDTO.getDescription());
		assertEquals((Float) 1024.0f, createdWorkItemDTO.getPriority());
		assertEquals(WorkItem.Status.UNSTARTED, createdWorkItemDTO.getStatus());
		assertNotNull(createdWorkItemDTO.getItemKey());
		assertNotNull(createdWorkItemDTO.getOrigin());
	}
	
	@Test
	public void canCreateAndGetWorkItems() throws JSONException {
		// Create first work item
		client.target(WORKITEM_RESOURCE_URI).request().post(Entity.json(newWorkItem.toString()));

		// Create second work item
		client.target(WORKITEM_RESOURCE_URI).request().post(Entity.json(newWorkItem.toString()));
		
		// Get all work item and test
		Response getResponse = client.target(WORKITEM_RESOURCE_URI).request().get();
		List<WorkItemDTO> workItemDTOs = getResponse.readEntity(new GenericType<List<WorkItemDTO>>(){});
		assertEquals(2, workItemDTOs.size());
	}

	@Test
	public void canCreateAndCountWorkItems() throws JSONException {
		// Create first work item
		client.target(WORKITEM_RESOURCE_URI).request().post(Entity.json(newWorkItem.toString()));

		// Create second work item
		newWorkItem.put("title", "Second item title");
		newWorkItem.put("description", "Second item description");
		client.target(WORKITEM_RESOURCE_URI).request().post(Entity.json(newWorkItem.toString()));

		// Count all work items
		Response countResponse = client.target(WORKITEM_RESOURCE_URI).path("count").request().get();
		int quantity = countResponse.readEntity(Integer.class);
		assertEquals(2, quantity);

		// Count work items with title including "...title..."
		countResponse = client.target(WORKITEM_RESOURCE_URI).path("count").queryParam("title", "title").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(2, quantity);

		// Count work items with description including "...Second..."
		countResponse = client.target(WORKITEM_RESOURCE_URI).path("count").queryParam("description", "Second").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(1, quantity);
		
		// Count work items with status DONE
		countResponse = client.target(WORKITEM_RESOURCE_URI).path("count").queryParam("status", "UNSTARTED").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(2, quantity);

		// Count work items with isssues
		countResponse = client.target(WORKITEM_RESOURCE_URI).path("count").queryParam("hasissues", "true").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(0, quantity);

		// Count work items without isssues
		countResponse = client.target(WORKITEM_RESOURCE_URI).path("count").queryParam("hasissues", "false").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(2, quantity);
		
		// Count UNSTARTED work items without issues
		countResponse = client.target(WORKITEM_RESOURCE_URI).path("count").queryParam("status", "UNSTARTED").queryParam("hasissues", "true").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(0, quantity);
		
		// Count UNSTARTED work items with title "Second..."
		countResponse = client.target(WORKITEM_RESOURCE_URI).path("count").queryParam("status", "UNSTARTED").queryParam("title", "Second").request().get();
		quantity = countResponse.readEntity(Integer.class);
		assertEquals(1, quantity);
	}
	
	@Test
	public void canCreateAndGetWorkitemsWithQueries() throws JSONException {
		// Create first work item
		client.target(WORKITEM_RESOURCE_URI).request().post(Entity.json(newWorkItem.toString()));

		// Create second work item
		newWorkItem.put("title", "Second item title");
		newWorkItem.put("description", "Second item description");
		client.target(WORKITEM_RESOURCE_URI).request().post(Entity.json(newWorkItem.toString()));
		
		// Title
		Response getResponse = client.target(WORKITEM_RESOURCE_URI).queryParam("title", "title").request().get();
		List<WorkItemDTO> workItemDTOs = getResponse.readEntity(new GenericType<List<WorkItemDTO>>(){});
		assertEquals(2, workItemDTOs.size());

		// Description
		getResponse = client.target(WORKITEM_RESOURCE_URI).queryParam("description", "Second item").request().get();
		workItemDTOs = getResponse.readEntity(new GenericType<List<WorkItemDTO>>(){});
		assertEquals(1, workItemDTOs.size());

		// Status UNSTARTED
		getResponse = client.target(WORKITEM_RESOURCE_URI).queryParam("status", "UNSTARTED").request().get();
		workItemDTOs = getResponse.readEntity(new GenericType<List<WorkItemDTO>>(){});
		assertEquals(2, workItemDTOs.size());

		// Status UNSTARTED
		getResponse = client.target(WORKITEM_RESOURCE_URI).queryParam("status", "DONE").request().get();
		workItemDTOs = getResponse.readEntity(new GenericType<List<WorkItemDTO>>(){});
		assertEquals(0, workItemDTOs.size());

		// Has issues
		getResponse = client.target(WORKITEM_RESOURCE_URI).queryParam("hasissues", "true").request().get();
		workItemDTOs = getResponse.readEntity(new GenericType<List<WorkItemDTO>>(){});
		assertEquals(0, workItemDTOs.size());

		// Has no issues
		getResponse = client.target(WORKITEM_RESOURCE_URI).queryParam("hasissues", "false").request().get();
		workItemDTOs = getResponse.readEntity(new GenericType<List<WorkItemDTO>>(){});
		assertEquals(2, workItemDTOs.size());

		// Two matching params
		getResponse = client.target(WORKITEM_RESOURCE_URI).queryParam("status", "UNSTARTED").queryParam("hasissues", "false").request().get();
		workItemDTOs = getResponse.readEntity(new GenericType<List<WorkItemDTO>>(){});
		assertEquals(2, workItemDTOs.size());

		// Two non matching params
		getResponse = client.target(WORKITEM_RESOURCE_URI).queryParam("title", "title").queryParam("description", "cake").request().get();
		workItemDTOs = getResponse.readEntity(new GenericType<List<WorkItemDTO>>(){});
		assertEquals(0, workItemDTOs.size());

		// Three matching params
		getResponse = client.target(WORKITEM_RESOURCE_URI).queryParam("title", "title").queryParam("description", "description").queryParam("status", "UNSTARTED").request().get();
		workItemDTOs = getResponse.readEntity(new GenericType<List<WorkItemDTO>>(){});
		assertEquals(2, workItemDTOs.size());
		
		// Three non matching params
		getResponse = client.target(WORKITEM_RESOURCE_URI).queryParam("title", "title").queryParam("description", "description").queryParam("hasissues", "true").request().get();
		workItemDTOs = getResponse.readEntity(new GenericType<List<WorkItemDTO>>(){});
		assertEquals(0, workItemDTOs.size());

		// Four matching params
		getResponse = client.target(WORKITEM_RESOURCE_URI).queryParam("title", "title").queryParam("description", "description").queryParam("status", "UNSTARTED").queryParam("hasissues", "false").request().get();
		workItemDTOs = getResponse.readEntity(new GenericType<List<WorkItemDTO>>(){});
		assertEquals(2, workItemDTOs.size());
		
		// Four non matching params
		getResponse = client.target(WORKITEM_RESOURCE_URI).queryParam("title", "title").queryParam("description", "description").queryParam("status", "UNSTARTED").queryParam("hasissues", "true").request().get();
		workItemDTOs = getResponse.readEntity(new GenericType<List<WorkItemDTO>>(){});
		assertEquals(0, workItemDTOs.size());

	}
	
	@Test
	public void canCreateAndUpdateWorkItem() throws JSONException {
		// Create the work item
		Response postResponse = client.target(WORKITEM_RESOURCE_URI).request().post(Entity.json(newWorkItem.toString()));
		String location = postResponse.getHeaderString("location");
		Response getResponse = client.target(location).request().get();
		WorkItemDTO createdWorkItemDTO = getResponse.readEntity(WorkItemDTO.class);
		
		// Prepare updated JSON and update
		updateWorkItem.put("itemKey", createdWorkItemDTO.getItemKey()).put("title", "Updated title").put("description", "Updated description").put("status", "DONE").put("priority", 2048);
		client.target(location).request().put(Entity.json(updateWorkItem.toString()));
		
		// Get updated work item and tests
		Response secondGetResponse = client.target(location).request().get();
		WorkItemDTO updatedWorkItemDTO = secondGetResponse.readEntity(WorkItemDTO.class);
		assertEquals("Updated title", updatedWorkItemDTO.getTitle());
		assertEquals("Updated description", updatedWorkItemDTO.getDescription());
		assertEquals(WorkItem.Status.DONE, updatedWorkItemDTO.getStatus());
		assertEquals((Float) 2048.0f, updatedWorkItemDTO.getPriority());
	}
	
	@Test
	public void canCreateAndDeleteWorkItem() {
		// Create the work item
		Response postResponse = client.target(WORKITEM_RESOURCE_URI).request().post(Entity.json(newWorkItem.toString()));
		String location = postResponse.getHeaderString("location");

		// Delete the work item and test
		Response deleteResponse = client.target(location).request().delete();
		assertEquals(204, deleteResponse.getStatus());
	}
	
	@Test
	public void canAddAndRemoveIssuesFromWorkItem() throws JSONException {
		// Create the work item
		Response postResponse = client.target(WORKITEM_RESOURCE_URI).request().post(Entity.json(newWorkItem.toString()));
		String workItemlocation = postResponse.getHeaderString("location");
		Response getResponse = client.target(workItemlocation).request().get();
		WorkItemDTO createdWorkItemDTO = getResponse.readEntity(WorkItemDTO.class);
		
		// Set work item status DONE
		updateWorkItem.put("itemKey", createdWorkItemDTO.getItemKey()).put("title", createdWorkItemDTO.getTitle()).put("description", createdWorkItemDTO.getDescription()).put("status", "DONE").put("priority", 1024);
		client.target(workItemlocation).request().put(Entity.json(updateWorkItem.toString()));
		
		// Create and get the issue
		Response issuePostResponse = client.target(ISSUE_RESOURCE_URI).request().post(Entity.json(newIssue.toString()));
		String issueLocation = issuePostResponse.getHeaderString("location");
		Response issueGetResponse = client.target(issueLocation).request().get();
		IssueDTO createdIssueDTO = issueGetResponse.readEntity(IssueDTO.class);
		
		// Add the issue to the work item and test
		client.target(workItemlocation).path("issues").request().put(Entity.json(createdIssueDTO.toJSONString()));
		Response workItemIssuesGetResponse = client.target(workItemlocation).path("issues").request().get();		
		List<IssueDTO> issueDTOs = workItemIssuesGetResponse.readEntity(new GenericType<List<IssueDTO>>(){});
		assertEquals(1, issueDTOs.size());
		
		//Create and add issue in one request and test
		Response postAddIssueResponse = client.target(workItemlocation).path("issues").request().post(Entity.json(newIssue.toString()));
		String secondIssueLocation = postAddIssueResponse.getHeaderString("location");
		issueGetResponse = client.target(secondIssueLocation).request().get();
		IssueDTO secondCreatedIssueDTO = issueGetResponse.readEntity(IssueDTO.class);
		workItemIssuesGetResponse = client.target(workItemlocation).path("issues").request().get();
		issueDTOs = workItemIssuesGetResponse.readEntity(new GenericType<List<IssueDTO>>(){});
		assertEquals(2, issueDTOs.size());
		
		// Withdraw the work item and test
		client.target(workItemlocation).path("issues").path(createdIssueDTO.getItemKey()).request().delete();
		client.target(workItemlocation).path("issues").path(secondCreatedIssueDTO.getItemKey()).request().delete();
		workItemIssuesGetResponse = client.target(workItemlocation).path("issues").request().get();		
		issueDTOs = workItemIssuesGetResponse.readEntity(new GenericType<List<IssueDTO>>(){});
		assertEquals(0, issueDTOs.size());
	}
	
}
