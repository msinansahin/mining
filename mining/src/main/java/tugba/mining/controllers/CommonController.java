package tugba.mining.controllers;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import org.apache.poi.*;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
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
import tugba.mining.domain.Doctor;
import tugba.mining.domain.Event;
import tugba.mining.domain.Patient;
import tugba.mining.domain.Pattern;
import tugba.mining.domain.Surgery;
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
	@PostMapping("/event")
	public ResponseEntity<Event> saveEvent(@RequestBody Event event) {
		commonService.saveOrUpdate(event);
		return ResponseEntity.ok(event);
	}
	@GetMapping("/vt-hazirla")
	public ResponseEntity<?> vtHazirla() {
		
		commonService.deleteAll();	
		
		// TODO excel dosyasından ya da bir yerden oku
		// TODO neo'ya yaz
		try {
			FileInputStream excelFile = new FileInputStream(new File("deneme2.xls"));
			HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
		    HSSFSheet datatypeSheet =  workbook.getSheetAt(0);
		    Iterator<Row> iterator = datatypeSheet.iterator();
	        iterator.next();
	        while (iterator.hasNext()) {
	            Row currentRow = iterator.next();
	            Iterator<Cell> cellIterator = currentRow.iterator();
	            readRow(cellIterator);
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(true);
		
	}
	public void addEvents(Integer eventId, double patientId, double surgeryNo,double age, String gender, 
			String activitySurgeryOrder, Date surgeryOrderDate, 
			String surgeryCategory,
			String activityAdmission, Date admissionDate, 
			String department, String service, String doctor, 
			String servisDegisiklik, Date servisDegisiklikTar,
			String bolumDegisiklik, Date bolumDegisiklikTar,
			String service2, String department2, String doctor2,String operatingRoom,
			Date surgeryStartDate, Date surgeryFinishDate, String surgeryName, String surgeryDoctor,
			String activityServiseCikis,  Date serviseCikisTar , 
			String activityOlum,Date olumDate, 
			String activityTaburcu, Date taburcuDate, 
			String kesintanı) {
		
		
		Patient patient = new Patient (patientId,age,gender);
		commonService.saveOrUpdate(patient);
		
		Doctor myDoctor = new Doctor (doctor);
		commonService.saveOrUpdate(myDoctor);
		
		Surgery surgery = new Surgery ((int)surgeryNo,surgeryName, surgeryCategory );
		commonService.saveOrUpdate(surgery);
		
		// surgery order
		Event event = new Event(eventId, patient,activityAdmission, admissionDate,admissionDate, department,service,myDoctor, surgery);
		commonService.saveOrUpdate(event);
		
	
    	// servis degisikliği
    	if (servisDegisiklik.equals("E") )
    	{	
    		myDoctor = new Doctor (doctor2);
    		commonService.saveOrUpdate(myDoctor);
    		
    		eventId = eventId + 1;
    		if (service2.contains("Yoğun Bakı"))
    			event = new Event(eventId, patient,"Transfer to Intensive Care Unit", servisDegisiklikTar,servisDegisiklikTar, department,service2,myDoctor, surgery);
    		else
    			event = new Event(eventId, patient,"Change Service", servisDegisiklikTar,servisDegisiklikTar, department,service2,myDoctor, surgery);
    		commonService.saveOrUpdate(event);
    	}
    	// bolum degisikliği
    	if (bolumDegisiklik.equals("E"))
    	{			
    		myDoctor = new Doctor (doctor2);
    		commonService.saveOrUpdate(myDoctor);
    		
    		eventId = eventId + 1;   
    		event = new Event (eventId, patient,"Change Department", bolumDegisiklikTar,bolumDegisiklikTar,department2,service2,myDoctor, surgery);
    		commonService.saveOrUpdate(event);
    	}
    	
    	//ameliyat_bas
    	eventId = eventId + 1;
    	event = new Event (eventId, patient,"Surgery Started", surgeryStartDate,surgeryStartDate, department,service,myDoctor, surgery);
    	commonService.saveOrUpdate(event);
    	
    	eventId = eventId + 1;
        event = new Event (eventId, patient,"Surgery Finished", surgeryFinishDate,surgeryFinishDate, department,service,myDoctor, surgery);
      	commonService.saveOrUpdate(event);
        
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	String formattedDate = formatter.format(olumDate);
    	
    	if (!formattedDate.equals("2017-01-01"))
    	{
    		eventId = eventId + 1;
    		event = new Event (eventId, patient,activityOlum, olumDate, olumDate, department,service,myDoctor, surgery);
    	 	commonService.saveOrUpdate(event);
    	}
    	
    	//servise çıkış
    	eventId = eventId + 1;
    	event = new Event  (eventId, patient,activityServiseCikis, serviseCikisTar,serviseCikisTar, department,service,myDoctor, surgery);
    	commonService.saveOrUpdate(event);
    	
    	// taburcu
    	eventId = eventId + 1;
    	event = new Event  (eventId, patient,activityTaburcu, taburcuDate,taburcuDate,department,service,myDoctor, surgery);
		commonService.saveOrUpdate(event);
	}
	
private boolean checkDuplicateEvent(Patient p, String activity, Date activityDate) {
	 List <Event> events = commonService.listEvent();
	 Iterator iterator = events.iterator();
     while(iterator.hasNext()) {
   		Event e = (Event) iterator.next();
     	if (e.getActivity().equals(activity) && 
				e.getStartDate().equals(activityDate))
				return true;		
     }
	return false;
}
	 private void readRow(Iterator<Cell> cellIterator) {
	    	
	    	String eventid=cellIterator.next().getStringCellValue(); //eventid
	        addEvents (	Integer.parseInt(eventid), 
	        			cellIterator.next().getNumericCellValue(), // patient id 
	            		cellIterator.next().getNumericCellValue(), // ameliyat no
	            		cellIterator.next().getNumericCellValue(),	// yas
	            		cellIterator.next().getStringCellValue(), //gender
	            		"Surgery Ordered",cellIterator.next().getDateCellValue() , // surgery order
	            		cellIterator.next().getStringCellValue(), //surgery category
	            		"Admission to Hospital",cellIterator.next().getDateCellValue() ,
	            		cellIterator.next().getStringCellValue(), //bölüm
	            		cellIterator.next().getStringCellValue(), // servis
	            		cellIterator.next().getStringCellValue(), //doktor
	            		cellIterator.next().getStringCellValue(), 
	            		cellIterator.next().getDateCellValue(), // servis değişikliği
	            		cellIterator.next().getStringCellValue(),
	            		cellIterator.next().getDateCellValue(), // bölüm değişlliği
	            		cellIterator.next().getStringCellValue(), // servis
	            		cellIterator.next().getStringCellValue(),// bolum
	            		cellIterator.next().getStringCellValue(), // bolum doktoru
	            		cellIterator.next().getStringCellValue(), // operating room
	            		cellIterator.next().getDateCellValue(), // ameliyat başlangcı
	            		cellIterator.next().getDateCellValue(), 	//amalieyat bitişi
	            		cellIterator.next().getStringCellValue(), // amaeliyar
	            		cellIterator.next().getStringCellValue(), // ameliyat doktor
	            		"Transfer to a service" ,  cellIterator.next().getDateCellValue(),
	            		"Ex" , cellIterator.next().getDateCellValue(),
	            		"Discharged" , cellIterator.next().getDateCellValue(),
	            		cellIterator.next().getStringCellValue() //kesin tanı
	            		);
	     
			
	}
	 @GetMapping("/patterns")
	 public ResponseEntity<?> getPatterns() throws SQLException {
		 
			String minedColon = "activityname";
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
            stmt.execute("create table pattern(road varchar(8192),count int,ps varchar(8192))");

            ResultSet rs = stmt.executeQuery("select patientid,"+ minedColon+ " from event order by patientid,startdate,eventid");
            ResultSet rs2;
            Statement stmt2 = conn.createStatement();

            String road = "";
            String nps = "";
            int ei = -1;
            int tei;
            while (rs.next()) {
                tei = rs.getInt("patientid");
                if (tei != ei) {
                    
                    if (road.length() != 0) {
                        road = road.substring(0, road.length() - 2);
                        rs2 = stmt2.executeQuery("select * from pattern where road='" + road + "'");
                        if (rs2.next()) {
                            int cnt = rs2.getInt("count") + 1;
                            nps = rs2.getString("ps") + "," + ei;
                            stmt2.executeUpdate("update pattern set count=" + cnt + ", ps='" + nps + "' where road='" + road + "'");
                        } else {
                            stmt2.execute("insert into pattern values('" + road + "',1,'" + ei + "')");
                        }
                    }
                    ei = tei;
                    road = "";
                }
                road += rs.getString(minedColon).toLowerCase().trim() + "->";
            }
            
            rs = stmt.executeQuery("select * from pattern order by count desc");
            int idx = 0;
            while (rs.next()) {
                 System.out.println("Pattern " + idx + ";" + rs.getString("road") + ";" + rs.getInt("count")
                        + "\n\t\tEvents:" + rs.getString("ps"));
                idx++;
            }

		 return ResponseEntity.ok(true);
	 }
}
