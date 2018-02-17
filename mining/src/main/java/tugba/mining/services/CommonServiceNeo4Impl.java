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

import com.google.common.collect.Lists;

import tugba.mining.domain.Activity;
import tugba.mining.domain.BaseEntity;
import tugba.mining.domain.Doctor;
import tugba.mining.domain.Event;
import tugba.mining.domain.Path;
import tugba.mining.domain.Patient;
import tugba.mining.domain.Pattern;
import tugba.mining.domain.Surgery;

import tugba.mining.repositories.ActivityRepository;
import tugba.mining.repositories.DoctorRepository;
import tugba.mining.repositories.EventRepository;
import tugba.mining.repositories.PathRepository;
import tugba.mining.repositories.PatientRepository;
import tugba.mining.repositories.SurgeryRepository;
import tugba.mining.repositories.PatternRepository;
import tugba.mining.util.RowContext;

@Service
public class CommonServiceNeo4Impl implements CommonService {

	public static Integer eventId = 0;
	public static Integer patternId = 0;
	public static Integer pathId = 0;
	public static Integer activityId = 0;

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
	
	@Autowired
	PathRepository pathRepository;

	@Override
	public List<Event> listEvent() {
		List<Event> result = new ArrayList<>();
		eventRepository.findAll(new Sort ("eventId")).forEach(event -> result.add(event));
		return result;
	}
	@Override
	public List<Path> listPath() {
		List<Path> result = new ArrayList<>();
		pathRepository.findAll(new Sort ("pathId")).forEach(p -> result.add(p));
		return result;
	}
	@Override
	public List<Activity> listActivity() {
		List<Activity> result = new ArrayList<>();
		activityRepository.findAll(new Sort ("activityId")).forEach(activity -> result.add(activity));
		return result;
	}
	@Override
	public List<Pattern> listPattern() {
		List<Pattern> result = new ArrayList<>();
		patternRepository.findAll(new Sort ("patternId")).forEach(pattern -> result.add(pattern));
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
		else if (entity instanceof Activity)
			activityRepository.save((Activity) entity);
		else if (entity instanceof Path)
			pathRepository.save((Path) entity);
	}

	@Override
	public void deleteAll() {
		patientRepository.deleteAll();
		doctorRepository.deleteAll();
		eventRepository.deleteAll();
		surgeryRepository.deleteAll();
		patternRepository.deleteAll();
		activityRepository.deleteAll();
		patternRepository.deleteAll();
		patternId = 0;
		eventId = 0; 
		activityId = 0;
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
		
		// eventcount and patientcount of activites
		updateActivities();
				
	}

	private void updateActivities() {
		List <Activity> activities= (List<Activity>) activityRepository.findAll( new Sort ("activityId"));
		Iterator it = activities.iterator();
		while (it.hasNext())
		{
			Activity a = (Activity) it.next();
			a.setEventCount(eventRepository.findByActivityActivityId(a.getActivityId()).size());
			a.setPatientCount(patientRepository.getPatients(a.getActivityId()).size());
			saveOrUpdate(a);
		}
		
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

		if ( eventRepository.findByPatientPatientIdAndActivityActivityNameAndStartDate(patient.getPatientId(), activity, activityDate).isEmpty()) {

			Activity a = addActivity(activity);
			Event event = Event.builder()
					.eventId(addEventId())
					.patient(patient)
					//.patientId(patient.getPatientId())
					.activity(a)
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

	public Activity addActivity(String activityName) {
		Activity activity = null;
		if (activityRepository.findByActivityName(activityName).isEmpty()) {
			activity = Activity.builder()
					.activityId(addActivityId())
					.activityName(activityName)
					.build();
			saveOrUpdate(activity);
		} else
			activity = activityRepository.findByActivityName(activityName).get(0);

		return activity;
	}

	private Integer addEventId() {
		eventId = eventId + 1;
		return eventId;
	}
	private Integer addPatternId() {
		patternId = patternId + 1;
		return patternId;
	}
	private Integer addPathId() {
		pathId = pathId + 1;
		return pathId;
	}
	private Integer addActivityId() {
		activityId = activityId + 1;
		return activityId;
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
		
		deletePatterns();
		List<Event> events = (List<Event>) eventRepository.findAll(new Sort ("eventId"));
		
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
                    	 p.setPatientCount(p.getPatientCount() + 1);
                    	 p.setMyPatients( p.getMyPatients() + "," + ei );
                    	 saveOrUpdate(p);
                     }
                     else {	 
                    	 System.out.println("Myroad:" + road);
                    	 Pattern p = Pattern.builder()
                    			 .patternId(addPatternId ())
                    			 .trace(road)
                    			 .patientCount(1)
                    			 .myPatients(Integer.toString(ei))
                    			 .build();
                    	 saveOrUpdate (p);
                     }
				 }
				 ei = tei;
                 road = "";
			}
			
			String starting ="";
			
			if (!road.isEmpty())
			{
				String pathroad = road.substring(0, road.length() - 2);
				if (pathroad.contains("->"))
					starting = pathroad.substring(pathroad.lastIndexOf("->"), pathroad.length() );
				else
					starting = pathroad;
					
			}
			else{
				starting = "Start";
			}
			// adding a path
			addPath (e, starting.replace("->", ""), e.getActivity().getActivityName().replace("->", ""));
			road += e.getActivity().getActivityName() + "->";
			
		}
		
		// update event counts and patient counts of paths
		updatePaths();
	}

	private void updatePaths() {
		List <Path> paths = (List<Path>) pathRepository.findAll( new Sort ("pathId"));
		Iterator it = paths.iterator();
		while (it.hasNext())
		{
			Path path = (Path) it.next();
			path.setEventCount(path.getEvents().size());
			path.setPatientCount(path.getPatients().size());
			saveOrUpdate(path);
		}
		
	}
	private void addPath(Event event, String from, String to) {
		Path path = null;
		List <Integer> events = Lists.newArrayList();
		List <Integer> patients = Lists.newArrayList();

		if (pathRepository.findByFromAndTo(from,to).isEmpty()) {
			events.add(event.getEventId());
			patients.add(patientRepository.findByPatientId(event.getPatient().getPatientId()).get(0).getPatientId() );
					
			path = Path.builder()
					.pathId(addPathId())
					.from(from)
					.to(to)
					.events(events)
					.patients(patients)
					.build();			
			saveOrUpdate(path);
		} 
		else
		{
			path = pathRepository.findByFromAndTo(from,to).get(0);
			System.out.println( "path:" + path.getFrom());
			events.addAll(path.getEvents());
			events.add(event.getEventId());
			
			if (!existPatient (path.getPatients(), event.getPatient().getPatientId()))
			{
				//path.setPatientNumber(path.getPatientNumber() + 1 );
				patients.addAll(path.getPatients());
				patients.add(event.getPatient().getPatientId());
				path.setPatients(patients);
			}
			
			path.setEvents(events);
			saveOrUpdate(path);
		}       
		
	}

	private boolean existPatient(List<Integer> patients, double patientId) {
		
		boolean exist = false;
		if (!patients.isEmpty())
		{
			Iterator it = patients.iterator();
			while (it.hasNext())
			{
				int p = (Integer) it.next();
				if ( p == patientId)
					exist = true ;
			}
		}
		return exist;
	}
	private boolean existEvent(List<Event> events, Integer eventId) {
		boolean exist = false;
		if (!events.isEmpty())
		{
			Iterator it = events.iterator();
			while (it.hasNext())
			{
				Event e = (Event) it.next();
				if ( e.getEventId() == eventId)
					exist = true ;
			}
		}
		return exist;
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
		List<Event> events = (List<Event>) eventRepository.findAll(new Sort ("eventId"));
		Iterable <Event> es = eventRepository.findAll(new Sort ("eventId"));
		es.forEach(p->System.out.println(p));
	
	
	}

	@Override
	public void deletePatterns() {
		patternRepository.deleteAll();
		pathRepository.deleteAll();
		patternId = 0;
		pathId = 0;
	}

	@Override
	public void processMap() {
		
		List<Activity> activities = (List<Activity>) activityRepository.findAll(new Sort ("activityId"));
		Iterator it = activities.iterator();
		while (it.hasNext())
		{
			Activity a = (Activity) it.next();
	//		a.setPatients(patientRepository.getPatients(a.getActivityId()));
	//		a.setEvents(eventRepository.findByActivityActivityId(a.getActivityId()));
	//		activityRepository.save(a);
		
		}
		
		
	}
	
}


