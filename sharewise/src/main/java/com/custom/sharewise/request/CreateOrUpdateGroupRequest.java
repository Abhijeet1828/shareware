package com.custom.sharewise.request;

import com.custom.common.utilities.validators.SafeInput;
import com.custom.sharewise.validation.OnCreate;
import com.custom.sharewise.validation.OnUpdate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateOrUpdateGroupRequest {

	@Positive(groups = OnUpdate.class)
	@NotNull(groups = OnUpdate.class)
	private Long groupId;

	@SafeInput(groups = { OnCreate.class, OnUpdate.class })
	@NotBlank(groups = { OnCreate.class, OnUpdate.class })
	private String name;

	@SafeInput(groups = { OnCreate.class, OnUpdate.class })
	private String description;

}
