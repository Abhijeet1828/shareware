package com.custom.sharewise.service;

import com.custom.sharewise.model.User;
import com.custom.sharewise.request.LoginRequest;
import com.custom.sharewise.request.SignUpRequest;
import com.custom.sharewise.response.LoginResponse;

public interface AuthenticationService {
	
	public User userSignUp(SignUpRequest signUpRequest);
	
	public LoginResponse userLogin(LoginRequest loginRequest);

}
