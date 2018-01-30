package tugba.mining.repositories;


import java.util.List;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import tugba.mining.domain.Pattern;

@Repository
public interface PatternRepository extends GraphRepository<Pattern> {
	List <Pattern> findByRoad (String road);
}
