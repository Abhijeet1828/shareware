package com.custom.sharewise.validation;

import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.exception.UnauthorizedException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BusinessValidationService {

	private final BusinessValidatorRegistry businessValidatorRegistry;

	public void validate(Map<String, Object> parameters) throws CommonException, UnauthorizedException {
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			BusinessValidator businessValidator = businessValidatorRegistry.getValidator(entry.getKey());
			if (Objects.nonNull(businessValidator)) {
				businessValidator.validate(entry.getValue());
			}
		}
	}

}
