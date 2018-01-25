package tugba.mining.repositories;


import java.util.List;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import tugba.mining.domain.Event;
import tugba.mining.domain.Surgery;

@Repository
public interface SurgeryRepository extends GraphRepository<Surgery> {

	List<Surgery> findBySurgeryName(String surgeryName);
	
}
