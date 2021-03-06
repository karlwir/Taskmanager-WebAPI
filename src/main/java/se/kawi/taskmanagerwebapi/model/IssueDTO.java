package se.kawi.taskmanagerwebapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import se.kawi.taskmanagerservicelib.model.Issue;

@JsonInclude(Include.NON_NULL)
public class IssueDTO extends AbstractDTO {

	@JsonProperty
	private String title;
	@JsonProperty
	private String description;
	@JsonProperty
	private Boolean open;
	@JsonProperty(access = Access.READ_ONLY)
	@JsonIgnoreProperties(value={"issues", "users"})
	private WorkItemDTO workItem;

	protected IssueDTO() {}
	
	protected IssueDTO(Issue issue) {
		super(issue);
		this.title = issue.getTitle();
		this.description = issue.getDescription();
		this.open = issue.isOpen();
	}
	
	public Issue reflectDTO(Issue issue) {
		issue.setTitle(title);
		issue.setDescription(description);
		issue.setOpen(open);
		return issue;
	}
	
	public Issue buildIssue() {
		return new Issue(null, title, description);
	}

	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Boolean isOpen() {
		return open;
	}
	
	public void setWorkItem(WorkItemDTO workItem) {
		this.workItem = workItem;
	}

	public String toJSONString() {
		return String.format("{\"itemKey\": \"%s\", \"title\": \"%s\", \"description\": \"%s\", \"open\": %s}", getItemKey(), title, description, open);
	}
	
}
