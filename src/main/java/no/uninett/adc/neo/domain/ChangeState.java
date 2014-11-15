package no.uninett.adc.neo.domain;

import java.util.Date;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class ChangeState {
	@GraphId
	private Long id;
	@GraphProperty(propertyType = Integer.class)
	private ChangeStateCode stateCode;
	private String text;

	private Date dateUpdated;
	private int errorCount;
	private SystemURN urn;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public ChangeStateCode getStateCode() {
		return stateCode;
	}

	public void setStateCode(final ChangeStateCode stateCode) {
		this.stateCode = stateCode;
	}

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public SystemURN getSystemUrn() {
		return urn;
	}

	public void setSystemUrn(final SystemURN systemUrn) {
		this.urn = systemUrn;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(final Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public int getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(final int errorCount) {
		this.errorCount = errorCount;
	}

	@Override
	public String toString() {
		return "ChangeState for urn: " + urn.getUrn() + ": "
				+ stateCode.toString();
	}

}
