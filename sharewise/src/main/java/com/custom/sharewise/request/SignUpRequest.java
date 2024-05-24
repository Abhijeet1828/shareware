package com.custom.sharewise.request;

import java.io.Serializable;

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

	@Email(message = "EmailID field should have well-formatted email")
	@NotBlank(message = "EmailID field cannot be blank")
	private String email;

	@NotBlank(message = "Mobile number field cannot be blank")
	private String mobileNumber;

	@NotBlank(message = "First name field cannot be blank")
	private String firstName;

	@NotBlank(message = "Last name field cannot be blank")
	private String lastName;

	@NotBlank(message = "Password field cannot be blank")
	private String password;

}
