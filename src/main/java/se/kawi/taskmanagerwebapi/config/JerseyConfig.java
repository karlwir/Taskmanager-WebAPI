package se.kawi.taskmanagerwebapi.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import se.kawi.taskmanagerwebapi.resource.HomeResource;
import se.kawi.taskmanagerwebapi.resource.IssueResource;
import se.kawi.taskmanagerwebapi.resource.TeamResource;
import se.kawi.taskmanagerwebapi.resource.UserResource;
import se.kawi.taskmanagerwebapi.resource.WorkItemResource;
import se.kawi.taskmanagerwebapi.resource.filter.AuthFilter;
import se.kawi.taskmanagerwebapi.resource.filter.CorsFilter;

@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(HomeResource.class);
        register(UserResource.class);
        register(TeamResource.class);
        register(IssueResource.class);
        register(WorkItemResource.class);
        register(CorsFilter.class);
        register(AuthFilter.class);
    }
}