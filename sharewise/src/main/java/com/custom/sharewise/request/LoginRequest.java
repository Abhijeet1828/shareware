package com.custom.sharewise.request;

import com.custom.common.utilities.validators.Password;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

	@Email
	@NotBlank
	private String email;

	@Password
	@NotBlank(message = "Password field cannot be blank")
	private String password;

}
