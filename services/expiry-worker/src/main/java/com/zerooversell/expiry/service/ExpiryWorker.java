package com.zerooversell.expiry.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ExpiryWorker {
    private static final Logger log = LoggerFactory.getLogger(ExpiryWorker.class);
    private final RestClient reservationClient;

    public ExpiryWorker(RestClient reservationClient) { this.reservationClient = reservationClient; }

    @Scheduled(fixedDelayString = "${stockguard.expiry-poll-ms:5000}", initialDelayString = "${stockguard.expiry-initial-delay-ms:5000}")
    public void expireDueReservations() {
        try {
            var response = reservationClient.post().uri("/internal/v1/reservations/expire-due")
                    .retrieve().body(ExpiryResponse.class);
            if (response != null && response.expiredReservations() > 0) {
                log.info("Expired {} reservations", response.expiredReservations());
            }
        } catch (Exception exception) {
            log.warn("Reservation expiry call failed; it will be retried on the next poll: {}", exception.getMessage());
        }
    }

    public record ExpiryResponse(int expiredReservations) {}
}
