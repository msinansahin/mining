package tugba.mining.controllers;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import tugba.mining.domain.Activity;
import tugba.mining.domain.Event;
import tugba.mining.dto.ProcessMap;
import tugba.mining.repositories.EventRepository;
import tugba.mining.services.CommonService;
import tugba.mining.util.RowContext;

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
	@PostMapping("/event")
	public ResponseEntity<Event> saveEvent(@RequestBody Event event) {
		commonService.saveOrUpdate(event);
		return ResponseEntity.ok(event);
	}
	@GetMapping("/vt-hazirla")
	public ResponseEntity<?> vtHazirla() {		
		commonService.deleteAll();	
		try {
			FileInputStream excelFile = new FileInputStream(new File("deneme2.xls"));
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
		 
			/*
			 * String minedColon = "activityname";
			String durationColon = null;
		    final String CHARSET = "windows-1254";
		    final String DBCLASSNAME = "org.apache.derby.jdbc.EmbeddedDriver";
		    final String DBNAME = "tugbatezdb";
		    final String DBURL = "jdbc:derby:" + DBNAME + ";create=true;user=tugba;password=tez";
		    Connection conn = DriverManager.getConnection(DBURL);
            
            String createStmt = "create table event (";
            createStmt += "eventId int, ";
            createStmt += "patientId int, ";
            createStmt += "activityname varchar(500), ";
            createStmt += "startdate varchar(500), ";
            createStmt += "finishdate varchar(500), ";
            createStmt += "department varchar(500), ";
            createStmt += "service varchar(500), ";
            createStmt += "doctor varchar(500))";
           
            Statement stmt = conn.createStatement();      
            try {
                stmt.execute("drop table event");
            } catch (Exception ex) {
            }
            stmt.execute(createStmt);

            String insertStmt;
            List <Event> events = commonService.listEvent();
            System.out.println("Event size:" + events.size());
            Iterator iterator = events.iterator();
            int cell=0;
            int po = 0;
            while(iterator.hasNext()) {
          	
            
            	Event e = (Event) iterator.next();
            	
            	insertStmt = "insert into event values(" + (e.getEventId()) + "," + e.getPatient().getPatientId() + ",'";
                insertStmt += e.getActivity() + "','";
                insertStmt += e.getStartDate() + "','";
                insertStmt += e.getFinishDate() + "','";
                insertStmt += e.getDepartment() + "','";
                insertStmt += e.getService() + "','";
                insertStmt += e.getDoctor() + "')";
             
                try {
					if (stmt.executeUpdate(insertStmt) != 1) {
					    throw new Exception("eklenemedi:" + insertStmt);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
            try {
                stmt.execute("drop table pattern");
            } catch (Exception ex) {
            }
            */
			commonService.addPatterns();
           
		 return ResponseEntity.ok(true);
	 }
	
	@Autowired
	EventRepository er;
	
	@GetMapping("/test")
	public ResponseEntity<?> test(Double patientId, String activity, Date startDate) {
		return ResponseEntity.ok(er.findByPatientPatientIdAndActivityAndStartDate(patientId, activity, startDate));
	}
}
