package com.custom.sharewise.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddOrRemoveMemberRequest {

	@NotNull
	private Long userId;

	@NotNull
	private Long groupId;

}
