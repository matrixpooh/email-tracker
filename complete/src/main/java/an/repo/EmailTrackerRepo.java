package an.repo;

import org.springframework.data.repository.CrudRepository;

import an.model.EmailTracker;

public interface EmailTrackerRepo extends CrudRepository<EmailTracker, Long>{

}
