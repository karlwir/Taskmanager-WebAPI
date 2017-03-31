package se.kawi.taskmanagerwebapi.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import se.kawi.taskmanagerservicelib.model.WorkItem;
import se.kawi.taskmanagerservicelib.model.WorkItem.Status;

public class WorkItemDTO extends AbstractDTO {

	@JsonProperty
	private String title;
	@JsonProperty
	private String description;
	@JsonProperty
	private Status status;
	@JsonProperty
	@JsonIgnoreProperties(value = {"workItems",  "teams"})
	private List<UserDTO> users;
	@JsonProperty
	@JsonIgnoreProperties(value = {"workItem"})
	private List<IssueDTO> issues;

	protected WorkItemDTO() {}
	
	protected WorkItemDTO(WorkItem workItem) {
		super(workItem);
		this.title = workItem.getTitle();
		this.description = workItem.getDescription();
		this.status = workItem.getStatus();
	}
	
	public WorkItem reflectDTO(WorkItem workItem) {
		workItem.setTitle(this.title);
		workItem.setDescription(this.description);
		workItem.setStatus(this.status);
		return workItem;
	}

	public WorkItem buildWorkItem() {
		return new WorkItem(title, description);
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Status getStatus() {
		return status;
	}
	
	public void setUsers(List<UserDTO> users) {
		this.users = users;
	}
	
	public void setIssues(List<IssueDTO> issues) {
		this.issues = issues;
	}
}
