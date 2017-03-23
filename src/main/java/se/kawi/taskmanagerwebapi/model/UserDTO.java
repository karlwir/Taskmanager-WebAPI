package se.kawi.taskmanagerwebapi.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import se.kawi.taskmanagerservicelib.model.User;

public class UserDTO extends AbstractDTO {

	@JsonProperty
	private String username;
	@JsonProperty
	private String firstname;
	@JsonProperty
	private String lastname;
	@JsonProperty
	private boolean activeUser;
	@JsonProperty
	@JsonIgnoreProperties(value = "users")
	private List<TeamDTO> teams;
	@JsonProperty
	@JsonIgnoreProperties(value = {"users", "issues"})
	private List<WorkItemDTO> workItems;
	
	protected UserDTO() {}

	protected UserDTO(User user) {
		this.itemKey = user.getItemKey();
		this.username = user.getUsername();
		this.firstname = user.getFirstname();
		this.lastname = user.getLastname();
		this.activeUser = user.isActiveUser();
	}

	public User reflectDTO(User user) {
		user.setFirstName(this.firstname);
		user.setLastName(this.lastname);
		user.setActiveUser(this.activeUser);
		return user;
	}
	
	public User buildUser() {
		return new User(username, firstname, lastname);
	}

	public String getUsername() {
		return username;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public boolean isActiveUser() {
		return activeUser;
	}
	
	public void setTeamDTOs(List<TeamDTO> teams) {
		this.teams = teams;
	}

	public void setWorkItemDTOs(List<WorkItemDTO> workItems) {
		this.workItems = workItems;
	}
	
}
