package org.example.projectmanagementsystem.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Project Management System (PMS) API")
                        .version("1.0.0")
                        .description("Loyihalarni boshqarish tizimi uchun REST API. CRUD, Dashboard statistikasi va KPI hisoblash.")
                        .termsOfService("https://example.com/terms")
                        .contact(new Contact()
                                .name("Abdurahim")
                                .email("abdurahimbohodirov@example.com")
                                .url("https://github.com/abdurahim-dev/pms-backend"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))

                // JWT Bearer autentifikatsiyasini ta'riflash
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT tokenni 'Bearer {token}' formatida kiriting")))

                // Global security requirement (barcha endpointlar uchun Authorize ko‘rsatiladi)
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}