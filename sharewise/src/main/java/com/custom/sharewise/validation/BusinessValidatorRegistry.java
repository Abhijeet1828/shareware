package com.custom.sharewise.validation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BusinessValidatorRegistry {

	private final Map<String, BusinessValidator> businessValidators = new HashMap<>();

	public BusinessValidatorRegistry(ApplicationContext context) {
		businessValidators.put("userId", context.getBean(UserIdValidator.class));
		businessValidators.put("groupId", context.getBean(GroupIdValidator.class));
	}

	public BusinessValidator getValidator(String name) {
		return businessValidators.get(name);
	}

}
