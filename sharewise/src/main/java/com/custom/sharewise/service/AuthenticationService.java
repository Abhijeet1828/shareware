package com.custom.sharewise.service;

import com.custom.sharewise.request.LoginRequest;
import com.custom.sharewise.request.SignUpRequest;

public interface AuthenticationService {
	
	public Object userSignUp(SignUpRequest signUpRequest);
	
	public Object userLogin(LoginRequest loginRequest);

}
