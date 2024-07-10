package com.custom.sharewise.validation;

import org.springframework.stereotype.Component;

import com.custom.common.utilities.exception.ResourceNotFoundException;
import com.custom.sharewise.constants.FailureConstants;
import com.custom.sharewise.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserIdValidator implements BusinessValidator {

	private final UserRepository userRepository;

	@Override
	public void validate(Object value) {
		if (!userRepository.existsByUserId(Long.parseLong(value.toString()))) {
			throw new ResourceNotFoundException(FailureConstants.USER_NOT_FOUND.getFailureCode(),
					FailureConstants.USER_NOT_FOUND.getFailureMsg());
		}
	}

}
