/**
 * 
 */
package no.uninett.adc.neo.dao;

import no.uninett.adc.neo.domain.EduOrg;
import no.uninett.adc.neo.domain.EduPerson;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 * @author soren
 *
 */
@Repository
public interface EduOrgRepository extends GraphRepository<EduOrg> {
	@Query(value = "start org=node({0}) match org<-[:WORKS_FOR_ORG]-person return person")
	Iterable<EduPerson> getEmployees(EduOrg org);
}
