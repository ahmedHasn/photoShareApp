package com.fullstack.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fullstack.model.User;
import com.fullstack.utility.Constants;

public class JwtAuthentication extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	public JwtAuthentication(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		ModelMapper mapper = new ModelMapper();
		User user = null;
		try {
			user = mapper.map(request.getInputStream(), User.class);
			return authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to convert user from json to java object!");
		}
	}

	protected void successfulAuthenticated(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		User user = (User) authentication.getPrincipal();
		List<String> roles = new ArrayList<String>();
		user.getAuthorities().forEach(role -> {
			roles.add(role.getAuthority());
		});
		String jwtToken = JWT.create().withIssuer(request.getRequestURI()).withSubject(user.getUsername())
				.withArrayClaim("roles", roles.toArray(new String[roles.size()]))
				.withExpiresAt(new Date(System.currentTimeMillis() + Constants.EXPIRATION_TIME))
				.sign(Algorithm.HMAC256(Constants.SECRET));
		response.addHeader(Constants.HEADER_TYPE, Constants.TOKEN_PREFIX + jwtToken);
	}

}
