package tugba.mining.domain;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
//@Entity
@NodeEntity
@NoArgsConstructor
@AllArgsConstructor
public class Event extends BaseEntity {

	private String doctor;
	
	//@ManyToOne
	@Relationship(type = "PATIENT", direction = Relationship.UNDIRECTED)
	private Patient patient;
	
	//@ManyToOne
	@Relationship(type = "ACTIVITY", direction = Relationship.UNDIRECTED)
	private Activity activity;
}
