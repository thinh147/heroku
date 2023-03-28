package com.gogitek.toeictest.controller;

import com.gogitek.toeictest.config.response.ResponseEntityBuilder;
import com.gogitek.toeictest.controller.dto.request.CreatePaymentRequest;
import com.gogitek.toeictest.controller.dto.request.RechargeRequest;
import com.gogitek.toeictest.controller.route.BaseRoute;
import com.gogitek.toeictest.controller.route.UserRoute;
import com.gogitek.toeictest.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(BaseRoute.BASE_ENDPOINT + BaseRoute.USER_BASE)
@RequiredArgsConstructor
public class UserController {
    private final PaymentService paymentService;

    @PostMapping(UserRoute.PAYMENT)
    public ResponseEntity<?> payment(@RequestBody CreatePaymentRequest request){
        paymentService.createPayment(request.getPeriod());
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails("Create successfully!")
                .build();
    }

    @GetMapping(UserRoute.HISTORY)
    public ResponseEntity<?> history(){
        return ResponseEntityBuilder.getBuilder().build();
    }

    @GetMapping(UserRoute.TRANSACTION)
    public ResponseEntity<?> transaction(@RequestParam(name = "offset", defaultValue = "0") Integer offset,
                                         @RequestParam(name = "limit", defaultValue = "10") Integer limit){
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(paymentService.retrieveListResponseUser(offset, limit))
                .build();
    }

    @GetMapping(UserRoute.USER_INFORMATION)
    public ResponseEntity<?> information(){
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(paymentService.retrieveCurrentUserInformation())
                .build();
    }

    @PostMapping(UserRoute.RECHARGE)
    public ResponseEntity<?> recharge(@RequestBody RechargeRequest request) {
        paymentService.recharge(request.getTotalMoney());
        return ResponseEntityBuilder
                .getBuilder()
                .build();
    }
}
