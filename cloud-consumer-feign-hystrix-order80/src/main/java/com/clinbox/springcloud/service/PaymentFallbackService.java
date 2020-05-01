package com.clinbox.springcloud.service;

import org.springframework.stereotype.Component;

@Component
public class PaymentFallbackService implements PaymentHystrixService{
    @Override
    public String get_ok(Integer id) {
        return "------PaymentFallbackService fall back-paymentinfo_ok";
    }

    @Override
    public String get_timeout(Integer id) {
        return "------PaymentFallbackService fall back-paymentinfo_timeout";
    }
}
