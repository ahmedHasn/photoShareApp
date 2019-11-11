package com.fullstack.jwt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fullstack.model.User;
import com.fullstack.utility.Constants;

@Component
public class JwtProvider {

	public String generateToken(Authentication authentication) {
		User userDetails = (User) authentication.getPrincipal();
		List<String> roles = new ArrayList<String>();
		userDetails.getAuthorities().forEach(role -> {
			roles.add(role.getAuthority());
		});
		return JWT.create().withSubject(userDetails.getUsername())
				.withArrayClaim("roles", roles.toArray(new String[roles.size()]))
				.withExpiresAt(new Date(System.currentTimeMillis() + Constants.EXPIRATION_TIME))
				.sign(Algorithm.HMAC256(Constants.SECRET));
	}
}
