package com.education.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Drives the interactive docs at http://localhost:8080/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI educationOpenAPI() {
        Server local = new Server();
        local.setUrl("http://localhost:8080");
        local.setDescription("Local development");

        Contact contact = new Contact();
        contact.setName("Education API Team");
        contact.setEmail("support@education.local");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Education Management REST API")
                .version("1.0.0")
                .contact(contact)
                .license(license)
                .description("REST API for students, teachers, courses, enrollments, "
                        + "exams and grades. Supports integration between student portals, "
                        + "teacher dashboards and administrative systems.");

        return new OpenAPI().info(info).servers(List.of(local));
    }
}
