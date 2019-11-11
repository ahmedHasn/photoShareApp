package com.fullstack.rest;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fullstack.model.Post;
import com.fullstack.model.User;
import com.fullstack.service.PostService;
import com.fullstack.service.UserService;
import com.fullstack.utility.SecurityUtils;

@RestController
@RequestMapping("/api")
public class PostResource {
	
	private String postImageName;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/post")
	public ResponseEntity<?> create(@RequestBody Post post ) {
		postImageName = RandomStringUtils.random(10, true, true);
		System.out.println("Random Name -----------> " + postImageName);
		Post newPost = postService.savePost(post, postImageName);
		return ResponseEntity.ok().body(newPost);
	}
	
	@GetMapping("/post")
	public ResponseEntity<?> getPosts() {
		List<Post> posts = postService.postList();
		return ResponseEntity.ok().body(posts);
	}
	
	@GetMapping("/post/{id}")
	public ResponseEntity<?> getPostById(@PathVariable Long id) {
		Post post = postService.getPostById(id);
		return ResponseEntity.ok().body(post);
	}
	
	@GetMapping("/post/username")
	public ResponseEntity<?> getPostsByUsername() {
		String username = SecurityUtils.getCurrentUserUsername().orElseThrow(() -> new UsernameNotFoundException("User not found"));
		List<Post> posts = postService.findPostsByUsername(username);
		return ResponseEntity.ok().body(posts);
	}
	
	@PostMapping("/post/like")
	public ResponseEntity<?> likePost(@RequestBody Long id) {
		Post post = postService.getPostById(id);
		if(post == null) {
			return new ResponseEntity<>("Post Not found", HttpStatus.BAD_REQUEST);
		}
		String username = SecurityUtils.getCurrentUserUsername().orElseThrow(() -> new UsernameNotFoundException("User not Found"));
		User user = userService.findByUsername(username);
		if(user == null) {
			return new ResponseEntity<>("User Not found", HttpStatus.BAD_REQUEST);
		}
		post.setLikes(1);
		user.getLikedPosts().add(post);
		userService.simpleSave(user);
		return new ResponseEntity<>("Post was liked" , HttpStatus.OK);
	}
	
	@PostMapping("/post/unlike")
	public ResponseEntity<?> unLikePost(@RequestBody Long id) {
		Post post = postService.getPostById(id);
		if(post == null) {
			return new ResponseEntity<>("Post Not found", HttpStatus.BAD_REQUEST);
		}
		String username = SecurityUtils.getCurrentUserUsername().orElseThrow(() -> new UsernameNotFoundException("User not Found"));
		User user = userService.findByUsername(username);
		if(user == null) {
			return new ResponseEntity<>("User Not found", HttpStatus.BAD_REQUEST);
		}
		post.setLikes(post.getLikes() - 1);
		user.getLikedPosts().remove(post);
		userService.simpleSave(user);
		return new ResponseEntity<>("Post was unliked" , HttpStatus.OK);
	}
	
	@DeleteMapping("/post/{id}")
	public ResponseEntity<?> deletePost(@PathVariable Long id) {
		Post post = postService.getPostById(id);
		try {
			postService.deletePost(post);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("An error occured" , HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/post/photo-upload")
	public ResponseEntity<?> postUploadPhoto(@RequestParam("image") MultipartFile multipartFile) {
		try {
			postService.savePostImage(multipartFile, postImageName);
			return new ResponseEntity<>("Post Picture Saved!" , HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Post Picture Not Saved! " , HttpStatus.BAD_REQUEST);
		}
	}

}
