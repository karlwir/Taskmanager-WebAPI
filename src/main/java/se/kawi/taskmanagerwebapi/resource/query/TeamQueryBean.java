package se.kawi.taskmanagerwebapi.resource.query;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import org.springframework.data.jpa.domain.Specification;

import se.kawi.taskmanagerservicelib.model.Team;
import se.kawi.taskmanagerservicelib.model.Team_;

public class TeamQueryBean extends BaseQueryBean {

	@QueryParam("name") @DefaultValue("") private String teamName;
	@QueryParam("active") @DefaultValue("true") private String activeTeam;

	public Specification<Team> buildSpecification() {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			
			if (!teamName.equals("")) {
				predicates.add(cb.like(root.get(Team_.teamName), "%" + teamName + "%"));
			}
			if (activeTeam.toLowerCase().equals("true") || activeTeam.toLowerCase().equals("false")) {
				predicates.add(cb.equal(root.get(Team_.activeTeam), Boolean.parseBoolean(activeTeam)));
			}
			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}
}
