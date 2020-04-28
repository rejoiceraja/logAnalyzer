package edu.luc.logAnalyzer;

import javax.naming.directory.Attributes;

import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;

@Configuration
public class LDAPConfig {

	public DefaultSpringSecurityContextSource getContextStore()
	{
		DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(
				"ldaps://XXXXXXXXXXX:999/DC=XX,DC=XX,DC=XX");
		contextSource.setUserDn("XXXXXXXX@luc.edu");
		contextSource.setPassword("XXXXX");
		contextSource.setReferral("follow");
		contextSource.afterPropertiesSet();

		
		return contextSource;
	}
	
	public FilterBasedLdapUserSearch getFilterBasedLdapUserSearch()
	{
		FilterBasedLdapUserSearch search = new FilterBasedLdapUserSearch(
				"", "(&(cn={0}))", getContextStore());
		return search;
	}
	
	
	public String getLID(String uvid)
	{
		
		System.out.println("userName entered is "+uvid);
		try
		{
			FilterBasedLdapUserSearch search =  getFilterBasedLdapUserSearch();
			DirContextOperations ctx = search.searchForUser(uvid.trim());
			Attributes attributes = ctx.getAttributes();
				
			return ctx.getStringAttribute("employeenumber");
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return "";
		}
	}

}
