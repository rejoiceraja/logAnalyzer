package edu.luc.logAnalyzer;

import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.configurers.ldap.LdapAuthenticationProviderConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


/**
 * Security configuration for the production environment.  Authenticates against
 * Active Directory.
 * 
 * @author cline
 */
@Configuration
@Profile("production")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	LDAPConfig ldapConfig;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
		
			.antMatchers("/css/**", "/resources/**", "/images/**", "/js/**").permitAll()
			
			.anyRequest().fullyAuthenticated().and().formLogin()
			
			.loginPage("/login").permitAll().successHandler(authenticationSuccessHandler())
			.and().logout().permitAll();
			
	}
	
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new CutOffDateCheckOnAuthenticationSuccessHandler();
    }

	@Configuration
	protected static class AuthenticationConfiguration extends
			GlobalAuthenticationConfigurerAdapter {
		
		@Autowired
		@Qualifier("authoritiesPopulator")
		LdapAuthoritiesPopulator customAuthoritiesPopulator;
		

		
		
		

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			
			//System.out.println("SecurityConfig - initi method");
			DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(
				"ldaps://XXXXXXXXXXX:999/DC=XX,DC=XX,DC=XX");
		contextSource.setUserDn("XXXXXXXX@luc.edu");
		contextSource.setPassword("XXXXX");
		contextSource.setReferral("follow");
		contextSource.afterPropertiesSet();
			

			LdapAuthenticationProviderConfigurer<AuthenticationManagerBuilder> ldapAuthenticationProviderConfigurer = auth
					.ldapAuthentication().ldapAuthoritiesPopulator(customAuthoritiesPopulator);

			ldapAuthenticationProviderConfigurer.userSearchFilter("(&(cn={0}))").userSearchBase("")
					.contextSource(contextSource);
		}
	}
	
	
	public DefaultSpringSecurityContextSource getContextStore()
	{

		return ldapConfig.getContextStore();
	}
	
	
	
}
