package se.kawi.taskmanagerwebapi.resource.query;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import se.kawi.taskmanagerservicelib.model.Team;
import se.kawi.taskmanagerservicelib.model.Team_;

public class TeamQueryBean extends BaseQueryBean {

	@QueryParam("name") private String name;
	@QueryParam("active") private String active;
	
	@Override
	public Pageable buildPageable() {
		possibleSortArray = new String[]{"name", "active"};
		defaultSortArray = new String[]{"name"};
		return super.buildPageable();
	}

	public Specification<Team> buildSpecification() {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			
			if (name != null) {
				predicates.add(cb.like(root.get(Team_.name), "%" + name + "%"));
			}
			if (active != null) {
				if (active.toLowerCase().equals("true") || active.toLowerCase().equals("false")) {
					predicates.add(cb.equal(root.get(Team_.active), Boolean.parseBoolean(active)));
				}
			}
			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
