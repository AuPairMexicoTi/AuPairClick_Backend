package com.aupair.aupaircl.model.subscription;


import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subscription")
public class Subscription {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id_payment")
    private UUID idPayment;
    @Column(name = "id_price")
    private String idPrice;
    @Column(name = "title_subscription")
    private String titleSubscription;
    @Column(name = "subscription_date")
    private Date transactionDate;
    @Column(name = "subscription_description")
    private String transactionDescription;
    @Column(name = "subscription_amount")
    private Double transactionAmount;
    @Column(name = "subscription_status")
    private String transactionStatus;
    @Column(name = "subscription_currency")
    private String transactionCurrency;
    @Column(name = "subscription_features")
    private String subscriptionFeatures;
    @PrePersist
    private void generateUUID() {
        if (this.idPayment == null) {
            this.idPayment = UUID.randomUUID();
        }
        if (this.transactionDate == null) {
            this.transactionDate = new Date();
        }
    }
}
