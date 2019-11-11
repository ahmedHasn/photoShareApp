package com.fullstack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fullstack.model.Role;
import com.fullstack.model.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	
	@Query("SELECT r FROM Role r where r.name = ?1")
	public Role findRoleByName(RoleName name);
}
