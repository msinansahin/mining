package tugba.mining.repositories;


import java.util.List;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import tugba.mining.domain.Activity;
import tugba.mining.domain.Event;

@Repository
public interface ActivityRepository extends GraphRepository<Activity> {
	List<Activity> findByActivityName(String activityName);
	
}
