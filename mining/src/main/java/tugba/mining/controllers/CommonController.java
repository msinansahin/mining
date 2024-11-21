package tugba.mining.controllers;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.google.common.collect.Lists;

import tugba.mining.domain.Activity;
import tugba.mining.domain.Event;
import tugba.mining.domain.Path;
import tugba.mining.domain.Pattern;
import tugba.mining.dto.ProcessMap;
import tugba.mining.repositories.ActivityRepository;
import tugba.mining.repositories.EventRepository;
import tugba.mining.repositories.PatternRepository;
import tugba.mining.services.CommonService;
import tugba.mining.util.EventRow;
import tugba.mining.util.RowContextKons;
import tugba.mining.util.RowContext;
import tugba.mining.util.RowContextER;

import java.util.List;
@Controller
public class CommonController {
	
	@Autowired
    CommonService commonService;
	@Autowired
	EventRepository er;
	@Autowired
	ActivityRepository ar;
	@Autowired
	PatternRepository pr;

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
	
	@GetMapping("/vt-hazirla")
	public ResponseEntity<?> vtHazirla() {		
		commonService.deleteAll();	
		System.out.println("github flow");
		System.out.println("github flow");
		//commonService.addActivity("Start");
		try {
			FileInputStream excelFile = new FileInputStream(new File("veri-ocak.xls"));
			commonService.addActivity ("Start");
			HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
		    HSSFSheet datatypeSheet =  workbook.getSheetAt(0);
		    Iterator<Row> iterator = datatypeSheet.iterator();
	        iterator.next();
	        List <RowContext> rows = Lists.newArrayList();
	        while (iterator.hasNext() ){
	            Row currentRow = iterator.next();
	            Iterator<Cell> cellIterator = currentRow.iterator();
	            RowContext rowContext = readRow(cellIterator);
	            rows.add(rowContext);
	            if (rowContext != null)
	            {
	             	commonService.addRowContext(rowContext);        	
	            }
	        }
	        //update events by start date
	        commonService.updateEventsByStartDate();
			// eventcount and patientcount of activites
	        commonService.updateActivities();
	        // write events to excel fil
			writeEventsExcel();		
			writeEventsCsv();		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return ResponseEntity.ok(true);
		
	}
	private void writeEventsExcelAcil() {
		 Workbook workbook = new HSSFWorkbook();
	     HSSFSheet sheet = (HSSFSheet) workbook.createSheet("ALL");
	     long cell=0;
	     for (Iterator<Event> iter = er.findAll(new Sort("eventId")).iterator(); iter.hasNext();)
	        {
	            Event e = iter.next();
	            System.out.println (e.getEventId() + " " + e.getActivity().getActivityName() + " " + e.getPatient().getPatientId() );
		       	HSSFRow rowEvent = sheet.createRow((int) cell );
	        	HSSFCell cellA1 = rowEvent.createCell((int) 0);
	        	cellA1.setCellValue(e.getEventId());
	            
	        	HSSFCell cellA2 = rowEvent.createCell((int) 1);
	        	cellA2.setCellValue(e.getPatient().getPatientId());
	        	
	        	HSSFCell cellA3 = rowEvent.createCell((int) 2);
	        	cellA3.setCellValue(e.getPatient().getAge());
	        
	        	HSSFCell cellA4 = rowEvent.createCell((int) 3);
	        	cellA4.setCellValue(e.getPatient().getGender());
	        	
	        	HSSFCell cellA5 = rowEvent.createCell((int) 4);
	        	cellA5.setCellValue(e.getAcileGelisDurum());
	        
	        	HSSFCell cellA6 = rowEvent.createCell((int) 5);
	        	cellA6.setCellValue(e.getAcilDurum());
	        
	        	HSSFCell cellA7 = rowEvent.createCell((int) 6);
	        	cellA7.setCellValue(e.getActivity().getActivityName());
	        	
	        	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        	HSSFCell cellA8 = rowEvent.createCell((int) 7); 
	        	cellA8.setCellValue(formatter.format (e.getStartDate()));
	        	
	        	HSSFCell cellA9 = rowEvent.createCell((int) 8);
	        	if ( e.getFinishDate() != null)
		        	cellA9.setCellValue(formatter.format (e.getFinishDate()));
		        
	        	HSSFCell cellA10 = rowEvent.createCell((int) 9);
	        	cellA10.setCellValue(e.getDepartment());
	        	
	        	HSSFCell cellA11 = rowEvent.createCell((int) 10);
	        	cellA11.setCellValue(e.getService() );
	        	
	        	HSSFCell cellA12 = rowEvent.createCell((int) 11);
	        	cellA12.setCellValue(e.getDoctor().getName());
	        	
	        	HSSFCell cellA13 = rowEvent.createCell((int) 12);
	        	cellA13.setCellValue(e.getAcileGelisDurum());
	        	
	        	HSSFCell cellA14 = rowEvent.createCell((int) 13);
	        	cellA14.setCellValue(e.getAcilDurum());
	        	
	        	HSSFCell cellA15 = rowEvent.createCell((int) 14);
	        	cellA15.setCellValue(e.getDoctor().getName());
	        	
	        	HSSFCell cellA16 = rowEvent.createCell((int) 15);
	        	cellA16.setCellValue(e.getTani());
	        	
	        	cell++;
	        }
	        try {
				FileOutputStream outputStream = new FileOutputStream(new File("events-acil-detail.xls"));
			
				workbook.write(outputStream);
				workbook.close();
			} catch ( IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
	
	@GetMapping("/vt-hazirla-acil")
	public ResponseEntity<?> vtHazirlaAcil() {		
		commonService.deleteAll();	
		//commonService.addActivity("Start");
		try {
			FileInputStream excelFile = new FileInputStream(new File("acil.xls"));
			commonService.addActivity ("Start");
			HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
		    HSSFSheet datatypeSheet =  workbook.getSheetAt(0);
		    Iterator<Row> iterator = datatypeSheet.iterator();
	        iterator.next();
	        List <RowContextER> rows = Lists.newArrayList();
	        while (iterator.hasNext() ){
	            Row currentRow = iterator.next();
	            Iterator<Cell> cellIterator = currentRow.iterator();
	           
	            RowContextER rowContext = readRowER(cellIterator);
	            rows.add(rowContext);
	            if (rowContext != null)
	            {
	             	commonService.addRowContextER(rowContext);        	
	            }
	        }
	        //update events by start date
	      //  commonService.updateEventsByStartDate();
			// eventcount and patientcount of activites
	      //  commonService.updateActivities();
	        // write events to excel fil
			writeEventsExcelAcil();		
			writeEventsCsv();		
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return ResponseEntity.ok(true);
		
	}
	private void writeEventsCsv() {
		PrintWriter pw;
		try {
			pw = new PrintWriter( new OutputStreamWriter
					( new FileOutputStream( "events-ALL-single.csv", true ), "UTF-8" )); 
			String header = "Patient ID"+";" + "Age" + ";"+ "Gender" +";" +"Surgery No" +";"+ 
			"Surgery Category" + ";" + "Surgery Name" +";"+"Activity" +";"+"Activity Start Date" +";"+
					"Activity Finish Date" +";"+"Department" +";"+"Service" +";"+"Doctor"; 
			pw.write(header + '\n');
			for (Iterator<Event> iter = er.findAll(new Sort("eventId")).iterator(); iter.hasNext();)
			{	
				Event e = iter.next();
				pw.write(e.toString());
			}
			 pw.close();
	        System.out.println("done!");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	      
		
	}
	public void writeEventsExcel() {
		
        Workbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = (HSSFSheet) workbook.createSheet("ALL");
        long cell=0;
      
        for (Iterator<Event> iter = er.findAll(new Sort("eventId")).iterator(); iter.hasNext();)
        {
            Event e = iter.next();
	       	HSSFRow rowEvent = sheet.createRow((int) cell );
        	HSSFCell cellA1 = rowEvent.createCell((int) 0);
        	cellA1.setCellValue(e.getEventId());
            
        	HSSFCell cellA2 = rowEvent.createCell((int) 1);
        	cellA2.setCellValue(e.getPatient().getPatientId());
        	
        	HSSFCell cellA3 = rowEvent.createCell((int) 2);
        	cellA3.setCellValue(e.getPatient().getAge());
        
        	HSSFCell cellA4 = rowEvent.createCell((int) 3);
        	cellA4.setCellValue(e.getPatient().getGender());
        	
        	HSSFCell cellA5 = rowEvent.createCell((int) 4);
        	cellA5.setCellValue(e.getSurgery().getSurgeryCategory());
        
        	HSSFCell cellA6 = rowEvent.createCell((int) 5);
        	cellA6.setCellValue(e.getSurgery().getSurgeryName());
        
        	HSSFCell cellA7 = rowEvent.createCell((int) 6);
        	cellA7.setCellValue(e.getActivity().getActivityName());
        	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            
        	HSSFCell cellA8 = rowEvent.createCell((int) 7); 
        	cellA8.setCellValue(formatter.format (e.getStartDate()));
        	
        	HSSFCell cellA9 = rowEvent.createCell((int) 8);
        	cellA9.setCellValue(formatter.format (e.getStartDate()));
	        
        	HSSFCell cellA10 = rowEvent.createCell((int) 9);
        	cellA10.setCellValue(e.getDepartment());
        	
        	HSSFCell cellA11 = rowEvent.createCell((int) 10);
        	cellA11.setCellValue(e.getService() );
        	
        	HSSFCell cellA12 = rowEvent.createCell((int) 11);
        	cellA12.setCellValue(e.getDoctor().getName());
        	
        	cell++;
        	
        }
        try {
			FileOutputStream outputStream = new FileOutputStream(new File("events.xls"));
		
			workbook.write(outputStream);
			workbook.close();
		} catch ( IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	@GetMapping("/excel-hazirla")
	public ResponseEntity<?> excelHazirla() {		
		commonService.deleteAll();	
		try {
			FileInputStream excelFile = new FileInputStream(new File("deneme2.xls"));
			HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
		    HSSFSheet datatypeSheet =  workbook.getSheetAt(0);
		    Iterator<Row> iterator = datatypeSheet.iterator();
	        iterator.next();
	        List <RowContext> rows = Lists.newArrayList();
	        while (iterator.hasNext() ){
	            Row currentRow = iterator.next();
	            Iterator<Cell> cellIterator = currentRow.iterator();
	            RowContext rowContext = readRow(cellIterator);
	            rows.add(rowContext);
	            if (rowContext != null)
	            {
	            //	commonService.addEventRowContext(rowContext);
	            }
	        }
	       
	         //  commonService.writeEventRows();
					
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return ResponseEntity.ok(true);
		
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
	private RowContextKons readRowKons(Iterator<Cell> cellIterator) {
		RowContextKons rowContext = RowContextKons.builder().build();
		 while (cellIterator.hasNext()) 
		 {
			 Cell cell = cellIterator.next();
			 System.out.println( "RowIndex:" + cell.getRowIndex() );
				
				
			 if (cell.getRowIndex() >= 751)
			 {
				 return null;
			 }
			 if (cell.getColumnIndex() == 0 )	 //patient id
				 rowContext.setPatientId(Integer.parseInt( cell.getStringCellValue())) ;
			 if (cell.getColumnIndex() == 1 && (!isCellEmpty (cell))) //konsultasyon
			 {	 	
				 System.out.println("Column index:" + cell.getColumnIndex() );
				 rowContext.setKonsZamani( cell.getDateCellValue());
			 }
			 if (cell.getColumnIndex() == 2 && (!isCellEmpty (cell))) //karsılanma zamanı
			 {	 	
				 System.out.println("Column index:" + cell.getColumnIndex() );
				 rowContext.setKarsilanmaZamani(cell.getDateCellValue());
			 }
			 
				 
		 }
		return rowContext;
	}
	private RowContextER readRowER(Iterator<Cell> cellIterator) {
		 RowContextER rowContext = RowContextER.builder().build();
			/*
			 * 	private String acilDurum; // acil, normal, cok acil 
				private String acileGelisDurum; // kendisi, ambulans
				private Integer eventId;
				private Integer patientId; 
				private Integer patientName; 
				private String gender; 
				private Integer age; 
				private String muayeneDoktor;
				private String kararSonucu;
				private Date acilGiris; 
				private Date triyajGiris; 
				private Date triyajCikis; 
				private Date muayene; 
				private Date musahedeGiris; 
				private Date musahedeCikis; 
				private Date acilCikis; 
			 */
		 while (cellIterator.hasNext()) 
		 {
				
			 Cell cell = cellIterator.next();
			 if (cell.getRowIndex() >= 2756)
			 {
				 return null;
			 }
			 System.out.println( "RowIndex:" + cell.getRowIndex() );
			 if (cell.getColumnIndex() == 0 )	 //acil durum
				 rowContext.setAcilDurum (cell.getStringCellValue() );
			 if (cell.getColumnIndex() == 1 )	// acile geliş durum
				 rowContext.setAcileGelisDurum (cell.getStringCellValue() );
			 if (cell.getColumnIndex() == 2 )	//patient id
				 rowContext.setPatientId(Integer.parseInt( cell.getStringCellValue())) ;
			 if (cell.getColumnIndex() == 3 )	// patient Name
				 rowContext.setPatientName(cell.getStringCellValue());
			 if (cell.getColumnIndex() == 4 )	// gender
				 rowContext.setGender (cell.getStringCellValue());
			 if (cell.getColumnIndex() == 5) // age
			 	rowContext.setAge( Integer.parseInt( cell.getStringCellValue())) ;
			 if (cell.getColumnIndex() == 6 ) //muayenedoktoru
				 rowContext.setDoctor(cell.getStringCellValue());
			 if (cell.getColumnIndex() == 7 ) //karar sonucu
				 rowContext.setKararSonucu(cell.getStringCellValue());
			 if (cell.getColumnIndex() == 8  && (!isCellEmpty (cell))) //acile giris
			 {	 	
				 System.out.println("Column index:" + cell.getColumnIndex() );
				 rowContext.setAcilGiris( cell.getDateCellValue());
			 }
			 if (cell.getColumnIndex() == 9  && (!isCellEmpty (cell))) //triyaja giris
			 {	 	
				 System.out.println("Column index:" + cell.getColumnIndex() );
				 rowContext.setTriyajGiris( cell.getDateCellValue());
			 }
			 if (cell.getColumnIndex() == 10  && (!isCellEmpty (cell))) //triyaja çıkış
			 {	 	
				 System.out.println("Column index:" + cell.getColumnIndex() );
				 rowContext.setTriyajCikis ( cell.getDateCellValue());
			 }
			 if (cell.getColumnIndex() == 11  && (!isCellEmpty (cell))) // muayene
			 {	 	
				 System.out.println("Column index:" + cell.getColumnIndex() );
				 rowContext.setMuayene ( cell.getDateCellValue());
			 }
			 if (cell.getColumnIndex() == 12  && (!isCellEmpty (cell))) // musahede giris
			 {	 	
				 System.out.println("Column index:" + cell.getColumnIndex() );
				 rowContext.setMusahedeGiris ( cell.getDateCellValue());
			 }
			 if (cell.getColumnIndex() == 13  && (!isCellEmpty (cell))) // musahede çıkış
			 {	 	
				 System.out.println("Column index:" + cell.getColumnIndex() );
				 rowContext.setMusahedeCikis ( cell.getDateCellValue());
			 }
			
			 if (cell.getColumnIndex() == 14  && (!isCellEmpty (cell))) // acile çıkış
			 {	 	
				 System.out.println("Column index:" + cell.getColumnIndex() );
				 rowContext.setAcilCikis ( cell.getDateCellValue());
			 }
			
			 if (cell.getColumnIndex() == 15  ) // test var mı?
				 rowContext.setTest( cell.getStringCellValue());
			
			 if (cell.getColumnIndex() == 16  ) // kons
				 rowContext.setKons( cell.getStringCellValue());	
			 if (cell.getColumnIndex() == 17  ) // ex
				 rowContext.setEx( cell.getStringCellValue());	
			 
			 if (cell.getColumnIndex() == 18  ) // tanı 
				 rowContext.setTani( cell.getStringCellValue());
			 
			 if (cell.getColumnIndex() == 19  ) // sevk edilen bolum
				 rowContext.setSevkBolum( cell.getStringCellValue());
			 
			 if (cell.getColumnIndex() == 20  ) // başvuru doktor
				 rowContext.setDoctor( cell.getStringCellValue());
			
			
			
			
			 // set department and service 
			 rowContext.setDepartment("Acil");
			 rowContext.setService("Acil Servis");
		 }
		return rowContext;
	}
	private RowContext readRow(Iterator<Cell> cellIterator) {
		 RowContext rowContext = RowContext.builder().build();;
			
		 while (cellIterator.hasNext()) 
		 {
				
			 Cell cell = cellIterator.next();
			 if (cell.getRowIndex() >= 3116)
			 {
				 return null;
			 }
			 System.out.println( "RowIndex:" + cell.getRowIndex() );
			 if (cell.getColumnIndex() == 0 )
				 rowContext.setEventId(  Integer.parseInt(cell.getStringCellValue() ));
			 if (cell.getColumnIndex() == 1 )
				 rowContext.setPatientId((int) cell.getNumericCellValue()) ;
			 if (cell.getColumnIndex() == 2 )
				 rowContext.setSurgeryNo((int) cell.getNumericCellValue()) ;
			 if (cell.getColumnIndex() == 3 )
				 rowContext.setAge((int) cell.getNumericCellValue()) ;
			 if (cell.getColumnIndex() == 4 )
				 rowContext.setGender(cell.getStringCellValue());
			 if (cell.getColumnIndex() == 5 )
				 rowContext.setSurgeryCategory (cell.getStringCellValue());
			 if (cell.getColumnIndex() == 6)//admission date
			 {
				 System.out.println("Column index:" + cell.getColumnIndex()  );
				 if (!isCellEmpty (cell))
					 rowContext.setAdmissionDate (cell.getDateCellValue());
			 }
			 if (cell.getColumnIndex() == 7 ) //department
				 rowContext.setDepartment(cell.getStringCellValue());
			 if (cell.getColumnIndex() == 8 ) //service
				 rowContext.setService(cell.getStringCellValue());
			 if (cell.getColumnIndex() == 9 ) //doctor
				 rowContext.setDoctor(cell.getStringCellValue());
			 if (cell.getColumnIndex() == 10 )
				 rowContext.setServisDegisiklik(cell.getStringCellValue());
			 if (cell.getColumnIndex() == 11 && (!isCellEmpty (cell)))// servise değişikliği tarihi
			 {
				 System.out.println("Column index:" + cell.getColumnIndex() );
				 rowContext.setServisDegisiklikTar( cell.getDateCellValue());
			 }
			 if (cell.getColumnIndex() == 12 ) // bolum değişiklik
				 rowContext.setBolumDegisiklik(cell.getStringCellValue());
			 if (cell.getColumnIndex() == 13 && (!isCellEmpty (cell)))
			 {	
				 System.out.println("Column index:" + cell.getColumnIndex() );
				 rowContext.setBolumDegisiklikTar( cell.getDateCellValue());
			 }
			 if (cell.getColumnIndex() == 14 )
				 rowContext.setService2(cell.getStringCellValue());
			 if (cell.getColumnIndex() == 15 )
				 rowContext.setDepartment2(cell.getStringCellValue());
			 if (cell.getColumnIndex() == 16 )
				 rowContext.setDoctor2(cell.getStringCellValue());
			 if (cell.getColumnIndex() == 17 )
				 rowContext.setOperatingRoom(cell.getStringCellValue());
			 if (cell.getColumnIndex() == 18 && (!isCellEmpty (cell))) // surgery start
			 {
				 System.out.println("Column index:" + cell.getColumnIndex() );
				 rowContext.setSurgeryStartDate( cell.getDateCellValue());
			 }
			 if (cell.getColumnIndex() == 19 && (!isCellEmpty (cell))) // surgery finish
			 {	 
				 System.out.println("Column index:" + cell.getColumnIndex() );
				 rowContext.setSurgeryFinishDate( cell.getDateCellValue());
			 }
			 if (cell.getColumnIndex() == 20 )
				 rowContext.setSurgeryName(cell.getStringCellValue());
			 if (cell.getColumnIndex() == 21 )
				 rowContext.setSurgeryDoctor(cell.getStringCellValue());
			 if (cell.getColumnIndex() == 22 && (!isCellEmpty (cell)) )
			 {	  
				 System.out.println("Column index:" + cell.getColumnIndex());
				 rowContext.setServiseCikisTar( cell.getDateCellValue());
			 }
			 if (cell.getColumnIndex() == 23 && (!isCellEmpty (cell)))
			 {
				 System.out.println("Column index:" + cell.getColumnIndex() );
				 rowContext.setOlumDate(cell.getDateCellValue());
			 }
			 if (cell.getColumnIndex() == 24 && (!isCellEmpty (cell))	)
					
			 {
				 System.out.println("Column index:" + cell.getColumnIndex() );
				 rowContext.setTaburcuDate(cell.getDateCellValue());
			 }
			 if (cell.getColumnIndex() == 25)
					rowContext.setKesintanı(cell.getStringCellValue());
			    
		 }
		return rowContext;
	}
	
	@GetMapping("/patterns")
	public ResponseEntity<?> getPatterns() throws SQLException {
		commonService.addPatterns();
		return ResponseEntity.ok(true);
	}
	
	
	@GetMapping("/test")
	public ResponseEntity<?> test() {
		writeEventsCsv();	
		return ResponseEntity.ok(true);
	}
	
	
	@GetMapping("/testEventsFile")
	public ResponseEntity<?> testMap() {
		 PrintWriter pw;
		 Integer my=4;
		try {
			pw = new PrintWriter(new File("events-ALL.csv"));
			String header = "Patient ID"+";" + "Age" + ";"+ "Gender" +";" +"Surgery No" +";"+ 
			"Surgery Category" + ";" + "Surgery Name" +";"+"Activity" +";"+"Activity Start Date" +";"+
					"Activity Finish Date" +";"+"Department" +";"+"Service" +";"+"Doctor"; 
			pw.write(header + '\n');
			for (Iterator<EventRow> iter = commonService.getEventRows().listIterator(); iter.hasNext();)
			{	
				EventRow e = iter.next();
				pw.write(e.toString());
			}
			 pw.close();
	        System.out.println("done!");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
		return ResponseEntity.ok(true);
	}
}
