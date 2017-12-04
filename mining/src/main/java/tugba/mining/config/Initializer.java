package tugba.mining.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tugba.mining.domain.Activity;
import tugba.mining.domain.Event;
import tugba.mining.services.CommonService;

@Component
public class Initializer {

	@Autowired
	CommonService commonService;
	
	@PostConstruct
	public void init() {
		Activity activity = new Activity();
		activity.setActivityName("activity1");
		commonService.saveOrUpdate(activity);
		
		Event event = new Event();
		event.setActivity(activity);
		commonService.saveOrUpdate(event);
	}
}
