package tugba.mining.services;

import java.util.List;

import tugba.mining.domain.BaseEntity;
import tugba.mining.domain.Event;
import tugba.mining.domain.Patient;
import tugba.mining.util.RowContext;

public interface CommonService {

    List<Event> listEvent();

    <T> T getById(Class <T> clazz, Long id);
    
    <T> boolean exists(Class <T> clazz, Long id);

    void saveOrUpdate(BaseEntity entity);

    <T> void delete(T clazz, Long id);

	void deleteAll();
	
	void addRowContext(RowContext row);

	void addPatterns();
	
	// update events by start date for each patient and updates in neo4j
	void updateEventsByStartDate();



}
