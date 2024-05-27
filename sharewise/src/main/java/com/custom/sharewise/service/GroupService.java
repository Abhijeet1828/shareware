package com.custom.sharewise.service;

import com.custom.common.utilities.exception.CommonException;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.request.CreateGroupRequest;

public interface GroupService {

	public Object createGroup(CreateGroupRequest createGroupRequest, CustomUserDetails userDetails)
			throws CommonException;

}
