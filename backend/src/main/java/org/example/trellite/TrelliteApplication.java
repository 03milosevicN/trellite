package org.example.trellite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TrelliteApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrelliteApplication.class, args);
    }

}
