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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.kawi.taskmanagerservicelib.model.Issue;
import se.kawi.taskmanagerservicelib.service.IssueService;
import se.kawi.taskmanagerwebapi.exception.BadRequestException;
import se.kawi.taskmanagerwebapi.model.IssueDTO;
import se.kawi.taskmanagerwebapi.resource.query.IssueQueryBean;
import se.kawi.taskmanagerwebapi.resource.validation.ValidIssue;
import se.kawi.taskmanagerwebapi.resource.validation.ValidIssueNew;

@Component
@Path("/issues")
@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
@Consumes({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
public class IssueResource extends BaseResource<Issue, IssueService> {

	@Autowired
	protected IssueResource(IssueService service) {
		super(service);
	}

	@POST
	public Response createIssue(@ValidIssueNew IssueDTO issueDTO) {
		return super.create(issueDTO.buildIssue());
	}

	@GET
	@Path("/{itemKey}")
	public Response getIssue(@PathParam("itemKey") String itemKey) {
		return super.byItemKey(itemKey);
	}

	@GET
	public Response getIssues(@BeanParam IssueQueryBean issueQuery) {
		return super.get(issueQuery.buildSpecification(), issueQuery.buildPageable());
	}
	
	@GET
	@Path("/count")
	public Response countIssues(@BeanParam IssueQueryBean issueQuery) {
		return super.count(issueQuery.buildSpecification());
	}

	@PUT
	@Path("/{itemKey}")
	public Response updateIssue(@PathParam("itemKey") String itemKey, @ValidIssue IssueDTO issueDTO) {
		if (itemKey.equals(issueDTO.getItemKey())) {
			return serviceRequest(() -> {
				Issue issue = service.getByItemKey(issueDTO.getItemKey());
				service.save(issueDTO.reflectDTO(issue));
				return Response.noContent().build();
			});
		}
		else {
			throw new BadRequestException("Item key in JSON dont match item key in url");
		}
	}

	@DELETE
	@Path("/{itemKey}")
	public Response deleteIssue(@PathParam("itemKey") String itemKey) {
		return super.delete(itemKey);
	}

}