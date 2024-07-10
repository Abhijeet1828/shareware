package com.custom.sharewise.request;

import java.io.Serializable;

import com.custom.common.utilities.validators.MobileNumber;
import com.custom.common.utilities.validators.SafeInput;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest implements Serializable {

	private static final long serialVersionUID = -9100759041933563299L;

	@SafeInput
	@NotBlank
	private String firstName;

	@SafeInput
	@NotBlank
	private String lastName;

	@NotBlank
	@MobileNumber
	private String mobileNumber;

}
