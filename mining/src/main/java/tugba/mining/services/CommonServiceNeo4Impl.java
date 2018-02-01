package tugba.mining.services;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tugba.mining.domain.Activity;
import tugba.mining.domain.BaseEntity;
import tugba.mining.domain.Doctor;
import tugba.mining.domain.Event;
import tugba.mining.domain.Patient;
import tugba.mining.domain.Pattern;
import tugba.mining.domain.Surgery;
import tugba.mining.repositories.ActivityRepository;
import tugba.mining.repositories.DoctorRepository;
import tugba.mining.repositories.EventRepository;
import tugba.mining.repositories.PatientRepository;
import tugba.mining.repositories.SurgeryRepository;
import tugba.mining.repositories.PatternRepository;
import tugba.mining.util.RowContext;

@Service
public class CommonServiceNeo4Impl implements CommonService {

	public static Integer eventId = 0;
	public static Integer patternId = 0;
	
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
	
	@Autowired
	PatternRepository patternRepository;

	@Override
	public List<Event> listEvent() {
		List<Event> result = new ArrayList<>();
		eventRepository.findAll().forEach(event -> result.add(event));
		return result;
	}

	@Override
	public <T> T getById(Class<T> clazz, Long id) {
		if (Event.class.equals(clazz)) {
			return (T) eventRepository.findOne(id);
		} else if (Activity.class.equals(clazz)) {
			return (T) activityRepository.findOne(id);
		} else if (Patient.class.equals(clazz)) {
			return (T) patientRepository.findOne(id);
		} else if (Surgery.class.equals(clazz)) {
			return (T) surgeryRepository.findOne(id);
		} else if (Doctor.class.equals(clazz)) {
			return (T) doctorRepository.findOne(id);
		}
		return null;

	}

	@Override
	public <T> boolean exists(Class<T> clazz, Long id) {
		if (Event.class.equals(clazz)) {
			return eventRepository.exists(id);
		} else if (Activity.class.equals(clazz)) {
			return activityRepository.exists(id);
		} else if (Patient.class.equals(clazz)) {
			return patientRepository.exists(id);
		} else if (Surgery.class.equals(clazz)) {
			return surgeryRepository.exists(id);
		} else if (Doctor.class.equals(clazz)) {
			return doctorRepository.exists(id);
		}
		return false;

	}

	@Override
	public void saveOrUpdate(BaseEntity entity) {
		if (entity instanceof Event)
			eventRepository.save((Event) entity);
		else if (entity instanceof Activity)
			activityRepository.save((Activity) entity);
		else if (entity instanceof Patient)
			patientRepository.save((Patient) entity);
		else if (entity instanceof Surgery)
			surgeryRepository.save((Surgery) entity);
		else if (entity instanceof Doctor)
			doctorRepository.save((Doctor) entity);
		else if (entity instanceof Pattern)
			patternRepository.save((Pattern) entity);
	}

	@Override
	public void deleteAll() {
		patientRepository.deleteAll();
		doctorRepository.deleteAll();
		eventRepository.deleteAll();
		surgeryRepository.deleteAll();
		patternRepository.deleteAll();
	}

	@Override
	public <T> void delete(T clazz, Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addRowContext(RowContext row) {

		Patient patient = addPatient(row.getPatientId(), row.getAge(), row.getGender());
		Doctor doctor = addDoctor(row.getDoctor());
		Surgery surgery = addSurgery(row.getSurgeryNo(), row.getSurgeryName(), row.getSurgeryCategory());
		// admission
		addEvent(row.getEventId(), patient, row.getActivityAdmission(), row.getAdmissionDate(),
				row.getDepartment(), row.getService(), doctor, surgery);
		// servis degisikliği
		if ("E".equals(row.getServisDegisiklik())) // null kontrolü yok, başa al karşılaştıracağın değeri
		{
			Doctor doctor2 = addDoctor(row.getDoctor2());
			
			String activity = "";
			if (row.getService2().contains("Yoğun Bakı"))
				activity = "Transfer to Intensive Care Unit";
			else
				activity = "Change Service";
			addEvent(row.getEventId(), patient, activity, row.getServisDegisiklikTar(),
					row.getDepartment(), row.getService2(), doctor2, surgery);
		}

		// bolum degisikliği
		if ("E".equals(row.getBolumDegisiklik())) {
			Doctor doctor2 = addDoctor(row.getDoctor2());
			addEvent(row.getEventId(), patient, "Change Department", row.getBolumDegisiklikTar(),
					row.getDepartment(), row.getService2(), doctor2, surgery);
		}
		// ameliyat_bas
		Doctor surgeryDoctor = addDoctor(row.getSurgeryDoctor());
		
		// surgerystarted
		addEvent(row.getEventId(), patient, "Surgery Started", row.getSurgeryStartDate(),
				row.getDepartment(), row.getService(), surgeryDoctor, surgery);

		// surgeryfinished
		addEvent(row.getEventId(), patient, "Surgery Finished", row.getSurgeryFinishDate(),
				row.getDepartment(), row.getService(), surgeryDoctor, surgery);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = formatter.format(row.getOlumDate());

		if (!formattedDate.equals("2017-01-01")) {
			addEvent(row.getEventId(), patient, row.getActivityOlum(), row.getOlumDate(),
					row.getDepartment(), row.getService(), doctor, surgery);
		}

		// servise cıkıs
		addEvent(row.getEventId(), patient, row.getActivityServiseCikis(), row.getServiseCikisTar(),
				row.getDepartment(), row.getService(), doctor, surgery);
		// taburcu
		addEvent(row.getEventId(), patient, row.getActivityTaburcu(), row.getTaburcuDate(),
				row.getDepartment(), row.getService(), doctor, surgery);
		
		//update events by start data
		updateEventsByStartDate();
	}

	private Surgery addSurgery(Integer surgeryNo, String surgeryName, String surgeryCategory) {
		Surgery surgery;
		if (surgeryRepository.findBySurgeryName(surgeryName).isEmpty()) {
			surgery = Surgery.builder()
					.surgeryNo(surgeryNo)
					.surgeryName(surgeryName)
					.surgeryCategory(surgeryCategory)
					.build();
			saveOrUpdate(surgery);
		} else
			surgery = surgeryRepository.findBySurgeryName(surgeryName).get(0);

		return surgery;
	}

	private Doctor addDoctor(String name) {
		Doctor doctor = null;
		if (doctorRepository.findByName(name).isEmpty()) {
			doctor = Doctor.builder()
					.name(name)
					.build();
			saveOrUpdate(doctor);
		} else
			doctor = doctorRepository.findByName(name).get(0);

		return doctor;
	}

	private void addEvent(Integer eventId, Patient patient, String activity, Date activityDate, String department,
			String service, Doctor doctor, Surgery surgery) {

		if ( eventRepository.findByPatientPatientIdAndActivityAndStartDate(patient.getPatientId(), activity, activityDate).isEmpty()) {

			Event event = Event.builder()
					.eventId(addEventId())
					.patient(patient)
					//.patientId(patient.getPatientId())
					.activity(activity)
					.startDate(activityDate)
					.finishDate(activityDate)
					.department(department)
					.service(service)
					.doctor(doctor)
					.surgery(surgery)
					.build();
			
			saveOrUpdate(event);
		}

	}

	private Integer addEventId() {
		eventId = eventId + 1;
		return eventId;
	}
	private Integer addPatternId() {
		patternId = patternId + 1;
		return patternId;
	}
	private Patient addPatient(Integer patienId, Integer age, String gender) {
		Patient patient = null;
		if (patientRepository.findByPatientId(patienId).isEmpty()) {
			patient = Patient.builder()
					.patientId(patienId)
					.age(age)
					.gender(gender)
					.build();
			saveOrUpdate(patient);
		} else
			patient = patientRepository.findByPatientId(patienId).get(0);

		return patient;
	}

	

	@Override
	public void addPatterns() {
		
	//	eventRepository.findAll (new Sort(Sort.Direction.ASC, "<patient>"));
		patternRepository.deleteAll();
		List<Event> events = (List<Event>) eventRepository.findAll(new Sort ("eventId"));
		Iterable <Event> es = eventRepository.findAll(new Sort ("eventId"));
		es.forEach(p->System.out.println(p));
	
		String road = "";
        String nps = "";
        int ei = -1;
        int tei;
		Iterator iterator = events.iterator();
		while (iterator.hasNext())
		{
			 
			Event e = (Event)iterator.next();
			tei = (int) e.getPatient().getPatientId();
			 if (tei != ei) {
				 if (road.length() != 0) {
                     road = road.substring(0, road.length() - 2);
                     List<Pattern> patterns = patternRepository.findByTrace(road);
                     Iterator iterator2 = patterns.iterator();
                     if (iterator2.hasNext())
                     {
                    	
                    	 Pattern p = (Pattern) iterator2.next();
                    	 p.setEventNumber(p.getEventNumber() +1);
                    	 p.setMyPatients( p.getMyPatients() + "," +ei );
                    	 
                    	 saveOrUpdate(p);
                     }
                     else {
                    	 
                    	 Pattern p = Pattern.builder()
                    			 .patternId(addPatternId ())
                    			 .trace(road)
                    			 .eventNumber(1)
                    			 .myPatients(Integer.toString(ei))
                    			 .build();
                    	 saveOrUpdate (p);
                     }
                    	 
				 }
				 ei = tei;
                 road = "";
			}
			road += e.getActivity().toLowerCase().trim() + "->";
		}
		
		Iterable <Pattern> patterns = patternRepository.findAll(new Sort ("patternId"));
		patterns.forEach(p->System.out.println(p));
		
		
	}

	@Override
	public void updateEventsByStartDate() {
		
		final Comparator<Event> START_DATE_ORDER = new Comparator<Event>() {
			public int compare(Event e1, Event e2) {
			return e1.getStartDate().compareTo(e2.getStartDate());
		}
		};
		final Comparator<Event> EVENT_ID_ORDER = new Comparator<Event>() {
			public int compare(Event e1, Event e2) {
				return e1.getEventId().compareTo(e2.getEventId());
			}
		};
		List <Patient> patients = (List<Patient>) patientRepository.findAll(new Sort ("patientId"));
		Iterator it = patients.iterator();
		
		while (it.hasNext()){
			Patient p = (Patient) it.next();
			
			// get the patient's events
			List <Event> events = eventRepository.findByPatientPatientId(p.getPatientId());
			
			// sort events by start date
			Collections.sort(events, START_DATE_ORDER);
			
			// get min event id
			Integer minEventId = (Collections.min(events, EVENT_ID_ORDER)).getEventId();
		   
			// update event id by startdate
			Integer i=0;
			Iterator<Event> it2  = events.iterator();
			while (it2.hasNext()){
				Event e = (Event) it2.next();
				e.setEventId(minEventId +i );
				i++;
			}
			eventRepository.save(events);
		  
		}
		
	
	}
}


