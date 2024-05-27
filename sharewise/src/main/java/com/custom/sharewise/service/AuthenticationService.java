package com.custom.sharewise.service;

import com.custom.common.utilities.exception.CommonException;
import com.custom.sharewise.request.LoginRequest;
import com.custom.sharewise.request.SignUpRequest;

public interface AuthenticationService {
	
	public Object userSignUp(SignUpRequest signUpRequest) throws CommonException;
	
	public Object userLogin(LoginRequest loginRequest) throws CommonException;

}
