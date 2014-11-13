package no.uninett.adc.neo.dao;

import no.uninett.adc.neo.domain.ChangeSet;
import no.uninett.adc.neo.domain.EduOrg;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeSetRepository extends GraphRepository<ChangeSet> {

	@Query(value = "START org=node({0}) MATCH (org)<-[:WORKS_FOR_ORG]-(person)"
			+ "<-[:CHANGE_OF_OBJECT]-(cSet)-[:CHANGE_STATE]->(state){stateCode:2} return cSet")
	Iterable<ChangeSet> getFailed(EduOrg org);
}
