package no.uninett.adc.neo.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Set;

import org.junit.Test;

public class EduPersonTest {

	public static final String FEIDE_ID = "a-feide-id";
	private static final String ENTITLEMENT = "a-entitlement";

	@Test
	public void noEntitlementChange() {
		EduPerson p1 = new EduPerson(FEIDE_ID);
		EduPerson p2 = new EduPerson(FEIDE_ID);
		ChangeSet cs = p1.diff(p2);
		assertNull("There should not be any change...", cs);
		p1.add(new Entitlement(ENTITLEMENT));
		p2.add(new Entitlement(ENTITLEMENT));
		cs = p1.diff(p2);
		assertNull("There should not be any change...", cs);
	}

	@Test
	public void aEntitlementRemovedAndAdded() {
		EduPerson p1 = new EduPerson(FEIDE_ID);
		EduPerson p2 = new EduPerson(FEIDE_ID);
		ChangeSet cs = p1.diff(p2);
		assertNull("There should not be any change...", cs);
		Entitlement ent = new Entitlement(ENTITLEMENT);
		p1.add(ent);
		// the remove:
		cs = p1.diff(p2);
		assertNotNull("There should be a change...", cs);
		Set<Change> changes = cs.getChanges();
		assertEquals("There should be one change", 1, changes.size());
		Change c = changes.iterator().next();
		assertEquals("It should be an entitlement change",
				EntitlementChange.class, c.getClass());
		EntitlementChange eC = (EntitlementChange) c;
		assertEquals("It should be the ent from above", ent,
				eC.getEntitlement());
		assertEquals("It should be a delete", ChangeOperation.DELETE,
				eC.getChangeOperation());
		// the add
		cs = p2.diff(p1);
		assertNotNull("There should be a change...", cs);
		changes = cs.getChanges();
		assertEquals("There should be one change", 1, changes.size());
		c = changes.iterator().next();
		assertEquals("It should be an entitlement change",
				EntitlementChange.class, c.getClass());
		eC = (EntitlementChange) c;
		assertEquals("It should be the ent from above", ent,
				eC.getEntitlement());
		assertEquals("It should be a Create", ChangeOperation.CREATE,
				eC.getChangeOperation());

	}

}
