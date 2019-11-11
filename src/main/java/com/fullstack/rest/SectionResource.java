package com.fullstack.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fullstack.model.Section;
import com.fullstack.repository.SectionRepository;

@RestController
@RequestMapping("/api")
public class SectionResource {

	@Autowired
	private SectionRepository sectionRepository;

	@GetMapping("/sections")
	public ResponseEntity<?> getSections() {
		List<Section> sections = sectionRepository.findAllUser();
		return new ResponseEntity<>(sections, HttpStatus.OK);
	}
}
