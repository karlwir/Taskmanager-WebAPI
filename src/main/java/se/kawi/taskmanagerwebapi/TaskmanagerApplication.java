package se.kawi.taskmanagerwebapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"se.kawi.taskmanagerservicelib","se.kawi.taskmanagerwebapi"})
public class TaskmanagerApplication extends SpringBootServletInitializer {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TaskmanagerApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(TaskmanagerApplication.class, args);
	}
}
