package com.fullstack.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.model.Directory;
import com.fullstack.repository.DirectoryRepository;

@RestController
@RequestMapping("/api")
public class DirectoryResourse {

	@Autowired
	private DirectoryRepository directoryRepository;
	
	@GetMapping("/directories")
	public ResponseEntity<?> getDirectories(){
		List<Directory> directories = directoryRepository.findAll();
		return new ResponseEntity<>(directories, HttpStatus.OK);
	}
}
