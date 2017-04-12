package se.kawi.taskmanagerwebapi.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import se.kawi.taskmanagerwebapi.exception.BadRequestException;
import se.kawi.taskmanagerwebapi.exception.NotFoundException;
import se.kawi.taskmanagerwebapi.resource.HomeResource;
import se.kawi.taskmanagerwebapi.resource.IssueResource;
import se.kawi.taskmanagerwebapi.resource.TeamResource;
import se.kawi.taskmanagerwebapi.resource.UserResource;
import se.kawi.taskmanagerwebapi.resource.WorkItemResource;
import se.kawi.taskmanagerwebapi.resource.filter.CorsFilter;

@Profile("test")
@Component
public class JerseyConfigTest extends ResourceConfig {
	public JerseyConfigTest() {
	        register(HomeResource.class);
	        register(UserResource.class);
	        register(TeamResource.class);
	        register(IssueResource.class);
	        register(WorkItemResource.class);
	        register(CorsFilter.class);
	        register(BadRequestException.class);
	        register(NotFoundException.class);
	    }
}