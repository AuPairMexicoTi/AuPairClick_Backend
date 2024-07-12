package com.aupair.aupaircl.controller.stripecontroller;
import com.aupair.aupaircl.service.stripeservice.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = {"http://localhost:5173/"})
public class StripeController {

    @Autowired
    private StripeService stripeService;

        @PostMapping("/create-session")
        public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody Map<String, Object> data) {
            Stripe.apiKey = "sk_test_51O3K1zBbp4OirJYPuT8odFOZnDBNDo9utUaICELE18CdkzqUoR51CXh4IPzNd0U5XZUN6pXKR5H98nWDv2lhFo1R00RT9aJ7px"; // Reemplaza con tu clave secreta de Stripe

            List<Object> paymentMethodTypes =
                    new ArrayList<>();
            paymentMethodTypes.add("card");

            Map<String, Object> params = new HashMap<>();
            params.put("payment_method_types", paymentMethodTypes);
            params.put("mode", "payment");
            params.put("success_url", "http://localhost:8080/success");
            params.put("cancel_url", "http://localhost:8080/cancel");

            // Agrega el precio total al parámetro line_items
            List<Object> lineItems = new ArrayList<>();
            Map<String, Object> lineItem = new HashMap<>();
            Map<String, Object> priceData = new HashMap<>();
            Map<String, Object> productData = new HashMap<>();
            productData.put("name", "Paquete express");
            productData.put("description","20 creditos");
            priceData.put("product_data", productData);
            priceData.put("unit_amount", data.get("amount")); // Monto total en centavos
            priceData.put("currency", "usd");
            lineItem.put("price_data", priceData);
            lineItem.put("quantity", 1);
            lineItems.add(lineItem);
            params.put("line_items", lineItems);

            try {
                Session session = Session.create(params);
                Map<String, String> responseData = new HashMap<>();
                responseData.put("id", session.getId());
                return ResponseEntity.ok(responseData);
            } catch (StripeException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

