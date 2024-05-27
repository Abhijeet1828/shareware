package com.custom.sharewise.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.custom.common.utilities.exception.CommonException;
import com.custom.sharewise.constants.FailureConstants;
import com.custom.sharewise.model.UserGroupMapping;
import com.custom.sharewise.repository.UserGroupMappingRepository;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = CommonException.class, transactionManager = "transactionManager")
public class UserGroupMappingServiceImpl implements UserGroupMappingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserGroupMappingServiceImpl.class);

	private final UserGroupMappingRepository userGroupMappingRepository;

	public UserGroupMappingServiceImpl(UserGroupMappingRepository userGroupMappingRepository) {
		this.userGroupMappingRepository = userGroupMappingRepository;
	}

	@Override
	public boolean addUserToGroup(Long groupId, Long userId) throws CommonException {
		try {
			UserGroupMapping userGroupMapping = UserGroupMapping.builder().userId(userId).groupId(groupId)
					.isRemoved(false).build();
			userGroupMapping.setCreatedTimestamp(new Date());
			userGroupMapping.setModifiedTimestamp(new Date());

			userGroupMappingRepository.save(userGroupMapping);
			return true;
		} catch (Exception e) {
			LOGGER.error("Exception in addUserToGroup", e);
			throw new CommonException(FailureConstants.ADD_USER_TO_GROUP_ERROR.getFailureCode(),
					FailureConstants.ADD_USER_TO_GROUP_ERROR.getFailureMsg());
		}
	}

}
