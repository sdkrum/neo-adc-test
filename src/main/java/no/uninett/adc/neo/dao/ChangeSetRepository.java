package no.uninett.adc.neo.dao;

import no.uninett.adc.neo.domain.ChangeSet;
import no.uninett.adc.neo.domain.EduOrg;
import no.uninett.adc.neo.domain.RelationshipType;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeSetRepository extends GraphRepository<ChangeSet>,
		CustomChangeSetRepository {

	@Query(value = "START org=node({0}) MATCH (org)<-[:"
			+ RelationshipType.WORKS_FOR_ORG + "]-(person)<-[:"
			+ RelationshipType.CHANGESET_FOR_OBJECT + "]-(cSet)-[:"
			+ RelationshipType.HAS_STATE
			+ "]->(state{stateCode:2}) return cSet")
	Iterable<ChangeSet> getFailed(EduOrg org);

}
