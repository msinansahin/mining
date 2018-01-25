package tugba.mining.repositories;


import java.util.List;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import tugba.mining.domain.Patient;

@Repository
public interface PatientRepository extends GraphRepository<Patient> {
	List<Patient> findByPatientId(double patientId);
}
