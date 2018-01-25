package tugba.mining.repositories;


import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import tugba.mining.domain.Event;
import tugba.mining.domain.Patient;
import tugba.mining.domain.Surgery;

@Repository
public interface EventRepository extends GraphRepository<Event> {
	List<Event> findByPatientIdAndActivityAndStartDate(Double patientId, String activity, Date startDate);
	
	@Query(value = "MATCH (event:Event) RETURN event;")
    Stream<Event> getAllEvents();
}
