package com.aupair.aupaircl.controller.subscriptioncontroller.subscriptiondto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class SubscriptionDTO {
    private String idProduct;
    private String idPrice;
    private String titleSubscription;
    private String transactionDescription;
    private Double transactionAmount;
    private String transactionCurrency;
    private String features;
    private Integer credits;
}
