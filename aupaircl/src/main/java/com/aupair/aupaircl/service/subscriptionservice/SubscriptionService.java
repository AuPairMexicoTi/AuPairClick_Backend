package com.aupair.aupaircl.service.subscriptionservice;

import com.aupair.aupaircl.controller.subscriptioncontroller.subscriptiondto.SubscriptionDTO;
import com.aupair.aupaircl.model.subscription.Subscription;
import com.aupair.aupaircl.model.subscription.SubscriptionRepository;
import com.aupair.aupaircl.model.updatestatus.UpdateStatus;
import com.aupair.aupaircl.utils.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Service
@Transactional
@Slf4j
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }
    @Transactional(rollbackFor={SQLException.class})
    public ResponseEntity<CustomResponse> registerSubscription(SubscriptionDTO subscriptiondto){
        try {
            if (this.subscriptionRepository.findByIdProduct(subscriptiondto.getIdProduct()).isPresent()){
                log.error("El producto de subscripcion ya existe");
                return new ResponseEntity<>(new CustomResponse(false, 190, "Ya tienes registrado ese id de producto"), HttpStatus.OK);
            }
            Subscription subscription = new Subscription();
            subscription.setTitleSubscription(subscriptiondto.getTitleSubscription());
            subscription.setSubscriptionFeatures(subscriptiondto.getFeatures());
            subscription.setIdProduct(subscriptiondto.getIdProduct());
            subscription.setTransactionAmount(subscriptiondto.getTransactionAmount());
            subscription.setTransactionStatus("activo");
            subscription.setTransactionCurrency(subscriptiondto.getTransactionCurrency());
            subscription.setTransactionDescription(subscriptiondto.getTransactionDescription());
            subscriptionRepository.save(subscription);
            return new ResponseEntity<>(new CustomResponse(false, 200, "Suscripcion registrada correctamente"), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(new CustomResponse(
                    true, 500, "Algo ha ocurrido al registrar la suscripcion"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public ResponseEntity<CustomResponse> getAllSubscriptions(){
        try {
            List<Subscription> subscriptionList = this.subscriptionRepository.findAll();
            return new ResponseEntity<>(new CustomResponse("Lista de subscripciones registradas:",HttpStatus.OK.value(), false,subscriptionList),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new CustomResponse(
                    true, 500, "Algo ha ocurrido al obtener las suscripciones"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional(rollbackFor={SQLException.class})
    public ResponseEntity<CustomResponse> updateStatusSubscription(UpdateStatus updateStatus){
        try {
            if (this.subscriptionRepository.findByIdProduct(updateStatus.getData()).isPresent()){
                Subscription subscription = this.subscriptionRepository.findByIdProduct(updateStatus.getData()).get();
                subscription.setTransactionStatus(updateStatus.getStatus());
                subscriptionRepository.save(subscription);
                return new ResponseEntity<>(new CustomResponse(false, 200, "Suscripcion actualizada correctamente"), HttpStatus.OK);
            }
            log.error("No se encontro la suscripcion con el id: "+updateStatus.getData());
            return new ResponseEntity<>(new CustomResponse(false, 190, "No se encontro la suscripcion"), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(new CustomResponse(
                    true, 500, "Algo ha ocurrido al cancelar la suscripcion"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
