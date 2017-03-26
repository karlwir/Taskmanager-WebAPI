package se.kawi.taskmanagerwebapi.resource.query;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.QueryParam;
import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import se.kawi.taskmanagerservicelib.model.Issue;
import se.kawi.taskmanagerservicelib.model.Issue_;
import se.kawi.taskmanagerservicelib.model.WorkItem;

public class IssueQueryBean extends BaseQueryBean {
	
	@QueryParam("title") private String title;
	@QueryParam("description") private String description;
	@QueryParam("open") private String open;
	
	private WorkItem workItem;
	
	public void setWorkItem(WorkItem workItem) {
		this.workItem = workItem;
	}
	
	@Override
	public Pageable buildPageable() {
		possibleSortArray = new String[]{"title", "description", "open", "workItem"};
		defaultSortArray = new String[]{"workItem", "title"};
		return super.buildPageable();
	}

	public Specification<Issue> buildSpecification() {
		return (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<>();

			if (title != null) {
				predicates.add(cb.like(root.get(Issue_.title), "%" + title + "%"));
			}
			if (description != null) {
				predicates.add(cb.like(root.get(Issue_.description), "%" + description + "%"));
			}
			if (open != null) {
				if (open.toLowerCase().equals("true") || open.toLowerCase().equals("false")) {
					predicates.add(cb.equal(root.get(Issue_.open), Boolean.parseBoolean(open)));
				}
			}
			if (workItem != null) {
				predicates.add(cb.equal(root.get(Issue_.workItem), workItem));
			}
			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}

}
