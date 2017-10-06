package an.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import an.AbstractTest;
import an.repo.EmailRepo;
import an.repo.EmailTrackerRepo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailTest extends AbstractTest{

	@Autowired
	EmailRepo repo;
	
	@Autowired
	EmailTrackerRepo trepo;
	
	@Test
	@Transactional
	public void saveEmailAndTrackers(){
		Email e = new Email("subject", "body");
		
		Collection<EmailTracker> t= new ArrayList<>();
		EmailTracker tracker = new EmailTracker("http://duckduckgo.com");
		//tracker.setEmail(e);
		t.add(tracker);
		
		e.getTrackers().addAll(t);
		
		e = repo.save(e);
		Assert.assertNotNull(e.getId());
		Assert.assertNotEquals(new Long(0), e.getId());
		
		Iterable<EmailTracker> trackers= trepo.findAll();
		Assert.assertTrue(trackers.iterator().hasNext());
		
//		EmailTracker et = trackers.iterator().next();
		//Email eSaved = et.getEmail();
		
//		Assert.assertNotNull(eSaved);
//		Assert.assertEquals(e.getId(), eSaved.getId());
//	
	}
}
