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

import se.kawi.taskmanagerservicelib.model.Issue;
import se.kawi.taskmanagerservicelib.model.WorkItem;
import se.kawi.taskmanagerservicelib.service.WorkItemService;
import se.kawi.taskmanagerwebapi.exception.BadRequestException;
import se.kawi.taskmanagerwebapi.model.IssueDTO;
import se.kawi.taskmanagerwebapi.model.WorkItemDTO;
import se.kawi.taskmanagerwebapi.resource.query.IssueQueryBean;
import se.kawi.taskmanagerwebapi.resource.query.WorkItemQueryBean;
import se.kawi.taskmanagerwebapi.resource.validation.ValidIssue;
import se.kawi.taskmanagerwebapi.resource.validation.ValidIssueNew;
import se.kawi.taskmanagerwebapi.resource.validation.ValidWorkItem;
import se.kawi.taskmanagerwebapi.resource.validation.ValidWorkItemNew;

@Component
@Path("/workitems")
@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
@Consumes({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
public class WorkItemResource extends BaseResource<WorkItem, WorkItemService> {

	protected WorkItemResource(WorkItemService service) {
		super(service);
	}

	@POST
	public Response createWorkItem(@ValidWorkItemNew WorkItemDTO workItemDTO) {
		return super.create(workItemDTO.buildWorkItem());
	}

	@GET
	@Path("/{itemKey}")
	public Response getWorkItem(@PathParam("itemKey") String itemKey) {
		return super.byItemKey(itemKey);
	}

	@GET
	public Response getWorkItems(@BeanParam WorkItemQueryBean workItemQuery) {
		return super.get(workItemQuery.buildSpecification(), workItemQuery.buildPageable());
	}
	
	@GET
	@Path("/count")
	public Response countWorkItem(@BeanParam WorkItemQueryBean workItemQuery) {
		return super.count(workItemQuery.buildSpecification());
	}

	@PUT
	@Path("/{itemKey}")
	public Response updateWorkItem(@PathParam("itemKey") String itemKey, @ValidWorkItem WorkItemDTO workItemDTO) {
		if (itemKey.equals(workItemDTO.getItemKey())) {
			return serviceRequest(() -> {
				WorkItem workItem = service.getByItemKey(workItemDTO.getItemKey());
				service.save(workItemDTO.reflectDTO(workItem));
				return Response.noContent().build();
			});
		}
		else {
			throw new BadRequestException("Item key in JSON dont match item key in url");
		}
	}
	
	@DELETE
	@Path("/{itemKey}")
	public Response deleteWorkItem(@PathParam("itemKey") String itemKey) {
		return super.delete(itemKey);
	}
	
	@GET
	@Path("{itemKey}/issues")
	public Response getWorkItemIssues(@BeanParam IssueQueryBean issueQuery, @PathParam("itemKey") String itemKey) {
		return serviceRequest(() -> {
			WorkItem workItem = service.getByItemKey(itemKey);
			issueQuery.setWorkItem(workItem);
			List<Issue> workItemsIssues = service.getWorkItemIssues(issueQuery.buildSpecification(), issueQuery.buildPageable());
			return Response.ok().entity(dtoFactory.buildIssuesDTOs(workItemsIssues, false, uriInfo)).build();
		});
	}
	
	@POST
	@Path("{itemKey}/issues")
	public Response addNewIssueToWorkItem(@ValidIssueNew IssueDTO issueDTO, @PathParam("itemKey") String itemKey) {
		return serviceRequest(() -> {
			WorkItem workItem = service.getByItemKey(itemKey);
			Issue issue = service.addNewIssue(issueDTO.buildIssue(), workItem);
			URI location = uriInfo.getBaseUriBuilder().path(IssueResource.class).path(issue.getItemKey()).build();
			return Response.created(location).build();
		});
	}
	
	@PUT
	@Path("{itemKey}/issues")
	public Response addIssueToWorkItem(@ValidIssue IssueDTO issueDTO, @PathParam("itemKey") String itemKey) {
		return serviceRequest(() -> {
			WorkItem workItem = service.getByItemKey(itemKey);
			service.addIssue(issueDTO.getItemKey(), workItem);
			return Response.noContent().build();
		});
	}
	
	@DELETE
	@Path("{workitemItemKey}/issues/{issueItemKey}")
	public Response removeIssueFromWorkItem(@PathParam("workitemItemKey") String workitemItemKey, @PathParam("issueItemKey") String issueItemKey) {
		return serviceRequest(() -> {
			WorkItem workItem = service.getByItemKey(workitemItemKey);
			service.removeIssueFromWorkItem(issueItemKey, workItem);
			return Response.noContent().build();
		});
	}

}