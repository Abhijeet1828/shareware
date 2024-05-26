package com.custom.sharewise.request;

import java.io.Serializable;

import com.custom.common.utilities.validators.Password;

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
public class UpdatePasswordRequest implements Serializable {

	private static final long serialVersionUID = 5104276680561024629L;

	@NotBlank(message = "Old Password field cannot be blank")
	private String oldPassword;

	@Password
	private String newPassword;

}
