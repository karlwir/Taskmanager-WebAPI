package se.kawi.taskmanagerwebapi.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

@Component
@Path("/")
public class HomeResource {

	@GET
	@Produces({ MediaType.TEXT_HTML + "; charset=UTF-8" })
	public InputStream hello() throws IOException {
		File file = new File("./src/main/resources/index.html");
		return new FileInputStream(file);
	}
	
	@GET
	@Path("/docjson")
	@Produces({ MediaType.APPLICATION_JSON + "; charset=UTF-8" })
	public InputStream docjson() throws IOException {
		File file = new File("./src/main/resources/docdata.json");
		return new FileInputStream(file);
	}
}
