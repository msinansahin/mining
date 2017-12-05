package tugba.mining.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tugba.mining.domain.Activity;
import tugba.mining.domain.BaseEntity;
import tugba.mining.domain.Event;
import tugba.mining.domain.Patient;
import tugba.mining.repositories.ActivityRepository;
import tugba.mining.repositories.EventRepository;
import tugba.mining.repositories.PatientRepository;

@Service
public class CommonServiceNeo4Impl implements CommonService {

	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	ActivityRepository activityRepository;
	
	@Autowired
	PatientRepository patientRepository;

	@Override
	public List<Event> listEvent() {
		List<Event> result = new ArrayList<>();
		eventRepository.findAll().forEach(event -> result.add(event));
		return result;
	}

	@Override
	public <T> T getById(Class<T> clazz, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveOrUpdate(BaseEntity entity) {
		if (entity instanceof Event) {
			eventRepository.save((Event)entity);
		}
		else if (entity instanceof Activity) {
			activityRepository.save((Activity)entity);
		}
		else if (entity instanceof Patient) {
			patientRepository.save((Patient)entity);
		}

	}

	@Override
	public <T> void delete(T clazz, Long id) {
		// TODO Auto-generated method stub
		
	}

}
