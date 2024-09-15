package com.etna.sportevents.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
	@Value("${jwt.header}")
  private String jwtHeaderName;

  @Value("${jwt.prefix}")
  private String jwtPrefix;
	@Bean
	public OpenAPI customizeOpenAPI() {
	    final String securitySchemeName = "JWT";
	    return new OpenAPI()
	      .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
	      .components(new Components()
	        .addSecuritySchemes(
							securitySchemeName,
			        new SecurityScheme()
		          .name(securitySchemeName)
		          .type(SecurityScheme.Type.HTTP)
		          .scheme(jwtPrefix)
		          .bearerFormat(jwtHeaderName))
	      );
	    }
}
