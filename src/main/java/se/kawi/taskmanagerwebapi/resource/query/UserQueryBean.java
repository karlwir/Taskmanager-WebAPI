package se.kawi.taskmanagerwebapi.resource.query;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.QueryParam;
import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import se.kawi.taskmanagerservicelib.model.Team;
import se.kawi.taskmanagerservicelib.model.User;
import se.kawi.taskmanagerservicelib.model.User_;

public class UserQueryBean extends BaseQueryBean {
	
	@QueryParam("firstname") private String firstname;
	@QueryParam("lastname") private String lastname;
	@QueryParam("username") private String username;
	@QueryParam("email") private String email;
	@QueryParam("active") private String active;
	
	
	private Team teams;
	
	public void setTeam(Team teams) {
		this.teams = teams;
	}
	
	public void setEmail(String email) {
		this.email = email;
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

			if (firstname != null) {
				predicates.add(cb.like(root.get(User_.firstname), "%" + firstname + "%"));
			}
			if (lastname != null) {
				predicates.add(cb.like(root.get(User_.lastname), "%" + lastname + "%"));
			}
			if (username != null) {
				predicates.add(cb.equal(root.get(User_.username), username));
			}
			if (email != null) {
				predicates.add(cb.equal(root.get(User_.email), email));
			}
			if (active != null) {
				if (active.toLowerCase().equals("true") || active.toLowerCase().equals("false")) {
					predicates.add(cb.equal(root.get(User_.active), Boolean.parseBoolean(active)));
				}
			}
			if (teams != null) {
				predicates.add(cb.isMember(teams, root.get(User_.teams)));
			}

			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}

}
