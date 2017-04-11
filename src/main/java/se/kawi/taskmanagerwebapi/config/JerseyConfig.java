package se.kawi.taskmanagerwebapi.config;

import org.springframework.stereotype.Component;
import org.glassfish.jersey.server.ResourceConfig;

import se.kawi.taskmanagerwebapi.exception.BadRequestException;
import se.kawi.taskmanagerwebapi.exception.NotFoundException;
import se.kawi.taskmanagerwebapi.resource.HomeResource;
import se.kawi.taskmanagerwebapi.resource.IssueResource;
import se.kawi.taskmanagerwebapi.resource.TeamResource;
import se.kawi.taskmanagerwebapi.resource.UserResource;
import se.kawi.taskmanagerwebapi.resource.WorkItemResource;
import se.kawi.taskmanagerwebapi.resource.filter.AuthFilter;
import se.kawi.taskmanagerwebapi.resource.filter.CorsFilter;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(HomeResource.class);
        register(UserResource.class);
        register(TeamResource.class);
        register(IssueResource.class);
        register(WorkItemResource.class);
        register(CorsFilter.class);
        register(AuthFilter.class);
        register(BadRequestException.class);
        register(NotFoundException.class);
    }
}