package tugba.mining.domain;


import org.neo4j.ogm.annotation.NodeEntity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Builder;

//@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NodeEntity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor extends BaseEntity {
	private String name;
}
