package an;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import javax.mail.MessagingException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import an.function.ResourceReader;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class IntegrationTest {

	@Autowired
	private TestRestTemplate template;

	/**
	 * load urls into a persistent store (mysql) read in application properties
	 * based on env - dev,test, etc hit {env} url with tracker=1 assert 1.
	 * rediect to origin happened 2. counter incremented 3. last clicked date
	 * updated
	 */
	@Value("${spring.datasource.user}")
	String dbuser;

	@Value("${hibernate.hbm2ddl.auto}")
	String hbmSchemaValidate;

    @LocalServerPort
    private int port;
    
    @Value(value="${web.server.name}")
    private String servername;
 
	private URL base;

	@Before
	public void setUp() throws Exception {
	 	this.base = new URL("http://" + servername + ":" + port + "/");
	}

	@Test
	public void testCreateEmail() throws MessagingException, IOException{
		InputStream source = ResourceReader.read("email.eml");
		Assert.assertNotNull(source);

		String content = new String(ResourceReader.readAll(source));
		//String content="hard coded in test with url <a href=\"http://www.apple.com/legal/\">Apple Legal</a>";
		
		StringBuilder  sb = new StringBuilder(base.toString());
		sb.append("createtrackers?content=")
			.append(URLEncoder.encode(content, "UTF-8"))
				.append("&subject=Hard Coded Subject");
	
		ResponseEntity<String> response = template.getForEntity(sb.toString(), String.class);
		Assert.assertEquals("done", response.getBody());
	}
	
	@Test
	public void testClick() throws Exception {
	
	}

	/**
	 * read in html capture all urls store all urls in a persistent store(mysql)
	 * assert
	 * 
	 * @throws IOException
	 * @throws MessagingException
	 */
	//FIXME::fails
	@Test
	public void testCreateTrackers() throws MessagingException, IOException {
		InputStream source = ResourceReader.read("email.eml");
		Assert.assertNotNull(source);

		String content = new String(ResourceReader.readAll(source));

		StringBuilder  sb = new StringBuilder(base.toString());
		sb.append("createtrackers?content=").append(URLEncoder.encode(content, "UTF-8"));
		
		ResponseEntity<String> response = template.getForEntity(sb.toString(), String.class);
		System.err.println("Received response: " + response.getBody());
		Assert.assertEquals(200, response.getStatusCode());
	}
}
