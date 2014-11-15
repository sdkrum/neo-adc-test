package no.uninett.adc.neo.dao;

import java.util.Set;

import no.uninett.adc.neo.domain.EduOrg;
import no.uninett.adc.neo.domain.EduPerson;
import no.uninett.adc.neo.domain.RelationshipType;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

public interface EduPersonRepository extends GraphRepository<EduPerson> {
	EduPerson findByFeideId(String feideId);

	// @Query("match (group:g{name:{0}})-[:persons]->(aMember) return aMember")
	// Set<Person> getTeamMembersAsSetViaQuery(String groupName);

	// @Query("match (boss)-[:boss]->(person) where id(person) = {p_person} return boss")
	// Person findBoss(@Param("p_person") Person person);

	// @Query("MATCH (user:User {name:{name}})-[:Loves]->(car) return car limit 1")
	// public Car getSingleCar(@Param("name") String name);

	@Query("MATCH (person:EduPerson{feideId:{0}}) RETURN person")
	EduPerson findFeide1(String feideId);

	@Query("MATCH (person:EduPerson{feideId:{FEIDE_ID}}) RETURN person")
	EduPerson findFeide(@Param("FEIDE_ID") String feideId);

	@Query("MATCH (ent:Entitlement{entitlement:{0}})<-[:"
			+ RelationshipType.IS_ENTITLET + "]-(person) return person")
	Set<EduPerson> findPersonWithEntitlement(String entitlement);

	//@Query("MATCH (ent:Entitlement{entitlement:{0}})<-[:ENTITLED]-(person)-[:WORKS_FOR_ORG]->(org:EduOrg{1}) return person")
	@Query("START org=node({1}) MATCH org<-[:" + RelationshipType.WORKS_FOR_ORG
			+ "]-person-[:" + RelationshipType.IS_ENTITLET
			+ "]->(ent:Entitlement{entitlement:{0}}) return person")
	Set<EduPerson> findByEntitlementAndOrg(String entitlement, EduOrg org);

	@Query("MATCH (org:EduOrg{orgNIN:{1}})<-[:"
			+ RelationshipType.WORKS_FOR_ORG + "]-person-[:"
			+ RelationshipType.IS_ENTITLET
			+ "]->(ent:Entitlement{entitlement:{0}}) return person")
	Set<EduPerson> findByEntitlementAndOrg(String entitlement, String orgNIN);
}
