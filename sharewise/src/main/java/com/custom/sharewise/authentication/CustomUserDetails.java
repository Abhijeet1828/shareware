package com.custom.sharewise.authentication;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.custom.sharewise.model.User;

/**
 * This is a custom user details class which is used for Spring Security
 * authentication. This is required by Spring Security to authenticate user by
 * their respective username and passwords.
 * 
 * @implNote Extends the model {@link User}.
 * @implNote Implements the Spring Security {@link UserDetails}.
 * 
 * @author Abhijeet
 *
 */
public class CustomUserDetails extends User implements UserDetails {

	private static final long serialVersionUID = 4583199412588673714L;

	private String username;
	private String password;

	public CustomUserDetails(User user) {
		this.username = user.getEmail();
		this.password = user.getPassword();
	}

	/**
	 * This list can be used for role based authorization.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
