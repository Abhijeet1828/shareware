package com.custom.sharewise.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.custom.common.utilities.exception.CommonException;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.model.User;
import com.custom.sharewise.repository.UserRepository;
import com.custom.sharewise.request.UpdateUserRequest;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = CommonException.class, transactionManager = "transactionManager")
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public Object updateUser(UpdateUserRequest updateUserRequest, CustomUserDetails userDetails) {
		try {
			User existingUser = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
			modelMapper.getConfiguration().setSkipNullEnabled(true);
			modelMapper.typeMap(UpdateUserRequest.class, User.class);

			modelMapper.map(updateUserRequest, existingUser);

			return userRepository.save(existingUser);
		} catch (Exception e) {
			LOGGER.error("Exception in updateUser", e);
			return 2;
		}
	}

}
