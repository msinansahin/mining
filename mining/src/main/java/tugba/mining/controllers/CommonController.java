package tugba.mining.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import tugba.mining.domain.Activity;
import tugba.mining.dto.ProcessMap;
import tugba.mining.services.CommonService;

@Controller
public class CommonController {
	
	@Autowired
    CommonService commonService;

	@GetMapping("/process-map")
	public ResponseEntity<ProcessMap> getProcessMap() {
		ProcessMap map = new ProcessMap();
		map.setEvents(commonService.listEvent());
		return ResponseEntity.ok(map);
		
	}
	
	
	
	
	@PostMapping("/activity")
	public ResponseEntity<Activity> saveActivity(@RequestBody Activity activity) {
		commonService.saveOrUpdate(activity);
		return ResponseEntity.ok(activity);
	}
	
	

}
