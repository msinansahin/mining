package tugba.mining.domain;

import javax.persistence.Entity;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Activity extends BaseEntity {

	private String activityName;
	
	// diğerlerini ekle
	
	
}
