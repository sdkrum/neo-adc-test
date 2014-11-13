package no.uninett.adc.neo.blindvei;

import no.uninett.adc.neo.domain.Attribute;
import no.uninett.adc.neo.domain.EduObject;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.RelationshipType;
import org.springframework.data.neo4j.annotation.StartNode;

// NOT IN USE!

@RelationshipEntity
public class AttributeKey {
	@GraphId
	private Long id;
	@RelationshipType
	private String keyName;

	@StartNode
	private EduObject object;
	@EndNode
	private Attribute attribute;

	@Deprecated
	public AttributeKey() {

	}

	public AttributeKey(final EduObject obj, final Attribute val, final String key) {
		object = obj;
		attribute = val;
		keyName = key;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(final String keyName) {
		this.keyName = keyName;
	}

	public EduObject getObject() {
		return object;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setObject(final EduObject object) {
		this.object = object;
	}

	public void setAttribute(final Attribute attribute) {
		this.attribute = attribute;
	}

}
