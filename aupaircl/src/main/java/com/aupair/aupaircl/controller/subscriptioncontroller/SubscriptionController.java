package com.aupair.aupaircl.controller.subscriptioncontroller;

import com.aupair.aupaircl.controller.subscriptioncontroller.subscriptiondto.SubscriptionDTO;
import com.aupair.aupaircl.model.updatestatus.UpdateStatus;
import com.aupair.aupaircl.service.subscriptionservice.SubscriptionService;
import com.aupair.aupaircl.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscription")
@CrossOrigin(origins = {"http://localhost:5173/"})
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }
    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<CustomResponse> registerSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        try {
            return this.subscriptionService.registerSubscription(subscriptionDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo sucedio al registrar subscription"));
        }
    }
    @GetMapping(value = "/getAllSubscriptions",produces = "application/json")
    public ResponseEntity<CustomResponse> getAllSubscriptions (){
        try {
            return this.subscriptionService.getAllSubscriptions();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo sucedio al obtener subscripciones"));
        }
    }
    @PostMapping(value = "/updateStatusSubscription",produces = "application/json")
    public ResponseEntity<CustomResponse> updateStatusSubscription(@RequestBody UpdateStatus updateStatus) {
        try {
            return this.subscriptionService.updateStatusSubscription(updateStatus);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo sucedio al actualizar la suscripcion"));
        }

    }
    @PostMapping(value = "/updateSubscription", produces = "application/json")
    public ResponseEntity<CustomResponse> updateSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        try {
            System.out.println(subscriptionDTO);
            return this.subscriptionService.updateSubscription(subscriptionDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo sucedio al registrar subscription"));
        }
    }
}
