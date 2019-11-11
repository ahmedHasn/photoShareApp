package com.fullstack.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fullstack.model.Comment;
import com.fullstack.repository.CommentRepository;
import com.fullstack.service.CommentService;
import com.fullstack.utility.SecurityUtils;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	private CommentRepository commentRepository;

	@Override
	public void saveComment(Comment comment) {
		String username = SecurityUtils.getCurrentUserUsername().orElseThrow(() -> new UsernameNotFoundException("User not found"));
		comment.setUsername(username);
		commentRepository.save(comment);
	}

}
