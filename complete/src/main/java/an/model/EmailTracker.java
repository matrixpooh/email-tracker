package an.model;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="email_tracker")
public class EmailTracker {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long token;
    
    @Column(name="url", nullable=false)
    private String url;
    
    @Column(name="click_count")
    private short clicks;
 
    @Column(name="last_clicked_time")
    private Date lastClickedTime;

    @ManyToOne(targetEntity=Email.class, optional=false, cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private Email email;
    
    public EmailTracker() {}

	public EmailTracker(String url) {
		this.url = url;
	}

	public void click() {
		this.clicks++;
		this.lastClickedTime = new Date(System.currentTimeMillis());
	}
	
    public Long getToken() {
		return token;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public short getClicks() {
		return clicks;
	}

	public void setClicks(short clicks) {
		this.clicks = clicks;
	}

	public Date getLastClickedTime() {
		return lastClickedTime;
	}

	public void setToken(Long token) {
		this.token = token;
	}
	
	public void setLastClickedTime(Date lastClickedTime) {
		this.lastClickedTime = lastClickedTime;
	}
	
	@Override
	public String toString() {
		return "EmailTracker [token=" + token + ", url=" + url + 
				", clicks=" + clicks + ", lastClickedTime=" + lastClickedTime + "]";
	}

	public void setEmail(Email e) {
		this.email = e;
	}

}