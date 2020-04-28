package edu.luc.logAnalyzer.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	

	/**
	 * Gets the UVID of whoever logged into the current session
	 * 
	 * @return the user's UVID, or null if no one is logged in
	 */
	public String getCurrentUserUvid() {
		// Get the current user, regardless of if it's a student or not
		String uvid = null;
		UserDetails userDetails = getCurrentUserDetails();
		System.out.println("USer Details received is "+userDetails);
		if (userDetails != null) {
			uvid = userDetails.getUsername();
		}
		System.out.println("The UVID retrieved is "+uvid);
		return StringUtils.upperCase(uvid);
		//return uvid;
	}

	/**
	 * Helper method to retrieve the currently logged in user
	 * 
	 * @return the UserDetails object used by Spring Security
	 */
	private UserDetails getCurrentUserDetails() {
		UserDetails userDetails = null;
		SecurityContext securityContext = SecurityContextHolder.getContext();
		
		Authentication authentication = securityContext.getAuthentication();
		
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			String userName = String.valueOf(authentication.getPrincipal());
			
			if (principal != null && principal instanceof UserDetails) {
				userDetails = (UserDetails) principal;
				
			}
		}
		
		return userDetails;
		
	}

}
