package se.kawi.taskmanagerwebapi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Component;

import se.kawi.taskmanagerservicelib.model.AbstractEntity;
import se.kawi.taskmanagerservicelib.model.Issue;
import se.kawi.taskmanagerservicelib.model.Team;
import se.kawi.taskmanagerservicelib.model.User;
import se.kawi.taskmanagerservicelib.model.WorkItem;

@Component
public class DTOFactory<E extends AbstractEntity> {

	public AbstractDTO buildDTO(E entity) {
		if (entity.getClass().equals(User.class)) {
			User user = (User) entity;
			return buildUserDTO(user, true);
		}
		if (entity.getClass().equals(WorkItem.class)) {
			WorkItem workItem = (WorkItem) entity;
			return buildWorkItemDTO(workItem, true);
		}
		if (entity.getClass().equals(Team.class)) {
			Team team = (Team) entity;
			return buildTeamDTO(team, true);
		}
		if (entity.getClass().equals(Issue.class)) {
			Issue issue = (Issue) entity;
			return buildIssueDTO(issue, true);
		}
		return null;
	}
	
	public List<AbstractDTO> buildDTO(Collection<E> entities) {
		List<AbstractDTO> dtoList = new ArrayList<>();
		for (E entity : entities) {
			dtoList.add(buildDTO(entity));
		} 
		return dtoList;
	}
	
	public UserDTO buildUserDTO(User user, boolean withChilds) {
		UserDTO userDTO = new UserDTO(user);
		if (withChilds) {
			userDTO.setTeamDTOs(buildTeamDTOs(user.getTeams(), false));
			userDTO.setWorkItemDTOs(buildWorkItemDTOs(user.getWorkItems(), false));
		}
		return userDTO;
	}
	
	public WorkItemDTO buildWorkItemDTO(WorkItem workItem, boolean withChilds) {
		WorkItemDTO workItemDTO = new WorkItemDTO(workItem);
		if (withChilds) {
			workItemDTO.setUsers(buildUserDTOs(workItem.getUsers(), false));
			workItemDTO.setIssues(buildIssuesDTOs(workItem.getIssues(), false));
		}
		return workItemDTO;
	}
	
	public TeamDTO buildTeamDTO(Team team, boolean withChilds) {
		TeamDTO teamDTO = new TeamDTO(team);
		if (withChilds) {
			teamDTO.setUserDTOs(buildUserDTOs(team.getUsers(), false));
		}
		return teamDTO;
	}
	
	public IssueDTO buildIssueDTO(Issue issue, boolean withChilds) {
		IssueDTO issueDTO = new IssueDTO(issue);
		if(withChilds && issue.getWorkItem() != null) {
			issueDTO.setWorkItem(new WorkItemDTO(issue.getWorkItem()));				
		}
		return issueDTO;
	}
	
	public List<UserDTO> buildUserDTOs(Collection<User> users, boolean withChilds) {
		List<UserDTO> dtoList = new ArrayList<>();
		for (User user : users) {
			dtoList.add(buildUserDTO(user, withChilds));
		} 
		return dtoList;
	}
	
	public List<WorkItemDTO> buildWorkItemDTOs(Collection<WorkItem> workItems, boolean withChilds) {
		List<WorkItemDTO> dtoList = new ArrayList<>();
		for (WorkItem workItem : workItems) {
			dtoList.add(buildWorkItemDTO(workItem, withChilds));
		} 
		return dtoList;
	}
	
	public List<TeamDTO> buildTeamDTOs(Collection<Team> teams, boolean withChilds) {
		List<TeamDTO> dtoList = new ArrayList<>();
		for(Team team : teams) {
			dtoList.add(buildTeamDTO(team, withChilds));
		}
		return dtoList;
	}
	
	public List<IssueDTO> buildIssuesDTOs(Collection<Issue> issues, boolean withChilds) {
		List<IssueDTO> dtoList = new ArrayList<>();
		for (Issue issue : issues) {
			dtoList.add(buildIssueDTO(issue, withChilds));
		} 
		return dtoList;
	}
}
