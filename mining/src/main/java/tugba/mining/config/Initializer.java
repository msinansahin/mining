package tugba.mining.config;

import javax.annotation.PostConstruct;

import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tugba.mining.domain.Activity;
import tugba.mining.domain.Event;
import tugba.mining.domain.Patient;
import tugba.mining.services.CommonService;


@Component
public class Initializer {

	@Autowired
	CommonService commonService;
	
	@PostConstruct
	public void init() {
	
		/*
		Activity activity = new Activity();
		activity.setActivityName("activity1");
		commonService.saveOrUpdate(activity);

		Patient patient = new Patient();
		patient.setAge(1);
		patient.setPatientClass("Class1");
		patient.setSex("K");
		commonService.saveOrUpdate(patient);

		Event event = new Event();
		event.setActivity(activity);
		event.setPatient(patient);
		commonService.saveOrUpdate(event);
		*/
	}
}
