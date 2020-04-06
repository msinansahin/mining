package tugba.mining.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
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
import tugba.mining.util.EventRow;
import tugba.mining.util.RowContext;
import tugba.mining.util.RowContextER;
import tugba.mining.util.RowContextKons;

@Service
public class CommonServiceNeo4Impl implements CommonService {

	public static Integer eventId = 0;
	public static Integer patternId = 0;
	public static Integer pathId = 0;
	public static Integer activityId = 0;
	public static Integer eventRowId = 0;
	
	public static List <EventRow> eventRows = Lists.newArrayList();
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
		activityRepository.deleteAll();
		patternRepository.deleteAll();
		pathRepository.deleteAll();
		patternId = 0;
		eventId = 0; 
		activityId = 0;
		
	}
	
	@Override
	public <T> void delete(T clazz, Long id) {
		// TODO Auto-generated method stub

	}
	/*@Override
	public void addEventRowContext(RowContext row) {
	
		// admission
		addEventRow( row, "Admission to Hospital", row.getAdmissionDate());
		// servis degisikliği
		
		if ("E".equals(row.getServisDegisiklik())) // null kontrolü yok, başa al karşılaştıracağın değeri
		{
			String activity = "";
			if (row.getService2().contains("Yoğun Bakı"))
				activity = "Transfer to Intensive Care Unit";
			else
				activity = "Change Service";
			
			addEventRow( row, activity, row.getServisDegisiklikTar());
		}
		// bolum degisikliği
		if ("E".equals(row.getBolumDegisiklik())) {
			addEventRow( row,"Change Department", row.getBolumDegisiklikTar());
		}
		// surgerystarted
		addEventRow( row,"Surgery Started", row.getSurgeryStartDate());
		// surgeryfinished
		addEventRow( row,"Surgery Finished", row.getSurgeryFinishDate());

		// servise cıkıs
		addEventRow( row, "Transfer to Service", row.getServiseCikisTar());

		// taburcu
		addEventRow( row, "Discharged", row.getTaburcuDate());
	}*/
	/*
	@Override
	public void addEventRow(RowContext row, String activity, Date activityDate) {
		if (activityDate == null)
			return;
		if (!existEventRow (row.getPatientId(), activity, activityDate))
		{
			EventRow eventRow = EventRow.builder()
					.patientId(row.getPatientId())
					.age(row.getAge())
					.gender(row.getGender())
					.surgeryNo(row.getSurgeryNo())
					.surgeryCategory(row.getSurgeryCategory())
					.surgeryName(row.getSurgeryName())
					.activity(activity)
					.activityDate(activityDate)
					.department(row.getDepartment())
					.service(row.getService())
					.doctor(row.getDoctor())
					.build();
			eventRows.add(eventRow);
		}
		
	}*/
	/*private boolean existEventRow(Integer patientId, String activity, Date activityDate) {
		Iterator it  = eventRows.iterator();
		while (it.hasNext())
		{
			EventRow eventRow = (EventRow) it.next();
			if(eventRow.getPatientId() == patientId &&
					eventRow.getActivity().equals(activity) &&
					eventRow.getActivityDate().equals(activityDate) )
				return true;
				
		}
		return false;
	}*/
	@Override
	public void addRowContext(RowContext row) {
	
		Patient patient = addPatient(row.getPatientId(), row.getAge(), row.getGender());
		Doctor doctor = addDoctor(row.getDoctor());
		Surgery surgery = addSurgery(row.getSurgeryNo(), row.getSurgeryName(), row.getSurgeryCategory());
		
		// admission
		addEvent( patient, "Hastaneye Yatış", row.getAdmissionDate(),row.getAdmissionDate(),
					row.getDepartment(), row.getService(), doctor, surgery, null, null, null);
		// servis degisikliği
		if ("E".equals(row.getServisDegisiklik())) // null kontrolü yok, başa al karşılaştıracağın değeri
		{
			Doctor doctor2 = addDoctor(row.getDoctor2());
			
			String activity = "";
			if (row.getService2().contains("Yoğun Bakı"))
				activity = "Yoğun Bakıma Transfer";
			else
				activity = "Servis Değişikliği";
			
			addEvent( patient, activity, row.getServisDegisiklikTar(),row.getServisDegisiklikTar(),
					row.getDepartment(), row.getService2(), doctor2, surgery, null, null, null);
		}

		// bolum degisikliği
		if ("E".equals(row.getBolumDegisiklik()) ) {
			Doctor doctor2 = addDoctor(row.getDoctor2());
			addEvent( patient, "Bölüm Değişikliği", row.getBolumDegisiklikTar(),row.getBolumDegisiklikTar(),
					row.getDepartment(), row.getService2(), doctor2, surgery, null, null, null);
		}
		// ameliyat_bas
		Doctor surgeryDoctor = addDoctor(row.getSurgeryDoctor());
		// surgerystarted
		addEvent( patient, "Ameliyatın Başlaması", row.getSurgeryStartDate(),row.getSurgeryStartDate(),
				row.getDepartment(), row.getService(), surgeryDoctor, surgery, null, null, null);

		// surgeryfinished
		addEvent(patient, "Ameliyatın Bitmesi", row.getSurgeryFinishDate(),row.getSurgeryFinishDate(),
				row.getDepartment(), row.getService(), surgeryDoctor, surgery, null, null, null);
		addEvent( patient, "Ex", row.getOlumDate(), row.getOlumDate(),
					row.getDepartment(), row.getService(), doctor, surgery, null, null, null);
		// servise cıkıs
		addEvent( patient, "Servise Çıkış", row.getServiseCikisTar(),row.getServiseCikisTar(),
				row.getDepartment(), row.getService(), doctor, surgery, null, null, null);
		// taburcu
		addEvent( patient, "Taburcu", row.getTaburcuDate(),row.getTaburcuDate(),
					row.getDepartment(), row.getService(), doctor, surgery, null, null, null);
		
		
	}
	
	
	private Surgery addSurgery(Integer surgeryNo, String surgeryName, String surgeryCategory) {
		Surgery surgery;
		List <Surgery> surgeries = surgeryRepository.findBySurgeryName(surgeryName);
		if (surgeries.isEmpty()) {
			surgery = Surgery.builder()
					.surgeryNo(surgeryNo)
					.surgeryName(surgeryName)
					.surgeryCategory(surgeryCategory)
					.build();
			saveOrUpdate(surgery);
		} else
			surgery = surgeries.get(0);

		return surgery;
	}

	private Doctor addDoctor(String name) {
		Doctor doctor = null;
		List <Doctor> doctors = doctorRepository.findByName(name);
		if (doctors.isEmpty()) {
			doctor = Doctor.builder()
					.name(name)
					.build();
			saveOrUpdate(doctor);
		} else
			doctor = doctors.get(0);

		return doctor;
	}

	private void addEvent( Patient patient, String activity, Date activityStartDate, Date activityFinishDate, String department,
			String service, Doctor doctor, Surgery surgery, String acileGelisDurum, String acilDurum, String tani) {
		// if activity date is not defined, do not record
		if (activityStartDate == null)
			return;
		if ( eventRepository.findByPatientPatientIdAndActivityActivityNameAndStartDate(patient.getPatientId(), activity, activityStartDate).isEmpty()) {
			Activity a = addActivity(activity);
			Event event = Event.builder()
					.eventId(addEventId())
					.patient(patient)
					//.patientId(patient.getPatientId())
					.activity(a)
					.startDate(activityStartDate)
					.finishDate(activityFinishDate)
					.department(department)
					.service(service)
					.doctor(doctor)
					.surgery(surgery)
					.acilDurum(acilDurum)
					.acileGelisDurum(acileGelisDurum)
					.tani(tani)
					.build();
			
			saveOrUpdate(event);
		}

	}

	public Activity addActivity(String activityName) {
		Activity activity = null;
		List <Activity> activities = activityRepository.findByActivityName(activityName);
		if (activities.isEmpty()) {
			activity = Activity.builder()
					.activityId(addActivityId())
					.activityName(activityName)
					.build();
			saveOrUpdate(activity);
		} else
			activity = activities.get(0);

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
	private Integer addEventRowId() {
		eventRowId = eventRowId + 1;
		return eventRowId;
	}
	private Patient addPatient(Integer patienId, Integer age, String gender) {
		Patient patient = null;
		List<Patient> patients = patientRepository.findByPatientId(patienId);
		if (patients.isEmpty()) {
			patient = Patient.builder()
					.patientId(patienId)
					.age(age)
					.gender(gender)
					.build();
			saveOrUpdate(patient);
		} else
			patient = patients.get(0);

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
		final Comparator<Event> OID_ORDER = new Comparator<Event>() {
			public int compare(Event e1, Event e2) {
				return e1.getOid().compareTo(e2.getOid());
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
			
			// sort events by oid date
			Collections.sort(events, OID_ORDER);
						
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
	@Override
	public void updateActivities() {
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
	@Override
	public List<EventRow> getEventRows() {
		// TODO Auto-generated method stub
		return this.eventRows;
	}
	
	@Override
	public void addRowContextER(RowContextER row) {
		
		Patient patient = addPatient(row.getPatientId(), row.getAge(), row.getGender());
		Doctor doctor = addDoctor(row.getDoctor());
		//Surgery surgery = addSurgery(row.getSurgeryNo(), row.getSurgeryName(), row.getSurgeryCategory());
		
		// acile giriş
		addEvent( patient, "Registration", row.getAcilGiris(),row.getAcilGiris(),
					row.getDepartment(), row.getService(), doctor, null, row.getAcileGelisDurum(), 
					row.getAcilDurum(), row.getTani());
		// triyaj giriş
		addEvent( patient, "Triage", row.getTriyajGiris(),row.getTriyajCikis(),
							row.getDepartment(), row.getService(), doctor, null, row.getAcileGelisDurum(), 
							row.getAcilDurum(), row.getTani());
		// triyaj çıkış
		/*addEvent( patient, "Triyaj Çıkış", row.getTriyajCikis(),
									row.getDepartment(), row.getService(), doctor, null, row.getAcileGelisDurum(), 
									row.getAcilDurum() , row.getTani());*/
		System.out.println("Acil durum:" + row.getAcilDurum() +"A");

		if ( row.getAcilDurum() != null)
		{
			
			String triyajActivity = "";
			if (row.getAcilDurum().equals("Acil"))
				triyajActivity = "Yellow";
			else if (row.getAcilDurum().equals("Çok Acil"))
				triyajActivity = "Red";
			else if (row.getAcilDurum().equals("Normal"))
				triyajActivity = "Green";
			
			// triyaj çıkış
			addEvent( patient, triyajActivity, row.getTriyajCikis(), row.getTriyajCikis(),
										row.getDepartment(), row.getService(), doctor, null, row.getAcileGelisDurum(), 
										row.getAcilDurum() , row.getTani());
			
		}
		// muayene
		addEvent( patient, "Examination", row.getMuayene() ,row.getMuayene(),
											row.getDepartment(), row.getService(), doctor, null, row.getAcileGelisDurum(), 
											row.getAcilDurum() , row.getTani());
		// musahede giriş
		addEvent( patient, "Observation", row.getMusahedeGiris(), row.getMusahedeCikis(), row.getDepartment(), row.getService(), doctor, null, row.getAcileGelisDurum(), 
							row.getAcilDurum() , row.getTani());
		// musahede çıkış
		/*addEvent( patient, "Müşahade Çıkış", row.getMusahedeCikis(), row.getDepartment(), row.getService(), doctor, null, row.getAcileGelisDurum(), 
							row.getAcilDurum() , row.getTani());*/
		
		if (row.getAcilCikis() != null)
			row.setAcilCikis(DateUtils.addMinutes(row.getAcilCikis(), 1) );											
		
		//Servis yatışı yapıldı
		//Başka Merkeze Sevk
			//Durum Bildirir Raporu Verildi
			//Habersiz ayrılma
			//Arrest (Exitus)
			//Öneriler
			//İleri tetkik ve tedaviyi kabul etmeyip A-2 imza
			//Yoğunbakım  yatışı yapıldı
			//Reçete Verildi

		if (row.getKararSonucu() != null )
		{
			/*StringTokenizer st = new StringTokenizer (row.getKararSonucu(),",");	
			while (st.hasMoreTokens()) {
				// karar
				String decision= st.nextToken().trim();
				if (decision.contains("Sevk"))
					addEvent( patient, "Referral", row.getAcilCikis(),row.getAcilCikis(), row.getDepartment(), row.getService(), doctor, null, row.getAcileGelisDurum(), row.getAcilDurum(), row.getTani() );
				
				else if (decision.contains("Servis yatışı"))
					addEvent( patient, "Inpatient Admission", row.getAcilCikis(),row.getAcilCikis(), row.getDepartment(), row.getService(), doctor, null, row.getAcileGelisDurum(), row.getAcilDurum(), row.getTani() );
				
				else if (decision.contains("Ameliyata"))
					addEvent( patient, "Operation", row.getAcilCikis(),row.getAcilCikis(), row.getDepartment(), row.getService(), doctor, null, row.getAcileGelisDurum(), row.getAcilDurum(), row.getTani() );
				else if (decision.contains("Yoğun"))
					addEvent( patient, "Admission to ICU", row.getAcilCikis(),row.getAcilCikis(), row.getDepartment(), row.getService(), doctor, null, row.getAcileGelisDurum(), row.getAcilDurum(), row.getTani() );
				
				else if (decision.contains("Reçete"))
					addEvent( patient, "Prescription", row.getAcilCikis(),row.getAcilCikis(), row.getDepartment(), row.getService(), doctor, null, row.getAcileGelisDurum(), row.getAcilDurum(), row.getTani() );
				else 
					addEvent( patient, decision, row.getAcilCikis(),row.getAcilCikis(), row.getDepartment(), row.getService(), doctor, null, row.getAcileGelisDurum(), row.getAcilDurum(), row.getTani() );
		 		
			}*/
			addEvent( patient, "Decision", row.getAcilCikis(),row.getAcilCikis(),row.getDepartment(), row.getService(), doctor, null, row.getAcileGelisDurum(), row.getAcilDurum() ,row.getTani());
			 
		}
		if (row.getAcilCikis() != null)
			row.setAcilCikis(DateUtils.addMinutes(row.getAcilCikis(), 1) );
		// acil çıkış
		addEvent( patient, "Discharge", row.getAcilCikis(), row.getAcilCikis(),row.getDepartment(), row.getService(), doctor, null, row.getAcileGelisDurum(), row.getAcilDurum(), row.getTani() );
		

		System.out.println("Kons:" + row.getKons() );
		if (row.getKons().contains("Var"))
		{
			RowContextKons kons = readKonsFile (row.getPatientId());
			if ( kons != null ) {
				System.out.println("Kons:" + kons.getKonsZamani() );
				// kons istekd
				addEvent( patient, "Consultation Request", kons.getKonsZamani(),kons.getKonsZamani(),row.getDepartment(), row.getService(), doctor, null, row.getAcileGelisDurum(), row.getAcilDurum(), row.getTani() );
				// kons istekd
				addEvent( patient, "Consultation Accepted", kons.getKarsilanmaZamani(),kons.getKarsilanmaZamani(),row.getDepartment(), row.getService(), doctor, null, row.getAcileGelisDurum(), row.getAcilDurum() ,row.getTani() );
			}
		}
		
		
	}
	private RowContextKons readKonsFile(Integer patientId) {
		RowContextKons rowContext = RowContextKons.builder().build();
		FileInputStream excelFile;
		try {
			excelFile = new FileInputStream(new File("konsultasyon.xls"));
		
		HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
	    HSSFSheet datatypeSheet=  workbook.getSheetAt(0);
	    Iterator<Row> iterator= datatypeSheet.iterator();
        iterator.next();
        while (iterator.hasNext() ){
            Row currentRow = iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();
            while (cellIterator.hasNext()) 
            {
            	Cell cell = cellIterator.next();
      			 
            	if (cell.getRowIndex() >= 751)
    			 {
    				 return null;
    			 }
            	
   			 	if (cell.getColumnIndex() == 0 &&  Integer.parseInt ( cell.getStringCellValue() ) == patientId )
   			 	{	
   			 		rowContext.setPatientId(patientId) ;
   			 		cell = cellIterator.next();
   			 		if ( !isCellEmpty (cell) ) //konsultasyon
   			 		{
   			 			rowContext.setKonsZamani( cell.getDateCellValue());
   			 			System.out.println(rowContext.getKonsZamani());
   			 		}
   			 		cell = cellIterator.next();
   			 	
   					if (!isCellEmpty (cell)) //karsılanma zamanı
						rowContext.setKarsilanmaZamani(cell.getDateCellValue());
   					return rowContext;
   			 	}
   			}
           
        }
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(rowContext.getKonsZamani());
		return rowContext;
		
	}
	public static boolean isCellEmpty(Cell cell) {
	    if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
	        return true;
	    }

	    if (cell.getCellType() == Cell.CELL_TYPE_STRING && cell.getStringCellValue().isEmpty()) {
	        return true;
	    }
	    return false;
	}
	
}


