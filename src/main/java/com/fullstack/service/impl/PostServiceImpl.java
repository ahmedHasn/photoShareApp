package com.fullstack.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fullstack.model.Post;
import com.fullstack.model.User;
import com.fullstack.repository.PostRepository;
import com.fullstack.service.PostService;
import com.fullstack.service.UserService;
import com.fullstack.utility.Constants;
import com.fullstack.utility.SecurityUtils;

@Service
@Transactional
public class PostServiceImpl implements PostService {
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserService userService;

	@Override
	public Post savePost(Post post, String postImageName) {
		String username = SecurityUtils.getCurrentUserUsername().orElseThrow(() -> new UsernameNotFoundException("User not found"));
		System.out.println("----------------> " + username);
		User user = userService.findByUsername(username);
		post.setName(postImageName);
		post.setUsername(username);
		post.setPostedDate(new Date());
		post.setUserImageId(user.getId());
		user.getPosts().add(post);
		Post newPost = postRepository.save(post);
		return newPost;
	}

	@Override
	public List<Post> postList() {
		return postRepository.findAll();
	}

	@Override
	public Post getPostById(Long id) {
		return postRepository.findPostById(id);
	}

	@Override
	public List<Post> findPostsByUsername(String username) {
		return postRepository.findByUsername(username);
	}

	@Override
	public Post deletePost(Post post) {
		try {
			Files.deleteIfExists(Paths.get(Constants.POST_FOLDER + "/" + post.getName() + ".png" ));
			postRepository.deletePostById(post.getId());
			return post;
		} catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public String savePostImage(MultipartFile multipartFile, String fileName) {
//		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//		Iterator<String> iterator = multipartRequest.getFileNames();
//		MultipartFile multipartFile = multipartRequest.getFile(iterator.next());
		try {
			Files.deleteIfExists(Paths.get(Constants.POST_FOLDER + "/" + fileName + ".png" ));
			byte[] bytes = multipartFile.getBytes();
			Path path = Paths.get(Constants.POST_FOLDER + fileName + ".png");
			Files.write(path, bytes, StandardOpenOption.CREATE);
		} catch (Exception e) {
			return "Error occured. Photo not saved";
		}
		return "Photo Saved successfully";
	}

}
