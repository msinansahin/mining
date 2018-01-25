package tugba.mining.util;

import java.util.Date;

import org.neo4j.ogm.annotation.NodeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NodeEntity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RowContext {

	private Integer eventId;
	private Integer patientId; 
	private Integer surgeryNo; 
	private Integer age; 
	private String gender; 
	private String activitySurgeryOrder; 
	private Date surgeryOrderDate; 
	private String surgeryCategory;
	private String activityAdmission; 
	private Date admissionDate; 
	private String department;
	private String service; 
	private String doctor; 
	private String servisDegisiklik; 
	private Date servisDegisiklikTar;
	private String bolumDegisiklik; 
	private Date bolumDegisiklikTar;
	private String service2; 
	private String department2; 
	private String doctor2;
	private String operatingRoom;
	private Date surgeryStartDate; 
	private Date surgeryFinishDate; 
	private String surgeryName; 
	private String surgeryDoctor;
	private String activityServiseCikis;  
	private Date serviseCikisTar ; 
	private String activityOlum;
	private Date olumDate; 
	private String activityTaburcu; 
	private Date taburcuDate; 
	private String kesintanı;
}
