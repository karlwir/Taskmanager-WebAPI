package se.kawi.taskmanagerwebapi.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import se.kawi.taskmanagerservicelib.model.Team;
import se.kawi.taskmanagerservicelib.model.User;
import se.kawi.taskmanagerservicelib.model.WorkItem;

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
	private Set<Team> teams;
	@JsonProperty
	private Set<WorkItem> workItems;
	
	protected UserDTO() {}

	protected UserDTO(User user) {
		this.itemKey = user.getItemKey();
		this.username = user.getUsername();
		this.firstname = user.getFirstname();
		this.lastname = user.getLastname();
		this.activeUser = user.isActiveUser();
		this.teams = user.getTeams();
		this.workItems = user.getWorkItems();
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
	
}
