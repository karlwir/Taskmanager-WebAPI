package se.kawi.taskmanagerwebapi;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@TestPropertySource("/test.properties")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public abstract class AbstractResourceTest {

	protected final static String USER_RESOURCE_URI = "http://localhost:8443/users";
	protected final static String WORKITEM_RESOURCE_URI = "http://localhost:8443/workitems";
	protected final static String ISSUE_RESOURCE_URI = "http://localhost:8443/issues";
	protected final static String TEAM_RESOURCE_URI = "http://localhost:8443/teams";
	
	protected Client client;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Autowired
	DataSource dataSource;
	
	@Before
	public void prepare() throws JSONException {
		client = ClientBuilder.newClient();
	}
	
	@After
	public void tearDown() {
	    try {
	        clearDatabase();
	    } catch (Exception e) {
	        fail(e.getMessage());
	    }
	}

	public void clearDatabase() throws Exception {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			try {
				Statement stmt = connection.createStatement();
				try {
					stmt.execute("DELETE FROM PUBLIC.USERS");
					stmt.execute("DELETE FROM PUBLIC.WORKITEMS");
					connection.commit();
				} finally {
					stmt.close();
				}
			} catch (SQLException e) {
				connection.rollback();
				throw new Exception(e);
			}
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}
}