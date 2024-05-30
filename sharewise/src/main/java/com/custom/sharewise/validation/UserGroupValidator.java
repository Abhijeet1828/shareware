package com.custom.sharewise.validation;

import org.springframework.stereotype.Component;

import com.custom.common.utilities.convertors.TypeConversionUtils;
import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.exception.UnauthorizedException;
import com.custom.sharewise.constants.FailureConstants;
import com.custom.sharewise.dto.UserGroupDto;
import com.custom.sharewise.repository.UserGroupMappingRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserGroupValidator implements BusinessValidator {

	private final UserGroupMappingRepository userGroupMappingRepository;

	@Override
	public void validate(Object value) throws CommonException, UnauthorizedException {
		UserGroupDto userGroupDto = TypeConversionUtils.convertToCustomClass(value, UserGroupDto.class);

		for (Long userId : userGroupDto.userIdList()) {
			if (!userGroupMappingRepository.existsByGroupIdAndUserIdAndIsRemovedFalse(userGroupDto.groupId(), userId)) {
				throw new CommonException(FailureConstants.USER_NOT_MAPPED_TO_GROUP.getFailureCode(),
						FailureConstants.USER_NOT_MAPPED_TO_GROUP.getFailureMsg());
			}
		}
	}

}
