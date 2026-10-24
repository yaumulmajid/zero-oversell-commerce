package com.zerooversell.payment.service;

import com.zerooversell.payment.repository.PaymentOutboxRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class PaymentOutboxPublisher {
    private final PaymentOutboxRepository outbox;
    private final KafkaTemplate<String, String> kafka;
    public PaymentOutboxPublisher(PaymentOutboxRepository outbox, KafkaTemplate<String, String> kafka) {
        this.outbox = outbox; this.kafka = kafka;
    }
    @Scheduled(fixedDelayString = "${stockguard.outbox-poll-ms:500}")
    public void publishPending() {
        for (var event : outbox.findTop100ByPublishedAtIsNullOrderByCreatedAtAsc()) {
            try {
                kafka.send(event.getTopic(), event.getMessageKey(), event.getPayload()).get(5, TimeUnit.SECONDS);
                event.markPublished(); outbox.save(event);
            } catch (Exception ignored) { /* Keep pending for the next retry. */ }
        }
    }
}
