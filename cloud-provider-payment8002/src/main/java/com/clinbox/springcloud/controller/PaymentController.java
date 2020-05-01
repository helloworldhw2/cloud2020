package com.clinbox.springcloud.controller;

import com.clinbox.springcloud.entities.CommonResult;
import com.clinbox.springcloud.entities.Payment;
import com.clinbox.springcloud.service.PaymentService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
public class PaymentController {
    @Resource
    PaymentService paymentService;
    @Value("${server.port}")
    private String serverPort;

    @PutMapping("/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int i = paymentService.create(payment);
        if(i>0){
            return new CommonResult(200,"插入成功",null);
        }else{
            return new CommonResult(400,"失败",null);
        }
    }
    @GetMapping("/payment/get/{id}")
    public CommonResult getPayment(@PathVariable("id") Long id){
        Payment payment = paymentService.getPaymentById(id);
        if(payment != null){
            return new CommonResult(200,"查询成功，端口："+serverPort,payment);
        }else{
            return new CommonResult(400,"失败",null);
        }
    }
    @GetMapping("/payment/lb")
    @HystrixCommand(fallbackMethod = "getPaymentLBHandler")
    public String getPaymentLB(){
        return serverPort;
    }

    public String getPaymentLBHandler(){
        return "error page";
    }

}
