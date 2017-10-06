package an.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Email {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "body", nullable = false)
	private String body;

	@Column(name = "subject", nullable = false)
	private String subject;

	@OneToMany(fetch=FetchType.EAGER, orphanRemoval=true, cascade=CascadeType.ALL)
	private Collection<EmailTracker> trackers = new ArrayList<EmailTracker>();
	
	public Email() {}

	public Email(String subject, String body) {
		this.body = body;
		this.subject = subject;
	}

	public Long getId() {
		return id;
	}

	public String getBody() {
		return body;
	}

	public String getSubject() {
		return subject;
	}
	
	public Collection<EmailTracker> getTrackers() {
		return trackers;
	}
	
	@Override
	public String toString() {
		return "Email [id=" + id + ", body=" + body + ", subject=" + subject + "]";
	}

	public void setTrackers(Collection<EmailTracker> trackers) {
		this.trackers = trackers;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void addTrackers(Collection<EmailTracker> ts) {
		ts.forEach(t -> {
			t.setEmail(this);
			this.trackers.add(t);
		});
	}

}
