package com.zerooversell.payment.repository;

import com.zerooversell.payment.domain.PaymentOutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PaymentOutboxRepository extends JpaRepository<PaymentOutboxEvent, UUID> {
    List<PaymentOutboxEvent> findTop100ByPublishedAtIsNullOrderByCreatedAtAsc();
}
