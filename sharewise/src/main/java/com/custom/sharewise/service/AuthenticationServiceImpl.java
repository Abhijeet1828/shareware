package com.custom.sharewise.service;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.custom.common.utilities.exception.CommonException;
import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.authentication.JwtService;
import com.custom.sharewise.constants.FailureConstants;
import com.custom.sharewise.model.User;
import com.custom.sharewise.repository.UserRepository;
import com.custom.sharewise.request.LoginRequest;
import com.custom.sharewise.request.SignUpRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = CommonException.class, transactionManager = "transactionManager")
public class AuthenticationServiceImpl implements AuthenticationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	@Override
	public Object userSignUp(SignUpRequest signUpRequest) throws CommonException {
		try {
			if (userRepository.existsByEmail(signUpRequest.getEmail())) {
				LOGGER.error("User with emailID - {} already exists", signUpRequest.getEmail());
				throw new CommonException(FailureConstants.USER_ALREADY_EXISTS.getFailureCode(),
						FailureConstants.USER_ALREADY_EXISTS.getFailureMsg());
			}

			modelMapper.typeMap(SignUpRequest.class, User.class).addMappings(mapper -> mapper.skip(User::setPassword));

			User newUser = modelMapper.map(signUpRequest, User.class);

			newUser.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
			newUser.setCreatedTimestamp(new Date());
			newUser.setModifiedTimestamp(new Date());

			return userRepository.save(newUser);
		} catch (Exception e) {
			LOGGER.error("Exception in userSignUp", e);
			if (e instanceof CommonException ce)
				throw ce;
			else
				throw new CommonException(FailureConstants.SIGN_UP_ERROR.getFailureCode(),
						FailureConstants.SIGN_UP_ERROR.getFailureMsg());
		}
	}

	@Override
	public Object userLogin(LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		return jwtService.generateToken((CustomUserDetails) authentication.getPrincipal());
	}

}
