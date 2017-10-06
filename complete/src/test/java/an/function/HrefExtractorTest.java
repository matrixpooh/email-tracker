package an.function;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import an.AbstractTest;
import an.model.EmailTracker;
import an.repo.EmailTrackerRepo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HrefExtractorTest extends AbstractTest{

	@Autowired
	EmailTrackerRepo repo;
	
	@Autowired
	MailStats stats;
	
	@Test 
	public void testHrefExtractEncoded() throws UnsupportedEncodingException{
		String origin = "hard coded in test with url <a href=\"http://www.apple.com/legal/\">Apple Legal</a>";
		String content = URLDecoder.decode(
				URLEncoder.encode(
						origin, 
							"UTF-8"),
				"UTF-8");
		Assert.assertEquals(origin, content);
		
		Collection<String> urls = new HrefExtractor().extractAll(content);
		Assert.assertEquals(1, urls.size());
		Assert.assertEquals("http://www.apple.com/legal/", urls.iterator().next());
	}
	
	@Test
	public void testHrefExtract() throws IOException, MessagingException{
		
		InputStream source = ResourceReader.read("email.eml");
		Assert.assertNotNull(source);
		
		String content = new String(ResourceReader.readAll(source));
		
        Assert.assertFalse(content.contains("<a href=3D"));
		Assert.assertTrue(content.contains("<a href=\""));

		
		Collection<String> urls = new HrefExtractor().extractAll(content);
		urls.forEach(System.out::println);
		Assert.assertEquals(10, urls.size());
	}
	
	@Test
	public void testMaskUrl(){
		String origin = "http://survey.medallia.com/?ekvv9k2h2kvpyrbtxwsmht&lng=en_US&_score=5";
		EmailTracker record = stats.createTracker(origin);
		Assert.assertEquals(new Long(1), record.getToken());
	}
	
	@Test
	public void testMaskUrls(){
		
		Collection<String> urls = new TreeSet<>();
		urls.addAll(Arrays.asList("http://survey.medallia.com/?ekvv9k2h2kvpyrbtxwsmht&lng=en_US&_score=5",
			"http://survey.medallia.com/?ekvv9k2h2kvpyrbtxwsmht&lng=en_US",
			"http://www.apple.com/legal/",
			"http://www.apple.com/privacy/privacy-policy/",
			"http://mynews.apple.com/survey/unsubscribe?v=2&la=en_us&a=%2BjZUWluSDkEWEbqViUdb515a6bYCN%2FENh4LcgqO7i2Q%3D",
			"http://survey.medallia.com/?ekvv9k2h2kvpyrbtxwsmht&lng=en_US&_score=1",
			"http://survey.medallia.com/?ekvv9k2h2kvpyrbtxwsmht&lng=en_US&_score=2",
			"http://survey.medallia.com/?ekvv9k2h2kvpyrbtxwsmht&lng=en_US&_score=3",
			"mailto:retailresearch@apple.com?subject=Recent Feedback",
			"http://survey.medallia.com/?ekvv9k2h2kvpyrbtxwsmht&lng=en_US&_score=4"));
		
		Collection<String> masked = urls.stream().map( url-> {
			
			EmailTracker token = stats.createTracker(url);
			return token.getUrl();
			
		}).collect(Collectors.toSet());
		
		masked.forEach(System.out::println);
		Assert.assertEquals(10, masked.size());
	}
	
	
}
