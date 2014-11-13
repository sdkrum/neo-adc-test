/**
 *
 */
package no.uninett.adc.neo.dao;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.AssertionFailedError;
import no.uninett.adc.neo.domain.Attribute;
import no.uninett.adc.neo.domain.EduOrg;
import no.uninett.adc.neo.domain.EduPerson;
import no.uninett.adc.neo.domain.Entitlement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author soren
 *
 */
@ContextConfiguration(classes = { AppConfigTest.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class RepositoryTest {

	static final String SORENK_UNINETT_NO = "sorenk@uninett.no";
	private static final String ARMAZ_UNINETT_NO = "armaz@uninett.no";
	private static final String RUNEMY_UNINETT_NO = "runemy@uninett.no";
	@Autowired
	private Neo4jTemplate template;

	@Autowired
	private EduPersonRepository repo;
	@Autowired
	private EduOrgRepository orgRepo;

	@Test
	public void initialized() {
		if (template == null) {
			throw new AssertionFailedError("Template not initialized!");
		}
		if (repo == null) {
			throw new AssertionFailedError("repository not initialized!");
		}
	}

	@Test
	@Transactional
	public void thereShouldBeJustOneEntitlement() {
		final EduPerson p1 = getSoren();
		Entitlement ent = new Entitlement("agresso");
		p1.add(ent);
		final EduPerson soren = repo.save(p1);
		final EduPerson p2 = new EduPerson(ARMAZ_UNINETT_NO);
		ent = new Entitlement("agresso");
		p2.add(ent);
		final EduPerson armaz = repo.save(p2);

		final EduPerson r1 = repo.findOne(soren.getId());
		final EduPerson r2 = repo.findOne(armaz.getId());

		final Entitlement rEnt1 = r1.getEntitlements().iterator().next();
		final Entitlement rEnt2 = r2.getEntitlements().iterator().next();

		assertEquals("The entitlement should be equal!", ent, rEnt1);
		assertEquals("The entitlement should be equal!", ent, rEnt2);
		assertEquals("The entitlement id should be equal!", rEnt1.getId(), rEnt2.getId());
	}

	@Test
	@Transactional
	public void thereShouldBeAPersonWithGivenEntitlement() {
		final EduPerson p1 = getSoren();
		final Entitlement ent = new Entitlement("agresso");
		p1.add(ent);
		final EduPerson soren = repo.save(p1);
		Set<EduPerson> ps = repo.findPersonWithEntitlement("agresso");
		assertEquals("there shall be a person in the collection ", 1, ps.size());
		final EduPerson read = ps.iterator().next();
		assertEquals(soren, read);
		final EduPerson p2 = new EduPerson(ARMAZ_UNINETT_NO);
		p2.add(ent);
		final EduPerson armaz = repo.save(p2);
		ps = repo.findPersonWithEntitlement("agresso");
		assertEquals("there shall be two person in the collection ", 2, ps.size());
		final Set<EduPerson> orig = new HashSet<EduPerson>();
		orig.add(soren);
		orig.add(armaz);
		assertEquals(orig, ps);
	}

	@Test
	@Transactional
	public void thereShouldBeOnlyPersonWithEntitlementAndOrg() {
		EduOrg uni = getUninettOrg();
		uni = orgRepo.save(uni);
		final EduPerson p1 = getSoren();
		final Entitlement ent = new Entitlement("agresso");
		p1.add(ent);
		p1.setOrg(uni);
		final EduPerson soren = repo.save(p1);
		final EduPerson p2 = new EduPerson(ARMAZ_UNINETT_NO);

		p2.add(ent);
		final EduPerson armaz = repo.save(p2);
		final EduPerson p3 = new EduPerson(RUNEMY_UNINETT_NO);
		p3.setOrg(uni);
		Set<EduPerson> ps = repo.findByEntitlementAndOrg("agresso", uni);
		assertEquals("there shall be one person in the collection ", 1, ps.size());
		Set<EduPerson> orig = new HashSet<EduPerson>();
		orig.add(soren);
		assertEquals(orig, ps);
		ps = repo.findByEntitlementAndOrg("agresso", uni.getOrgNIN());
		assertEquals("there shall be one person in the collection ", 1, ps.size());
		orig = new HashSet<EduPerson>();
		orig.add(soren);
		assertEquals(orig, ps);
	}

	@Test
	@Transactional
	public void persistedPersShouldBeRetrievableById() {
		final EduPerson p = getSoren();
		final EduPerson soren = repo.save(p);
		final EduPerson retrievedP = repo.findOne(soren.getId());
		assertEquals("retrieved org matches persisted one", soren, retrievedP);
	}

	@Test
	@Transactional
	public void persistedPersShouldBeRetrievableByFeideId() {
		final EduPerson p = getSoren();
		final EduPerson soren = repo.save(p);
		EduPerson retrievedP = repo.findBySchemaPropertyValue("feideId", SORENK_UNINETT_NO);
		assertEquals("retrieved org matches persisted one", soren, retrievedP);
		retrievedP = repo.findByFeideId(SORENK_UNINETT_NO);
		assertEquals("retrieved org matches persisted one", soren, retrievedP);
		retrievedP = repo.findFeide(SORENK_UNINETT_NO);
		assertEquals("retrieved org matches persisted one", soren, retrievedP);
	}

	@Test
	@Transactional
	public void persistedPersShouldKeepEntitlement() {
		final EduPerson p = getSoren();
		final Entitlement ent = new Entitlement("agresso");
		p.add(ent);
		final EduPerson soren = repo.save(p);
		final EduPerson retrievedP = repo.findOne(soren.getId());
		assertEquals("retrieved org matches persisted one", soren, retrievedP);
		final Iterator<Entitlement> it = retrievedP.getEntitlements().iterator();
		if (!it.hasNext()) {
			throw new AssertionFailedError("There should be one entitlement..");
		}
		final Entitlement readEnt = it.next();
		if (it.hasNext()) {
			throw new AssertionFailedError("There should be one entitlement..");
		}
		assertEquals("The entitlements should match", ent.getEntitlement(), readEnt.getEntitlement());
	}

	@Test
	@Transactional
	public void persistedOrgShouldBeRetrievableFromGraphDb() {
		final EduOrg org = getUninettOrg();
		final EduOrg uni = orgRepo.save(org);
		final EduOrg retrievedOrg = orgRepo.findOne(uni.getId());
		assertEquals("retrieved org matches persisted one", org, retrievedOrg);
	}

	@Test
	@Transactional
	public void orgWithPersonShallHavePerson() {
		final EduOrg org = getUninettOrg();
		final EduPerson emp = getSoren();
		emp.setOrg(org);
		org.addEmployee(emp);
		final EduOrg uni = orgRepo.save(org);
		System.err.println("EduOrg id is " + uni.getId());
		final EduOrg retrOrg = orgRepo.findOne(uni.getId());
		// the employees are sallow, only their id is filled! to really fill
		// them, iterate over the coll, or use the EduOrgRepository
		assertEquals("Expect exactely one person", 1, retrOrg.getEmployees().size());
		final Iterable<EduPerson> iterable = orgRepo.getEmployees(retrOrg);
		final Iterator<EduPerson> it = iterable.iterator();
		final EduPerson retrEmp = it.next();
		assertEquals("Person should match", emp, retrEmp);
	}

	private EduOrg getUninettOrg() {
		final EduOrg org = new EduOrg();
		org.setOrgNIN("NO211");
		return org;
	}

	private EduPerson getSoren() {
		final EduPerson p1 = new EduPerson(SORENK_UNINETT_NO);
		return p1;
	}

	@Test
	@Transactional
	public void addAndRemoveAttributesToPerson() {
		final EduPerson p1 = getSoren();
		final Attribute val = new Attribute("cn", "Søren Krum");
		p1.addAttribute(val);
		EduPerson soren = repo.save(p1);
		EduPerson read = repo.findOne(soren.getId());
		assertEquals("Person should have a attribute...", 1, read.getAttributes().size());
		final Attribute readAtt = read.getAttributes().iterator().next();
		assertEquals("The key should be the one we specified...", "cn", readAtt.getKey());
		read.removeAttibute("cn", "Søren Krum");
		soren = repo.save(read);
		read = repo.findOne(soren.getId());
		assertEquals("Person should have no attribute...", 0, read.getAttributes().size());
		read.addAttribute("cn", "Søren Krum");
		read.addAttribute("sn", "Krum");
		read.addAttribute("mail", "sorenk@uninett.no");
		soren = repo.save(read);
		read = repo.findOne(soren.getId());
		assertEquals("Person should have 3 attributes...", 3, read.getAttributes().size());

	}

}
