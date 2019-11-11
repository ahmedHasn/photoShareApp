package com.fullstack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fullstack.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	@Query("SELECT u FROM User u where u.username = ?1")
	public User findByUsername(String username);
//	Optional<User> findByUsername(String username);
	
	public User findByEmail(String email);
	
	@Query("SELECT u FROM User u where u.id = ?1")
	public User findUserById(Long id);
	
	public List<User> findByUsernameContaining(String username);
}
