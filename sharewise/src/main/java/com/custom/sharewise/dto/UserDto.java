package com.custom.sharewise.dto;

import java.io.Serializable;

public record UserDto(Long userId, String name) implements Serializable {

}
