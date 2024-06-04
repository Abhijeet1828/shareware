package com.custom.sharewise.service;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.custom.common.utilities.exception.CommonException;
import com.custom.common.utilities.exception.UnauthorizedException;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.constants.FailureConstants;
import com.custom.sharewise.dto.UserDto;
import com.custom.sharewise.model.User;
import com.custom.sharewise.repository.UserRepository;
import com.custom.sharewise.request.UpdatePasswordRequest;
import com.custom.sharewise.request.UpdateUserRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { CommonException.class,
		UnauthorizedException.class }, transactionManager = "transactionManager")
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Object updateUser(UpdateUserRequest updateUserRequest, CustomUserDetails userDetails)
			throws CommonException {
		try {
			User existingUser = userRepository.findByEmail(userDetails.getUsername())
					.orElseThrow(() -> new CommonException(FailureConstants.USER_NOT_FOUND.getFailureCode(),
							FailureConstants.USER_NOT_FOUND.getFailureMsg()));

			modelMapper.getConfiguration().setSkipNullEnabled(true);
			modelMapper.typeMap(UpdateUserRequest.class, User.class);

			modelMapper.map(updateUserRequest, existingUser);
			existingUser.setModifiedTimestamp(new Date());

			return userRepository.save(existingUser);
		} catch (Exception e) {
			LOGGER.error("Exception in updateUser", e);
			if (e instanceof CommonException ce)
				throw ce;
			else
				throw new CommonException(FailureConstants.UPDATE_USER_ERROR.getFailureCode(),
						FailureConstants.UPDATE_USER_ERROR.getFailureMsg());
		}
	}

	@CacheEvict(value = "users", key = "#userDetails.username")
	@Override
	public void updatePassword(UpdatePasswordRequest updatePasswordRequest, CustomUserDetails userDetails)
			throws CommonException, UnauthorizedException {
		try {
			if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), userDetails.getPassword())) {
				LOGGER.error("Old Passwords do not match");
				throw new UnauthorizedException(FailureConstants.PASSWORDS_DO_NOT_MATCH.getFailureCode(),
						FailureConstants.PASSWORDS_DO_NOT_MATCH.getFailureMsg());
			}

			User existingUser = userRepository.findByEmail(userDetails.getEmail()).orElseThrow();
			existingUser.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
			existingUser.setModifiedTimestamp(new Date());

			userRepository.save(existingUser);
		} catch (Exception e) {
			LOGGER.error("Exception in updatePassword", e);
			switch (e) {
			case CommonException ce -> throw ce;
			case UnauthorizedException au -> throw au;
			default -> throw new CommonException(FailureConstants.INTERNAL_SERVER_ERROR.getFailureCode(),
					FailureConstants.INTERNAL_SERVER_ERROR.getFailureMsg());
			}
		}
	}

	@Override
	public Map<Long, UserDto> findUsersById(Set<Long> userIds) {
		return userRepository.findUsersByIdListAsMap(userIds);
	}

}
