package an.repo;

import org.springframework.data.repository.CrudRepository;

import an.model.Email;

public interface EmailRepo extends CrudRepository<Email, Long>{

}
