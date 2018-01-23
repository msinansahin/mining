package tugba.mining.repositories;


import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import tugba.mining.domain.Surgery;

@Repository
public interface SurgeryRepository extends GraphRepository<Surgery> {
	
}
