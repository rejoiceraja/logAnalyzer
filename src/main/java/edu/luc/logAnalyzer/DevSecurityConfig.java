package edu.luc.logAnalyzer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.naming.directory.Attributes;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import edu.luc.logAnalyzer.domain.ApplicationProperties;
import edu.luc.logAnalyzer.service.AppPropertyService;


/**
 * A security configuration for the development/test environments. Does not
 * check passwords, but automatically grants access using whatever username was
 * entered.
 * 
 * @author rjebamalaidass
 */
@Configuration
@Profile("dev")
public class DevSecurityConfig extends WebSecurityConfigurerAdapter {


	
	@Autowired
	private AppPropertyService appPropertyService;
	
	@Autowired
	CutOffDateCheckOnAuthenticationSuccessHandler cutOffDateCheckOnAuthenticationSuccessHandler;
	


	private class CustomAuthenticationProvider implements
			AuthenticationProvider {

		// Custom authentication provider for exclusive use in test.
		// Authenticates the user regardless of the password entered
		public Authentication authenticate(Authentication authentication)
				throws AuthenticationException {
			UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
			String userName = String.valueOf(auth.getPrincipal());

			List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
			
			
			System.out.println("******************************* auth.getPrincipal Value is "+userName);
			
			ApplicationProperties properties = appPropertyService.getProperties("ADMIN_USERS");
			
			String users[] = properties.getValue().split(",");
			
			boolean superUser = false;
			for(String user : users)
			{
				
				
				if(StringUtils.equalsIgnoreCase(userName.trim(), StringUtils.trimToEmpty(user)))
				{
					superUser = true;
					break;
				}
			}
			
			
		
			
			if(superUser)
			{
				grantedAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			}
			else 
			{
				grantedAuths.add(new SimpleGrantedAuthority("ROLE_DENIED"));
			}
			
		

			
	


			UserDetails userDetails = new User(userName, "", grantedAuths);
			Authentication userAuthentication = new UsernamePasswordAuthenticationToken(
					userDetails, userDetails.getPassword(), grantedAuths);

			return userAuthentication;
		}

		public boolean supports(Class<?> authentication) {
			return true;
		}

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
				.antMatchers("/css/**", "/resources/**", "/images/**", "/js/**").permitAll()
				.anyRequest().fullyAuthenticated().and().formLogin()
				.loginPage("/login").permitAll().successHandler(cutOffDateCheckOnAuthenticationSuccessHandler).and().logout().permitAll();
	}
	
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new CutOffDateCheckOnAuthenticationSuccessHandler();
    }


	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.authenticationProvider(new CustomAuthenticationProvider());
	}

	
	
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
