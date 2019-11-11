package com.fullstack.service;

import java.util.List;


import org.springframework.web.multipart.MultipartFile;

import com.fullstack.model.Post;

public interface PostService {

	public Post savePost(Post post, String postImageName);
	
	public List<Post> postList();
	
	public Post getPostById(Long id);
	
	public List<Post> findPostsByUsername(String username);
	
	public Post deletePost(Post post);
	
	public String savePostImage(MultipartFile multipartFile, String fileName);
}
