package com.education.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Entry point.
 *
 * @SpringBootApplication is three annotations in one:
 *   @Configuration        - this class can define beans
 *   @EnableAutoConfiguration - Spring wires Tomcat, Hibernate, Jackson from the classpath
 *   @ComponentScan        - scans com.education.api and below for @RestController,
 *                           @Service, @Repository, @Component
 *
 * Everything in this project lives under com.education.api for that reason - a
 * class in a sibling package would never be found.
 */
@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class EducationApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EducationApiApplication.class, args);
    }
}
