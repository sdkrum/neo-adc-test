package no.uninett.adc.neo.domain;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class AttributeKey {

	@GraphId
	private Long id;

	@Indexed(unique = true)
	private String name;

	@Deprecated
	public AttributeKey() {

	}

	public AttributeKey(final String key) {
		name = key;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
