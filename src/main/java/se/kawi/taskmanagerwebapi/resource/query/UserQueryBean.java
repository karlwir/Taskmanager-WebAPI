package se.kawi.taskmanagerwebapi.resource.query;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import se.kawi.taskmanagerservicelib.model.Team;
import se.kawi.taskmanagerservicelib.model.User;
import se.kawi.taskmanagerservicelib.model.User_;

public class UserQueryBean extends BaseQueryBean {
	
	@QueryParam("firstname") @DefaultValue("") private String firstname;
	@QueryParam("lastname") @DefaultValue("") private String lastname;
	@QueryParam("username") @DefaultValue("") private String username;
	@QueryParam("active") @DefaultValue("") private String active;
	
	
	private Team teams;
	
	public void setTeam(Team teams) {
		this.teams = teams;
	}
	
	@Override
	public Pageable buildPageable() {
		possibleSortArray = new String[]{"firstname", "lastname", "username", "active"};
		defaultSortArray = new String[]{"lastname", "firstname"};
		return super.buildPageable();
	}
	
	public Specification<User> buildSpecification() {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (!firstname.equals("")) {
				predicates.add(cb.like(root.get(User_.firstname), "%" + firstname + "%"));
			}
			if (!lastname.equals("")) {
				predicates.add(cb.like(root.get(User_.lastname), "%" + lastname + "%"));
			}
			if (!username.equals("")) {
				predicates.add(cb.equal(root.get(User_.username), username));
			}
			if (active.toLowerCase().equals("true") || active.toLowerCase().equals("false")) {
				predicates.add(cb.equal(root.get(User_.active), Boolean.parseBoolean(active)));
			}
			if (teams != null) {
				predicates.add(cb.isMember(teams, root.get(User_.teams)));
			}

			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}

}
