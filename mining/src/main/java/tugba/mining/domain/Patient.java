package tugba.mining.domain;

import org.neo4j.ogm.annotation.NodeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NodeEntity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient extends BaseEntity {

	private Integer patientId;
	private Integer age;
	private String gender;

}
