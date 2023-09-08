package com.tt.unitify.modules.payment;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @GetMapping()
    public void getPayments() throws Exception {
        paymentService.getPayments();
    }

    @GetMapping("/where")
    public void getPaymentsWhere() throws Exception {
        paymentService.getPaymentsWithWhere();
    }
}
