package com.fullstack.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fullstack.model.Role;
import com.fullstack.model.RoleName;
import com.fullstack.repository.RoleRepository;
import com.fullstack.service.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Role saveRole(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public Role findRoleByName(RoleName name) {
		return roleRepository.findRoleByName(name);
	}

}
