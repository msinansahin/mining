package tugba.mining.domain;

import org.neo4j.ogm.annotation.NodeEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NodeEntity
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends BaseEntity {

	private String patientClass;
	private Integer age;
	private String sex;

}
