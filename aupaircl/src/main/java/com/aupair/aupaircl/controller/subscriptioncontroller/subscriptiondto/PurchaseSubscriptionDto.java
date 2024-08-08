package com.aupair.aupaircl.controller.subscriptioncontroller.subscriptiondto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class PurchaseSubscriptionDto {
    private String idProduct;
    private String email;
    private String sessionId;
}
