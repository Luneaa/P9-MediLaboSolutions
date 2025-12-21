package com.medilabo.evaluation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EvaluationRisqueApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvaluationRisqueApplication.class, args);
    }

}
