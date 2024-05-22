package com.custom.sharewise.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * This class contains basic beans required by the application.
 * 
 * @author Abhijeet
 *
 */
@Configuration
public class ApplicationConfiguration {

	/**
	 * ModelMapper bean used for converting DTOs.
	 * 
	 * @return
	 */
	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}

	/**
	 * Jackson object mapper is used for transforming string dates to timestamps in
	 * response.
	 * 
	 * @return
	 */
	@Bean
	Jackson2ObjectMapperBuilder jacksonBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.featuresToEnable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		return builder;
	}

}
