package com.fullstack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fullstack.model.Directory;

@Repository
public interface DirectoryRepository extends JpaRepository<Directory, Long> {

}
