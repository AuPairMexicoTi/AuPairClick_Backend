package com.aupair.aupaircl.service.subscriptionservice;

import com.aupair.aupaircl.controller.subscriptioncontroller.subscriptiondto.PurchaseSubscriptionDto;
import com.aupair.aupaircl.controller.subscriptioncontroller.subscriptiondto.SubscriptionDTO;
import com.aupair.aupaircl.model.subscription.Subscription;
import com.aupair.aupaircl.model.subscription.SubscriptionRepository;
import com.aupair.aupaircl.model.updatestatus.UpdateStatus;
import com.aupair.aupaircl.model.user.User;
import com.aupair.aupaircl.model.user.UserRepository;
import com.aupair.aupaircl.model.user_has_credits.UserHasCredits;
import com.aupair.aupaircl.model.user_has_credits.UserHasCreditsRepository;
import com.aupair.aupaircl.model.user_has_subscription.UserHasSubscription;
import com.aupair.aupaircl.model.user_has_subscription.UserHasSubscriptionRepository;
import com.aupair.aupaircl.utils.CustomResponse;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserHasSubscriptionRepository userHasSubscriptionRepository;
    private final UserRepository userRepository;
    private final UserHasCreditsRepository userHasCreditsRepository;
    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               UserHasSubscriptionRepository userHasSubscriptionRepository,
                               UserRepository userRepository,UserHasCreditsRepository userHasCreditsRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userHasSubscriptionRepository = userHasSubscriptionRepository;
        this.userRepository = userRepository;
        this.userHasCreditsRepository = userHasCreditsRepository;
    }

    @Transactional(rollbackFor={SQLException.class})
    public ResponseEntity<CustomResponse> registerSubscription(SubscriptionDTO subscriptiondto){
        try {
            Optional<Subscription> existingSubscription = this.subscriptionRepository.findByIdProduct(subscriptiondto.getIdProduct());
            if (existingSubscription.isPresent()){
                log.error("El producto de subscripcion ya existe");
                return new ResponseEntity<>(new CustomResponse(false, 190, "Ya tienes registrado ese id de producto"), HttpStatus.OK);
            }
            Subscription subscription = new Subscription();
            subscription.setTitleSubscription(subscriptiondto.getTitleSubscription());
            subscription.setSubscriptionFeatures(subscriptiondto.getFeatures());
            subscription.setIdProduct(subscriptiondto.getIdProduct());
            subscription.setIdPrice(subscriptiondto.getIdPrice());
            subscription.setTransactionAmount(subscriptiondto.getTransactionAmount());
            subscription.setSubscriptionStatus("activo");
            subscription.setTransactionCurrency(subscriptiondto.getTransactionCurrency());
            subscription.setTransactionDescription(subscriptiondto.getTransactionDescription());
            subscription.setCredits(subscriptiondto.getCredits());
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
            if(this.subscriptionRepository.countBySubscriptionStatus(updateStatus.getStatus())>=3){
                log.error("No puede agregar mas de 3 productos como principales");
                return new ResponseEntity<>(new CustomResponse(false, 191, "No puedes agregar mas de 3 productos principales"), HttpStatus.OK);
            }
            if (this.subscriptionRepository.findByIdProduct(updateStatus.getData()).isPresent()){
                Subscription subscription = this.subscriptionRepository.findByIdProduct(updateStatus.getData()).get();
                subscription.setSubscriptionStatus(updateStatus.getStatus());
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

    @Transactional(rollbackFor={SQLException.class})
    public ResponseEntity<CustomResponse> updateSubscription(SubscriptionDTO subscriptionDTO) {
        try {
            if (this.subscriptionRepository.findByIdProduct(subscriptionDTO.getIdProduct()).isPresent()) {
                Subscription subscription = this.subscriptionRepository.findByIdProduct(subscriptionDTO.getIdProduct()).get();
                subscription.setTitleSubscription(subscriptionDTO.getTitleSubscription());
                subscription.setSubscriptionFeatures(subscriptionDTO.getFeatures());
                subscription.setIdPrice(subscriptionDTO.getIdPrice());
                subscription.setTransactionAmount(subscriptionDTO.getTransactionAmount());
                subscription.setTransactionCurrency(subscriptionDTO.getTransactionCurrency());
                subscription.setTransactionDescription(subscriptionDTO.getTransactionDescription());
                subscriptionRepository.saveAndFlush(subscription);
                return new ResponseEntity<>(new CustomResponse(false, 200, "Suscripcion actualizada correctamente"), HttpStatus.OK);
            }
            log.error("No se encontro la suscripcion con el id: " + subscriptionDTO.getIdProduct());
            return new ResponseEntity<>(new CustomResponse(false, 190, "No se encontro la suscripcion"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomResponse(
                    true, 500, "Algo ha ocurrido al actualizar la suscripcion"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<CustomResponse> userPurchaseSubscription(PurchaseSubscriptionDto purchaseSubscriptionDto){
        try {
            Optional<Subscription> subscriptionExist = this.subscriptionRepository.findByIdProductAndSubscriptionStatus(purchaseSubscriptionDto.getIdProduct(),"principal");
            if(subscriptionExist.isEmpty()){
                log.error("No esta activa la subscription");
                return new ResponseEntity<>(new CustomResponse(false, 101, "No esta activa la suscripcion"), HttpStatus.OK);
            }
            Optional<User> userExist = this.userRepository.findByEmailAndIsLocked((purchaseSubscriptionDto.getEmail()),false);
            if(userExist.isEmpty()){
                log.error("El usuario no existe o esta bloqueado");
                return new ResponseEntity<>(new CustomResponse(false, 102, "El usuario no existe o esta bloqueado"), HttpStatus.OK);
            }
            if (userExist.get().getRole().getRoleName().equals("admin")){
                log.error("No puede realizar compras con un administrador");
                return new ResponseEntity<>(new CustomResponse(false, 103, "No puede realizar compras con este perfil"), HttpStatus.OK);
            }
            UserHasSubscription userHasSubscription = new UserHasSubscription();
            userHasSubscription.setUser(userExist.get());
            userHasSubscription.setSubscription(subscriptionExist.get());
            this.userHasSubscriptionRepository.save(userHasSubscription);
            Optional<UserHasCredits> userHasCreditsExist = this.userHasCreditsRepository.findByUser_Email(userExist.get().getEmail());
            if (userHasCreditsExist.isPresent()) {
                UserHasCredits userHasCredits = userHasCreditsExist.get();
                userHasCredits.setCurrentCredits(userHasCreditsExist.get().getCurrentCredits()+subscriptionExist.get().getCredits());
                this.userHasCreditsRepository.saveAndFlush(userHasCredits);
            }else{
                UserHasCredits userHasCredits = new UserHasCredits();
                userHasCredits.setUser(userExist.get());
                userHasCredits.setCurrentCredits(subscriptionExist.get().getCredits());
                this.userHasCreditsRepository.save(userHasCredits);
            }

            return new ResponseEntity<>(new CustomResponse(false, 200, "Compra realizada con exito"), HttpStatus.OK);

        }catch (Exception e) {
            log.error("Algo sucedio  al comprar subscripcion");
            return new ResponseEntity<>(new CustomResponse(
                    true, 500, "Algo ha ocurrido al realizar la compra de la suscripcion"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Value("${stripe.apiKey}")
    private String stripeApiKey;

    public boolean validateStripeSession(String sessionId) {
        try {
            Stripe.apiKey = stripeApiKey;
            Session session = Session.retrieve(sessionId);
            System.out.println(session);
            return "paid".equals(session.getPaymentStatus());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

