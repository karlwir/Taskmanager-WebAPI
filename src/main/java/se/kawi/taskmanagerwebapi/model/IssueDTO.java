package se.kawi.taskmanagerwebapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import se.kawi.taskmanagerservicelib.model.Issue;
import se.kawi.taskmanagerservicelib.model.WorkItem;

public class IssueDTO extends AbstractDTO {

	@JsonProperty
	private String title;
	@JsonProperty
	private String description;
	@JsonProperty
	private boolean openIssue;
	@JsonProperty
	private WorkItem workItem;

	protected IssueDTO() {};
	
	protected IssueDTO(Issue issue) {
		this.title = issue.getTitle();
		this.description = issue.getDescription();
		this.openIssue = issue.isOpenIssue();
		this.workItem = issue.getWorkItem();
	}
	
	public Issue reflectDTO(Issue issue) {
		issue.setTitle(title);
		issue.setDescription(description);
		issue.setOpenIssue(openIssue);
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
	
	public boolean isOpenIssue() {
		return openIssue;
	}
	
}
