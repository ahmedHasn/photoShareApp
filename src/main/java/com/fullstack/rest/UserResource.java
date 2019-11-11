package com.fullstack.rest;

import java.util.List;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fullstack.model.User;
import com.fullstack.payload.ChangePasswordModel;
import com.fullstack.payload.RegisterModel;
import com.fullstack.payload.UserResponse;
import com.fullstack.service.UserService;
import com.fullstack.utility.SecurityUtils;

@RestController
@RequestMapping("/api")
public class UserResource {
	
	private Long userImageId;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
//	@Autowired
//	private RoleService roleService;
//	
//	@Autowired
//	private PostService postService;
//	
//	@Autowired
//	private CommentService commentService;

	@GetMapping("/users")
	public ResponseEntity<?> getUsers(){
		List<User> users = userService.userList();
		if(users.isEmpty()) {
			return new ResponseEntity<String>("No Users found", HttpStatus.OK);
		}
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@GetMapping("/users/username/{username}")
	public ResponseEntity<?> getUserInfo(@PathVariable String username){
		ModelMapper mapper = new ModelMapper();
		User user = userService.findByUsername(username);
		UserResponse returnUser = mapper.map(user, UserResponse.class);
		if(user == null) {
			return new ResponseEntity<String>("No User Foud", HttpStatus.OK);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@GetMapping("/users/list/{username}")
	public ResponseEntity<?> getUsersByUsername(@PathVariable String username){
		List<User> users = userService.getUserListByUsername(username);
		if(users.isEmpty()) {
			return new ResponseEntity<String>("No Users found", HttpStatus.OK);
		}
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> createUser(@RequestBody RegisterModel register){
		ModelMapper mapper = new ModelMapper();
		User user = mapper.map(register, User.class);
		if(!user.getUsername().isEmpty()) {
			if(userService.findByUsername(user.getUsername()) != null ) {
				return new ResponseEntity<String>("usernameExists", HttpStatus.CONFLICT);
			}
		}
		if(!user.getEmail().isEmpty()) {
			if(userService.findByEmail(user.getEmail()) != null) {
				return new ResponseEntity<String>("emailExists", HttpStatus.CONFLICT);
			}
		}
		try {
			User userSaved = userService.saveUser(user);
			return new ResponseEntity<User>(userSaved, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("an error occured", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/users")
	public ResponseEntity<?> updateProfile(@RequestBody User user){
		if(user.getId() == null) {
			return new ResponseEntity<>("Invalid ID", HttpStatus.NOT_FOUND);
		}
		try {
			User userUpdated = userService.simpleSave(user);
			userImageId = userUpdated.getId();
			return new ResponseEntity<User>(userUpdated, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("an error occured when update user", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/users/photo-upload")
	public ResponseEntity<?> userUploadPhoto(@RequestParam("image") MultipartFile multipartFile){
		try {
			userService.saveUserImage(multipartFile, userImageId);
			return new ResponseEntity<>("User Picture Saved", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("an error occured when upload user picture", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/change-password")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordModel change){
		if(!change.getNewPassword().equals(change.getConfirmPassword())) {
			return new ResponseEntity<>("PasswordNotMatched", HttpStatus.BAD_REQUEST);
		}
		String username = SecurityUtils.getCurrentUserUsername().orElseThrow(() -> new UsernameNotFoundException("User not found"));
		User user = userService.findByUsername(username);
		try {
			if(change.getNewPassword() != null && !change.getNewPassword().isEmpty() && !StringUtils.isEmpty(change.getNewPassword())) {
				if(passwordEncoder.matches(change.getCurrentPassword(), user.getPassword())) {
					userService.updateUserPassword(user, change.getNewPassword());
				}else {
					return new ResponseEntity<>("IncorrectCurrentPassword", HttpStatus.BAD_REQUEST);
				}
			}
			return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("an error occured", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/reset-password/{email}")
	public ResponseEntity<?> resetPassword(@PathVariable String email){
		User user = userService.findByEmail(email);
		if(user == null) {
			return new ResponseEntity<>("User Not found", HttpStatus.BAD_REQUEST);
		}
		userService.resetPassword(user);
		return new ResponseEntity<>("EmailSent", HttpStatus.OK);
	}
}
