package tugba.mining.domain;

import java.util.Date;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import tugba.mining.domain.Patient.PatientBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
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
	/*//@ManyToOne
	@Relationship(type = "ACTIVITY", direction = Relationship.UNDIRECTED)
	private Activity activity;*/
	
	private String activity;
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
	
	
		
	
	
}
