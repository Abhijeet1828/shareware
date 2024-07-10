package com.custom.sharewise.request;

import java.io.Serializable;

import com.custom.common.utilities.validators.SafeInput;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUpRequest implements Serializable {

	private static final long serialVersionUID = 3014829614592514082L;

	@SafeInput
	@Email(message = "EmailID field should have well-formatted email")
	@NotBlank(message = "EmailID field cannot be blank")
	private String email;

	@SafeInput
	@NotBlank(message = "Mobile number field cannot be blank")
	private String mobileNumber;

	@SafeInput
	@NotBlank(message = "First name field cannot be blank")
	private String firstName;

	@SafeInput
	@NotBlank(message = "Last name field cannot be blank")
	private String lastName;

	@SafeInput
	@NotBlank(message = "Password field cannot be blank")
	private String password;

}
