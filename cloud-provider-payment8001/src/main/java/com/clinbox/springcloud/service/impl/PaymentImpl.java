package com.clinbox.springcloud.service.impl;

import com.clinbox.springcloud.dao.PaymentDao;
import com.clinbox.springcloud.entities.Payment;
import com.clinbox.springcloud.service.PaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PaymentImpl implements PaymentService {
    @Resource
    private PaymentDao paymentDao;
    public int create(Payment payment){
        return paymentDao.create(payment);
    }

    public  Payment getPaymentById(Long id){
        return paymentDao.getPaymentById(id);
    }

}
