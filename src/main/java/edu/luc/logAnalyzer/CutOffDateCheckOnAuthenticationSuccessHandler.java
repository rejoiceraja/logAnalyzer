package edu.luc.logAnalyzer;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import edu.luc.logAnalyzer.service.AppPropertyService;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;

@Configuration
public class CutOffDateCheckOnAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(CutOffDateCheckOnAuthenticationSuccessHandler.class);
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Autowired
	AppPropertyService propertyService;
	

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		//System.out.println("In Successful Handler OnAuthentication Success method for checking the role");
		
		
		
		 String userName = "";
	        if(authentication.getPrincipal() instanceof Principal) {
	             userName = ((Principal)authentication.getPrincipal()).getName();

	        }
	        else if(authentication.getPrincipal() instanceof LdapUserDetailsImpl)
	        {
	        	userName = ((LdapUserDetailsImpl)authentication.getPrincipal()).getUsername();
	        }
	        else {
	            userName = ((User)authentication.getPrincipal()).getUsername();
	        }
	        System.out.println("CutOffDateCheckOnAuthenticationSuccessHandler: userName to bet set in Session : " + userName);
	        	        
	        request.getSession().setAttribute("userName", userName);
	        
	        
	        
		 Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		 
		 boolean denied = false;
		 boolean restricted = false;
		 boolean superUser = false;
		  for (GrantedAuthority grantedAuthority : authorities) {
			  System.out.println("CutOffDateCheckOnAuthenticationSuccessHandler - onAuthenticationSuccess - role is "+grantedAuthority.getAuthority());
	            if (grantedAuthority.getAuthority().equals("ROLE_DENIED")) {
	            	denied = true;
	            	
	            }
	            else if(grantedAuthority.getAuthority().equals("ROLE_RESTRICTED")) {
	            	restricted = true;
	            	
	            }
	            else if(grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
	            {
	            	superUser = true;
	            }
	           
	           
		  }
		  
		  if(denied)
		  {
			  System.out.println("returning back to the cutoffDate page");
			  
			  redirectStrategy.sendRedirect(request, response, "/cutOffDate");
			  //redirectStrategy.sendRedirect(request, response, "/");
		  }
		  else if(superUser)
		  {
			  redirectStrategy.sendRedirect(request, response, "/home");
		  }
		
		  else
		  {
			  redirectStrategy.sendRedirect(request, response, "/cutOffDate");
		  }

	}
	
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

}
