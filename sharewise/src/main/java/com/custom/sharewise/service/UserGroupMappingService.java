package com.custom.sharewise.service;

import com.custom.common.utilities.exception.CommonException;

public interface UserGroupMappingService {
	
	boolean addUserToGroup(Long groupId, Long userId) throws CommonException;

}
