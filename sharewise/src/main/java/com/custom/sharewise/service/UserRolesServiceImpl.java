package com.custom.sharewise.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.custom.common.utilities.exception.CommonException;
import com.custom.sharewise.constants.FailureConstants;
import com.custom.sharewise.model.UserRoles;
import com.custom.sharewise.repository.UserRolesRepository;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = CommonException.class, transactionManager = "transactionManager")
public class UserRolesServiceImpl implements UserRolesService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRolesServiceImpl.class);

	private final UserRolesRepository userRolesRepository;

	public UserRolesServiceImpl(UserRolesRepository userRolesRepository) {
		this.userRolesRepository = userRolesRepository;
	}

	@Override
	public boolean addRoleForUser(Long userId, String role) throws CommonException {
		try {
			UserRoles userRoles = UserRoles.builder().userId(userId).role(role).build();
			userRoles.setCreatedTimestamp(new Date());
			userRoles.setModifiedTimestamp(new Date());

			userRolesRepository.save(userRoles);
			return true;
		} catch (Exception e) {
			LOGGER.error("Exception in addRoleForUser", e);
			throw new CommonException(FailureConstants.ADD_ROLE_FOR_USER_ERROR.getFailureCode(),
					FailureConstants.ADD_ROLE_FOR_USER_ERROR.getFailureMsg());
		}
	}

}
