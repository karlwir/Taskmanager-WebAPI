package se.kawi.taskmanagerwebapi.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import se.kawi.taskmanagerservicelib.model.Team;

public class TeamDTO extends AbstractDTO {

	@JsonProperty
	private String name;
	@JsonProperty
	private String description;
	@JsonProperty
	private Boolean active;
	@JsonProperty(access = Access.READ_ONLY)
	@JsonIgnoreProperties(value = {"teams", "workItems"})
	private List<UserDTO> users;
	
	protected TeamDTO() {}
	
	public TeamDTO(Team team) {
		super(team);
		this.name = team.getName();
		this.active = team.isActive();
		this.description = team.getDescription();
	}
	
	public Team reflectDTO(Team team) {
		team.setName(name);
		team.setDescription(description);
		team.setActive(active);
		return team;
	}
	
	public Team buildTeam() {
		return new Team(name, description);
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Boolean isActive() {
		return active;
	}
	
	public void setUserDTOs(List<UserDTO> users) {
		this.users = users;
	}

	public String toJSONString() {
		return String.format("{\"itemKey\": \"%s\", \"name\": \"%s\", \"description\": \"%s\", \"active\": %s}", getItemKey(), name, description, active);
	}
	
}
