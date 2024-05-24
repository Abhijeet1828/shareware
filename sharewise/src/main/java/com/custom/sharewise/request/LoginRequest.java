package com.custom.sharewise.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

	@Email(message = "EmailID field should have well-formatted email")
	@NotBlank(message = "EmailID field cannot be blank")
	private String email;

	@NotBlank(message = "Password field cannot be blank")
	private String password;

}
