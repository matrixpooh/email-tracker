package an;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.junit.After;
import org.junit.Before;

public class AbstractTest {
	Server h2WebConsole;

	@Before
	public void init() throws SQLException {
		h2WebConsole = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
		h2WebConsole.start();
	}

	@After
	public void destroy() {
		h2WebConsole.stop();
		h2WebConsole.shutdown();
	}
}
