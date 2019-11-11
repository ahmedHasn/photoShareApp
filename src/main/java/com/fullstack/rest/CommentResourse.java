package com.fullstack.rest;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.dto.CommentDTO;
import com.fullstack.model.Comment;
import com.fullstack.model.Post;
import com.fullstack.service.CommentService;
import com.fullstack.service.PostService;

@RestController
@RequestMapping("/api")
public class CommentResourse {

	@Autowired
	private CommentService commentService;
	
	@Autowired
	private PostService postService;
	
	@PostMapping("/comments")
	public ResponseEntity<?> saveComment(@RequestBody CommentDTO commentDTO) {
		Post post = postService.getPostById(commentDTO.getPostId());
		if(post == null) {
			return new ResponseEntity<>("Post not found", HttpStatus.BAD_REQUEST);
		}
		ModelMapper mapper = new ModelMapper();
		Comment comment = mapper.map(commentDTO, Comment.class);
		comment.setPostedDate(new Date());
		post.getCommentList().add(comment);
		commentService.saveComment(comment);
		return ResponseEntity.ok().build();
	}
}
