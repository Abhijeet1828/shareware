package com.custom.sharewise.service;

import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.model.Group;
import com.custom.sharewise.request.CreateOrUpdateGroupRequest;

public interface GroupService {

	public Group createGroup(CreateOrUpdateGroupRequest createGroupRequest, CustomUserDetails userDetails);

	public Group updateGroup(CreateOrUpdateGroupRequest updateGroupRequest, CustomUserDetails userDetails);

	public void deleteGroup(Long groupId, CustomUserDetails userDetails);

}
