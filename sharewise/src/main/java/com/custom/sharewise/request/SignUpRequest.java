package com.custom.sharewise.request;

import java.io.Serializable;

import com.custom.common.utilities.validators.MobileNumber;
import com.custom.common.utilities.validators.Password;
import com.custom.common.utilities.validators.SafeInput;

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
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest implements Serializable {

	private static final long serialVersionUID = 3014829614592514082L;

	@Email
	@NotBlank
	private String email;

	@MobileNumber
	@NotBlank
	private String mobileNumber;

	@SafeInput
	@NotBlank
	private String firstName;

	@SafeInput
	@NotBlank
	private String lastName;

	@Password
	@NotBlank
	private String password;

}
