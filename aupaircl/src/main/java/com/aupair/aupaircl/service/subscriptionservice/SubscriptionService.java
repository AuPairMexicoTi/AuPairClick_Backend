package com.aupair.aupaircl.service.subscriptionservice;

import com.aupair.aupaircl.controller.subscriptioncontroller.subscriptiondto.SubscriptionDTO;
import com.aupair.aupaircl.model.subscription.Subscription;
import com.aupair.aupaircl.model.subscription.SubscriptionRepository;
import com.aupair.aupaircl.utils.CustomResponse;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
@Transactional
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }
    @Transactional(rollbackFor={SQLException.class})
    public ResponseEntity<CustomResponse> registerSubscription(SubscriptionDTO subscriptiondto){
        try {
            Subscription subscription = new Subscription();
            subscription.setTitleSubscription(subscriptiondto.getTitleSubscription());
            subscription.setSubscriptionFeatures(subscriptiondto.getSubscriptionFeatures());
            subscription.setIdPrice(subscriptiondto.getIdPrice());
            subscription.setTransactionAmount(subscriptiondto.getTransactionAmount());
            subscription.setTransactionStatus(subscriptiondto.getTransactionStatus());
            subscriptionRepository.save(subscription);
            return new ResponseEntity<>(new CustomResponse(false, 200, "Suscripcion registrada correctamente"), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(new CustomResponse(
                    true, 500, "Algo ha ocurrido al registrar la suscripcion"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
