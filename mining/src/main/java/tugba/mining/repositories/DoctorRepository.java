package tugba.mining.repositories;


import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import tugba.mining.domain.Doctor;


@Repository
public interface DoctorRepository extends GraphRepository<Doctor> {
	
}
