package se.kawi.taskmanagerwebapi.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import se.kawi.taskmanagerservicelib.model.Team;
import se.kawi.taskmanagerservicelib.model.User;

public class TeamDTO extends AbstractDTO {

	@JsonProperty
	private String teamName;
	@JsonProperty
	private boolean activeTeam;
	@JsonProperty
	private Set<User> users;

	public TeamDTO(Team team) {
		this.teamName = team.getTeamName();
		this.activeTeam = team.isActiveTeam();
		this.users = team.getUsers();
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
}
