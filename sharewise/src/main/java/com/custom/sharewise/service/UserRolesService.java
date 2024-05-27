package com.custom.sharewise.service;

import com.custom.common.utilities.exception.CommonException;

public interface UserRolesService {
	
	public boolean addRoleForUser(Long userId, String role) throws CommonException;

}
