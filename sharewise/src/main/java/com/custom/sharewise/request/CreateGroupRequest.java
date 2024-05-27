package com.custom.sharewise.request;

import com.custom.common.utilities.validators.SafeInput;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupRequest {

	@SafeInput
	@NotBlank
	private String name;

	@SafeInput
	private String description;

}
