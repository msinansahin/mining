package tugba.mining.domain;


import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class Path extends BaseEntity {

	private Integer pathId;
//	@Relationship(type = "ACTIVITY", direction = Relationship.INCOMING)
	private String from;
	
//	@Relationship(type = "ACTIVITY", direction = Relationship.OUTGOING)
	private String to;
	
	private Integer eventCount;
	private Integer patientCount;
	
	private List <Integer> events;
	private List <Integer> patients;
	private double totalDuration;
	private double meanDuration;
	
	
	
	
	// diğerlerini ekle
	
	
}
