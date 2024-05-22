package com.custom.sharewise.service;

import java.util.Date;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.custom.sharewise.authentication.CustomUserDetails;
import com.custom.sharewise.authentication.JwtService;
import com.custom.sharewise.model.User;
import com.custom.sharewise.repository.UserRepository;
import com.custom.sharewise.request.LoginRequest;
import com.custom.sharewise.request.SignUpRequest;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public AuthenticationServiceImpl(UserRepository userRepository, ModelMapper modelMapper,
			PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	@Override
	public User userSignUp(SignUpRequest signUpRequest) {
		try {
			modelMapper.typeMap(SignUpRequest.class, User.class).addMappings(mapper -> mapper.skip(User::setPassword));

			User newUser = modelMapper.map(signUpRequest, User.class);

			newUser.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
			newUser.setCreatedTimestamp(new Date());
			newUser.setModifiedTimestamp(new Date());

			return userRepository.save(newUser);
		} catch (Exception e) {
			LOGGER.error("Exception in userSignUp", e);
			return null;
		}
	}

	@Override
	public Object userLogin(LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
			if (authentication.isAuthenticated()) {
				Optional<User> loggedInUser = userRepository.findByEmail(loginRequest.getEmail());
				if (loggedInUser.isPresent()) {
					return jwtService.generateToken(new CustomUserDetails(loggedInUser.get()));
				}
			}

			return 1;
		} catch (Exception e) {
			LOGGER.error("Exception in userLogin", e);
			return 2;
		}
	}

}
