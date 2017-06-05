package se.kawi.taskmanagerwebapi.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import se.kawi.taskmanagerservicelib.model.User;

public class UserDTO extends AbstractDTO {

	@JsonProperty
	private String username;
	@JsonProperty
	private String firstname;
	@JsonProperty
	private String lastname;
	@JsonProperty
	private String email;
	@JsonProperty
	private Boolean active;
	@JsonProperty(access = Access.READ_ONLY)
	@JsonIgnoreProperties(value = "users")
	private List<TeamDTO> teams;
	@JsonProperty(access = Access.READ_ONLY)
	@JsonIgnoreProperties(value = {"users", "issues"})
	private List<WorkItemDTO> workItems;
	
	protected UserDTO() {}

	protected UserDTO(User user) {
		super(user);
		this.username = user.getUsername();
		this.firstname = user.getFirstname();
		this.lastname = user.getLastname();
		this.email = user.getEmail();
		this.active = user.isActiveUser();
	}

	public User reflectDTO(User user) {
		user.setFirstName(this.firstname);
		user.setLastName(this.lastname);
		user.setUsername(this.username);
		user.setActiveUser(this.active);
		user.setEmail(email);
		return user;
	}
	
	public User buildUser() {
		return new User(username, firstname, lastname, email);
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
	
	public String getEmail() {
		return email;
	}

	public Boolean isActive() {
		return active;
	}
	
	public void setTeamDTOs(List<TeamDTO> teams) {
		this.teams = teams;
	}

	public void setWorkItemDTOs(List<WorkItemDTO> workItems) {
		this.workItems = workItems;
	}

	public String toJSONString() {
		return String.format("{\"itemKey\": \"%s\", \"firstname\": \"%s\", \"lastname\": \"%s\", \"username\": \"%s\", \"email\": \"%s\", \"active\": %s}", getItemKey(), firstname, lastname, username, email, active);
	}
	
}
