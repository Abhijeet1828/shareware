package com.custom.sharewise.validation;

import org.springframework.stereotype.Component;

import com.custom.common.utilities.exception.ResourceNotFoundException;
import com.custom.sharewise.constants.FailureConstants;
import com.custom.sharewise.repository.GroupRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class GroupIdValidator implements BusinessValidator {

	private final GroupRepository groupRepository;

	@Override
	public void validate(Object value) {
		if (!groupRepository.existsByGroupIdAndIsActiveTrue(Long.parseLong(value.toString()))) {
			throw new ResourceNotFoundException(FailureConstants.GROUP_NOT_FOUND.getFailureCode(),
					FailureConstants.GROUP_NOT_FOUND.getFailureMsg());
		}
	}

}
