package se.kawi.taskmanagerwebapi.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import se.kawi.taskmanagerservicelib.model.Team;

public class TeamDTO extends AbstractDTO {

	@JsonProperty
	private String teamName;
	@JsonProperty
	private boolean activeTeam;
	@JsonProperty
	@JsonIgnoreProperties(value = {"teams", "workItems"})
	private List<UserDTO> users;
	
	protected TeamDTO() {}
	
	public TeamDTO(Team team) {
		this.itemKey = team.getItemKey();
		this.teamName = team.getTeamName();
		this.activeTeam = team.isActiveTeam();
	}
	
	public Team reflectDTO(Team team) {
		team.setTeamName(teamName);
		team.setActiveTeam(activeTeam);
		return team;
	}
	
	public Team buildTeam() {
		return new Team(teamName);
	}
	
	public String getTeamName() {
		return teamName;
	}
	
	public boolean isActiveTeam() {
		return activeTeam;
	}
	
	public void setUserDTOs(List<UserDTO> users) {
		this.users = users;
	}
	
}
