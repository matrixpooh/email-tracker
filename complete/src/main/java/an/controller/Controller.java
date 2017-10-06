package an.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import an.function.HrefExtractor;
import an.model.Email;
import an.model.EmailTracker;
import an.repo.EmailRepo;
import an.repo.EmailTrackerRepo;

@RestController
@EnableAutoConfiguration
@RequestMapping(path = "/")
public class Controller {

	@Autowired
	EmailTrackerRepo trackerRepo;

	@Autowired
	EmailRepo repo;

	@RequestMapping("/upload")
	public String uploadContent() {
		// TODO:: render 'browse' for file with submit button
		return "parsed";
	}

	@RequestMapping("/createtrackers")
	public Email createTrackers(
			@RequestParam(value = "content", required = true) String content,
			@RequestParam(value = "subject", required = true) String subject) throws UnsupportedEncodingException {

		Email email = new Email(subject, URLDecoder.decode(content, "UTF-8"));
		Collection<String> urls = new HrefExtractor().extractAll(email.getBody());
		
		Collection<EmailTracker> trackers = urls.stream().map(url -> {
			EmailTracker token = new EmailTracker(url);
			return token;

		}).collect(Collectors.toSet());

		email.addTrackers(trackers);
		
		repo.save(email);

		return email;
	}

	@RequestMapping("/click")
	public String recordClick(@RequestParam(value = "t", required = true) Long token) {

		EmailTracker tracker = trackerRepo.findOne(token);

		if (tracker == null)
			throw new RuntimeException("Failed to find origin url for token " + token);

		tracker.click();
		trackerRepo.save(tracker);

		// FIXME:: redirect to the origin url

		return "Origin for token id " + token + " is " + tracker.getUrl() + " and has been clicked "
				+ tracker.getClicks() + " time(s).";
	}

}
