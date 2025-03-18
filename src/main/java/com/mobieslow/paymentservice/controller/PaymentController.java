package com.mobieslow.paymentservice.controller;

import com.mobieslow.paymentservice.exceptions.ServiceException;
import com.mobieslow.paymentservice.service.PaymentService;
import com.mobieslow.paymentservice.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment/{mid}")
public class PaymentController {
    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/initiate")
    public ApiResponse<PaymentService.InitiateResponse> initiate(@PathVariable("mid") String mid, @RequestBody String encryptedRequest) {
        try {
            var response =
                    paymentService.initiate(mid, encryptedRequest);

            return ApiResponse.success(response);

        } catch (ServiceException ex) {
            logger.error("Service Exception error while performing initiate request: {}", ex.getMessage(), ex);
            return ApiResponse.badRequest(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Unexpected Exception while performing initiate request: {}", ex.getMessage(), ex);
            return ApiResponse.internalServerError("Internal server error");
        }
    }

    @PostMapping("/checkStatus/{encryptedOrderId}")
    public ApiResponse<PaymentService.CheckStatusResponse> checkStatus(@PathVariable("mid") String mid, @PathVariable("encryptedOrderId") String encryptedOrderId) {
        try {
            var response =
                    paymentService.checkStatus(mid, encryptedOrderId);

            return ApiResponse.success(response);

        } catch (ServiceException ex) {
            logger.error("Service Exception error while performing check status request: {}", ex.getMessage(), ex);
            return ApiResponse.badRequest(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Unexpected Exception while performing check status request: {}", ex.getMessage(), ex);
            return ApiResponse.internalServerError("Internal server error");
        }
    }
}
