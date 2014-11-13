package no.uninett.adc.neo.domain;

import java.util.Date;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class Change {
	@GraphId
	private Long id;
	private ChangeOperation changeOperation;
	private Date dateChanged;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Date getDateChanged() {
		return dateChanged;
	}

	public void setDateChanged(final Date dateChanged) {
		this.dateChanged = dateChanged;
	}

	public ChangeOperation getChangeOperation() {
		return changeOperation;
	}

	public void setChangeOperation(final ChangeOperation changeOperation) {
		this.changeOperation = changeOperation;
	}

	public boolean is(final ChangeOperation co) {
		return getChangeOperation().equals(co);
	}

	@Override
	public String toString() {
		return getChangeOperation().toString() + getId();
	}
}
