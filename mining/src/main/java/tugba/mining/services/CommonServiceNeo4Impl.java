package tugba.mining.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tugba.mining.domain.Activity;
import tugba.mining.domain.BaseEntity;
import tugba.mining.domain.Doctor;
import tugba.mining.domain.Event;
import tugba.mining.domain.Patient;
import tugba.mining.domain.Surgery;
import tugba.mining.repositories.ActivityRepository;
import tugba.mining.repositories.DoctorRepository;
import tugba.mining.repositories.EventRepository;
import tugba.mining.repositories.PatientRepository;
import tugba.mining.repositories.SurgeryRepository;

@Service
public class CommonServiceNeo4Impl implements CommonService {

	@Autowired
	EventRepository eventRepository;
	
	@Autowired
	ActivityRepository activityRepository;
	
	@Autowired
	PatientRepository patientRepository;
	
	@Autowired
	SurgeryRepository surgeryRepository;
	
	@Autowired
	DoctorRepository doctorRepository;

	@Override
	public List<Event> listEvent() {
		List<Event> result = new ArrayList<>();
		eventRepository.findAll().forEach(event -> result.add(event));
		return result;
	} 	

	@Override
	public <T> T getById(Class<T> clazz, Long id) {
		
		return null;
	}

	@Override
	public void saveOrUpdate(BaseEntity entity) {
		if (entity instanceof Event) {
			List events = listEvent();
			eventRepository.save((Event)entity);
		}
		else if (entity instanceof Activity) {
			activityRepository.save((Activity)entity);
		}
		else if (entity instanceof Patient) {
			
			patientRepository.save((Patient)entity);
		}
		else if (entity instanceof Surgery) {
			
			surgeryRepository.save((Surgery)entity);
		}
		else if (entity instanceof Doctor) {
			
			doctorRepository.save((Doctor)entity);
		}

	}
	
	@Override
	public void deleteAll ()
	{
		patientRepository.deleteAll();
		eventRepository.deleteAll();
		surgeryRepository.deleteAll();
	}
	@Override
	public <T> void delete(T clazz, Long id) {
		// TODO Auto-generated method stub
		
	}

}
