package com.custom.sharewise.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddOrRemoveMemberRequest {

	@Positive
	@NotNull
	private Long userId;

	@Positive
	@NotNull
	private Long groupId;

}
