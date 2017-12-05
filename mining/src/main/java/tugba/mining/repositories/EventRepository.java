package tugba.mining.repositories;


import org.springframework.data.neo4j.repository.GraphRepository;

import tugba.mining.domain.Event;

public interface EventRepository extends GraphRepository<Event> {
	
}
