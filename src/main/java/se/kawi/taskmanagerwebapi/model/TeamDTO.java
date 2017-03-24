package se.kawi.taskmanagerwebapi.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import se.kawi.taskmanagerservicelib.model.Team;

public class TeamDTO extends AbstractDTO {

	@JsonProperty
	private String name;
	@JsonProperty
	private Boolean active;
	@JsonProperty
	@JsonIgnoreProperties(value = {"teams", "workItems"})
	private List<UserDTO> users;
	
	protected TeamDTO() {}
	
	public TeamDTO(Team team) {
		this.itemKey = team.getItemKey();
		this.name = team.getName();
		this.active = team.isActive();
	}
	
	public Team reflectDTO(Team team) {
		team.setName(name);
		team.setActive(active);
		return team;
	}
	
	public Team buildTeam() {
		return new Team(name);
	}
	
	public String getName() {
		return name;
	}
	
	public Boolean isActive() {
		return active;
	}
	
	public void setUserDTOs(List<UserDTO> users) {
		this.users = users;
	}
	
}
