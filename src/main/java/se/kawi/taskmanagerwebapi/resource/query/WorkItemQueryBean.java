package se.kawi.taskmanagerwebapi.resource.query;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

import javax.ws.rs.QueryParam;
import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import se.kawi.taskmanagerservicelib.model.User;
import se.kawi.taskmanagerservicelib.model.WorkItem;
import se.kawi.taskmanagerservicelib.model.WorkItem_;

public class WorkItemQueryBean extends BaseQueryBean {

	@QueryParam("title") private String title;
	@QueryParam("description") private String description;
	@QueryParam("status") private String status;
	@QueryParam("hasissues") private String hasIssues;
	
	private User user;
	
	private Set<User> users;
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	@Override
	public Pageable buildPageable() {
		possibleSortArray = new String[]{"title", "description", "status, updateDate"};
		defaultSortArray = new String[]{"title"};
		return super.buildPageable();
	}

	public Specification<WorkItem> buildSpecification() {
		return (root, query, cb) -> {
			final List<Predicate> andPredicates = new ArrayList<>();

			if (title != null) {
				andPredicates.add(cb.like(root.get(WorkItem_.title), "%" + title + "%"));
			}
			if (description != null) {
				andPredicates.add(cb.like(root.get(WorkItem_.description), "%" + description + "%"));
			}
			if (status != null) {
				WorkItem.Status statusEnum;
				try {
					statusEnum = (WorkItem.Status.valueOf(this.status.toUpperCase()));
					andPredicates.add(cb.equal(root.get(WorkItem_.status), statusEnum));
				} catch (IllegalArgumentException e) {}				
			}
			if (hasIssues != null) {
				if (hasIssues.toLowerCase().equals("true")) {
					andPredicates.add(cb.isNotEmpty(root.get(WorkItem_.issues)));
				}
				if (hasIssues.toLowerCase().equals("false")) {
					andPredicates.add(cb.isEmpty(root.get(WorkItem_.issues)));
				}
			}
			if (user != null) {
				andPredicates.add(cb.isMember(user, root.get(WorkItem_.users)));
			}
			if (users != null) {	
				Set<Predicate> orPredicates = new HashSet<>();
				
				for(User user : users) {
					List<Predicate> pandPedicatesCopy = new ArrayList<>();
					pandPedicatesCopy.addAll(andPredicates);
					pandPedicatesCopy.add(cb.isMember(user, root.get(WorkItem_.users)));
					orPredicates.add(cb.and(pandPedicatesCopy.toArray(new Predicate[pandPedicatesCopy.size()])));
				}
				return cb.or(orPredicates.toArray(new Predicate[orPredicates.size()]));
			}
			return cb.and(andPredicates.toArray(new Predicate[andPredicates.size()]));
		};		
	}
	
}
