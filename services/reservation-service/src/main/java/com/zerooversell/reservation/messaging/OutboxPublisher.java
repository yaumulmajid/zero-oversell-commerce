package com.zerooversell.reservation.messaging;

import com.zerooversell.reservation.repository.OutboxEventRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class OutboxPublisher {
    private final OutboxEventRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxPublisher(OutboxEventRepository repository, KafkaTemplate<String, String> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelayString = "${stockguard.outbox-poll-ms:500}")
    public void publishPending() {
        for (var event : repository.findTop100ByPublishedAtIsNullOrderByCreatedAtAsc()) {
            try {
                kafkaTemplate.send(event.getTopic(), event.getMessageKey(), event.getPayload())
                        .get(5, TimeUnit.SECONDS);
                event.markPublished();
                repository.save(event);
            } catch (Exception ignored) {
                // Keep the event pending; the next scheduled pass retries it.
            }
        }
    }
}
