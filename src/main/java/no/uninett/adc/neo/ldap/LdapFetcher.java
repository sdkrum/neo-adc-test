package no.uninett.adc.neo.ldap;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import no.uninett.adc.neo.domain.EduPerson;

import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.filter.HardcodedFilter;
import org.springframework.ldap.filter.PresentFilter;
import org.springframework.ldap.query.LdapQuery;

public class LdapFetcher {
	private LdapTemplate ldapTemplate;
	private List<String> personAttributes;
	private static final String ATTRIBUTE_FEIDE_ID = "eduPersonPrincipalName";


	private List<EduPerson> getPersons() {
		LdapQuery query = query().base(getSearchBase())
				.attributes(listAttributes()).filter(getPersonFilter());
		return ldapTemplate.search(query, new PersonAttributesMapper());
	}

	private Filter getPersonFilter() {
		// TODO Auto-generated method stub
		Filter oc = new HardcodedFilter("objectClass=EduPerson");
		Filter feideIdPresent = new PresentFilter(ATTRIBUTE_FEIDE_ID);
		Filter text = new HardcodedFilter(getPersonFilterString());
		AndFilter and = new AndFilter();
		return and.and(oc).and(text).and(feideIdPresent);
	}

	private String getPersonFilterString() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getSearchBase() {
		return null;
	}

	private String[] listAttributes() {
		return personAttributes.toArray(new String[0]);
	}

	private class PersonAttributesMapper implements AttributesMapper<EduPerson> {
		private static final String ATTRIBUTE_FEIDE_ID = "eduPersonPrincipalName";
		public EduPerson mapFromAttributes(Attributes attrs)
				throws NamingException {
			String feideId = (String) attrs.get(ATTRIBUTE_FEIDE_ID).get();
			EduPerson person = new EduPerson(feideId);
			for (String attName : personAttributes) {
				String att = (String) attrs.get(attName).get();
				if (att != null && att.trim().length() > 0) {
					person.addAttribute(attName, att);
				}
			}
			return person;
		}
	}
}
