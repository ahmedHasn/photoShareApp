package com.fullstack.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.jwt.JwtProvider;
import com.fullstack.model.User;
import com.fullstack.payload.JwtResponse;
import com.fullstack.payload.LoginModel;

@RestController
@RequestMapping("/login")
public class Login {
	
	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping
	public ResponseEntity<JwtResponse> login(@RequestBody LoginModel login){
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(login.getUsername(),login.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtProvider.generateToken(authentication);
		User userDetails = (User) authentication.getPrincipal();
		return ResponseEntity.ok(new JwtResponse(token, userDetails.getUsername(), userDetails.getAuthorities()));
	}
}
