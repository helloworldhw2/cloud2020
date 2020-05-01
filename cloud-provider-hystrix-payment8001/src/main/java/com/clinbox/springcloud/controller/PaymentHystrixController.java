package com.clinbox.springcloud.controller;

import com.clinbox.springcloud.service.PaymentHystrixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class PaymentHystrixController {
    @Resource
    PaymentHystrixService paymentHystirxService;
    @Value("${server.port}")
    public String serverPort;
    @GetMapping("/payment/hystrix/ok/{id}")
    public String get_ok(@PathVariable("id") Integer id){
        String ok = paymentHystirxService.get_ok(id);
        log.info(ok);
        return ok;
    }
    @GetMapping("/payment/hystrix/timeout/{id}")
    public String get_timeout(@PathVariable("id") Integer id){
        String timeout = paymentHystirxService.get_timeout(id);
        log.info(timeout);
        return timeout;
    }
    @GetMapping("/payment/circuit/{id}")
    public String PaymentCircuitBreaker(@PathVariable("id") Integer id){
        return paymentHystirxService.paymentCircuitBreaker(id);
    }
}
