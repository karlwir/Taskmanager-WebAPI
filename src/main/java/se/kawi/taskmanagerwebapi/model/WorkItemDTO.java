package se.kawi.taskmanagerwebapi.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import se.kawi.taskmanagerservicelib.model.Issue;
import se.kawi.taskmanagerservicelib.model.User;
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
	private Set<User> users;
	@JsonProperty
	private Set<Issue> issues;

	protected WorkItemDTO() {}
	
	protected WorkItemDTO(WorkItem workItem) {
		this.title = workItem.getTitle();
		this.description = workItem.getDescription();
		this.status = workItem.getStatus();
		this.users = workItem.getUsers();
		this.issues = workItem.getIssues();
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
}
