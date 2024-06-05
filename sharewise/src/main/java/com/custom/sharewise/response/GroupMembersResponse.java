package com.custom.sharewise.response;

import java.util.List;

import com.custom.sharewise.dto.UserDto;

public record GroupMembersResponse(Long groupId, List<UserDto> members) {

}
