package com.mobieslow.paymentservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment/{mid}")
public class PaymentController {

    @PostMapping("/initiate")
    public ResponseEntity initiate(@PathVariable("mid") String mid) {
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/checkStatus/{orderId}")
    public ResponseEntity checkStatus(@PathVariable("mid") String mid, @PathVariable("orderId") String orderId) {
        return ResponseEntity.badRequest().build();
    }
}
