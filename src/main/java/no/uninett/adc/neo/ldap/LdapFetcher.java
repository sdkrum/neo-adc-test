package no.uninett.adc.neo.ldap;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import no.uninett.adc.neo.domain.EduOrg;
import no.uninett.adc.neo.domain.EduPerson;

import org.apache.log4j.Logger;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.ldap.filter.HardcodedFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.ldap.filter.PresentFilter;
import org.springframework.ldap.query.LdapQuery;

public class LdapFetcher {

	private static final String ATT_NAME_OBJECT_CLASS = "objectClass";
	private static final String ATT_NAME_FEIDE_ID = "eduPersonPrincipalName";
	private static final String ATT_NAME_ORG_NIN = "eduOrgNIN";

	private Logger logger = Logger.getLogger(getClass());
	private LdapTemplate ldapTemplate;
	private List<String> personAttributes;
	private List<String> orgAttributes;
	private String personFilterString;
	private String orgDN;

	public EduOrg readEduOrg() {
		EduOrg org = getOrg();
		List<EduPerson> persons = getPersons(org);
		Set<EduPerson> setP = new HashSet<EduPerson>(persons);
		org.setEmployees(setP);
		return org;
	}

	private EduOrg getOrg() {
		List<EduOrg> orgs = getOrgs();
		if (orgs.isEmpty()) {
			throw new RuntimeException(
					"Unable to find org, no match! Filter was "
							+ getOrgFilter().encode());
		}
		if (orgs.size() == 1) {
			return orgs.get(0);
		}
		logger.debug("Found " + orgs.size()
				+ " EduOrgs, searching for the right one...");
		for (EduOrg eduOrg : orgs) {
			if (eduOrg.getOrgNIN() != null) {
				logger.debug("There is one with orgNIN, we take it...");
				return eduOrg;
			}
			logger.trace("This one did not have any orgNIN, strange....");
		}
		throw new RuntimeException("No fitting eduOrg... Filter was: "
				+ getOrgFilter().encode());
	}

	private List<EduOrg> getOrgs() {
		LdapQuery query = query().attributes(listOrgAttributes()).filter(
				getOrgFilter());
		return getLdapTemplate().search(query, new OrgAttributesMapper());
	}

	private List<EduPerson> getPersons(EduOrg org) {
		LdapQuery query = query().base(getSearchBase())
				.attributes(listPersonAttributes()).filter(getPersonFilter());
		return getLdapTemplate().search(query, new PersonAttributesMapper(org));
	}

	private Filter getPersonFilter() {
		Filter oc = new EqualsFilter(ATT_NAME_OBJECT_CLASS, "EduPerson");
		Filter feideIdPresent = new PresentFilter(ATT_NAME_FEIDE_ID);
		Filter text = new HardcodedFilter(getPersonFilterString());
		AndFilter and = new AndFilter();
		Filter filter = and.and(oc).and(text).and(feideIdPresent);
		logger.debug("Person filter is" + filter.encode());
		return filter;
	}

	private Filter getOrgFilter() {
		Filter ocOrg = new EqualsFilter(ATT_NAME_OBJECT_CLASS, "Organization");
		Filter ocEduOrg = new EqualsFilter(ATT_NAME_OBJECT_CLASS, "EduOrg");
		OrFilter or = new OrFilter();
		or.or(ocOrg).or(ocEduOrg);
		Filter orgDN = new EqualsFilter("dn", getOrgDN());
		AndFilter and = new AndFilter();
		Filter filter = and.and(or).and(orgDN);
		logger.debug("Org filter is" + filter.encode());
		return filter;
	}

	private String getPersonFilterString() {
		return personFilterString;
	}

	private String getSearchBase() {
		return null;
	}

	private String[] listPersonAttributes() {
		return personAttributes.toArray(new String[0]);
	}

	private String[] listOrgAttributes() {
		return orgAttributes.toArray(new String[0]);
	}

	private class PersonAttributesMapper implements AttributesMapper<EduPerson> {
		private EduOrg org;

		public PersonAttributesMapper(EduOrg org) {
			this.org = org;
		}

		public EduPerson mapFromAttributes(Attributes attrs)
				throws NamingException {
			String feideId = (String) attrs.get(ATT_NAME_FEIDE_ID).get();
			EduPerson person = new EduPerson(feideId);
			for (String attName : getPersonAttributes()) {
				String att = (String) attrs.get(attName).get();
				if (att != null && att.trim().length() > 0) {
					person.addAttribute(attName, att);
				}
			}
			person.setOrg(org);
			return person;
		}
	}

	private class OrgAttributesMapper implements AttributesMapper<EduOrg> {

		public EduOrg mapFromAttributes(Attributes attrs)
				throws NamingException {
			String orgNIN = (String) attrs.get(ATT_NAME_ORG_NIN).get();
			EduOrg org = new EduOrg(orgNIN);
			for (String attName : getOrgAttributes()) {
				String att = (String) attrs.get(attName).get();
				if (att != null && att.trim().length() > 0) {
					org.addAttribute(attName, att);
				}
			}
			return org;
		}
	}

	/**
	 * @return the ldapTemplate
	 */
	private LdapTemplate getLdapTemplate() {
		return ldapTemplate;
	}

	/**
	 * @return the personAttributes
	 */
	private List<String> getPersonAttributes() {
		return personAttributes;
	}

	/**
	 * @return the orgAttributes
	 */
	private List<String> getOrgAttributes() {
		return orgAttributes;
	}

	/**
	 * @return the orgDN
	 */
	private String getOrgDN() {
		return orgDN;
	}

	/**
	 * @param ldapTemplate
	 *            the ldapTemplate to set
	 */
	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	/**
	 * @param personAttributes
	 *            the personAttributes to set
	 */
	public void setPersonAttributes(List<String> personAttributes) {
		this.personAttributes = personAttributes;
	}

	/**
	 * @param orgAttributes
	 *            the orgAttributes to set
	 */
	public void setOrgAttributes(List<String> orgAttributes) {
		this.orgAttributes = orgAttributes;
	}

	/**
	 * @param personFilterString
	 *            the personFilterString to set
	 */
	public void setPersonFilterString(String personFilterString) {
		this.personFilterString = personFilterString;
	}

	/**
	 * @param orgDN
	 *            the orgDN to set
	 */
	public void setOrgDN(String orgDN) {
		this.orgDN = orgDN;
	}
}
