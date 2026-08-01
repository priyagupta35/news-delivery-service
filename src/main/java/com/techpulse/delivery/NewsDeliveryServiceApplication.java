package com.techpulse.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class NewsDeliveryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            NewsDeliveryServiceApplication.class, args);
    }

	 @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}