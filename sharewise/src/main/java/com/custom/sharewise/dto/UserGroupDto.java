package com.custom.sharewise.dto;

import java.util.List;

public record UserGroupDto(Long groupId, Long userId, List<Long> userIdList) {

}
