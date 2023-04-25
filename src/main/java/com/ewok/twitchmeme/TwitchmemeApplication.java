package com.ewok.twitchmeme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TwitchmemeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwitchmemeApplication.class, args);
    }

}
