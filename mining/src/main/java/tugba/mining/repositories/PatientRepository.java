package tugba.mining.repositories;


import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tugba.mining.domain.Patient;

@Repository
public interface PatientRepository extends GraphRepository<Patient> {
	List<Patient> findByPatientId(double patientId);
	
	@Query( "MATCH (a:Activity {activityId:{activityId}})-[:ACTIVITY]- (e:Event)-[PATIENT]-(p:Patient) RETURN p")
	List<Patient> getPatients(@Param("activityId") Integer activityId); 

}
