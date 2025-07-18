package com.example.logging_backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig { // Sınıf adını değiştirmek daha uygun olur

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Uygulama API Dokümantasyonu")
                        .version("1.0")
                        .description("Bu, Spring Boot 3 ile çalışan API'mız için OpenAPI dokümantasyonudur."));
    }
}