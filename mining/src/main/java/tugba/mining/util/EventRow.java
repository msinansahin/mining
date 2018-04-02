package tugba.mining.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.neo4j.ogm.annotation.NodeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tugba.mining.util.RowContext.RowContextBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@NodeEntity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRow {

	private Integer eventId;
	private Integer patientId; 
	private Integer surgeryNo; 
	private Integer age; 
	private String gender; 
	private String surgeryCategory;
	private String activity; 
	private Date activityDate; 	
	private String department;
	private String service; 
	private String doctor; 
	private String operatingRoom;
	private String surgeryName; 
	private String kesintanı;
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
        sb.append(this.getPatientId());
        sb.append(';');
        sb.append(this.getAge());
        sb.append(';');
        sb.append(this.getGender());
        sb.append(';');
        sb.append(this.getSurgeryNo());
        sb.append(';');
        sb.append(this.getSurgeryCategory());
        sb.append(';');
        sb.append(this.getSurgeryName());
        sb.append(';');
        sb.append(this.getActivity());
        sb.append(';');
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        sb.append(formatter.format (this.getActivityDate()));
        sb.append(';');
        sb.append(formatter.format (this.getActivityDate()));
        sb.append(';');
        sb.append(this.getDepartment());
        sb.append(';');
        sb.append(this.getService());
        sb.append(';');
        sb.append(this.getDoctor());
        sb.append('\n');
        return(sb.toString());
	}
}
