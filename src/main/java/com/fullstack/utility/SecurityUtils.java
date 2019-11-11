package com.fullstack.utility;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {

	private SecurityUtils() {}
	
	// Get Username of the current user
	public static Optional<String> getCurrentUserUsername(){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		return Optional.ofNullable(securityContext.getAuthentication())
				.map(authenticatio -> {
					if(authenticatio.getPrincipal() instanceof UserDetails) {
						UserDetails springSecurityUser = (UserDetails) authenticatio.getPrincipal();
						return springSecurityUser.getUsername();
					}else if(authenticatio.getPrincipal() instanceof String) {
						return (String) authenticatio.getPrincipal();
					}
					return null;
				});
	}
}
