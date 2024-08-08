package com.aupair.aupaircl.controller.stripecontroller.stripeparamsdto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class StripeParamsDto {
    private String currentEmail;
    private String idPrice;
}
