package edu.luc.logAnalyzer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Service;

import edu.luc.logAnalyzer.domain.ApplicationProperties;
import edu.luc.logAnalyzer.service.AppPropertyService;



/**
 * Replacement for default LDAP authorities assignment.  Assigns ROLE_USER to all users and
 * ROLE_ADMIN to those who have been identified as super users in the DB.
 * @author cline
 *
 */
@Service("authoritiesPopulator")
public class CustomAuthoritiesPopulator implements LdapAuthoritiesPopulator {
	
	/*@Autowired
	private UserService userService;
	
	@Autowired
	private AdminService adminService;*/
	
	@Autowired
	private AppPropertyService appPropertyService;

	@Override
	public Collection<? extends GrantedAuthority> getGrantedAuthorities(
			DirContextOperations userData, String username) {
		//System.out.println("it is in the getGrantedAuthorities method ofCustomAuthoritiesPopulator");
		List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
		

		ApplicationProperties properties = appPropertyService.getProperties("ADMIN_USERS");
		
		String users[] = properties.getValue().split(",");
		
		boolean superUser = false;
		for(String user : users)
		{
			
			
			if(StringUtils.equalsIgnoreCase(username.trim(), StringUtils.trimToEmpty(user)))
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
		
	
		return grantedAuths;
	}
	
}
