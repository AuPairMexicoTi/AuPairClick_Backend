package com.aupair.aupaircl.controller.subscriptioncontroller.subscriptiondto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class SubscriptionDTO {
    private String idPrice;
    private String titleSubscription;
    private String transactionDescription;
    private Double transactionAmount;
    private String transactionStatus;
    private String transactionCurrency;
    private String subscriptionFeatures;
}
