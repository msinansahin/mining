package tugba.mining.repositories;


import org.springframework.data.neo4j.repository.GraphRepository;

import tugba.mining.domain.Activity;

public interface ActivityRepository extends GraphRepository<Activity> {
	
}
