package com.custom.sharewise.validation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.custom.sharewise.constants.Constants;

@Component
public class BusinessValidatorRegistry {

	private final Map<String, BusinessValidator> businessValidators = new HashMap<>();

	public BusinessValidatorRegistry(ApplicationContext context) {
		businessValidators.put(Constants.VALIDATION_USER_ID, context.getBean(UserIdValidator.class));
		businessValidators.put(Constants.VALIDATION_GROUP_ID, context.getBean(GroupIdValidator.class));
		businessValidators.put(Constants.VALIDATION_GROUP_ADMIN, context.getBean(GroupAdminValidator.class));
		businessValidators.put(Constants.VALIDATION_USER_GROUP, context.getBean(UserGroupValidator.class));
	}

	public BusinessValidator getValidator(String name) {
		return businessValidators.get(name);
	}

}
