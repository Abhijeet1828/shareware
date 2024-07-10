package com.custom.sharewise.request;

import com.custom.common.utilities.validators.SafeInput;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

	@SafeInput
	@Email(message = "EmailID field should have well-formatted email")
	@NotBlank(message = "EmailID field cannot be blank")
	private String email;

	@SafeInput
	@NotBlank(message = "Password field cannot be blank")
	private String password;

}
