package com.aupair.aupaircl.controller.stripecontroller;
import com.aupair.aupaircl.controller.stripecontroller.stripeparamsdto.StripeParamsDto;
import com.aupair.aupaircl.service.stripeservice.StripeService;
import com.aupair.aupaircl.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin(origins = {"http://localhost:5173/"})
public class StripeController {

    private final StripeService stripeService;
    @Autowired
    public StripeController(StripeService service) {
        this.stripeService = service;
    }

        @PostMapping("/create-session")
        public ResponseEntity<CustomResponse> createCheckoutSession(@RequestBody StripeParamsDto stripeParamsDto) {
            try {
                return stripeService.createSession(stripeParamsDto);
            }catch (Exception e){

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo salio mal"));
            }
        }
    }

