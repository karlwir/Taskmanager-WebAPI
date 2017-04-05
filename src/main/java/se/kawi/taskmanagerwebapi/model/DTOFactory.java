package se.kawi.taskmanagerwebapi.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.springframework.stereotype.Component;

import se.kawi.taskmanagerservicelib.model.AbstractEntity;
import se.kawi.taskmanagerservicelib.model.Issue;
import se.kawi.taskmanagerservicelib.model.Team;
import se.kawi.taskmanagerservicelib.model.User;
import se.kawi.taskmanagerservicelib.model.WorkItem;
import se.kawi.taskmanagerwebapi.resource.IssueResource;
import se.kawi.taskmanagerwebapi.resource.TeamResource;
import se.kawi.taskmanagerwebapi.resource.UserResource;
import se.kawi.taskmanagerwebapi.resource.WorkItemResource;

@Component
public class DTOFactory<E extends AbstractEntity> {

	public AbstractDTO buildDTO(E entity, UriInfo uriInfo) {
		if (entity.getClass().equals(User.class)) {
			User user = (User) entity;
			return buildUserDTO(user, true, uriInfo);
		}
		if (entity.getClass().equals(WorkItem.class)) {
			WorkItem workItem = (WorkItem) entity;
			return buildWorkItemDTO(workItem, true, uriInfo);
		}
		if (entity.getClass().equals(Team.class)) {
			Team team = (Team) entity;
			return buildTeamDTO(team, true, uriInfo);
		}
		if (entity.getClass().equals(Issue.class)) {
			Issue issue = (Issue) entity;
			return buildIssueDTO(issue, true, uriInfo);
		}
		return null;
	}
	
	public List<AbstractDTO> buildDTOs(Collection<E> entities, UriInfo uriInfo) {
		List<AbstractDTO> dtoList = new ArrayList<>();
		for (E entity : entities) {
			dtoList.add(buildDTO(entity, uriInfo));
		} 
		return dtoList;
	}
	
	public UserDTO buildUserDTO(User user, boolean withChilds, UriInfo uriInfo) {
		UserDTO userDTO = new UserDTO(user);
		URI origin = uriInfo.getBaseUriBuilder().path(UserResource.class).path(userDTO.getItemKey()).build();
		userDTO.setOrigin(origin);
		if (withChilds) {
			userDTO.setTeamDTOs(buildTeamDTOs(user.getTeams(), false, uriInfo));
			userDTO.setWorkItemDTOs(buildWorkItemDTOs(user.getWorkItems(), false, uriInfo));
		}
		return userDTO;
	}
	
	public WorkItemDTO buildWorkItemDTO(WorkItem workItem, boolean withChilds, UriInfo uriInfo) {
		WorkItemDTO workItemDTO = new WorkItemDTO(workItem);
		URI origin = uriInfo.getBaseUriBuilder().path(WorkItemResource.class).path(workItemDTO.getItemKey()).build();
		workItemDTO.setOrigin(origin);
		if (withChilds) {
			workItemDTO.setUsers(buildUserDTOs(workItem.getUsers(), false, uriInfo));
			workItemDTO.setIssues(buildIssuesDTOs(workItem.getIssues(), false, uriInfo));
		}
		return workItemDTO;
	}
	
	public TeamDTO buildTeamDTO(Team team, boolean withChilds, UriInfo uriInfo) {
		TeamDTO teamDTO = new TeamDTO(team);
		URI origin = uriInfo.getBaseUriBuilder().path(TeamResource.class).path(teamDTO.getItemKey()).build();
		teamDTO.setOrigin(origin);
		if (withChilds) {
			teamDTO.setUserDTOs(buildUserDTOs(team.getUsers(), false, uriInfo));
		}
		return teamDTO;
	}
	
	public IssueDTO buildIssueDTO(Issue issue, boolean withChilds, UriInfo uriInfo) {
		IssueDTO issueDTO = new IssueDTO(issue);
		URI origin = uriInfo.getBaseUriBuilder().path(IssueResource.class).path(issueDTO.getItemKey()).build();
		issueDTO.setOrigin(origin);		
		if(withChilds && issue.getWorkItem() != null) {
			issueDTO.setWorkItem(new WorkItemDTO(issue.getWorkItem()));				
		}
		return issueDTO;
	}
	
	public List<UserDTO> buildUserDTOs(Collection<User> users, boolean withChilds, UriInfo uriInfo) {
		List<UserDTO> dtoList = new ArrayList<>();
		for (User user : users) {
			dtoList.add(buildUserDTO(user, withChilds, uriInfo));
		} 
		return dtoList;
	}
	
	public List<WorkItemDTO> buildWorkItemDTOs(Collection<WorkItem> workItems, boolean withChilds, UriInfo uriInfo) {
		List<WorkItemDTO> dtoList = new ArrayList<>();
		for (WorkItem workItem : workItems) {
			dtoList.add(buildWorkItemDTO(workItem, withChilds, uriInfo));
		} 
		return dtoList;
	}
	
	public List<TeamDTO> buildTeamDTOs(Collection<Team> teams, boolean withChilds, UriInfo uriInfo) {
		List<TeamDTO> dtoList = new ArrayList<>();
		for(Team team : teams) {
			dtoList.add(buildTeamDTO(team, withChilds, uriInfo));
		}
		return dtoList;
	}
	
	public List<IssueDTO> buildIssuesDTOs(Collection<Issue> issues, boolean withChilds, UriInfo uriInfo) {
		List<IssueDTO> dtoList = new ArrayList<>();
		for (Issue issue : issues) {
			dtoList.add(buildIssueDTO(issue, withChilds, uriInfo));
		} 
		return dtoList;
	}
}
