package no.uninett.adc.neo.domain;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class OrgSystem {


	@GraphId
	private Long id;

	@RelatedTo(type = RelationshipType.SYSTEM_FOR_ORG, direction = Direction.OUTGOING)
	private EduOrg org;

	@RelatedTo(type = RelationshipType.SYSTEM_FOR_ENTITLEMENT, direction = Direction.OUTGOING)
	private Entitlement entitlement;

	@RelatedTo(type = RelationshipType.SYSTEM_URN, direction = Direction.OUTGOING)
	private SystemURN urn;

	private boolean selected;

	private boolean defaul;

	public EduOrg getOrg() {
		return org;
	}

	public void setOrg(final EduOrg org) {
		this.org = org;
	}

	public Entitlement getEntitlement() {
		return entitlement;
	}

	public void setEntitlement(final Entitlement entitlement) {
		this.entitlement = entitlement;
	}

	public SystemURN getUrn() {
		return urn;
	}

	public void setUrn(final SystemURN urn) {
		this.urn = urn;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(final boolean selected) {
		this.selected = selected;
	}

	public boolean isDefaul() {
		return defaul;
	}

	public void setDefaul(final boolean defaul) {
		this.defaul = defaul;
	}

}
