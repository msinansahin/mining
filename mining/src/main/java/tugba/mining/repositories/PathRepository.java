package tugba.mining.repositories;


import java.util.List;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import tugba.mining.domain.Event;
import tugba.mining.domain.Path;


@Repository
public interface PathRepository extends GraphRepository<Path> {

	
	List<Path> findByFromAndTo(String from,	String to);
	
}
