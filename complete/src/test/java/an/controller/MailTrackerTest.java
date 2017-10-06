package an.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import an.AbstractTest;
import an.model.Email;
import an.model.EmailTracker;
import an.repo.EmailRepo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MailTrackerTest extends AbstractTest{

    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;
    
    @Value(value="${web.server.name}")
    private String servername;

    @Autowired
    private EmailRepo repo;
    
    @Before
    @Transactional
    public void setUp() throws Exception {
        
    	this.base = new URL("http://" + servername + ":" + port + "/");
        
        Collection<String> urls = new ArrayList<>();
		urls.addAll(Arrays.asList(
			"http://www.apple.com/legal/",
			"http://www.apple.com/privacy/privacy-policy/",
			"mailto:retailresearch@apple.com?subject=Recent Feedback",
			"http://mynews.apple.com/survey/unsubscribe?v=2&la=en_us&a=%2BjZUWluSDkEWEbqViUdb515a6bYCN%2FENh4LcgqO7i2Q%3D",
			"http://survey.medallia.com/?ekvv9k2h2kvpyrbtxwsmht&lng=en_US&_score=1",
			"http://survey.medallia.com/?ekvv9k2h2kvpyrbtxwsmht&lng=en_US&_score=2",
			"http://survey.medallia.com/?ekvv9k2h2kvpyrbtxwsmht&lng=en_US&_score=3",
			"http://survey.medallia.com/?ekvv9k2h2kvpyrbtxwsmht&lng=en_US&_score=4",
			"http://survey.medallia.com/?ekvv9k2h2kvpyrbtxwsmht&lng=en_US&_score=5",
			"http://survey.medallia.com/?ekvv9k2h2kvpyrbtxwsmht&lng=en_US"
			));
		
		Email email = new Email("Dummy subject", "Dummy Content");
		
		Collection<EmailTracker> trackers = urls.stream().map(url -> {
			EmailTracker token = new EmailTracker(url);
			token.setEmail(email);
			return token;

		}).collect(Collectors.toSet());

		email.addTrackers(trackers);
		repo.save(email);
    }

    @Test
    public void testClick() throws Exception {
        ResponseEntity<String> response = template.getForEntity(base.toString() +"click?t=1",  String.class);
        assertThat(response.getStatusCodeValue(), equalTo(200));
        assertThat(response.getBody(), 
        		equalTo("Origin for token id 1 is http://www.apple.com/legal/ and has been clicked 1 time(s)."));
   
        response = template.getForEntity(base.toString() +"click?t=1",  String.class);
        assertThat(response.getBody(), 
        		equalTo("Origin for token id 1 is http://www.apple.com/legal/ and has been clicked 2 time(s)."));
        
        response = template.getForEntity(base.toString() +"click?t=3",  String.class);
        assertThat(response.getBody(), 
        		equalTo("Origin for token id 3 is mailto:retailresearch@apple.com?subject=Recent Feedback and has been clicked 1 time(s)."));
   
    }
    
    @Test 
    public void testMalformedClickRequest(){
    	ResponseEntity<String> response = template.getForEntity(base.toString() + "click",  String.class);
    	assertThat(response.getStatusCodeValue(), equalTo(400));
    }
}
