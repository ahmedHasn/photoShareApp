package com.fullstack.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fullstack.service.UserService;
import com.fullstack.service.impl.UserServiceImpl;
import com.fullstack.utility.Constants;

public class JwtAuthorization extends OncePerRequestFilter {
	
	private static final Logger LOG = LoggerFactory.getLogger(JwtAuthentication.class);
	
	@Autowired
	private UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, "
				+ "Content-Type, Access-Control-Request-Method, " + "Access-Control-Request-Headers, Authorization");
		response.addHeader("Access-Control-Expose-Headers",
				"Access-Control-Allow-Origin, " + "Access-Control-Allow-Creentials, " + "Authorization");
		response.addHeader("Access-Control-Allow-Methods", "PUT," + "GET," + "POST, " + "DELETE");
		
		if ((request.getMethod().equalsIgnoreCase("OPTIONS"))) {
			try {
				response.setStatus(HttpServletResponse.SC_OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			try {
			// get JWT token from header
			String jwtToken = request.getHeader(Constants.HEADER_TYPE);
			if(jwtToken == null || !jwtToken.startsWith(Constants.TOKEN_PREFIX)) {
				filterChain.doFilter(request, response);
				return;
			}
			JWT.require(Algorithm.HMAC256(Constants.SECRET));
			DecodedJWT jwt = JWT.decode(jwtToken.substring(Constants.TOKEN_PREFIX.length()));
			
			// parse username from token
			String username = jwt.getSubject();
			// load data from users table, then build an authentication object
			
			
//			UserDetails userDetails = userService.loadUserByUsername(username);
			List<String> roles = jwt.getClaims().get("roles").asList(String.class);
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					username, null, authorities);
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			// set the authentication object to Security Context
			SecurityContextHolder.getContext().setAuthentication(authentication);
			}catch(Exception e) {
				System.out.println(e.getMessage() + "---------");
			}

			filterChain.doFilter(request, response);
		}
	}

}
