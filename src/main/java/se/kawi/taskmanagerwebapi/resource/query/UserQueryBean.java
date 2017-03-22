package se.kawi.taskmanagerwebapi.resource.query;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import org.springframework.data.jpa.domain.Specification;

import se.kawi.taskmanagerservicelib.model.Team;
import se.kawi.taskmanagerservicelib.model.User;
import se.kawi.taskmanagerservicelib.model.User_;

public class UserQueryBean extends BaseQueryBean {
	
	@QueryParam("firstname") @DefaultValue("") private String firstname;
	@QueryParam("lastname") @DefaultValue("") private String lastname;
	@QueryParam("username") @DefaultValue("") private String username;
	@QueryParam("active") @DefaultValue("") private String activeUser;
	
	private Team team;
	
	public void setTeam(Team team) {
		this.team = team;
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
			if (activeUser.toLowerCase().equals("true") || activeUser.toLowerCase().equals("false")) {
				predicates.add(cb.equal(root.get(User_.activeUser), Boolean.parseBoolean(activeUser)));
			}
			if (team != null) {
				predicates.add(cb.isMember(team, root.get(User_.teams)));
			}

			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};
	}

}
