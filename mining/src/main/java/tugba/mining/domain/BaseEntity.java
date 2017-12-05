package tugba.mining.domain;


import org.neo4j.ogm.annotation.GraphId;

public abstract class BaseEntity {

    //@Id
    //@GeneratedValue
	@GraphId
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
