package tugba.mining.domain;


import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;

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
public class Activity extends BaseEntity {

	private String activityName;
	private Integer activityId;
	private List<Patient> patients;
	private List<Event> events;

	// diğerlerini ekle
	
	
}
