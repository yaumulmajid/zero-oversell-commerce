package com.zerooversell.campaign.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ReservationClientConfiguration {
    @Bean
    RestClient reservationRestClient(RestClient.Builder builder,
                                     @Value("${services.reservation.base-url}") String baseUrl) {
        return builder.baseUrl(baseUrl).build();
    }
}
