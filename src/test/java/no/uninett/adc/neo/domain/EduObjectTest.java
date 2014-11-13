package no.uninett.adc.neo.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

//@RunWith(SpringJUnit4ClassRunner.class)
public class EduObjectTest {

	private static final String FEIDE_ID = "FEIDE-ID";

	@Test
	public void findDelete() {
		EduPerson p1 = new EduPerson(FEIDE_ID);
		ChangeSet cs = EduObject.diff(p1, null);
		assertNotNull("There shall be a change set", cs);
		assertEquals("The changeOperation shall be delete",
				ChangeOperation.DELETE, cs.getOperation());
		assertFalse(p1.isActive());
		p1.setActive(true);
		EduPerson p2 = new EduPerson(FEIDE_ID);
		p2.setActive(false);
		cs = EduObject.diff(p1, p2);
		assertNotNull("There shall be a change set", cs);
		assertEquals("The changeOperation shall be delete",
				ChangeOperation.DELETE, cs.getOperation());
	}

	@Test
	public void findCreate() {
		EduPerson p1 = new EduPerson(FEIDE_ID);
		ChangeSet cs = EduObject.diff(null, p1);
		assertEquals("The changeOperation shall be create",
				ChangeOperation.CREATE, cs.getOperation());
		EduPerson p2 = new EduPerson(FEIDE_ID);
		p2.setActive(false);
		cs = EduObject.diff(p2, p1);
		assertEquals("The changeOperation shall be create",
				ChangeOperation.CREATE, cs.getOperation());
	}

	@Test
	public void findEmptyUpdate() {
		EduPerson p1 = new EduPerson(FEIDE_ID);
		ChangeSet cs = EduObject.diff(p1, p1);
		assertNull("There shall not be a change set", cs);
		Attribute att = new Attribute("key", "value");
		p1.addAttribute(att);
		cs = EduObject.diff(p1, p1);
		assertNull("There shall not be a change set", cs);
	}

	@Test
	public void findUpdateWithOneChange() {
		EduPerson p1 = new EduPerson(FEIDE_ID);
		Attribute att = new Attribute("key", "value");
		p1.addAttribute(att);
		EduPerson p2 = new EduPerson(FEIDE_ID);
		ChangeSet cs = EduObject.diff(p2, p1);
		assertNotNull("There shall be a change set", cs);
		assertEquals("The operation on the set does not fit:",
				ChangeOperation.UPDATE, cs.getOperation());
		assertEquals("There should be one change", 1, cs.getChanges().size());
		Change c = cs.getChanges().iterator().next();
		assertEquals("The operation on the change does not fit",
				ChangeOperation.CREATE, c.getChangeOperation());
		cs = EduObject.diff(p1, p2);
		assertNotNull("There shall be a change set", cs);
		assertEquals("The operation on the set does not fit:",
				ChangeOperation.UPDATE, cs.getOperation());
		assertEquals("There should be one change", 1, cs.getChanges().size());
		c = cs.getChanges().iterator().next();
		assertEquals("The operation on the change does not fit",
				ChangeOperation.DELETE, c.getChangeOperation());
		Attribute att2 = new Attribute("key", "anotherValue");
		p2.addAttribute(att2);
		cs = EduObject.diff(p1, p2);
		assertNotNull("There shall be a change set", cs);
		assertEquals("The operation on the set does not fit:",
				ChangeOperation.UPDATE, cs.getOperation());
		assertEquals("There should be one change", 1, cs.getChanges().size());
		c = cs.getChanges().iterator().next();
		assertEquals("The operation on the change does not fit",
				ChangeOperation.UPDATE, c.getChangeOperation());
		assertEquals("anotherValue", ((AttributeChange) c).getAttribute()
				.getValue());
		cs = EduObject.diff(p2, p1);
		assertNotNull("There shall be a change set", cs);
		assertEquals("The operation on the set does not fit:",
				ChangeOperation.UPDATE, cs.getOperation());
		assertEquals("There should be one change", 1, cs.getChanges().size());
		c = cs.getChanges().iterator().next();
		assertEquals("The operation on the change does not fit",
				ChangeOperation.UPDATE, c.getChangeOperation());
		assertEquals("value", ((AttributeChange) c).getAttribute().getValue());
	}

}
