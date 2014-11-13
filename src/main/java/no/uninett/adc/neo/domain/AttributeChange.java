package no.uninett.adc.neo.domain;

import java.util.Date;

import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class AttributeChange extends Change {

	private Attribute attribute;

	public AttributeChange(final ChangeOperation operation, final Attribute value) {
		setChangeOperation(operation);
		setAttribute(value);
		setDateChanged(new Date());
	}

	public AttributeChange(final ChangeOperation operation, final Attribute value, final Date dateChanged,
	    final String source) {
		setChangeOperation(operation);
		setAttribute(value);
		setDateChanged(dateChanged);
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(final Attribute attribute) {
		this.attribute = attribute;
	}

	@Override
	public String toString() {
		return "AC: KEY : " + getAttribute().getKey() + ", Op : " + getChangeOperation().toString();
	}
}
