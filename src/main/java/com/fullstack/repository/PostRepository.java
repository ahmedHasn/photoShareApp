package com.fullstack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fullstack.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

	@Query("SELECT p FROM Post p order by p.postedDate DESC")
	public List<Post> findAll();
	
	@Query("SELECT p FROM Post p where p.username =:username order by p.postedDate DESC")
	public List<Post> findByUsername(@Param("username") String username);
	
	@Query("SELECT p FROM Post p where p.id = ?1")
	public Post findPostById(Long id);
	
	@Modifying
	@Query("delete Post where id = ?1")
	public void deletePostById(Long id);
}
