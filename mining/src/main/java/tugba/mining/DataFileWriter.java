package tugba.mining;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import tugba.mining.domain.Event;
import tugba.mining.repositories.EventRepository;


public class DataFileWriter {
	@Autowired
	EventRepository er;
	
	public DataFileWriter() throws IOException {
		FileOutputStream excelFile = new FileOutputStream(new File("events.xls"));
        Workbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = (HSSFSheet) workbook.createSheet("ALL");
        List<Event> events = (List<Event>) er.findAll( new Sort ("eventId")).iterator();
        Iterator iterator = events.iterator();
       
        
        
        int cell=0;
        int eventnumber=0;
        while(iterator.hasNext()) {
      	
        	Event e = (Event)iterator.next();
        	
        	HSSFRow rowEvent = sheet.createRow((short) cell );
        	HSSFCell cellA1 = rowEvent.createCell((short) 0);
        	cellA1.setCellValue(e.getEventId());
 
        	HSSFCell cellA2 = rowEvent.createCell((short) 1);
        	cellA2.setCellValue(e.getPatient().getPatientId());
        	
        	HSSFCell cellA3 = rowEvent.createCell((short) 2);
        	cellA3.setCellValue(e.getPatient().getAge());
        
        	HSSFCell cellA4 = rowEvent.createCell((short) 3);
        	cellA4.setCellValue(e.getPatient().getGender());
        	
        	HSSFCell cellA5 = rowEvent.createCell((short) 4);
        	cellA5.setCellValue(e.getSurgery().getSurgeryCategory());
        
        	HSSFCell cellA6 = rowEvent.createCell((short) 5);
        	cellA6.setCellValue(e.getSurgery().getSurgeryName());
        
        	HSSFCell cellA7 = rowEvent.createCell((short) 6);
        	cellA7.setCellValue(e.getActivity().getActivityName());
        	
        	HSSFCell cellA8 = rowEvent.createCell((short) 7); 
        	cellA8.setCellValue(e.getStartDate());
        	
        	HSSFCell cellA9 = rowEvent.createCell((short) 8);
        	cellA9.setCellValue(e.getFinishDate());
	        
        	HSSFCell cellA10 = rowEvent.createCell((short) 9);
        	cellA10.setCellValue(e.getDepartment());
        	
        	HSSFCell cellA11 = rowEvent.createCell((short) 10);
        	cellA11.setCellValue(e.getService() );
        	
        	HSSFCell cellA12 = rowEvent.createCell((short) 11);
        	cellA12.setCellValue(e.getDoctor().getName());
        	cell++;
        	eventnumber++;
        }
        
        
         
         
        try (FileOutputStream outputStream = new FileOutputStream(new File("events.xls"))) {
            workbook.write(outputStream);
        }
    }
}
