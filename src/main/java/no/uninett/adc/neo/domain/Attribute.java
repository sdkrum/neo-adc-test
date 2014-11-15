package no.uninett.adc.neo.domain;

import no.uninett.adc.util.Hasher;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class Attribute {


	@GraphId
	private Long id;

	@RelatedTo(type = RelationshipType.HAS_ATTRIBUTE, direction = Direction.INCOMING)
	private EduObject object;

	@RelatedTo(type = RelationshipType.HAS_KEY, direction = Direction.OUTGOING)
	@Fetch
	private AttributeKey key;

	private String value;

	private boolean hashed;

	/**
	 * this one is for spring only!
	 */
	@Deprecated
	public Attribute() {

	}

	public Attribute(final String key, final String value) {
		this.key = new AttributeKey(key);
		this.value = value;
	}

	public void hashIt() {
		if (!isHashed()) {
			setValue(value, true);
		}
	}

	public String getValueHashed() {
		if (hashed) {
			return value;
		}
		return hash(value);
	}

	public String getValue() {
		return value;
	}

	private void setValue(final String value, final boolean hashIt) {
		if (hashIt) {
			try {
				this.value = hash(value);
				hashed = true;
			} catch (final Exception e) {
				this.value = value;
			}
		} else {
			hashed = false;
			this.value = value;
		}
	}

	public boolean isHashed() {
		return hashed;
	}

	private String hash(final String in) {
		return Hasher.hash(in);
	}

	public boolean valueEquals(final Attribute comp) {
		boolean equals = false;
		if (this.isHashed() || comp.isHashed()) {
			equals = getValueHashed().equals(comp.getValueHashed());
		} else {
			equals = getValue().equals(comp.getValue());
		}
		return equals;
	}

	public boolean valueEquals(final String comp) {
		boolean equals = false;
		if (this.isHashed()) {
			equals = getValue().equals(hash(comp));
		} else {
			equals = getValue().equals(comp);
		}
		return equals;
	}

	public EduObject getObject() {
		return object;
	}

	public void setObject(final EduObject object) {
		this.object = object;
	}

	public String getKey() {
		return key.getName();
	}

	public void setKey(final String key) {
		this.key = new AttributeKey(key);
	}

	@Override
	public String toString() {
		return "Attribute [id=" + id + ", key=" + key + ", value=" + value + "]";
	}

}
