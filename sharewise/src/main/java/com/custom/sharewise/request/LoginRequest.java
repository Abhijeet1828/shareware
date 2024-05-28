package com.custom.sharewise.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

	@Email(message = "EmailID field should have well-formatted email")
	@NotBlank(message = "EmailID field cannot be blank")
	private String email;

	@NotBlank(message = "Password field cannot be blank")
	private String password;

}
