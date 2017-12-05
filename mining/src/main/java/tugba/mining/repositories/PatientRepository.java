package tugba.mining.repositories;


import org.springframework.data.neo4j.repository.GraphRepository;

import tugba.mining.domain.Patient;

public interface PatientRepository extends GraphRepository<Patient> {
	
}
