package com.joyned.reddit.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {
    @Bean
    public OpenAPI usersSwaggerOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Reddit Swagger")
                        .description("\n" +
                                "Reddit Swagger is an integration of the Swagger framework into the Reddit application, providing a user-friendly interface to explore and interact with the application's RESTful APIs.")
                        .version("1.0"));
    }
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
