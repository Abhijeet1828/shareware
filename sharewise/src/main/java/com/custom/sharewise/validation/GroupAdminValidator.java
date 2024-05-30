package com.custom.sharewise.validation;

import org.springframework.stereotype.Component;

import com.custom.common.utilities.convertors.TypeConversionUtils;
import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.exception.UnauthorizedException;
import com.custom.sharewise.constants.FailureConstants;
import com.custom.sharewise.dto.UserGroupDto;
import com.custom.sharewise.repository.GroupRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class GroupAdminValidator implements BusinessValidator {

	private final GroupRepository groupRepository;

	@Override
	public void validate(Object value) throws CommonException, UnauthorizedException {
		UserGroupDto userGroupDto = TypeConversionUtils.convertToCustomClass(value, UserGroupDto.class);
		if (!groupRepository.existsByGroupIdAndCreatedByAndIsActiveTrue(userGroupDto.groupId(),
				userGroupDto.userId())) {
			throw new UnauthorizedException(FailureConstants.USER_NOT_GROUP_ADMIN.getFailureCode(),
					FailureConstants.USER_NOT_GROUP_ADMIN.getFailureMsg());
		}

	}

}
