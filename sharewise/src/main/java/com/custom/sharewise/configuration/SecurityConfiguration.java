package com.custom.sharewise.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter.HeaderValue;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.custom.sharewise.authentication.UserDetailsServiceImpl;
import com.custom.sharewise.filter.JwtAuthFilter;
import com.custom.sharewise.repository.UserRepository;

/**
 * This class is used to define security configurations of the application such
 * as JWT Filter, Username-Password Authentications and Role based
 * authentication.
 * 
 * @author Abhijeet
 *
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

	private final JwtAuthFilter jwtAuthFilter;
	private final UserRepository userRepository;

	public SecurityConfiguration(JwtAuthFilter jwtAuthFilter, UserRepository userRepository) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.userRepository = userRepository;
	}

	/**
	 * This bean is used as a DAO for loading user details with custom
	 * implementation using {@link UserDetailsServiceImpl}.
	 * 
	 * @return {@link UserDetailsService}
	 */
	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl(userRepository);
	}

	/**
	 * This method is used to create a bean for {@link SecurityFilterChain} which
	 * handles security of the application by adding filters and different cors and
	 * xss protection.
	 * 
	 * @param http
	 * @return
	 * 
	 * @throws Exception
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.headers(headers -> headers.xssProtection(xss -> xss.headerValue(HeaderValue.ENABLED_MODE_BLOCK))
						.contentSecurityPolicy(csp -> csp.policyDirectives("script-src 'self'")))
				.authorizeHttpRequests(
						auth -> auth
								.requestMatchers("/auth/**", "/actuator/**", "/swagger-ui/**", "/v3/api-docs/**",
										"/swagger-resources/**", "/swagger-resources")
								.permitAll().anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	/**
	 * Creating a bean for the password encoder used to encrypt passwords and store
	 * in the database.
	 * 
	 * @return
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Creates a bean for the Authentication provider using
	 * {@link UserDetailsServiceImpl} and {@link BCryptPasswordEncoder}.
	 * 
	 * @return
	 */
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	/**
	 * Creates a bean for {@link AuthenticationManager}.
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	/**
	 * Creates a bean for CORS configuration of the application. It is later used in
	 * {@link SecurityFilterChain}.
	 * 
	 * @return
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();

		corsConfiguration.setAllowedOrigins(List.of("http://localhost:8080"));
		corsConfiguration.setAllowedMethods(List.of("GET", "POST", "DELETE", "PUT"));
		corsConfiguration.setAllowedHeaders(List.of("*"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		return source;
	}

}
