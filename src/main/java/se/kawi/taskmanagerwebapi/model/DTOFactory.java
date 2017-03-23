package se.kawi.taskmanagerwebapi.model;

import java.util.ArrayList;
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
			return new UserDTO((User) entity);
		}
		if (entity.getClass().equals(WorkItem.class)) {
			return new WorkItemDTO((WorkItem) entity);
		}
		if (entity.getClass().equals(Team.class)) {
			return new TeamDTO((Team) entity);
		}
		if (entity.getClass().equals(Issue.class)) {
			return new IssueDTO((Issue) entity);
		}
		return null;
	}
	
	public List<AbstractDTO> buildDTO(List<E> entities) {
		List<AbstractDTO> dtoList = new ArrayList<>();
		for (E entity : entities) {
			dtoList.add(buildDTO(entity));
		} 
		return dtoList;
	}

	public List<WorkItemDTO> buildWorkItemDTOs(List<WorkItem> workItems) {
		List<WorkItemDTO> dtoList = new ArrayList<>();
		for (WorkItem workItem : workItems) {
			dtoList.add(new WorkItemDTO(workItem));
		} 
		return dtoList;
	}

	public List<IssueDTO> buildIssuesDTOs(List<Issue> issues) {
		List<IssueDTO> dtoList = new ArrayList<>();
		for (Issue issue : issues) {
			dtoList.add(new IssueDTO(issue));
		} 
		return dtoList;
	}

	public List<UserDTO> buildUserDTOs(List<User> users) {
		List<UserDTO> dtoList = new ArrayList<>();
		for (User user : users) {
			dtoList.add(new UserDTO(user));
		} 
		return dtoList;
	}
}
