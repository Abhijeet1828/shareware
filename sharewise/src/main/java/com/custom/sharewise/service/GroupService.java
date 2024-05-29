package com.custom.sharewise.service;

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.exception.UnauthorizedException;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.request.CreateOrUpdateGroupRequest;

public interface GroupService {

	public Object createGroup(CreateOrUpdateGroupRequest createGroupRequest, CustomUserDetails userDetails)
			throws CommonException;

	public Object updateGroup(CreateOrUpdateGroupRequest updateGroupRequest, CustomUserDetails userDetails)
			throws CommonException, UnauthorizedException;

	public void deleteGroup(Long groupId, CustomUserDetails userDetails) throws CommonException, UnauthorizedException;

}
