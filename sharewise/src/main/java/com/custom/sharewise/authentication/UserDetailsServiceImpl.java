package com.custom.sharewise.authentication;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.custom.sharewise.model.User;
import com.custom.sharewise.repository.UserRepository;

/**
 * This service class is used by Spring Security to find the users by the
 * username.
 * 
 * @implNote It implements the {@link UserDetailsService} interface of Spring
 *           Security.
 * 
 * @author Abhijeet
 *
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	private final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * This method provides a custom implementation for loading users according to
	 * the application. This method is used by Spring Security for loading users and
	 * authenticating them later on.
	 */
	@Cacheable(value = "users", key = "#username")
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
