package com.zerooversell.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payment_outbox")
public class PaymentOutboxEvent {
    @Id private UUID id;
    @Column(nullable = false) private String topic;
    @Column(name = "message_key", nullable = false) private String messageKey;
    @Column(nullable = false, columnDefinition = "text") private String payload;
    @Column(name = "created_at", nullable = false) private Instant createdAt;
    @Column(name = "published_at") private Instant publishedAt;
    protected PaymentOutboxEvent() {}
    public PaymentOutboxEvent(String topic, String messageKey, String payload) {
        this.id = UUID.randomUUID(); this.topic = topic; this.messageKey = messageKey;
        this.payload = payload; this.createdAt = Instant.now();
    }
    public String getTopic() { return topic; }
    public String getMessageKey() { return messageKey; }
    public String getPayload() { return payload; }
    public void markPublished() { publishedAt = Instant.now(); }
}
