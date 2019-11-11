package com.fullstack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fullstack.model.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

	@Query("SELECT s FROM Section s")
	List<Section> findAllUser();
}
