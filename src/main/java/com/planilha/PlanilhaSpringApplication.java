package com.planilha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
public class PlanilhaSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlanilhaSpringApplication.class, args);
    }
}
