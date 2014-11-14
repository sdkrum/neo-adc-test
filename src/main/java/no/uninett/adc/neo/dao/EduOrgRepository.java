/**
 *
 */
package no.uninett.adc.neo.dao;

import no.uninett.adc.neo.domain.Attribute;
import no.uninett.adc.neo.domain.EduOrg;
import no.uninett.adc.neo.domain.EduPerson;
import no.uninett.adc.neo.domain.OrgSystem;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

/**
 * @author soren
 *
 */
@Repository
public interface EduOrgRepository extends GraphRepository<EduOrg> {
	@Query(value = "START org=node({0}) MATCH org<-[:WORKS_FOR_ORG]-person return person")
	Iterable<EduPerson> getEmployees(EduOrg org);

	@Query(value = "START org=node({0}) MATCH org<-[:SYSTEM_FOR_ORG]-sys return sys")
	Iterable<OrgSystem> getAllSystems(EduOrg org);

	@Query(value = "START org=node({0}) MATCH org<-[:SYSTEM_FOR_ORG]-(sys:OrgSystem{selected:true}) return sys")
	Iterable<OrgSystem> getSelectedSystems(EduOrg org);

	/**
	 * Returns the systems which are selected and have a default flag as specified
	 *
	 * @param org
	 * @param defaul
	 * @return
	 */
	@Query(value = "START org=node({0}) MATCH org<-[:SYSTEM_FOR_ORG]-(sys:OrgSystem{defaul:{1}, selected:true}) return sys")
	Iterable<OrgSystem> getSelectedSystemsDefaultFlag(EduOrg org, boolean defaul);

	/**
	 * Searches for persons in the given organization having the specified
	 * attribute (key and value!) Both parameters should be fetched from db,
	 * otherwise you will get strange results....
	 *
	 * @param org
	 *          the organization to scan
	 * @param att
	 *          the attribute.
	 * @return
	 */
	@Query("START org=node({0}), att=node({1}) MATCH org<-[:WORKS_FOR_ORG]-person-[:HAS_ATTRIBUTE]->att return person")
	Iterable<EduPerson> getPersonWithAttribute(EduOrg org, Attribute att);

	@Query("START att=node({1}) MATCH (org:EduOrg{orgNIN:{0}})<-[:WORKS_FOR_ORG]-person-[:HAS_ATTRIBUTE]->att return person")
	Iterable<EduPerson> getPersonWithAttribute(String orgNIN, Attribute att);

	@Query("MATCH (org:EduOrg{orgNIN:{1}})<-[:WORKS_FOR_ORG]-person-[:HAS_ATTRIBUTE]->att-[:HAS_KEY]->(aKey:AttributeKey{name:{1}}) return person")
	Iterable<EduPerson> getPersonWithAttributeKey(String orgNIN, String key);

}
