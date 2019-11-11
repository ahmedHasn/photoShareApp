package com.fullstack.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fullstack.model.Role;
import com.fullstack.model.RoleName;
import com.fullstack.model.User;
import com.fullstack.repository.RoleRepository;
import com.fullstack.repository.UserRepository;
import com.fullstack.service.UserService;
import com.fullstack.utility.Constants;
import com.fullstack.utility.EmailConstructor;

@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private EmailConstructor emailConstructor;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
//		.orElseThrow(() -> new UsernameNotFoundException("User not Found"));
		if(user == null) {
			throw new UsernameNotFoundException("User not Found");
		}
		return user;
	}

	@Override
	public User saveUser(User user) {
		String password = RandomStringUtils.randomAlphanumeric(10);
		String encryptedPassword = passwordEncoder.encode(password);
		user.setPassword(encryptedPassword);
		Set<Role> roles = new HashSet<>();
		Role role = roleRepository.findRoleByName(RoleName.ROLE_USER);
		if(role != null) {
			roles.add(role);
		}
		user.setRoles(roles);
		User newUser = userRepository.save(user);
		byte[] bytes;
		try {
			bytes = Files.readAllBytes(Constants.DEFAULT_PIC.toPath());
			String userImageId = newUser.getId() + ".png";
			Path path = Paths.get(Constants.USER_FOLDER + userImageId);
			Files.write(path, bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			mailSender.send(emailConstructor.contractNewUserEmail(newUser, password));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newUser;
	}

	@Override
	public User findByUsername(String username) {
		User user = userRepository.findByUsername(username);
//		.orElseThrow(() -> new UsernameNotFoundException(""));
//		if(user == null) {
//			throw new UsernameNotFoundException("User not Found");
//		}
		return user;
	}

	@Override
	public User findByEmail(String email) {
		User user = userRepository.findByEmail(email);
		return user;
	}

	@Override
	public List<User> userList() {
		return userRepository.findAll();
	}

	@Override
	public User updateUser(User user) {
		String password = user.getPassword();
		String encryptedPassword = passwordEncoder.encode(password);
		user.setPassword(encryptedPassword);
		User newUser = userRepository.save(user);
		mailSender.send(emailConstructor.constructUpdateUser(newUser));
		return newUser;
	}

	@Override
	public User findUserById(Long id) {
		return userRepository.findUserById(id);
	}

	@Override
	public void deleteUser(User user) {
		userRepository.delete(user);
	}

	@Override
	public void resetPassword(User user) {
		String password = RandomStringUtils.randomAlphanumeric(10);
		String encryptedPassword = passwordEncoder.encode(password);
		user.setPassword(encryptedPassword);
		userRepository.save(user);
		mailSender.send(emailConstructor.constructResetPasswordEmail(user, password));
	}

	@Override
	public List<User> getUserListByUsername(String username) {
		List<User> users = userRepository.findByUsernameContaining(username);
		return users;
	}

	@Override
	public User simpleSave(User user) {
		User newUser = userRepository.save(user);
//		mailSender.send(emailConstructor.constructUpdateUser(newUser));
		return newUser;
	}

	@Override
	public String saveUserImage(MultipartFile multipartFile, Long userImageId) {
//		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//		Iterator<String> iterator = multipartRequest.getFileNames();
//		MultipartFile multipartFile = multipartRequest.getFile(iterator.next());
		try {
			Files.deleteIfExists(Paths.get(Constants.USER_FOLDER + "/" + userImageId + ".png" ));
			byte[] bytes = multipartFile.getBytes();
			Path path = Paths.get(Constants.USER_FOLDER + userImageId + ".png");
			Files.write(path, bytes, StandardOpenOption.CREATE);
			return "User picture saved to server";
		} catch (Exception e) {
			return "Error occured. Photo not saved";
		}
	}

	@Override
	public void updateUserPassword(User user, String newPassword) {
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
        mailSender.send(emailConstructor.constructResetPasswordEmail(user, newPassword));
	}
}
