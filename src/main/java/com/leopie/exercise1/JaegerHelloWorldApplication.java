package com.leopie.exercise1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.leopie.repository")
@EnableJpaRepositories("com.leopie.repository")
public class JaegerHelloWorldApplication {
    public static void main(String[] args) {
        SpringApplication.run(JaegerHelloWorldApplication.class, args);
    }
}
