package tugba.mining.domain;


import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

//@Entity
@Data
//@ToString(exclude={"events", "patients"})
//@EqualsAndHashCode(callSuper = false, exclude={"events", "patients"})
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NodeEntity
public class Activity extends BaseEntity {

	private String activityName;
	private Integer activityId;
	private Integer eventCount;
	private Integer patientCount;
	// diğerlerini ekle
	
	
}
