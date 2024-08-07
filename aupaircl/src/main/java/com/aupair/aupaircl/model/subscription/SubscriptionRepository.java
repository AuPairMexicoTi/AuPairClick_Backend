package com.aupair.aupaircl.model.subscription;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Optional<Subscription> findByIdProduct(String idProduct);
    Optional<Subscription> findByIdProductAndSubscriptionStatus(String idProduct,String status);
    int countBySubscriptionStatus(String status);
}
