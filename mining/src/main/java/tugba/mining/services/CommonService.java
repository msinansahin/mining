package tugba.mining.services;

import java.util.Date;
import java.util.List;

import tugba.mining.domain.Activity;
import tugba.mining.domain.BaseEntity;
import tugba.mining.domain.Event;
import tugba.mining.domain.Path;
import tugba.mining.domain.Patient;
import tugba.mining.domain.Pattern;
import tugba.mining.util.EventRow;
import tugba.mining.util.RowContext;
import tugba.mining.util.RowContextER;
import tugba.mining.util.RowContextKons;

public interface CommonService {

    List<Event> listEvent();
    List<Activity> listActivity();
    List<Path> listPath();
    List<Pattern> listPattern();
    List<EventRow> getEventRows();
    <T> T getById(Class <T> clazz, Long id);
    
    <T> boolean exists(Class <T> clazz, Long id);

    void saveOrUpdate(BaseEntity entity);

    <T> void delete(T clazz, Long id);

	void deleteAll();
	void deletePatterns();
	
	void addRowContext (RowContext row);
	void addRowContextER (RowContextER row);

	void addPatterns();
	
	// update events by start date for each patient and updates in neo4j
	void updateEventsByStartDate();
	void updateActivities();
	
	void processMap();
	
	
	Activity addActivity(String activityName);
	//void writeEventRows();
	//void addEventRow(RowContext row, String activity, Date activityDate);
	//void addEventRowContext(RowContext row);
	
	
}
