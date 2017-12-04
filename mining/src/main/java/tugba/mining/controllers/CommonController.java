package tugba.mining.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import tugba.mining.domain.Event;
import tugba.mining.dto.ProcessMap;
import tugba.mining.services.CommonService;

@Controller
public class CommonController {
	
	@Autowired
    CommonService commonService;

	@GetMapping("/process-map")
	public ResponseEntity<ProcessMap> getProcessMap() {
		ProcessMap map = new ProcessMap();
		map.setEvents(commonService.listAll(Event.class));
		return ResponseEntity.ok(map);
		
	}

}
