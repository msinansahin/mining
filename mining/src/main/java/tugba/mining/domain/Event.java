package tugba.mining.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import tugba.mining.domain.Patient.PatientBuilder;

@Data
@ToString(exclude={"activity"})
@EqualsAndHashCode(callSuper = true, exclude={"activity"})
//@Entity
@NodeEntity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event extends BaseEntity {

	private Integer eventId;
	//@ManyToOne
	@Relationship(type = "PATIENT", direction = Relationship.UNDIRECTED)
	private Patient patient;
	//@ManyToOne
	@JsonIgnore
	@Relationship(type = "ACTIVITY", direction = Relationship.UNDIRECTED)
	private Activity activity;
	private Date startDate;
	private Date finishDate;
	private String department;
	private String service;
	//@ManyToOne
	@Relationship(type = "DOCTOR", direction = Relationship.UNDIRECTED)
	private Doctor doctor;
	
	//@ManyToOne
	@Relationship(type = "SURGERY", direction = Relationship.UNDIRECTED)
	private Surgery surgery;
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
        sb.append(this.getPatient().getPatientId());
        sb.append(';');
        sb.append(this.getPatient().getAge());
        sb.append(';');
        sb.append(this.getPatient().getGender());
        sb.append(';');
        sb.append(this.getSurgery().getSurgeryNo());
        sb.append(';');
        sb.append(this.getSurgery().getSurgeryCategory());
        sb.append(';');
        sb.append(this.getSurgery().getSurgeryName());
        sb.append(';');
        sb.append(this.getActivity().getActivityName());
        sb.append(';');
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      //  formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        sb.append(formatter.format (this.getStartDate()));
        sb.append(';');
        sb.append(formatter.format (this.getStartDate()));
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
