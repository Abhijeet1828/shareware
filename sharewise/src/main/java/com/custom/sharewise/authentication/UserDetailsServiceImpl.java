package com.custom.sharewise.authentication;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.custom.sharewise.model.User;
import com.custom.sharewise.repository.UserRepository;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	private final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LOGGER.info("Entering in loadUserByUsername method");
		Optional<User> user = userRepository.findByEmail(username);
		if (user.isEmpty()) {
			LOGGER.error("User with email {} not found", username);
			throw new UsernameNotFoundException("No user associated with emailId provided");
		}
		LOGGER.info("User authenticated successfully");
		return new CustomUserDetails(user.get());
	}

}
