package an.function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import an.model.EmailTracker;
import an.repo.EmailTrackerRepo;

@Service
public class MailStats {

	@Autowired
	EmailTrackerRepo repo;
	
	public EmailTracker createTracker(String url) {
		
		EmailTracker tracker = new EmailTracker();
		tracker.setClicks((short)0);
		tracker.setLastClickedTime(null);
		tracker.setUrl(url);
		
		EmailTracker et = repo.save(tracker);
		
		return et;
	}

}
