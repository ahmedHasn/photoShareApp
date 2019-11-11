package com.fullstack.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotBlank;

import com.fullstack.model.Post;
import com.fullstack.model.Role;

public class UserDTO {

	private Long id;
	
	private String name;
	
	@NotBlank
	private String username;
	
	private String email;
	
	private Date createdDate;

	private String bio;

	private List<Post> posts;

	private List<Post> likedPosts;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public List<Post> getLikedPosts() {
		return likedPosts;
	}

	public void setLikedPosts(List<Post> likedPosts) {
		this.likedPosts = likedPosts;
	}

}
