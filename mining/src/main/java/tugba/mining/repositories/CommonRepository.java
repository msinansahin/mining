package tugba.mining.repositories;

import org.springframework.data.repository.CrudRepository;

import tugba.mining.domain.BaseEntity;

public interface CommonRepository extends CrudRepository<BaseEntity, Long> {
	
}
