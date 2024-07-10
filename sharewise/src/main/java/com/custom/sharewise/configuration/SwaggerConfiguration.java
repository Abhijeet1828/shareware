package com.custom.sharewise.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;

/**
 * This class is used to configure Swagger and add important information about
 * the application.
 * 
 * @author Abhijeet
 *
 */
@Configuration
public class SwaggerConfiguration {

	/**
	 * Creating a bean for defining the OpenAPI Swagger's contact and information.
	 * This will displayed on the Swagger UI.
	 * 
	 * @return {@link OpenAPI}
	 */
	@Bean
	OpenAPI defineOpenAPI() {
		Server server = new Server();
		server.setUrl("http://localhost:8080/sharewise");
		server.setDescription("Development");

		Contact contact = new Contact();
		contact.setName("Abhijeet");
		contact.setEmail("srivastava.abhijeet96@gmail.com");

		Info info = new Info().title("Sharewise - Spring Boot Project").version("1.0")
				.description("This project provides APIs for splitting expenses between users in a group")
				.contact(contact);

		List<Server> servers = new ArrayList<>();
		servers.add(server);

		SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

		Components components = new Components().addSecuritySchemes("bearerAuth",
				new SecurityScheme().type(Type.HTTP).scheme("bearer").bearerFormat("JWT"));

		return new OpenAPI().info(info).servers(servers).addSecurityItem(securityRequirement).components(components);
	}

	@Bean
	OperationCustomizer operationCustomizer() {
		return (operation, handlerMethod) -> {
			Optional<PreAuthorize> preAuthorizeAnnotation = Optional
					.ofNullable(handlerMethod.getMethodAnnotation(PreAuthorize.class));
			StringBuilder sb = new StringBuilder();
			sb.append("Roles Required - **");
			if (preAuthorizeAnnotation.isPresent()) {
				sb.append((preAuthorizeAnnotation.get()).value().replaceAll("hasRole|\\(|\\)|\\'", ""));
			} else {
				sb.append("None");
			}
			sb.append("**");
			sb.append("<br>");
			sb.append(operation.getDescription());
			operation.setDescription(sb.toString());
			return operation;
		};
	}

}
