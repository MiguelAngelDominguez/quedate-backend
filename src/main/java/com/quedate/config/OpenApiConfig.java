package com.quedate.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI quedateOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Quédate API")
                                .version("v1")
                                .description(
                                        "API para la plataforma Quédate"
                                )
                );
    }
}