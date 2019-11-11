package com.fullstack.service;

import com.fullstack.model.Role;
import com.fullstack.model.RoleName;

public interface RoleService {

	public Role saveRole(Role role);
	
	public Role findRoleByName(RoleName name);
}
