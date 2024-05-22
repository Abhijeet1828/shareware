package com.custom.sharewise.request;

import java.io.Serializable;

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

	@NotBlank
	private String email;

	@NotBlank
	private String mobileNumber;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@NotBlank
	private String password;

}
