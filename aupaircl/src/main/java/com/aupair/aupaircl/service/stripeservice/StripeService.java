package com.aupair.aupaircl.service.stripeservice;
import com.aupair.aupaircl.controller.stripecontroller.stripeparamsdto.StripeParamsDto;
import com.aupair.aupaircl.utils.CustomResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class StripeService {

    @Value("${stripe.apiKey}")
    private String apiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }

    public ResponseEntity<CustomResponse> createSession(StripeParamsDto stripeParamsDto) throws StripeException {
        try {
            Stripe.apiKey = "sk_test_51O3K1zBbp4OirJYPuT8odFOZnDBNDo9utUaICELE18CdkzqUoR51CXh4IPzNd0U5XZUN6pXKR5H98nWDv2lhFo1R00RT9aJ7px"; // Reemplaza con tu clave secreta de Stripe

            List<Object> paymentMethodTypes =
                    new ArrayList<>();

            paymentMethodTypes.add("card");

            Map<String, Object> params = new HashMap<>();
            params.put("payment_method_types", paymentMethodTypes);
            params.put("mode", "payment");
            params.put("success_url", "http://localhost:5173/exitoso?session_id={SESSION_ID}");
            params.put("cancel_url", "http://localhost:5173/cancelar");

            List<Object> lineItems = new ArrayList<>();
            Map<String, Object> lineItem = new HashMap<>();
            lineItem.put("price", stripeParamsDto.getIdPrice());
            lineItem.put("quantity", 1);
            lineItems.add(lineItem);
            params.put("line_items", lineItems);
            params.put("customer_email",stripeParamsDto.getCurrentEmail());

            try {
                Session session = Session.create(params);
                Map<String, String> responseData = new HashMap<>();
                responseData.put("id", session.getId());
                return ResponseEntity.ok(new CustomResponse("Session",200,false,responseData));
            } catch (StripeException e) {
                log.error("Error creating session: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }catch (Exception e) {
            log.error("Error al crear la sesión de checkout de Stripe: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
