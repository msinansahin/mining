package tugba.mining.controllers;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import tugba.mining.domain.Activity;
import tugba.mining.domain.Event;
import tugba.mining.domain.Path;
import tugba.mining.domain.Pattern;
import tugba.mining.dto.ProcessMap;
import tugba.mining.repositories.ActivityRepository;
import tugba.mining.repositories.EventRepository;
import tugba.mining.repositories.PatternRepository;
import tugba.mining.services.CommonService;
import tugba.mining.util.RowContext;

@Controller
public class CommonController {
	
	@Autowired
    CommonService commonService;

	@GetMapping("/process-map")
	public ResponseEntity<ProcessMap> getProcessMap() {
		commonService.processMap();
		ProcessMap map = ProcessMap.builder().build();
		map.setActivities(commonService.listActivity());
		map.setPaths(commonService.listPath());
		map.setPatterns(commonService.listPattern());
		
		Iterable <Pattern> ps = map.getPatterns();
				ps.forEach(p->System.out.println(p));
				
		Iterable <Path> es = map.getPaths();
		es.forEach(p->System.out.println(p));
			
		
		
		return ResponseEntity.ok(map);	
	}
	@PostMapping("/activity")
	public ResponseEntity<Activity> saveActivity(@RequestBody Activity activity) {
		commonService.saveOrUpdate(activity);
		return ResponseEntity.ok(activity);
	}
	@PostMapping("/event")
	public ResponseEntity<Event> saveEvent(@RequestBody Event event) {
		commonService.saveOrUpdate(event);
		return ResponseEntity.ok(event);
	}
	@GetMapping("/vt-hazirla")
	public ResponseEntity<?> vtHazirla() {		
		commonService.deleteAll();	
		//commonService.addActivity("Start");
		try {
			FileInputStream excelFile = new FileInputStream(new File("deneme2.xls"));
			commonService.addActivity ("Start");
			HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
		    HSSFSheet datatypeSheet =  workbook.getSheetAt(0);
		    Iterator<Row> iterator = datatypeSheet.iterator();
	        iterator.next();
	        while (iterator.hasNext()) {
	            Row currentRow = iterator.next();
	            Iterator<Cell> cellIterator = currentRow.iterator();
	            RowContext rowContext = readRow(cellIterator);
	            commonService.addRowContext(rowContext);
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return ResponseEntity.ok(true);
		
	}
	 private RowContext readRow(Iterator<Cell> cellIterator) {
	    	
	    	return RowContext.builder()
	    			.eventId(Integer.parseInt(cellIterator.next().getStringCellValue())) 
	    			.patientId((int) cellIterator.next().getNumericCellValue()) 
	    			.surgeryNo((int) cellIterator.next().getNumericCellValue())
	    			.age((int) cellIterator.next().getNumericCellValue())
	    			.gender(cellIterator.next().getStringCellValue())
	    			.activitySurgeryOrder("Surgery Ordered")
	    			.surgeryOrderDate(cellIterator.next().getDateCellValue())
	    			.surgeryCategory(cellIterator.next().getStringCellValue())
	    			.activityAdmission("Admission to Hospital")
	    			.admissionDate(cellIterator.next().getDateCellValue())
	    			.department(cellIterator.next().getStringCellValue())
	    			.service(cellIterator.next().getStringCellValue())
	    			.doctor(cellIterator.next().getStringCellValue())
	    			.servisDegisiklik(cellIterator.next().getStringCellValue())
	    			.servisDegisiklikTar(cellIterator.next().getDateCellValue())
	    			.bolumDegisiklik(cellIterator.next().getStringCellValue())
	    			.bolumDegisiklikTar(cellIterator.next().getDateCellValue())
	    			.service2(cellIterator.next().getStringCellValue())
	    			.department2(cellIterator.next().getStringCellValue())
	    			.doctor2(cellIterator.next().getStringCellValue())
	    			.operatingRoom(cellIterator.next().getStringCellValue())
	    			.surgeryStartDate(cellIterator.next().getDateCellValue())
	    			.surgeryFinishDate(cellIterator.next().getDateCellValue())
	    			.surgeryName(cellIterator.next().getStringCellValue())
	    			.surgeryDoctor(cellIterator.next().getStringCellValue())
	    			.activityServiseCikis("Transfer to a service")
	    			.serviseCikisTar(cellIterator.next().getDateCellValue())
	    			.activityOlum("Ex")
	    			.olumDate(cellIterator.next().getDateCellValue())
	    			.activityTaburcu("Discharged")
	    			.taburcuDate(cellIterator.next().getDateCellValue())
	    			.kesintanı(cellIterator.next().getStringCellValue())
	    			.build();
			
	}
	@GetMapping("/patterns")
	public ResponseEntity<?> getPatterns() throws SQLException {
		commonService.addPatterns();
		return ResponseEntity.ok(true);
	}
	
	@Autowired
	EventRepository er;
	@Autowired
	ActivityRepository ar;
	@Autowired
	PatternRepository pr;
	@GetMapping("/test")
	public ResponseEntity<?> test(Double patientId, String activity, Date startDate) {
		Iterator it = (pr.findAll(new Sort ("patternId"))).iterator();
		while (it.hasNext())
		{
			Pattern p = (Pattern)it.next();
			System.out.println("Pattern:" + p.getPatternId() +":" + p.getTrace() );
			System.out.println ("Patient Count" + p.getPatientCount() + p.getMyPatients());
			
		}
		return ResponseEntity.ok(true);
	}
	
	
	@GetMapping("/testEventsFile")
	public ResponseEntity<?> testMap() {
		
		return ResponseEntity.ok(true);
	}
}
