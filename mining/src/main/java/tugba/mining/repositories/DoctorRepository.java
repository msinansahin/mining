package tugba.mining.repositories;


import java.util.List;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import tugba.mining.domain.Doctor;
import tugba.mining.domain.Patient;


@Repository
public interface DoctorRepository extends GraphRepository<Doctor> {
	List<Doctor> findByName(String name);
}
