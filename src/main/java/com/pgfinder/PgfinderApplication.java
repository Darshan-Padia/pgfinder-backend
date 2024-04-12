package com.pgfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.pgfinder.Model"}) // Specify the package where your entity classes are located
public class PgfinderApplication {

    public static void main(String[] args) {
        SpringApplication.run(PgfinderApplication.class, args);
    }
}
