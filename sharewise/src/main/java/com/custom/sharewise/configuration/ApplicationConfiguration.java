package com.custom.sharewise.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
