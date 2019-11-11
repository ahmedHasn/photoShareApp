package com.fullstack.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import com.fullstack.model.User;

public interface UserService extends UserDetailsService{

	public User saveUser(User user);
	
	User findByUsername(String username);
	
	public User findByEmail(String email);
	
	public List<User> userList();
	
	public User updateUser(User user);
	
	public void updateUserPassword(User user, String newPassword);
	
	public User findUserById(Long id);
	
	public void deleteUser(User user);
	
	public void resetPassword(User user);
	
	public List<User> getUserListByUsername(String username);
	
	public User simpleSave(User user);
	
	public String saveUserImage(MultipartFile multipartFile, Long userImageId);
}
