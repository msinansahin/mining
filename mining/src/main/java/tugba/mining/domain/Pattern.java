package tugba.mining.domain;


import org.neo4j.ogm.annotation.NodeEntity;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
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
public class Pattern extends BaseEntity {

	private String activityName;
	
	// diğerlerini ekle
	
	
}
