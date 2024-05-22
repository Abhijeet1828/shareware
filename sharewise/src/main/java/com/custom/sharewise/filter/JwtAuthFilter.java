package com.custom.sharewise.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.custom.sharewise.authentication.JwtService;
import com.custom.sharewise.authentication.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthFilter.class);

	private final HandlerExceptionResolver handlerExceptionResolver;
	private final JwtService jwtService;
	private final UserDetailsServiceImpl userDetailsServiceImpl;

	public JwtAuthFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsServiceImpl,
			HandlerExceptionResolver handlerExceptionResolver) {
		this.jwtService = jwtService;
		this.userDetailsServiceImpl = userDetailsServiceImpl;
		this.handlerExceptionResolver = handlerExceptionResolver;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			LOGGER.error("No Auth Header Provided for API {}", request.getRequestURL());
			return;
		}

		try {
			String token = authHeader.substring(7);
			String username = jwtService.extractUsername(token);

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

				if (jwtService.isTokenValid(token, userDetails)) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());

					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}

			filterChain.doFilter(request, response);
		} catch (Exception e) {
			LOGGER.error("Exception in JwtAuthFilter.doFilterInternal {}", e.getMessage());
			handlerExceptionResolver.resolveException(request, response, null, e);
		}
	}

}
